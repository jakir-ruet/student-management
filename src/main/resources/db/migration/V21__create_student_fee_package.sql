----------------------------------------------------------------------
-- V21: STUDENT FEE PACKAGE
----------------------------------------------------------------------

CREATE OR REPLACE PACKAGE STUDENT_FEE_PKG AS

    PROCEDURE ASSIGN_STUDENT_FEE(
        P_FEE_STRUCTURE_ID IN STUDENT_FEE.FEE_STRUCTURE_ID%TYPE,
        P_STUDENT_ID       IN STUDENT_FEE.STUDENT_ID%TYPE,
        P_BILLING_PERIOD   IN STUDENT_FEE.BILLING_PERIOD%TYPE,
        P_DUE_DATE         IN STUDENT_FEE.DUE_DATE%TYPE,
        P_REMARKS          IN STUDENT_FEE.REMARKS%TYPE,
        P_CREATED_BY       IN STUDENT_FEE.CREATED_BY%TYPE,
        P_STUDENT_FEE_ID   OUT STUDENT_FEE.STUDENT_FEE_ID%TYPE
    );

    PROCEDURE UPDATE_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_DUE_DATE       IN STUDENT_FEE.DUE_DATE%TYPE,
        P_REMARKS        IN STUDENT_FEE.REMARKS%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    );

    PROCEDURE GET_STUDENT_FEE_BY_ID(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ALL_STUDENT_FEES(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_FEES_BY_STUDENT(
        P_STUDENT_ID IN STUDENT_FEE.STUDENT_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    );

    PROCEDURE GET_FEES_BY_STRUCTURE(
        P_FEE_STRUCTURE_ID IN STUDENT_FEE.FEE_STRUCTURE_ID%TYPE,
        P_RESULT           OUT SYS_REFCURSOR
    );

    PROCEDURE GET_FEES_BY_STATUS(
        P_STATUS IN STUDENT_FEE.STATUS%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_OUTSTANDING_FEES(
        P_STUDENT_ID IN STUDENT_FEE.STUDENT_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    );

    PROCEDURE APPLY_LATE_FINE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    );

    PROCEDURE REFRESH_FEE_STATUS(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    );

    PROCEDURE WAIVE_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_REMARKS        IN STUDENT_FEE.REMARKS%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    );

    PROCEDURE CANCEL_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_REMARKS        IN STUDENT_FEE.REMARKS%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    );

    PROCEDURE DELETE_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE
    );

END STUDENT_FEE_PKG;
/

CREATE OR REPLACE PACKAGE BODY STUDENT_FEE_PKG AS

    ------------------------------------------------------------------
    -- Validate student fee status
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_STATUS(
        P_STATUS IN STUDENT_FEE.STATUS%TYPE
    ) AS
    BEGIN
        IF P_STATUS IS NULL
            OR P_STATUS NOT IN (
                                'PENDING',
                                'PARTIALLY_PAID',
                                'PAID',
                                'OVERDUE',
                                'WAIVED',
                                'CANCELLED'
                ) THEN
            RAISE_APPLICATION_ERROR(
                    -20940,
                    'INVALID STUDENT FEE STATUS'
            );
        END IF;
    END VALIDATE_STATUS;

    ------------------------------------------------------------------
    -- Calculate status from financial values and due date
    ------------------------------------------------------------------
    FUNCTION CALCULATE_STATUS(
        P_TOTAL_AMOUNT   IN STUDENT_FEE.TOTAL_AMOUNT%TYPE,
        P_PAID_AMOUNT    IN STUDENT_FEE.PAID_AMOUNT%TYPE,
        P_BALANCE_AMOUNT IN STUDENT_FEE.BALANCE_AMOUNT%TYPE,
        P_DUE_DATE       IN STUDENT_FEE.DUE_DATE%TYPE,
        P_CURRENT_STATUS IN STUDENT_FEE.STATUS%TYPE
    ) RETURN STUDENT_FEE.STATUS%TYPE AS
    BEGIN
        IF P_CURRENT_STATUS IN ('WAIVED', 'CANCELLED') THEN
            RETURN P_CURRENT_STATUS;
        END IF;

        IF P_BALANCE_AMOUNT <= 0
            OR P_PAID_AMOUNT >= P_TOTAL_AMOUNT THEN
            RETURN 'PAID';
        END IF;

        IF P_PAID_AMOUNT > 0 THEN
            RETURN 'PARTIALLY_PAID';
        END IF;

        IF TRUNC(P_DUE_DATE) < TRUNC(SYSDATE) THEN
            RETURN 'OVERDUE';
        END IF;

        RETURN 'PENDING';
    END CALCULATE_STATUS;

    ------------------------------------------------------------------
    -- Assign student fee
    ------------------------------------------------------------------
    PROCEDURE ASSIGN_STUDENT_FEE(
        P_FEE_STRUCTURE_ID IN STUDENT_FEE.FEE_STRUCTURE_ID%TYPE,
        P_STUDENT_ID       IN STUDENT_FEE.STUDENT_ID%TYPE,
        P_BILLING_PERIOD   IN STUDENT_FEE.BILLING_PERIOD%TYPE,
        P_DUE_DATE         IN STUDENT_FEE.DUE_DATE%TYPE,
        P_REMARKS          IN STUDENT_FEE.REMARKS%TYPE,
        P_CREATED_BY       IN STUDENT_FEE.CREATED_BY%TYPE,
        P_STUDENT_FEE_ID   OUT STUDENT_FEE.STUDENT_FEE_ID%TYPE
    ) AS
        V_BASE_AMOUNT      FEE_STRUCTURE.AMOUNT%TYPE;
        V_STRUCTURE_STATUS FEE_STRUCTURE.STATUS%TYPE;
        V_COUNT            NUMBER;
        V_STATUS           STUDENT_FEE.STATUS%TYPE;
    BEGIN
        IF P_FEE_STRUCTURE_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20941,
                    'FEE STRUCTURE ID IS REQUIRED'
            );
        END IF;

        IF P_STUDENT_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20942,
                    'STUDENT ID IS REQUIRED'
            );
        END IF;

        IF TRIM(P_BILLING_PERIOD) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20943,
                    'BILLING PERIOD IS REQUIRED'
            );
        END IF;

        IF P_DUE_DATE IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20944,
                    'DUE DATE IS REQUIRED'
            );
        END IF;

        BEGIN
            SELECT AMOUNT,
                   STATUS
            INTO V_BASE_AMOUNT,
                V_STRUCTURE_STATUS
            FROM FEE_STRUCTURE
            WHERE FEE_STRUCTURE_ID = P_FEE_STRUCTURE_ID;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20945,
                        'FEE STRUCTURE NOT FOUND'
                );
        END;

        IF V_STRUCTURE_STATUS <> 'ACTIVE' THEN
            RAISE_APPLICATION_ERROR(
                    -20946,
                    'INACTIVE FEE STRUCTURE CANNOT BE ASSIGNED'
            );
        END IF;

        SELECT COUNT(*)
        INTO V_COUNT
        FROM STUDENT_FEE
        WHERE FEE_STRUCTURE_ID = P_FEE_STRUCTURE_ID
          AND STUDENT_ID = P_STUDENT_ID
          AND UPPER(TRIM(BILLING_PERIOD)) =
              UPPER(TRIM(P_BILLING_PERIOD));

        IF V_COUNT > 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20947,
                    'STUDENT FEE ALREADY EXISTS FOR THIS BILLING PERIOD'
            );
        END IF;

        V_STATUS := CASE
                        WHEN TRUNC(P_DUE_DATE) < TRUNC(SYSDATE)
                            THEN 'OVERDUE'
                        ELSE 'PENDING'
            END;

        INSERT INTO STUDENT_FEE (
            FEE_STRUCTURE_ID,
            STUDENT_ID,
            BILLING_PERIOD,
            DUE_DATE,
            BASE_AMOUNT,
            DISCOUNT_AMOUNT,
            FINE_AMOUNT,
            TOTAL_AMOUNT,
            PAID_AMOUNT,
            BALANCE_AMOUNT,
            STATUS,
            REMARKS,
            CREATED_BY
        ) VALUES (
                     P_FEE_STRUCTURE_ID,
                     P_STUDENT_ID,
                     UPPER(TRIM(P_BILLING_PERIOD)),
                     P_DUE_DATE,
                     V_BASE_AMOUNT,
                     0,
                     0,
                     V_BASE_AMOUNT,
                     0,
                     V_BASE_AMOUNT,
                     V_STATUS,
                     P_REMARKS,
                     P_CREATED_BY
                 )
        RETURNING STUDENT_FEE_ID
        INTO P_STUDENT_FEE_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20947,
                    'STUDENT FEE ALREADY EXISTS FOR THIS BILLING PERIOD'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20948,
                    'ERROR ASSIGNING STUDENT FEE: ' || SQLERRM
            );
    END ASSIGN_STUDENT_FEE;

    ------------------------------------------------------------------
    -- Update due date and remarks
    ------------------------------------------------------------------
    PROCEDURE UPDATE_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_DUE_DATE       IN STUDENT_FEE.DUE_DATE%TYPE,
        P_REMARKS        IN STUDENT_FEE.REMARKS%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    ) AS
        V_PAID_AMOUNT STUDENT_FEE.PAID_AMOUNT%TYPE;
        V_STATUS      STUDENT_FEE.STATUS%TYPE;
    BEGIN
        IF P_DUE_DATE IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20944,
                    'DUE DATE IS REQUIRED'
            );
        END IF;

        BEGIN
            SELECT PAID_AMOUNT,
                   STATUS
            INTO V_PAID_AMOUNT,
                V_STATUS
            FROM STUDENT_FEE
            WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20949,
                        'STUDENT FEE NOT FOUND'
                );
        END;

        IF V_STATUS IN ('WAIVED', 'CANCELLED') THEN
            RAISE_APPLICATION_ERROR(
                    -20950,
                    'WAIVED OR CANCELLED FEE CANNOT BE UPDATED'
            );
        END IF;

        UPDATE STUDENT_FEE
        SET DUE_DATE   = P_DUE_DATE,
            REMARKS    = P_REMARKS,
            STATUS     = CASE
                             WHEN V_PAID_AMOUNT > 0
                                 THEN 'PARTIALLY_PAID'
                             WHEN TRUNC(P_DUE_DATE) <
                                  TRUNC(SYSDATE)
                                 THEN 'OVERDUE'
                             ELSE 'PENDING'
                END,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20951,
                    'ERROR UPDATING STUDENT FEE: ' || SQLERRM
            );
    END UPDATE_STUDENT_FEE;

    ------------------------------------------------------------------
    -- Get by ID
    ------------------------------------------------------------------
    PROCEDURE GET_STUDENT_FEE_BY_ID(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM STUDENT_FEE
        WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID;

        IF V_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20949,
                    'STUDENT FEE NOT FOUND'
            );
        END IF;

        OPEN P_RESULT FOR
            SELECT SF.*
            FROM STUDENT_FEE SF
            WHERE SF.STUDENT_FEE_ID = P_STUDENT_FEE_ID;
    END GET_STUDENT_FEE_BY_ID;

    ------------------------------------------------------------------
    -- Get all
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_STUDENT_FEES(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SF.*
            FROM STUDENT_FEE SF
            ORDER BY SF.DUE_DATE DESC,
                     SF.STUDENT_FEE_ID DESC;
    END GET_ALL_STUDENT_FEES;

    ------------------------------------------------------------------
    -- Get by student
    ------------------------------------------------------------------
    PROCEDURE GET_FEES_BY_STUDENT(
        P_STUDENT_ID IN STUDENT_FEE.STUDENT_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SF.*
            FROM STUDENT_FEE SF
            WHERE SF.STUDENT_ID = P_STUDENT_ID
            ORDER BY SF.DUE_DATE DESC,
                     SF.STUDENT_FEE_ID DESC;
    END GET_FEES_BY_STUDENT;

    ------------------------------------------------------------------
    -- Get by fee structure
    ------------------------------------------------------------------
    PROCEDURE GET_FEES_BY_STRUCTURE(
        P_FEE_STRUCTURE_ID IN STUDENT_FEE.FEE_STRUCTURE_ID%TYPE,
        P_RESULT           OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SF.*
            FROM STUDENT_FEE SF
            WHERE SF.FEE_STRUCTURE_ID =
                  P_FEE_STRUCTURE_ID
            ORDER BY SF.STUDENT_ID,
                     SF.DUE_DATE;
    END GET_FEES_BY_STRUCTURE;

    ------------------------------------------------------------------
    -- Get by status
    ------------------------------------------------------------------
    PROCEDURE GET_FEES_BY_STATUS(
        P_STATUS IN STUDENT_FEE.STATUS%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    ) AS
        V_STATUS STUDENT_FEE.STATUS%TYPE;
    BEGIN
        V_STATUS := UPPER(TRIM(P_STATUS));

        VALIDATE_STATUS(V_STATUS);

        OPEN P_RESULT FOR
            SELECT SF.*
            FROM STUDENT_FEE SF
            WHERE SF.STATUS = V_STATUS
            ORDER BY SF.DUE_DATE,
                     SF.STUDENT_ID;
    END GET_FEES_BY_STATUS;

    ------------------------------------------------------------------
    -- Get outstanding fees for student
    ------------------------------------------------------------------
    PROCEDURE GET_OUTSTANDING_FEES(
        P_STUDENT_ID IN STUDENT_FEE.STUDENT_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SF.*
            FROM STUDENT_FEE SF
            WHERE SF.STUDENT_ID = P_STUDENT_ID
              AND SF.BALANCE_AMOUNT > 0
              AND SF.STATUS NOT IN (
                                    'WAIVED',
                                    'CANCELLED'
                )
            ORDER BY SF.DUE_DATE,
                     SF.STUDENT_FEE_ID;
    END GET_OUTSTANDING_FEES;

    ------------------------------------------------------------------
    -- Apply or recalculate late fine
    ------------------------------------------------------------------
    PROCEDURE APPLY_LATE_FINE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    ) AS
        V_BASE_AMOUNT     STUDENT_FEE.BASE_AMOUNT%TYPE;
        V_DISCOUNT_AMOUNT STUDENT_FEE.DISCOUNT_AMOUNT%TYPE;
        V_PAID_AMOUNT     STUDENT_FEE.PAID_AMOUNT%TYPE;
        V_DUE_DATE        STUDENT_FEE.DUE_DATE%TYPE;
        V_STATUS          STUDENT_FEE.STATUS%TYPE;
        V_FINE_TYPE       FEE_STRUCTURE.FINE_TYPE%TYPE;
        V_FINE_VALUE      FEE_STRUCTURE.FINE_VALUE%TYPE;
        V_FINE_AMOUNT     STUDENT_FEE.FINE_AMOUNT%TYPE;
        V_TOTAL_AMOUNT    STUDENT_FEE.TOTAL_AMOUNT%TYPE;
        V_BALANCE_AMOUNT  STUDENT_FEE.BALANCE_AMOUNT%TYPE;
    BEGIN
        BEGIN
            SELECT SF.BASE_AMOUNT,
                   SF.DISCOUNT_AMOUNT,
                   SF.PAID_AMOUNT,
                   SF.DUE_DATE,
                   SF.STATUS,
                   FS.FINE_TYPE,
                   FS.FINE_VALUE
            INTO V_BASE_AMOUNT,
                V_DISCOUNT_AMOUNT,
                V_PAID_AMOUNT,
                V_DUE_DATE,
                V_STATUS,
                V_FINE_TYPE,
                V_FINE_VALUE
            FROM STUDENT_FEE SF
                     JOIN FEE_STRUCTURE FS
                          ON FS.FEE_STRUCTURE_ID =
                             SF.FEE_STRUCTURE_ID
            WHERE SF.STUDENT_FEE_ID =
                  P_STUDENT_FEE_ID
                FOR UPDATE OF SF.FINE_AMOUNT;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20949,
                        'STUDENT FEE NOT FOUND'
                );
        END;

        IF V_STATUS IN ('PAID', 'WAIVED', 'CANCELLED') THEN
            RAISE_APPLICATION_ERROR(
                    -20952,
                    'FINE CANNOT BE APPLIED TO PAID, WAIVED OR CANCELLED FEE'
            );
        END IF;

        IF TRUNC(V_DUE_DATE) >= TRUNC(SYSDATE) THEN
            RAISE_APPLICATION_ERROR(
                    -20953,
                    'FEE IS NOT OVERDUE'
            );
        END IF;

        V_FINE_AMOUNT := CASE V_FINE_TYPE
                             WHEN 'FIXED' THEN V_FINE_VALUE
                             WHEN 'PERCENTAGE' THEN
                                 ROUND(
                                         (V_BASE_AMOUNT - V_DISCOUNT_AMOUNT)
                                             * V_FINE_VALUE / 100,
                                         2
                                 )
                             ELSE 0
            END;

        V_TOTAL_AMOUNT :=
                V_BASE_AMOUNT
                    - V_DISCOUNT_AMOUNT
                    + V_FINE_AMOUNT;

        V_BALANCE_AMOUNT :=
                GREATEST(
                        V_TOTAL_AMOUNT - V_PAID_AMOUNT,
                        0
                );

        UPDATE STUDENT_FEE
        SET FINE_AMOUNT    = V_FINE_AMOUNT,
            TOTAL_AMOUNT   = V_TOTAL_AMOUNT,
            BALANCE_AMOUNT = V_BALANCE_AMOUNT,
            STATUS         = CASE
                                 WHEN V_BALANCE_AMOUNT = 0
                                     THEN 'PAID'
                                 WHEN V_PAID_AMOUNT > 0
                                     THEN 'PARTIALLY_PAID'
                                 ELSE 'OVERDUE'
                END,
            UPDATED_AT     = SYSTIMESTAMP,
            UPDATED_BY     = P_UPDATED_BY
        WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID;
    END APPLY_LATE_FINE;

    ------------------------------------------------------------------
    -- Refresh calculated status
    ------------------------------------------------------------------
    PROCEDURE REFRESH_FEE_STATUS(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    ) AS
        V_TOTAL_AMOUNT   STUDENT_FEE.TOTAL_AMOUNT%TYPE;
        V_PAID_AMOUNT    STUDENT_FEE.PAID_AMOUNT%TYPE;
        V_BALANCE_AMOUNT STUDENT_FEE.BALANCE_AMOUNT%TYPE;
        V_DUE_DATE       STUDENT_FEE.DUE_DATE%TYPE;
        V_CURRENT_STATUS STUDENT_FEE.STATUS%TYPE;
        V_NEW_STATUS     STUDENT_FEE.STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT TOTAL_AMOUNT,
                   PAID_AMOUNT,
                   BALANCE_AMOUNT,
                   DUE_DATE,
                   STATUS
            INTO V_TOTAL_AMOUNT,
                V_PAID_AMOUNT,
                V_BALANCE_AMOUNT,
                V_DUE_DATE,
                V_CURRENT_STATUS
            FROM STUDENT_FEE
            WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20949,
                        'STUDENT FEE NOT FOUND'
                );
        END;

        V_NEW_STATUS := CALCULATE_STATUS(
                V_TOTAL_AMOUNT,
                V_PAID_AMOUNT,
                V_BALANCE_AMOUNT,
                V_DUE_DATE,
                V_CURRENT_STATUS
                        );

        UPDATE STUDENT_FEE
        SET STATUS     = V_NEW_STATUS,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID;
    END REFRESH_FEE_STATUS;

    ------------------------------------------------------------------
    -- Waive student fee
    ------------------------------------------------------------------
    PROCEDURE WAIVE_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_REMARKS        IN STUDENT_FEE.REMARKS%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    ) AS
        V_PAID_AMOUNT STUDENT_FEE.PAID_AMOUNT%TYPE;
        V_STATUS      STUDENT_FEE.STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT PAID_AMOUNT,
                   STATUS
            INTO V_PAID_AMOUNT,
                V_STATUS
            FROM STUDENT_FEE
            WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20949,
                        'STUDENT FEE NOT FOUND'
                );
        END;

        IF V_PAID_AMOUNT > 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20954,
                    'PAID OR PARTIALLY PAID FEE CANNOT BE WAIVED'
            );
        END IF;

        IF V_STATUS = 'CANCELLED' THEN
            RAISE_APPLICATION_ERROR(
                    -20955,
                    'CANCELLED FEE CANNOT BE WAIVED'
            );
        END IF;

        UPDATE STUDENT_FEE
        SET BALANCE_AMOUNT = 0,
            STATUS         = 'WAIVED',
            REMARKS        = P_REMARKS,
            UPDATED_AT     = SYSTIMESTAMP,
            UPDATED_BY     = P_UPDATED_BY
        WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID;
    END WAIVE_STUDENT_FEE;

    ------------------------------------------------------------------
    -- Cancel student fee
    ------------------------------------------------------------------
    PROCEDURE CANCEL_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE,
        P_REMARKS        IN STUDENT_FEE.REMARKS%TYPE,
        P_UPDATED_BY     IN STUDENT_FEE.UPDATED_BY%TYPE
    ) AS
        V_PAID_AMOUNT STUDENT_FEE.PAID_AMOUNT%TYPE;
    BEGIN
        BEGIN
            SELECT PAID_AMOUNT
            INTO V_PAID_AMOUNT
            FROM STUDENT_FEE
            WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20949,
                        'STUDENT FEE NOT FOUND'
                );
        END;

        IF V_PAID_AMOUNT > 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20956,
                    'PAID OR PARTIALLY PAID FEE CANNOT BE CANCELLED'
            );
        END IF;

        UPDATE STUDENT_FEE
        SET BALANCE_AMOUNT = 0,
            STATUS         = 'CANCELLED',
            REMARKS        = P_REMARKS,
            UPDATED_AT     = SYSTIMESTAMP,
            UPDATED_BY     = P_UPDATED_BY
        WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID;
    END CANCEL_STUDENT_FEE;

    ------------------------------------------------------------------
    -- Delete student fee
    ------------------------------------------------------------------
    PROCEDURE DELETE_STUDENT_FEE(
        P_STUDENT_FEE_ID IN STUDENT_FEE.STUDENT_FEE_ID%TYPE
    ) AS
    BEGIN
        DELETE FROM STUDENT_FEE
        WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20949,
                    'STUDENT FEE NOT FOUND'
            );
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2292 THEN
                RAISE_APPLICATION_ERROR(
                        -20957,
                        'STUDENT FEE CANNOT BE DELETED BECAUSE DISCOUNTS OR PAYMENTS EXIST'
                );
            END IF;

            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20958,
                    'ERROR DELETING STUDENT FEE: ' || SQLERRM
            );
    END DELETE_STUDENT_FEE;

END STUDENT_FEE_PKG;
/