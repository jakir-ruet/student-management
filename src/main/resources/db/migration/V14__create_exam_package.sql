----------------------------------------------------------------------
-- V14: EXAM PACKAGE
----------------------------------------------------------------------

CREATE OR REPLACE PACKAGE EXAM_PKG AS

    ------------------------------------------------------------------
    -- Create
    ------------------------------------------------------------------
    PROCEDURE CREATE_EXAM(
        P_ACADEMIC_YEAR_ID IN EXAM.ACADEMIC_YEAR_ID%TYPE,
        P_EXAM_NAME        IN EXAM.EXAM_NAME%TYPE,
        P_EXAM_CODE        IN EXAM.EXAM_CODE%TYPE,
        P_EXAM_TYPE        IN EXAM.EXAM_TYPE%TYPE,
        P_START_DATE       IN EXAM.START_DATE%TYPE,
        P_END_DATE         IN EXAM.END_DATE%TYPE,
        P_DESCRIPTION      IN EXAM.DESCRIPTION%TYPE,
        P_STATUS           IN EXAM.STATUS%TYPE,
        P_CREATED_BY       IN EXAM.CREATED_BY%TYPE,
        P_EXAM_ID          OUT EXAM.EXAM_ID%TYPE
    );

    ------------------------------------------------------------------
    -- Update
    ------------------------------------------------------------------
    PROCEDURE UPDATE_EXAM(
        P_EXAM_ID          IN EXAM.EXAM_ID%TYPE,
        P_ACADEMIC_YEAR_ID IN EXAM.ACADEMIC_YEAR_ID%TYPE,
        P_EXAM_NAME        IN EXAM.EXAM_NAME%TYPE,
        P_EXAM_CODE        IN EXAM.EXAM_CODE%TYPE,
        P_EXAM_TYPE        IN EXAM.EXAM_TYPE%TYPE,
        P_START_DATE       IN EXAM.START_DATE%TYPE,
        P_END_DATE         IN EXAM.END_DATE%TYPE,
        P_DESCRIPTION      IN EXAM.DESCRIPTION%TYPE,
        P_STATUS           IN EXAM.STATUS%TYPE,
        P_UPDATED_BY       IN EXAM.UPDATED_BY%TYPE
    );

    ------------------------------------------------------------------
    -- Find by ID
    ------------------------------------------------------------------
    PROCEDURE GET_EXAM_BY_ID(
        P_EXAM_ID IN EXAM.EXAM_ID%TYPE,
        P_RESULT  OUT SYS_REFCURSOR
    );

    ------------------------------------------------------------------
    -- Find all
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_EXAMS(
        P_RESULT OUT SYS_REFCURSOR
    );

    ------------------------------------------------------------------
    -- Find by academic year
    ------------------------------------------------------------------
    PROCEDURE GET_EXAMS_BY_ACADEMIC_YEAR(
        P_ACADEMIC_YEAR_ID IN EXAM.ACADEMIC_YEAR_ID%TYPE,
        P_RESULT           OUT SYS_REFCURSOR
    );

    ------------------------------------------------------------------
    -- Find by status
    ------------------------------------------------------------------
    PROCEDURE GET_EXAMS_BY_STATUS(
        P_STATUS IN EXAM.STATUS%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

    ------------------------------------------------------------------
    -- Update status
    ------------------------------------------------------------------
    PROCEDURE UPDATE_EXAM_STATUS(
        P_EXAM_ID    IN EXAM.EXAM_ID%TYPE,
        P_STATUS     IN EXAM.STATUS%TYPE,
        P_UPDATED_BY IN EXAM.UPDATED_BY%TYPE
    );

    ------------------------------------------------------------------
    -- Delete
    ------------------------------------------------------------------
    PROCEDURE DELETE_EXAM(
        P_EXAM_ID IN EXAM.EXAM_ID%TYPE
    );

END EXAM_PKG;
/

CREATE OR REPLACE PACKAGE BODY EXAM_PKG AS

    ------------------------------------------------------------------
    -- Private validation: exam status
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_STATUS(
        P_STATUS IN EXAM.STATUS%TYPE
    ) AS
    BEGIN
        IF P_STATUS IS NULL OR P_STATUS NOT IN (
                                                'DRAFT',
                                                'SCHEDULED',
                                                'ONGOING',
                                                'COMPLETED',
                                                'CANCELLED'
            ) THEN
            RAISE_APPLICATION_ERROR(
                    -20501,
                    'INVALID EXAM STATUS'
            );
        END IF;
    END VALIDATE_STATUS;

    ------------------------------------------------------------------
    -- Private validation: exam type
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_EXAM_TYPE(
        P_EXAM_TYPE IN EXAM.EXAM_TYPE%TYPE
    ) AS
    BEGIN
        IF P_EXAM_TYPE IS NULL OR P_EXAM_TYPE NOT IN (
                                                      'CLASS_TEST',
                                                      'QUIZ',
                                                      'MIDTERM',
                                                      'FINAL',
                                                      'PRACTICAL',
                                                      'ASSIGNMENT',
                                                      'OTHER'
            ) THEN
            RAISE_APPLICATION_ERROR(
                    -20502,
                    'INVALID EXAM TYPE'
            );
        END IF;
    END VALIDATE_EXAM_TYPE;

    ------------------------------------------------------------------
    -- Private validation: date range
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_DATES(
        P_START_DATE IN EXAM.START_DATE%TYPE,
        P_END_DATE   IN EXAM.END_DATE%TYPE
    ) AS
    BEGIN
        IF P_START_DATE IS NULL OR P_END_DATE IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20503,
                    'EXAM START DATE AND END DATE ARE REQUIRED'
            );
        END IF;

        IF P_END_DATE < P_START_DATE THEN
            RAISE_APPLICATION_ERROR(
                    -20504,
                    'EXAM END DATE CANNOT BE BEFORE START DATE'
            );
        END IF;
    END VALIDATE_DATES;

    ------------------------------------------------------------------
    -- Create exam
    ------------------------------------------------------------------
    PROCEDURE CREATE_EXAM(
        P_ACADEMIC_YEAR_ID IN EXAM.ACADEMIC_YEAR_ID%TYPE,
        P_EXAM_NAME        IN EXAM.EXAM_NAME%TYPE,
        P_EXAM_CODE        IN EXAM.EXAM_CODE%TYPE,
        P_EXAM_TYPE        IN EXAM.EXAM_TYPE%TYPE,
        P_START_DATE       IN EXAM.START_DATE%TYPE,
        P_END_DATE         IN EXAM.END_DATE%TYPE,
        P_DESCRIPTION      IN EXAM.DESCRIPTION%TYPE,
        P_STATUS           IN EXAM.STATUS%TYPE,
        P_CREATED_BY       IN EXAM.CREATED_BY%TYPE,
        P_EXAM_ID          OUT EXAM.EXAM_ID%TYPE
    ) AS
        V_STATUS EXAM.STATUS%TYPE;
    BEGIN
        IF P_ACADEMIC_YEAR_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20505,
                    'ACADEMIC YEAR ID IS REQUIRED'
            );
        END IF;

        IF TRIM(P_EXAM_NAME) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20506,
                    'EXAM NAME IS REQUIRED'
            );
        END IF;

        IF TRIM(P_EXAM_CODE) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20507,
                    'EXAM CODE IS REQUIRED'
            );
        END IF;

        V_STATUS := NVL(UPPER(TRIM(P_STATUS)), 'DRAFT');

        VALIDATE_EXAM_TYPE(UPPER(TRIM(P_EXAM_TYPE)));
        VALIDATE_STATUS(V_STATUS);
        VALIDATE_DATES(P_START_DATE, P_END_DATE);

        INSERT INTO EXAM (
            ACADEMIC_YEAR_ID,
            EXAM_NAME,
            EXAM_CODE,
            EXAM_TYPE,
            START_DATE,
            END_DATE,
            DESCRIPTION,
            STATUS,
            CREATED_BY
        ) VALUES (
                     P_ACADEMIC_YEAR_ID,
                     TRIM(P_EXAM_NAME),
                     UPPER(TRIM(P_EXAM_CODE)),
                     UPPER(TRIM(P_EXAM_TYPE)),
                     P_START_DATE,
                     P_END_DATE,
                     P_DESCRIPTION,
                     V_STATUS,
                     P_CREATED_BY
                 )
        RETURNING EXAM_ID INTO P_EXAM_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20508,
                    'EXAM CODE ALREADY EXISTS FOR THIS ACADEMIC YEAR'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20599 AND -20501 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20509,
                    'ERROR CREATING EXAM: ' || SQLERRM
            );
    END CREATE_EXAM;

    ------------------------------------------------------------------
    -- Update exam
    ------------------------------------------------------------------
    PROCEDURE UPDATE_EXAM(
        P_EXAM_ID          IN EXAM.EXAM_ID%TYPE,
        P_ACADEMIC_YEAR_ID IN EXAM.ACADEMIC_YEAR_ID%TYPE,
        P_EXAM_NAME        IN EXAM.EXAM_NAME%TYPE,
        P_EXAM_CODE        IN EXAM.EXAM_CODE%TYPE,
        P_EXAM_TYPE        IN EXAM.EXAM_TYPE%TYPE,
        P_START_DATE       IN EXAM.START_DATE%TYPE,
        P_END_DATE         IN EXAM.END_DATE%TYPE,
        P_DESCRIPTION      IN EXAM.DESCRIPTION%TYPE,
        P_STATUS           IN EXAM.STATUS%TYPE,
        P_UPDATED_BY       IN EXAM.UPDATED_BY%TYPE
    ) AS
    BEGIN
        IF P_EXAM_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(-20510, 'EXAM ID IS REQUIRED');
        END IF;

        IF P_ACADEMIC_YEAR_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20505,
                    'ACADEMIC YEAR ID IS REQUIRED'
            );
        END IF;

        IF TRIM(P_EXAM_NAME) IS NULL THEN
            RAISE_APPLICATION_ERROR(-20506, 'EXAM NAME IS REQUIRED');
        END IF;

        IF TRIM(P_EXAM_CODE) IS NULL THEN
            RAISE_APPLICATION_ERROR(-20507, 'EXAM CODE IS REQUIRED');
        END IF;

        VALIDATE_EXAM_TYPE(UPPER(TRIM(P_EXAM_TYPE)));
        VALIDATE_STATUS(UPPER(TRIM(P_STATUS)));
        VALIDATE_DATES(P_START_DATE, P_END_DATE);

        UPDATE EXAM
        SET ACADEMIC_YEAR_ID = P_ACADEMIC_YEAR_ID,
            EXAM_NAME        = TRIM(P_EXAM_NAME),
            EXAM_CODE        = UPPER(TRIM(P_EXAM_CODE)),
            EXAM_TYPE        = UPPER(TRIM(P_EXAM_TYPE)),
            START_DATE       = P_START_DATE,
            END_DATE         = P_END_DATE,
            DESCRIPTION      = P_DESCRIPTION,
            STATUS           = UPPER(TRIM(P_STATUS)),
            UPDATED_AT       = SYSTIMESTAMP,
            UPDATED_BY       = P_UPDATED_BY
        WHERE EXAM_ID = P_EXAM_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20511, 'EXAM NOT FOUND');
        END IF;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20508,
                    'EXAM CODE ALREADY EXISTS FOR THIS ACADEMIC YEAR'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20599 AND -20501 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20512,
                    'ERROR UPDATING EXAM: ' || SQLERRM
            );
    END UPDATE_EXAM;

    ------------------------------------------------------------------
    -- Get exam by ID
    ------------------------------------------------------------------
    PROCEDURE GET_EXAM_BY_ID(
        P_EXAM_ID IN EXAM.EXAM_ID%TYPE,
        P_RESULT  OUT SYS_REFCURSOR
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM EXAM
        WHERE EXAM_ID = P_EXAM_ID;

        IF V_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20511, 'EXAM NOT FOUND');
        END IF;

        OPEN P_RESULT FOR
            SELECT E.EXAM_ID,
                   E.ACADEMIC_YEAR_ID,
                   AY.YEAR_NAME AS ACADEMIC_YEAR_NAME,
                   E.EXAM_NAME,
                   E.EXAM_CODE,
                   E.EXAM_TYPE,
                   E.START_DATE,
                   E.END_DATE,
                   E.DESCRIPTION,
                   E.STATUS,
                   E.CREATED_AT,
                   E.UPDATED_AT,
                   E.CREATED_BY,
                   E.UPDATED_BY
            FROM EXAM E
                     JOIN ACADEMIC_YEARS AY
                          ON AY.ACADEMIC_YEAR_ID = E.ACADEMIC_YEAR_ID
            WHERE E.EXAM_ID = P_EXAM_ID;
    END GET_EXAM_BY_ID;

    ------------------------------------------------------------------
    -- Get all exams
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_EXAMS(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT E.EXAM_ID,
                   E.ACADEMIC_YEAR_ID,
                   AY.YEAR_NAME AS ACADEMIC_YEAR_NAME,
                   E.EXAM_NAME,
                   E.EXAM_CODE,
                   E.EXAM_TYPE,
                   E.START_DATE,
                   E.END_DATE,
                   E.DESCRIPTION,
                   E.STATUS,
                   E.CREATED_AT,
                   E.UPDATED_AT,
                   E.CREATED_BY,
                   E.UPDATED_BY
            FROM EXAM E
                     JOIN ACADEMIC_YEARS AY
                          ON AY.ACADEMIC_YEAR_ID = E.ACADEMIC_YEAR_ID
            ORDER BY E.START_DATE DESC,
                     E.EXAM_ID DESC;
    END GET_ALL_EXAMS;

    ------------------------------------------------------------------
    -- Get exams by academic year
    ------------------------------------------------------------------
    PROCEDURE GET_EXAMS_BY_ACADEMIC_YEAR(
        P_ACADEMIC_YEAR_ID IN EXAM.ACADEMIC_YEAR_ID%TYPE,
        P_RESULT           OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT E.EXAM_ID,
                   E.ACADEMIC_YEAR_ID,
                   AY.YEAR_NAME AS ACADEMIC_YEAR_NAME,
                   E.EXAM_NAME,
                   E.EXAM_CODE,
                   E.EXAM_TYPE,
                   E.START_DATE,
                   E.END_DATE,
                   E.DESCRIPTION,
                   E.STATUS,
                   E.CREATED_AT,
                   E.UPDATED_AT,
                   E.CREATED_BY,
                   E.UPDATED_BY
            FROM EXAM E
                     JOIN ACADEMIC_YEARS AY
                          ON AY.ACADEMIC_YEAR_ID = E.ACADEMIC_YEAR_ID
            WHERE E.ACADEMIC_YEAR_ID = P_ACADEMIC_YEAR_ID
            ORDER BY E.START_DATE DESC,
                     E.EXAM_ID DESC;
    END GET_EXAMS_BY_ACADEMIC_YEAR;

    ------------------------------------------------------------------
    -- Get exams by status
    ------------------------------------------------------------------
    PROCEDURE GET_EXAMS_BY_STATUS(
        P_STATUS IN EXAM.STATUS%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    ) AS
        V_STATUS EXAM.STATUS%TYPE;
    BEGIN
        V_STATUS := UPPER(TRIM(P_STATUS));
        VALIDATE_STATUS(V_STATUS);

        OPEN P_RESULT FOR
            SELECT E.EXAM_ID,
                   E.ACADEMIC_YEAR_ID,
                   AY.YEAR_NAME AS ACADEMIC_YEAR_NAME,
                   E.EXAM_NAME,
                   E.EXAM_CODE,
                   E.EXAM_TYPE,
                   E.START_DATE,
                   E.END_DATE,
                   E.DESCRIPTION,
                   E.STATUS,
                   E.CREATED_AT,
                   E.UPDATED_AT,
                   E.CREATED_BY,
                   E.UPDATED_BY
            FROM EXAM E
                     JOIN ACADEMIC_YEARS AY
                          ON AY.ACADEMIC_YEAR_ID = E.ACADEMIC_YEAR_ID
            WHERE E.STATUS = V_STATUS
            ORDER BY E.START_DATE DESC,
                     E.EXAM_ID DESC;
    END GET_EXAMS_BY_STATUS;

    ------------------------------------------------------------------
    -- Update exam status
    ------------------------------------------------------------------
    PROCEDURE UPDATE_EXAM_STATUS(
        P_EXAM_ID    IN EXAM.EXAM_ID%TYPE,
        P_STATUS     IN EXAM.STATUS%TYPE,
        P_UPDATED_BY IN EXAM.UPDATED_BY%TYPE
    ) AS
        V_STATUS EXAM.STATUS%TYPE;
    BEGIN
        V_STATUS := UPPER(TRIM(P_STATUS));
        VALIDATE_STATUS(V_STATUS);

        UPDATE EXAM
        SET STATUS     = V_STATUS,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE EXAM_ID = P_EXAM_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20511, 'EXAM NOT FOUND');
        END IF;
    END UPDATE_EXAM_STATUS;

    ------------------------------------------------------------------
    -- Delete exam
    ------------------------------------------------------------------
    PROCEDURE DELETE_EXAM(
        P_EXAM_ID IN EXAM.EXAM_ID%TYPE
    ) AS
    BEGIN
        DELETE FROM EXAM
        WHERE EXAM_ID = P_EXAM_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20511, 'EXAM NOT FOUND');
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2292 THEN
                RAISE_APPLICATION_ERROR(
                        -20513,
                        'EXAM CANNOT BE DELETED BECAUSE SUBJECTS ARE ASSIGNED'
                );
            END IF;

            IF SQLCODE BETWEEN -20599 AND -20501 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20514,
                    'ERROR DELETING EXAM: ' || SQLERRM
            );
    END DELETE_EXAM;

END EXAM_PKG;
/