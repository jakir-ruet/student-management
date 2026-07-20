----------------------------------------------------------------------
-- V23: FEE PAYMENT PACKAGE
----------------------------------------------------------------------

CREATE OR REPLACE PACKAGE FEE_PAYMENT_PKG AS

    PROCEDURE CREATE_PAYMENT(
        P_STUDENT_ID            IN FEE_PAYMENT.STUDENT_ID%TYPE,
        P_RECEIPT_NUMBER        IN FEE_PAYMENT.RECEIPT_NUMBER%TYPE,
        P_PAYMENT_DATE          IN FEE_PAYMENT.PAYMENT_DATE%TYPE,
        P_PAYMENT_AMOUNT        IN FEE_PAYMENT.PAYMENT_AMOUNT%TYPE,
        P_PAYMENT_METHOD        IN FEE_PAYMENT.PAYMENT_METHOD%TYPE,
        P_TRANSACTION_REFERENCE IN FEE_PAYMENT.TRANSACTION_REFERENCE%TYPE,
        P_NOTES                 IN FEE_PAYMENT.NOTES%TYPE,
        P_RECEIVED_BY           IN FEE_PAYMENT.RECEIVED_BY%TYPE,
        P_CREATED_BY            IN FEE_PAYMENT.CREATED_BY%TYPE,
        P_FEE_PAYMENT_ID        OUT FEE_PAYMENT.FEE_PAYMENT_ID%TYPE
    );

    PROCEDURE ALLOCATE_PAYMENT(
        P_FEE_PAYMENT_ID       IN FEE_PAYMENT_ALLOCATION.FEE_PAYMENT_ID%TYPE,
        P_STUDENT_FEE_ID       IN FEE_PAYMENT_ALLOCATION.STUDENT_FEE_ID%TYPE,
        P_ALLOCATED_AMOUNT     IN FEE_PAYMENT_ALLOCATION.ALLOCATED_AMOUNT%TYPE,
        P_CREATED_BY           IN FEE_PAYMENT_ALLOCATION.CREATED_BY%TYPE,
        P_PAYMENT_ALLOCATION_ID
            OUT FEE_PAYMENT_ALLOCATION.PAYMENT_ALLOCATION_ID%TYPE
    );

    PROCEDURE COMPLETE_PAYMENT(
        P_FEE_PAYMENT_ID IN FEE_PAYMENT.FEE_PAYMENT_ID%TYPE,
        P_UPDATED_BY     IN FEE_PAYMENT.UPDATED_BY%TYPE
    );

    PROCEDURE CANCEL_PAYMENT(
        P_FEE_PAYMENT_ID IN FEE_PAYMENT.FEE_PAYMENT_ID%TYPE,
        P_UPDATED_BY     IN FEE_PAYMENT.UPDATED_BY%TYPE
    );

    PROCEDURE REFUND_PAYMENT(
        P_FEE_PAYMENT_ID IN FEE_PAYMENT.FEE_PAYMENT_ID%TYPE,
        P_UPDATED_BY     IN FEE_PAYMENT.UPDATED_BY%TYPE
    );

    PROCEDURE GET_PAYMENT_BY_ID(
        P_FEE_PAYMENT_ID IN FEE_PAYMENT.FEE_PAYMENT_ID%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ALL_PAYMENTS(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_PAYMENTS_BY_STUDENT(
        P_STUDENT_ID IN FEE_PAYMENT.STUDENT_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    );

    PROCEDURE GET_PAYMENTS_BY_STATUS(
        P_PAYMENT_STATUS IN FEE_PAYMENT.PAYMENT_STATUS%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ALLOCATIONS_BY_PAYMENT(
        P_FEE_PAYMENT_ID
            IN FEE_PAYMENT_ALLOCATION.FEE_PAYMENT_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE DELETE_PAYMENT_ALLOCATION(
        P_PAYMENT_ALLOCATION_ID
            IN FEE_PAYMENT_ALLOCATION.PAYMENT_ALLOCATION_ID%TYPE
    );

END FEE_PAYMENT_PKG;
/

CREATE OR REPLACE PACKAGE BODY FEE_PAYMENT_PKG AS

    ------------------------------------------------------------------
    -- Validate payment method
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_PAYMENT_METHOD(
        P_PAYMENT_METHOD IN FEE_PAYMENT.PAYMENT_METHOD%TYPE
    ) AS
    BEGIN
        IF P_PAYMENT_METHOD IS NULL
            OR P_PAYMENT_METHOD NOT IN (
                                        'CASH',
                                        'CARD',
                                        'BANK_TRANSFER',
                                        'MOBILE_BANKING',
                                        'CHEQUE',
                                        'ONLINE'
                ) THEN
            RAISE_APPLICATION_ERROR(
                    -20975,
                    'INVALID PAYMENT METHOD'
            );
        END IF;
    END VALIDATE_PAYMENT_METHOD;

    ------------------------------------------------------------------
    -- Validate payment status
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_PAYMENT_STATUS(
        P_PAYMENT_STATUS IN FEE_PAYMENT.PAYMENT_STATUS%TYPE
    ) AS
    BEGIN
        IF P_PAYMENT_STATUS IS NULL
            OR P_PAYMENT_STATUS NOT IN (
                                        'PENDING',
                                        'COMPLETED',
                                        'FAILED',
                                        'CANCELLED',
                                        'REFUNDED'
                ) THEN
            RAISE_APPLICATION_ERROR(
                    -20976,
                    'INVALID PAYMENT STATUS'
            );
        END IF;
    END VALIDATE_PAYMENT_STATUS;

    ------------------------------------------------------------------
    -- Create payment header
    ------------------------------------------------------------------
    PROCEDURE CREATE_PAYMENT(
        P_STUDENT_ID            IN FEE_PAYMENT.STUDENT_ID%TYPE,
        P_RECEIPT_NUMBER        IN FEE_PAYMENT.RECEIPT_NUMBER%TYPE,
        P_PAYMENT_DATE          IN FEE_PAYMENT.PAYMENT_DATE%TYPE,
        P_PAYMENT_AMOUNT        IN FEE_PAYMENT.PAYMENT_AMOUNT%TYPE,
        P_PAYMENT_METHOD        IN FEE_PAYMENT.PAYMENT_METHOD%TYPE,
        P_TRANSACTION_REFERENCE IN FEE_PAYMENT.TRANSACTION_REFERENCE%TYPE,
        P_NOTES                 IN FEE_PAYMENT.NOTES%TYPE,
        P_RECEIVED_BY           IN FEE_PAYMENT.RECEIVED_BY%TYPE,
        P_CREATED_BY            IN FEE_PAYMENT.CREATED_BY%TYPE,
        P_FEE_PAYMENT_ID        OUT FEE_PAYMENT.FEE_PAYMENT_ID%TYPE
    ) AS
        V_PAYMENT_METHOD FEE_PAYMENT.PAYMENT_METHOD%TYPE;
    BEGIN
        IF P_STUDENT_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20977,
                    'STUDENT ID IS REQUIRED'
            );
        END IF;

        IF TRIM(P_RECEIPT_NUMBER) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20978,
                    'RECEIPT NUMBER IS REQUIRED'
            );
        END IF;

        IF P_PAYMENT_AMOUNT IS NULL
            OR P_PAYMENT_AMOUNT <= 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20979,
                    'PAYMENT AMOUNT MUST BE GREATER THAN ZERO'
            );
        END IF;

        V_PAYMENT_METHOD :=
                UPPER(TRIM(P_PAYMENT_METHOD));

        VALIDATE_PAYMENT_METHOD(V_PAYMENT_METHOD);

        INSERT INTO FEE_PAYMENT (
            STUDENT_ID,
            RECEIPT_NUMBER,
            PAYMENT_DATE,
            PAYMENT_AMOUNT,
            PAYMENT_METHOD,
            TRANSACTION_REFERENCE,
            PAYMENT_STATUS,
            NOTES,
            RECEIVED_BY,
            CREATED_BY
        ) VALUES (
                     P_STUDENT_ID,
                     UPPER(TRIM(P_RECEIPT_NUMBER)),
                     NVL(P_PAYMENT_DATE, SYSTIMESTAMP),
                     P_PAYMENT_AMOUNT,
                     V_PAYMENT_METHOD,
                     TRIM(P_TRANSACTION_REFERENCE),
                     'PENDING',
                     P_NOTES,
                     P_RECEIVED_BY,
                     P_CREATED_BY
                 )
        RETURNING FEE_PAYMENT_ID
        INTO P_FEE_PAYMENT_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20980,
                    'RECEIPT NUMBER ALREADY EXISTS'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20995,
                    'ERROR CREATING FEE PAYMENT: ' || SQLERRM
            );
    END CREATE_PAYMENT;

    ------------------------------------------------------------------
    -- Allocate payment to student fee
    ------------------------------------------------------------------
    PROCEDURE ALLOCATE_PAYMENT(
        P_FEE_PAYMENT_ID       IN FEE_PAYMENT_ALLOCATION.FEE_PAYMENT_ID%TYPE,
        P_STUDENT_FEE_ID       IN FEE_PAYMENT_ALLOCATION.STUDENT_FEE_ID%TYPE,
        P_ALLOCATED_AMOUNT     IN FEE_PAYMENT_ALLOCATION.ALLOCATED_AMOUNT%TYPE,
        P_CREATED_BY           IN FEE_PAYMENT_ALLOCATION.CREATED_BY%TYPE,
        P_PAYMENT_ALLOCATION_ID
            OUT FEE_PAYMENT_ALLOCATION.PAYMENT_ALLOCATION_ID%TYPE
    ) AS
        V_PAYMENT_STUDENT_ID FEE_PAYMENT.STUDENT_ID%TYPE;
        V_PAYMENT_AMOUNT     FEE_PAYMENT.PAYMENT_AMOUNT%TYPE;
        V_PAYMENT_STATUS     FEE_PAYMENT.PAYMENT_STATUS%TYPE;
        V_FEE_STUDENT_ID     STUDENT_FEE.STUDENT_ID%TYPE;
        V_BALANCE_AMOUNT     STUDENT_FEE.BALANCE_AMOUNT%TYPE;
        V_FEE_STATUS         STUDENT_FEE.STATUS%TYPE;
        V_ALLOCATED_TOTAL    NUMBER(12,2);
    BEGIN
        IF P_ALLOCATED_AMOUNT IS NULL
            OR P_ALLOCATED_AMOUNT <= 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20981,
                    'ALLOCATED AMOUNT MUST BE GREATER THAN ZERO'
            );
        END IF;

        BEGIN
            SELECT STUDENT_ID,
                   PAYMENT_AMOUNT,
                   PAYMENT_STATUS
            INTO V_PAYMENT_STUDENT_ID,
                V_PAYMENT_AMOUNT,
                V_PAYMENT_STATUS
            FROM FEE_PAYMENT
            WHERE FEE_PAYMENT_ID = P_FEE_PAYMENT_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20982,
                        'FEE PAYMENT NOT FOUND'
                );
        END;

        IF V_PAYMENT_STATUS <> 'PENDING' THEN
            RAISE_APPLICATION_ERROR(
                    -20983,
                    'ONLY PENDING PAYMENT CAN BE ALLOCATED'
            );
        END IF;

        BEGIN
            SELECT STUDENT_ID,
                   BALANCE_AMOUNT,
                   STATUS
            INTO V_FEE_STUDENT_ID,
                V_BALANCE_AMOUNT,
                V_FEE_STATUS
            FROM STUDENT_FEE
            WHERE STUDENT_FEE_ID = P_STUDENT_FEE_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20984,
                        'STUDENT FEE NOT FOUND'
                );
        END;

        IF V_PAYMENT_STUDENT_ID <> V_FEE_STUDENT_ID THEN
            RAISE_APPLICATION_ERROR(
                    -20985,
                    'PAYMENT AND STUDENT FEE BELONG TO DIFFERENT STUDENTS'
            );
        END IF;

        IF V_FEE_STATUS IN (
                            'PAID',
                            'WAIVED',
                            'CANCELLED'
            ) THEN
            RAISE_APPLICATION_ERROR(
                    -20986,
                    'PAYMENT CANNOT BE ALLOCATED TO PAID, WAIVED OR CANCELLED FEE'
            );
        END IF;

        IF P_ALLOCATED_AMOUNT > V_BALANCE_AMOUNT THEN
            RAISE_APPLICATION_ERROR(
                    -20987,
                    'ALLOCATED AMOUNT EXCEEDS STUDENT FEE BALANCE'
            );
        END IF;

        SELECT NVL(SUM(ALLOCATED_AMOUNT), 0)
        INTO V_ALLOCATED_TOTAL
        FROM FEE_PAYMENT_ALLOCATION
        WHERE FEE_PAYMENT_ID = P_FEE_PAYMENT_ID;

        IF V_ALLOCATED_TOTAL + P_ALLOCATED_AMOUNT
            > V_PAYMENT_AMOUNT THEN
            RAISE_APPLICATION_ERROR(
                    -20988,
                    'TOTAL ALLOCATION EXCEEDS PAYMENT AMOUNT'
            );
        END IF;

        INSERT INTO FEE_PAYMENT_ALLOCATION (
            FEE_PAYMENT_ID,
            STUDENT_FEE_ID,
            ALLOCATED_AMOUNT,
            CREATED_BY
        ) VALUES (
                     P_FEE_PAYMENT_ID,
                     P_STUDENT_FEE_ID,
                     P_ALLOCATED_AMOUNT,
                     P_CREATED_BY
                 )
        RETURNING PAYMENT_ALLOCATION_ID
        INTO P_PAYMENT_ALLOCATION_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20989,
                    'PAYMENT IS ALREADY ALLOCATED TO THIS STUDENT FEE'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20996,
                    'ERROR ALLOCATING FEE PAYMENT: ' || SQLERRM
            );
    END ALLOCATE_PAYMENT;

    ------------------------------------------------------------------
    -- Complete payment
    ------------------------------------------------------------------
    PROCEDURE COMPLETE_PAYMENT(
        P_FEE_PAYMENT_ID IN FEE_PAYMENT.FEE_PAYMENT_ID%TYPE,
        P_UPDATED_BY     IN FEE_PAYMENT.UPDATED_BY%TYPE
    ) AS
        V_PAYMENT_AMOUNT  FEE_PAYMENT.PAYMENT_AMOUNT%TYPE;
        V_PAYMENT_STATUS  FEE_PAYMENT.PAYMENT_STATUS%TYPE;
        V_ALLOCATED_TOTAL NUMBER(12,2);
        V_ALLOCATION_COUNT NUMBER;
        V_NEW_PAID_AMOUNT STUDENT_FEE.PAID_AMOUNT%TYPE;
        V_NEW_BALANCE     STUDENT_FEE.BALANCE_AMOUNT%TYPE;
        V_NEW_STATUS      STUDENT_FEE.STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT PAYMENT_AMOUNT,
                   PAYMENT_STATUS
            INTO V_PAYMENT_AMOUNT,
                V_PAYMENT_STATUS
            FROM FEE_PAYMENT
            WHERE FEE_PAYMENT_ID = P_FEE_PAYMENT_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20982,
                        'FEE PAYMENT NOT FOUND'
                );
        END;

        IF V_PAYMENT_STATUS <> 'PENDING' THEN
            RAISE_APPLICATION_ERROR(
                    -20990,
                    'ONLY PENDING PAYMENT CAN BE COMPLETED'
            );
        END IF;

        SELECT COUNT(*),
               NVL(SUM(ALLOCATED_AMOUNT), 0)
        INTO V_ALLOCATION_COUNT,
            V_ALLOCATED_TOTAL
        FROM FEE_PAYMENT_ALLOCATION
        WHERE FEE_PAYMENT_ID = P_FEE_PAYMENT_ID;

        IF V_ALLOCATION_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20991,
                    'PAYMENT HAS NO ALLOCATIONS'
            );
        END IF;

        IF V_ALLOCATED_TOTAL <> V_PAYMENT_AMOUNT THEN
            RAISE_APPLICATION_ERROR(
                    -20992,
                    'TOTAL ALLOCATION MUST EQUAL PAYMENT AMOUNT'
            );
        END IF;

        FOR ALLOCATION_RECORD IN (
            SELECT FPA.STUDENT_FEE_ID,
                   FPA.ALLOCATED_AMOUNT
            FROM FEE_PAYMENT_ALLOCATION FPA
            WHERE FPA.FEE_PAYMENT_ID =
                  P_FEE_PAYMENT_ID
            ORDER BY FPA.PAYMENT_ALLOCATION_ID
            ) LOOP
                DECLARE
                    V_TOTAL_AMOUNT   STUDENT_FEE.TOTAL_AMOUNT%TYPE;
                    V_PAID_AMOUNT    STUDENT_FEE.PAID_AMOUNT%TYPE;
                    V_BALANCE_AMOUNT STUDENT_FEE.BALANCE_AMOUNT%TYPE;
                    V_DUE_DATE       STUDENT_FEE.DUE_DATE%TYPE;
                    V_FEE_STATUS     STUDENT_FEE.STATUS%TYPE;
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
                        V_FEE_STATUS
                    FROM STUDENT_FEE
                    WHERE STUDENT_FEE_ID =
                          ALLOCATION_RECORD.STUDENT_FEE_ID
                        FOR UPDATE;

                    IF V_FEE_STATUS IN (
                                        'PAID',
                                        'WAIVED',
                                        'CANCELLED'
                        ) THEN
                        RAISE_APPLICATION_ERROR(
                                -20986,
                                'PAYMENT ALLOCATION CONTAINS AN UNAVAILABLE STUDENT FEE'
                        );
                    END IF;

                    IF ALLOCATION_RECORD.ALLOCATED_AMOUNT
                        > V_BALANCE_AMOUNT THEN
                        RAISE_APPLICATION_ERROR(
                                -20987,
                                'ALLOCATED AMOUNT EXCEEDS CURRENT STUDENT FEE BALANCE'
                        );
                    END IF;

                    V_NEW_PAID_AMOUNT :=
                            V_PAID_AMOUNT
                                + ALLOCATION_RECORD.ALLOCATED_AMOUNT;

                    V_NEW_BALANCE :=
                            V_TOTAL_AMOUNT - V_NEW_PAID_AMOUNT;

                    V_NEW_STATUS := CASE
                                        WHEN V_NEW_BALANCE = 0
                                            THEN 'PAID'
                                        WHEN V_NEW_PAID_AMOUNT > 0
                                            THEN 'PARTIALLY_PAID'
                                        WHEN TRUNC(V_DUE_DATE) < TRUNC(SYSDATE)
                                            THEN 'OVERDUE'
                                        ELSE 'PENDING'
                        END;

                    UPDATE STUDENT_FEE
                    SET PAID_AMOUNT    = V_NEW_PAID_AMOUNT,
                        BALANCE_AMOUNT = V_NEW_BALANCE,
                        STATUS         = V_NEW_STATUS,
                        UPDATED_AT     = SYSTIMESTAMP,
                        UPDATED_BY     = P_UPDATED_BY
                    WHERE STUDENT_FEE_ID =
                          ALLOCATION_RECORD.STUDENT_FEE_ID;
                END;
            END LOOP;

        UPDATE FEE_PAYMENT
        SET PAYMENT_STATUS = 'COMPLETED',
            UPDATED_AT     = SYSTIMESTAMP,
            UPDATED_BY     = P_UPDATED_BY
        WHERE FEE_PAYMENT_ID = P_FEE_PAYMENT_ID;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20997,
                    'ERROR COMPLETING FEE PAYMENT: ' || SQLERRM
            );
    END COMPLETE_PAYMENT;

    ------------------------------------------------------------------
    -- Reverse completed payment allocations
    ------------------------------------------------------------------
    PROCEDURE REVERSE_PAYMENT(
        P_FEE_PAYMENT_ID IN FEE_PAYMENT.FEE_PAYMENT_ID%TYPE,
        P_NEW_STATUS     IN FEE_PAYMENT.PAYMENT_STATUS%TYPE,
        P_UPDATED_BY     IN FEE_PAYMENT.UPDATED_BY%TYPE
    ) AS
        V_PAYMENT_STATUS FEE_PAYMENT.PAYMENT_STATUS%TYPE;
        V_NEW_PAID       STUDENT_FEE.PAID_AMOUNT%TYPE;
        V_NEW_BALANCE    STUDENT_FEE.BALANCE_AMOUNT%TYPE;
        V_NEW_FEE_STATUS STUDENT_FEE.STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT PAYMENT_STATUS
            INTO V_PAYMENT_STATUS
            FROM FEE_PAYMENT
            WHERE FEE_PAYMENT_ID = P_FEE_PAYMENT_ID
                FOR UPDATE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20982,
                        'FEE PAYMENT NOT FOUND'
                );
        END;

        IF P_NEW_STATUS = 'REFUNDED'
            AND V_PAYMENT_STATUS <> 'COMPLETED' THEN
            RAISE_APPLICATION_ERROR(
                    -20993,
                    'ONLY COMPLETED PAYMENT CAN BE REFUNDED'
            );
        END IF;

        IF P_NEW_STATUS = 'CANCELLED'
            AND V_PAYMENT_STATUS NOT IN (
                                         'PENDING',
                                         'COMPLETED'
                ) THEN
            RAISE_APPLICATION_ERROR(
                    -20994,
                    'ONLY PENDING OR COMPLETED PAYMENT CAN BE CANCELLED'
            );
        END IF;

        IF V_PAYMENT_STATUS = 'COMPLETED' THEN
            FOR ALLOCATION_RECORD IN (
                SELECT STUDENT_FEE_ID,
                       ALLOCATED_AMOUNT
                FROM FEE_PAYMENT_ALLOCATION
                WHERE FEE_PAYMENT_ID = P_FEE_PAYMENT_ID
                ORDER BY PAYMENT_ALLOCATION_ID
                ) LOOP
                    DECLARE
                        V_TOTAL_AMOUNT STUDENT_FEE.TOTAL_AMOUNT%TYPE;
                        V_PAID_AMOUNT  STUDENT_FEE.PAID_AMOUNT%TYPE;
                        V_DUE_DATE     STUDENT_FEE.DUE_DATE%TYPE;
                    BEGIN
                        SELECT TOTAL_AMOUNT,
                               PAID_AMOUNT,
                               DUE_DATE
                        INTO V_TOTAL_AMOUNT,
                            V_PAID_AMOUNT,
                            V_DUE_DATE
                        FROM STUDENT_FEE
                        WHERE STUDENT_FEE_ID =
                              ALLOCATION_RECORD.STUDENT_FEE_ID
                            FOR UPDATE;

                        V_NEW_PAID :=
                                GREATEST(
                                        V_PAID_AMOUNT
                                            - ALLOCATION_RECORD.ALLOCATED_AMOUNT,
                                        0
                                );

                        V_NEW_BALANCE :=
                                V_TOTAL_AMOUNT - V_NEW_PAID;

                        V_NEW_FEE_STATUS := CASE
                                                WHEN V_NEW_BALANCE = 0
                                                    THEN 'PAID'
                                                WHEN V_NEW_PAID > 0
                                                    THEN 'PARTIALLY_PAID'
                                                WHEN TRUNC(V_DUE_DATE) < TRUNC(SYSDATE)
                                                    THEN 'OVERDUE'
                                                ELSE 'PENDING'
                            END;

                        UPDATE STUDENT_FEE
                        SET PAID_AMOUNT    = V_NEW_PAID,
                            BALANCE_AMOUNT = V_NEW_BALANCE,
                            STATUS         = V_NEW_FEE_STATUS,
                            UPDATED_AT     = SYSTIMESTAMP,
                            UPDATED_BY     = P_UPDATED_BY
                        WHERE STUDENT_FEE_ID =
                              ALLOCATION_RECORD.STUDENT_FEE_ID;
                    END;
                END LOOP;
        END IF;

        UPDATE FEE_PAYMENT
        SET PAYMENT_STATUS = P_NEW_STATUS,
            UPDATED_AT     = SYSTIMESTAMP,
            UPDATED_BY     = P_UPDATED_BY
        WHERE FEE_PAYMENT_ID = P_FEE_PAYMENT_ID;
    END REVERSE_PAYMENT;

    ------------------------------------------------------------------
    -- Cancel payment
    ------------------------------------------------------------------
    PROCEDURE CANCEL_PAYMENT(
        P_FEE_PAYMENT_ID IN FEE_PAYMENT.FEE_PAYMENT_ID%TYPE,
        P_UPDATED_BY     IN FEE_PAYMENT.UPDATED_BY%TYPE
    ) AS
    BEGIN
        REVERSE_PAYMENT(
                P_FEE_PAYMENT_ID,
                'CANCELLED',
                P_UPDATED_BY
        );

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20998,
                    'ERROR CANCELLING FEE PAYMENT: ' || SQLERRM
            );
    END CANCEL_PAYMENT;

    ------------------------------------------------------------------
    -- Refund payment
    ------------------------------------------------------------------
    PROCEDURE REFUND_PAYMENT(
        P_FEE_PAYMENT_ID IN FEE_PAYMENT.FEE_PAYMENT_ID%TYPE,
        P_UPDATED_BY     IN FEE_PAYMENT.UPDATED_BY%TYPE
    ) AS
    BEGIN
        REVERSE_PAYMENT(
                P_FEE_PAYMENT_ID,
                'REFUNDED',
                P_UPDATED_BY
        );

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20999,
                    'ERROR REFUNDING FEE PAYMENT: ' || SQLERRM
            );
    END REFUND_PAYMENT;

    ------------------------------------------------------------------
    -- Get payment by ID
    ------------------------------------------------------------------
    PROCEDURE GET_PAYMENT_BY_ID(
        P_FEE_PAYMENT_ID IN FEE_PAYMENT.FEE_PAYMENT_ID%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM FEE_PAYMENT
        WHERE FEE_PAYMENT_ID = P_FEE_PAYMENT_ID;

        IF V_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20982,
                    'FEE PAYMENT NOT FOUND'
            );
        END IF;

        OPEN P_RESULT FOR
            SELECT FP.*
            FROM FEE_PAYMENT FP
            WHERE FP.FEE_PAYMENT_ID =
                  P_FEE_PAYMENT_ID;
    END GET_PAYMENT_BY_ID;

    ------------------------------------------------------------------
    -- Get all payments
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_PAYMENTS(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FP.*
            FROM FEE_PAYMENT FP
            ORDER BY FP.PAYMENT_DATE DESC,
                     FP.FEE_PAYMENT_ID DESC;
    END GET_ALL_PAYMENTS;

    ------------------------------------------------------------------
    -- Get payments by student
    ------------------------------------------------------------------
    PROCEDURE GET_PAYMENTS_BY_STUDENT(
        P_STUDENT_ID IN FEE_PAYMENT.STUDENT_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FP.*
            FROM FEE_PAYMENT FP
            WHERE FP.STUDENT_ID = P_STUDENT_ID
            ORDER BY FP.PAYMENT_DATE DESC,
                     FP.FEE_PAYMENT_ID DESC;
    END GET_PAYMENTS_BY_STUDENT;

    ------------------------------------------------------------------
    -- Get payments by status
    ------------------------------------------------------------------
    PROCEDURE GET_PAYMENTS_BY_STATUS(
        P_PAYMENT_STATUS IN FEE_PAYMENT.PAYMENT_STATUS%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    ) AS
        V_PAYMENT_STATUS FEE_PAYMENT.PAYMENT_STATUS%TYPE;
    BEGIN
        V_PAYMENT_STATUS :=
                UPPER(TRIM(P_PAYMENT_STATUS));

        VALIDATE_PAYMENT_STATUS(V_PAYMENT_STATUS);

        OPEN P_RESULT FOR
            SELECT FP.*
            FROM FEE_PAYMENT FP
            WHERE FP.PAYMENT_STATUS =
                  V_PAYMENT_STATUS
            ORDER BY FP.PAYMENT_DATE DESC,
                     FP.FEE_PAYMENT_ID DESC;
    END GET_PAYMENTS_BY_STATUS;

    ------------------------------------------------------------------
    -- Get allocations by payment
    ------------------------------------------------------------------
    PROCEDURE GET_ALLOCATIONS_BY_PAYMENT(
        P_FEE_PAYMENT_ID
            IN FEE_PAYMENT_ALLOCATION.FEE_PAYMENT_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FPA.*
            FROM FEE_PAYMENT_ALLOCATION FPA
            WHERE FPA.FEE_PAYMENT_ID =
                  P_FEE_PAYMENT_ID
            ORDER BY FPA.PAYMENT_ALLOCATION_ID;
    END GET_ALLOCATIONS_BY_PAYMENT;

    ------------------------------------------------------------------
    -- Delete allocation from pending payment
    ------------------------------------------------------------------
    PROCEDURE DELETE_PAYMENT_ALLOCATION(
        P_PAYMENT_ALLOCATION_ID
            IN FEE_PAYMENT_ALLOCATION.PAYMENT_ALLOCATION_ID%TYPE
    ) AS
        V_PAYMENT_STATUS FEE_PAYMENT.PAYMENT_STATUS%TYPE;
    BEGIN
        BEGIN
            SELECT FP.PAYMENT_STATUS
            INTO V_PAYMENT_STATUS
            FROM FEE_PAYMENT_ALLOCATION FPA
                     JOIN FEE_PAYMENT FP
                          ON FP.FEE_PAYMENT_ID =
                             FPA.FEE_PAYMENT_ID
            WHERE FPA.PAYMENT_ALLOCATION_ID =
                  P_PAYMENT_ALLOCATION_ID;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20994,
                        'PAYMENT ALLOCATION NOT FOUND'
                );
        END;

        IF V_PAYMENT_STATUS <> 'PENDING' THEN
            RAISE_APPLICATION_ERROR(
                    -20983,
                    'ONLY PENDING PAYMENT ALLOCATION CAN BE DELETED'
            );
        END IF;

        DELETE FROM FEE_PAYMENT_ALLOCATION
        WHERE PAYMENT_ALLOCATION_ID =
              P_PAYMENT_ALLOCATION_ID;
    END DELETE_PAYMENT_ALLOCATION;

END FEE_PAYMENT_PKG;
/