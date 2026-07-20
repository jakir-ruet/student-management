----------------------------------------------------------------------
-- V22: STUDENT FEE DISCOUNT PACKAGE
----------------------------------------------------------------------

CREATE OR REPLACE PACKAGE STUDENT_FEE_DISCOUNT_PKG AS

    PROCEDURE CREATE_DISCOUNT(
        P_STUDENT_FEE_ID         IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_ID%TYPE,
        P_DISCOUNT_TYPE          IN STUDENT_FEE_DISCOUNT.DISCOUNT_TYPE%TYPE,
        P_DISCOUNT_VALUE         IN STUDENT_FEE_DISCOUNT.DISCOUNT_VALUE%TYPE,
        P_REASON                 IN STUDENT_FEE_DISCOUNT.REASON%TYPE,
        P_CREATED_BY             IN STUDENT_FEE_DISCOUNT.CREATED_BY%TYPE,
        P_STUDENT_FEE_DISCOUNT_ID
            OUT STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE
    );

    PROCEDURE GET_DISCOUNT_BY_ID(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ALL_DISCOUNTS(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_DISCOUNTS_BY_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_ID%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    );

    PROCEDURE GET_DISCOUNTS_BY_STATUS(
        P_APPROVAL_STATUS
            IN STUDENT_FEE_DISCOUNT.APPROVAL_STATUS%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE APPROVE_DISCOUNT(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE,
        P_APPROVED_BY IN STUDENT_FEE_DISCOUNT.APPROVED_BY%TYPE
    );

    PROCEDURE REJECT_DISCOUNT(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE,
        P_UPDATED_BY IN STUDENT_FEE_DISCOUNT.UPDATED_BY%TYPE
    );

    PROCEDURE CANCEL_DISCOUNT(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE,
        P_UPDATED_BY IN STUDENT_FEE_DISCOUNT.UPDATED_BY%TYPE
    );

    PROCEDURE DELETE_DISCOUNT(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE
    );

END STUDENT_FEE_DISCOUNT_PKG;
/

CREATE OR REPLACE PACKAGE BODY STUDENT_FEE_DISCOUNT_PKG AS

    ------------------------------------------------------------------
    -- Validate discount type and value
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_DISCOUNT(
        P_DISCOUNT_TYPE  IN STUDENT_FEE_DISCOUNT.DISCOUNT_TYPE%TYPE,
        P_DISCOUNT_VALUE IN STUDENT_FEE_DISCOUNT.DISCOUNT_VALUE%TYPE
    ) AS
    BEGIN
        IF P_DISCOUNT_TYPE IS NULL
            OR P_DISCOUNT_TYPE NOT IN (
                                       'FIXED',
                                       'PERCENTAGE'
                ) THEN
            RAISE_APPLICATION_ERROR(
                    -20960,
                    'INVALID DISCOUNT TYPE'
            );
        END IF;

        IF P_DISCOUNT_VALUE IS NULL
            OR P_DISCOUNT_VALUE <= 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20961,
                    'DISCOUNT VALUE MUST BE GREATER THAN ZERO'
            );
        END IF;

        IF P_DISCOUNT_TYPE = 'PERCENTAGE'
            AND P_DISCOUNT_VALUE > 100 THEN
            RAISE_APPLICATION_ERROR(
                    -20962,
                    'DISCOUNT PERCENTAGE CANNOT EXCEED 100'
            );
        END IF;
    END VALIDATE_DISCOUNT;

    ------------------------------------------------------------------
    -- Validate approval status
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_APPROVAL_STATUS(
        P_APPROVAL_STATUS
            IN STUDENT_FEE_DISCOUNT.APPROVAL_STATUS%TYPE
    ) AS
    BEGIN
        IF P_APPROVAL_STATUS IS NULL
            OR P_APPROVAL_STATUS NOT IN (
                                         'PENDING',
                                         'APPROVED',
                                         'REJECTED',
                                         'CANCELLED'
                ) THEN
            RAISE_APPLICATION_ERROR(
                    -20963,
                    'INVALID DISCOUNT APPROVAL STATUS'
            );
        END IF;
    END VALIDATE_APPROVAL_STATUS;

    ------------------------------------------------------------------
    -- Recalculate student fee from approved discounts
    ------------------------------------------------------------------
    PROCEDURE RECALCULATE_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    ) AS
        V_BASE_AMOUNT      STUDENT_FEE.BASE_AMOUNT%TYPE;
        V_FINE_AMOUNT      STUDENT_FEE.FINE_AMOUNT%TYPE;
        V_PAID_AMOUNT      STUDENT_FEE.PAID_AMOUNT%TYPE;
        V_DUE_DATE         STUDENT_FEE.DUE_DATE%TYPE;
        V_CURRENT_STATUS   STUDENT_FEE.STATUS%TYPE;
        V_DISCOUNT_AMOUNT  STUDENT_FEE.DISCOUNT_AMOUNT%TYPE;
        V_TOTAL_AMOUNT     STUDENT_FEE.TOTAL_AMOUNT%TYPE;
        V_BALANCE_AMOUNT   STUDENT_FEE.BALANCE_AMOUNT%TYPE;
        V_NEW_STATUS       STUDENT_FEE.STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT BASE_AMOUNT,
                   FINE_AMOUNT,
                   PAID_AMOUNT,
                   DUE_DATE,
                   STATUS
            INTO V_BASE_AMOUNT,
                V_FINE_AMOUNT,
                V_PAID_AMOUNT,
                V_DUE_DATE,
                V_CURRENT_STATUS
            FROM STUDENT_FEE
            WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20964,
                        'STUDENT FEE NOT FOUND'
                );
        END;

        IF V_CURRENT_STATUS IN ('WAIVED', 'CANCELLED') THEN
            RAISE_APPLICATION_ERROR(
                    -20965,
                    'DISCOUNT CANNOT BE APPLIED TO WAIVED OR CANCELLED FEE'
            );
        END IF;

        SELECT NVL(SUM(DISCOUNT_AMOUNT), 0)
        INTO V_DISCOUNT_AMOUNT
        FROM STUDENT_FEE_DISCOUNT
        WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID
          AND APPROVAL_STATUS = 'APPROVED';

        IF V_DISCOUNT_AMOUNT > V_BASE_AMOUNT THEN
            RAISE_APPLICATION_ERROR(
                    -20966,
                    'TOTAL DISCOUNT CANNOT EXCEED BASE AMOUNT'
            );
        END IF;

        V_TOTAL_AMOUNT :=
                V_BASE_AMOUNT
                    - V_DISCOUNT_AMOUNT
                    + V_FINE_AMOUNT;

        IF V_TOTAL_AMOUNT < V_PAID_AMOUNT THEN
            RAISE_APPLICATION_ERROR(
                    -20967,
                    'DISCOUNT WOULD REDUCE TOTAL BELOW PAID AMOUNT'
            );
        END IF;

        V_BALANCE_AMOUNT :=
                V_TOTAL_AMOUNT - V_PAID_AMOUNT;

        V_NEW_STATUS := CASE
                            WHEN V_BALANCE_AMOUNT = 0
                                THEN 'PAID'
                            WHEN V_PAID_AMOUNT > 0
                                THEN 'PARTIALLY_PAID'
                            WHEN TRUNC(V_DUE_DATE) < TRUNC(SYSDATE)
                                THEN 'OVERDUE'
                            ELSE 'PENDING'
            END;

        UPDATE STUDENT_FEE
        SET DISCOUNT_AMOUNT = V_DISCOUNT_AMOUNT,
            TOTAL_AMOUNT    = V_TOTAL_AMOUNT,
            BALANCE_AMOUNT  = V_BALANCE_AMOUNT,
            STATUS          = V_NEW_STATUS,
            UPDATED_AT      = SYSTIMESTAMP,
            UPDATED_BY      = P_UPDATED_BY
        WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID;
    END RECALCULATE_STUDENT_FEE;

    ------------------------------------------------------------------
    -- Create discount request
    ------------------------------------------------------------------
    PROCEDURE CREATE_DISCOUNT(
        P_STUDENT_FEE_ID         IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_ID%TYPE,
        P_DISCOUNT_TYPE          IN STUDENT_FEE_DISCOUNT.DISCOUNT_TYPE%TYPE,
        P_DISCOUNT_VALUE         IN STUDENT_FEE_DISCOUNT.DISCOUNT_VALUE%TYPE,
        P_REASON                 IN STUDENT_FEE_DISCOUNT.REASON%TYPE,
        P_CREATED_BY             IN STUDENT_FEE_DISCOUNT.CREATED_BY%TYPE,
        P_STUDENT_FEE_DISCOUNT_ID
            OUT STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE
    ) AS
        V_DISCOUNT_TYPE   STUDENT_FEE_DISCOUNT.DISCOUNT_TYPE%TYPE;
        V_BASE_AMOUNT     STUDENT_FEE.BASE_AMOUNT%TYPE;
        V_FEE_STATUS      STUDENT_FEE.STATUS%TYPE;
        V_DISCOUNT_AMOUNT STUDENT_FEE_DISCOUNT.DISCOUNT_AMOUNT%TYPE;
    BEGIN
        IF P_STUDENT_FEE_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20968,
                    'STUDENT FEE ID IS REQUIRED'
            );
        END IF;

        IF TRIM(P_REASON) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20969,
                    'DISCOUNT REASON IS REQUIRED'
            );
        END IF;

        V_DISCOUNT_TYPE :=
                UPPER(TRIM(P_DISCOUNT_TYPE));

        VALIDATE_DISCOUNT(
                V_DISCOUNT_TYPE,
                P_DISCOUNT_VALUE
        );

        BEGIN
            SELECT BASE_AMOUNT,
                   STATUS
            INTO V_BASE_AMOUNT,
                V_FEE_STATUS
            FROM STUDENT_FEE
            WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20964,
                        'STUDENT FEE NOT FOUND'
                );
        END;

        IF V_FEE_STATUS IN (
                            'PAID',
                            'WAIVED',
                            'CANCELLED'
            ) THEN
            RAISE_APPLICATION_ERROR(
                    -20965,
                    'DISCOUNT CANNOT BE REQUESTED FOR PAID, WAIVED OR CANCELLED FEE'
            );
        END IF;

        V_DISCOUNT_AMOUNT := CASE V_DISCOUNT_TYPE
                                 WHEN 'FIXED' THEN P_DISCOUNT_VALUE
                                 WHEN 'PERCENTAGE' THEN
                                     ROUND(
                                             V_BASE_AMOUNT
                                                 * P_DISCOUNT_VALUE / 100,
                                             2
                                     )
            END;

        IF V_DISCOUNT_AMOUNT > V_BASE_AMOUNT THEN
            RAISE_APPLICATION_ERROR(
                    -20966,
                    'DISCOUNT CANNOT EXCEED BASE AMOUNT'
            );
        END IF;

        INSERT INTO STUDENT_FEE_DISCOUNT (
            STUDENT_FEE_ID,
            DISCOUNT_TYPE,
            DISCOUNT_VALUE,
            DISCOUNT_AMOUNT,
            REASON,
            APPROVAL_STATUS,
            CREATED_BY
        ) VALUES (
                     P_STUDENT_FEE_ID,
                     V_DISCOUNT_TYPE,
                     P_DISCOUNT_VALUE,
                     V_DISCOUNT_AMOUNT,
                     TRIM(P_REASON),
                     'PENDING',
                     P_CREATED_BY
                 )
        RETURNING STUDENT_FEE_DISCOUNT_ID
        INTO P_STUDENT_FEE_DISCOUNT_ID;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20970,
                    'ERROR CREATING STUDENT FEE DISCOUNT: '
                        || SQLERRM
            );
    END CREATE_DISCOUNT;

    ------------------------------------------------------------------
    -- Get discount by ID
    ------------------------------------------------------------------
    PROCEDURE GET_DISCOUNT_BY_ID(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM STUDENT_FEE_DISCOUNT
        WHERE STUDENT_FEE_DISCOUNT_ID =
              P_STUDENT_FEE_DISCOUNT_ID;

        IF V_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20971,
                    'STUDENT FEE DISCOUNT NOT FOUND'
            );
        END IF;

        OPEN P_RESULT FOR
            SELECT SFD.*
            FROM STUDENT_FEE_DISCOUNT SFD
            WHERE SFD.STUDENT_FEE_DISCOUNT_ID =
                  P_STUDENT_FEE_DISCOUNT_ID;
    END GET_DISCOUNT_BY_ID;

    ------------------------------------------------------------------
    -- Get all discounts
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_DISCOUNTS(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SFD.*
            FROM STUDENT_FEE_DISCOUNT SFD
            ORDER BY SFD.CREATED_AT DESC,
                     SFD.STUDENT_FEE_DISCOUNT_ID DESC;
    END GET_ALL_DISCOUNTS;

    ------------------------------------------------------------------
    -- Get by student fee
    ------------------------------------------------------------------
    PROCEDURE GET_DISCOUNTS_BY_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_ID%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SFD.*
            FROM STUDENT_FEE_DISCOUNT SFD
            WHERE SFD.STUDENT_FEE_ID =
                  P_STUDENT_FEE_ID
            ORDER BY SFD.CREATED_AT DESC,
                     SFD.STUDENT_FEE_DISCOUNT_ID DESC;
    END GET_DISCOUNTS_BY_STUDENT_FEE;

    ------------------------------------------------------------------
    -- Get by approval status
    ------------------------------------------------------------------
    PROCEDURE GET_DISCOUNTS_BY_STATUS(
        P_APPROVAL_STATUS
            IN STUDENT_FEE_DISCOUNT.APPROVAL_STATUS%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    ) AS
        V_APPROVAL_STATUS
            STUDENT_FEE_DISCOUNT.APPROVAL_STATUS%TYPE;
    BEGIN
        V_APPROVAL_STATUS :=
                UPPER(TRIM(P_APPROVAL_STATUS));

        VALIDATE_APPROVAL_STATUS(V_APPROVAL_STATUS);

        OPEN P_RESULT FOR
            SELECT SFD.*
            FROM STUDENT_FEE_DISCOUNT SFD
            WHERE SFD.APPROVAL_STATUS =
                  V_APPROVAL_STATUS
            ORDER BY SFD.CREATED_AT,
                     SFD.STUDENT_FEE_DISCOUNT_ID;
    END GET_DISCOUNTS_BY_STATUS;

    ------------------------------------------------------------------
    -- Approve discount
    ------------------------------------------------------------------
    PROCEDURE APPROVE_DISCOUNT(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE,
        P_APPROVED_BY IN STUDENT_FEE_DISCOUNT.APPROVED_BY%TYPE
    ) AS
        V_STUDENT_FEE_ID
            STUDENT_FEE_DISCOUNT.STUDENT_FEE_ID%TYPE;
        V_APPROVAL_STATUS
            STUDENT_FEE_DISCOUNT.APPROVAL_STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT STUDENT_FEE_ID,
                   APPROVAL_STATUS
            INTO V_STUDENT_FEE_ID,
                V_APPROVAL_STATUS
            FROM STUDENT_FEE_DISCOUNT
            WHERE STUDENT_FEE_DISCOUNT_ID =
                  P_STUDENT_FEE_DISCOUNT_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20971,
                        'STUDENT FEE DISCOUNT NOT FOUND'
                );
        END;

        IF V_APPROVAL_STATUS <> 'PENDING' THEN
            RAISE_APPLICATION_ERROR(
                    -20972,
                    'ONLY PENDING DISCOUNT CAN BE APPROVED'
            );
        END IF;

        UPDATE STUDENT_FEE_DISCOUNT
        SET APPROVAL_STATUS = 'APPROVED',
            APPROVED_BY     = P_APPROVED_BY,
            APPROVED_AT     = SYSTIMESTAMP,
            UPDATED_AT      = SYSTIMESTAMP,
            UPDATED_BY      = P_APPROVED_BY
        WHERE STUDENT_FEE_DISCOUNT_ID =
              P_STUDENT_FEE_DISCOUNT_ID;

        RECALCULATE_STUDENT_FEE(
                V_STUDENT_FEE_ID,
                P_APPROVED_BY
        );
    END APPROVE_DISCOUNT;

    ------------------------------------------------------------------
    -- Reject discount
    ------------------------------------------------------------------
    PROCEDURE REJECT_DISCOUNT(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE,
        P_UPDATED_BY IN STUDENT_FEE_DISCOUNT.UPDATED_BY%TYPE
    ) AS
        V_APPROVAL_STATUS
            STUDENT_FEE_DISCOUNT.APPROVAL_STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT APPROVAL_STATUS
            INTO V_APPROVAL_STATUS
            FROM STUDENT_FEE_DISCOUNT
            WHERE STUDENT_FEE_DISCOUNT_ID =
                  P_STUDENT_FEE_DISCOUNT_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20971,
                        'STUDENT FEE DISCOUNT NOT FOUND'
                );
        END;

        IF V_APPROVAL_STATUS <> 'PENDING' THEN
            RAISE_APPLICATION_ERROR(
                    -20973,
                    'ONLY PENDING DISCOUNT CAN BE REJECTED'
            );
        END IF;

        UPDATE STUDENT_FEE_DISCOUNT
        SET APPROVAL_STATUS = 'REJECTED',
            UPDATED_AT      = SYSTIMESTAMP,
            UPDATED_BY      = P_UPDATED_BY
        WHERE STUDENT_FEE_DISCOUNT_ID =
              P_STUDENT_FEE_DISCOUNT_ID;
    END REJECT_DISCOUNT;

    ------------------------------------------------------------------
    -- Cancel discount
    ------------------------------------------------------------------
    PROCEDURE CANCEL_DISCOUNT(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE,
        P_UPDATED_BY IN STUDENT_FEE_DISCOUNT.UPDATED_BY%TYPE
    ) AS
        V_STUDENT_FEE_ID
            STUDENT_FEE_DISCOUNT.STUDENT_FEE_ID%TYPE;
        V_APPROVAL_STATUS
            STUDENT_FEE_DISCOUNT.APPROVAL_STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT STUDENT_FEE_ID,
                   APPROVAL_STATUS
            INTO V_STUDENT_FEE_ID,
                V_APPROVAL_STATUS
            FROM STUDENT_FEE_DISCOUNT
            WHERE STUDENT_FEE_DISCOUNT_ID =
                  P_STUDENT_FEE_DISCOUNT_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20971,
                        'STUDENT FEE DISCOUNT NOT FOUND'
                );
        END;

        IF V_APPROVAL_STATUS = 'CANCELLED' THEN
            RAISE_APPLICATION_ERROR(
                    -20974,
                    'DISCOUNT IS ALREADY CANCELLED'
            );
        END IF;

        IF V_APPROVAL_STATUS = 'REJECTED' THEN
            RAISE_APPLICATION_ERROR(
                    -20974,
                    'REJECTED DISCOUNT CANNOT BE CANCELLED'
            );
        END IF;

        UPDATE STUDENT_FEE_DISCOUNT
        SET APPROVAL_STATUS = 'CANCELLED',
            UPDATED_AT      = SYSTIMESTAMP,
            UPDATED_BY      = P_UPDATED_BY
        WHERE STUDENT_FEE_DISCOUNT_ID =
              P_STUDENT_FEE_DISCOUNT_ID;

        IF V_APPROVAL_STATUS = 'APPROVED' THEN
            RECALCULATE_STUDENT_FEE(
                    V_STUDENT_FEE_ID,
                    P_UPDATED_BY
            );
        END IF;
    END CANCEL_DISCOUNT;

    ------------------------------------------------------------------
    -- Delete discount
    ------------------------------------------------------------------
    PROCEDURE DELETE_DISCOUNT(
        P_STUDENT_FEE_DISCOUNT_ID
            IN STUDENT_FEE_DISCOUNT.STUDENT_FEE_DISCOUNT_ID%TYPE
    ) AS
        V_APPROVAL_STATUS
            STUDENT_FEE_DISCOUNT.APPROVAL_STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT APPROVAL_STATUS
            INTO V_APPROVAL_STATUS
            FROM STUDENT_FEE_DISCOUNT
            WHERE STUDENT_FEE_DISCOUNT_ID =
                  P_STUDENT_FEE_DISCOUNT_ID;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20971,
                        'STUDENT FEE DISCOUNT NOT FOUND'
                );
        END;

        IF V_APPROVAL_STATUS = 'APPROVED' THEN
            RAISE_APPLICATION_ERROR(
                    -20974,
                    'APPROVED DISCOUNT MUST BE CANCELLED BEFORE DELETION'
            );
        END IF;

        DELETE FROM STUDENT_FEE_DISCOUNT
        WHERE STUDENT_FEE_DISCOUNT_ID =
              P_STUDENT_FEE_DISCOUNT_ID;
    END DELETE_DISCOUNT;

END STUDENT_FEE_DISCOUNT_PKG;
/