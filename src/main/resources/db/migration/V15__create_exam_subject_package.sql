----------------------------------------------------------------------
-- V15: EXAM SUBJECT PACKAGE
----------------------------------------------------------------------

CREATE OR REPLACE PACKAGE EXAM_SUBJECT_PKG AS

    PROCEDURE CREATE_EXAM_SUBJECT(
        P_EXAM_ID                IN EXAM_SUBJECT.EXAM_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN EXAM_SUBJECT.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN EXAM_SUBJECT.SECTION_ID%TYPE,
        P_SUBJECT_ID             IN EXAM_SUBJECT.SUBJECT_ID%TYPE,
        P_EXAM_DATE              IN EXAM_SUBJECT.EXAM_DATE%TYPE,
        P_START_TIME             IN EXAM_SUBJECT.START_TIME%TYPE,
        P_END_TIME               IN EXAM_SUBJECT.END_TIME%TYPE,
        P_DURATION_MINUTES       IN EXAM_SUBJECT.DURATION_MINUTES%TYPE,
        P_FULL_MARKS             IN EXAM_SUBJECT.FULL_MARKS%TYPE,
        P_PASS_MARKS             IN EXAM_SUBJECT.PASS_MARKS%TYPE,
        P_ROOM_NUMBER            IN EXAM_SUBJECT.ROOM_NUMBER%TYPE,
        P_INSTRUCTIONS           IN EXAM_SUBJECT.INSTRUCTIONS%TYPE,
        P_STATUS                 IN EXAM_SUBJECT.STATUS%TYPE,
        P_CREATED_BY             IN EXAM_SUBJECT.CREATED_BY%TYPE,
        P_EXAM_SUBJECT_ID        OUT EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE
    );

    PROCEDURE UPDATE_EXAM_SUBJECT(
        P_EXAM_SUBJECT_ID        IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE,
        P_EXAM_ID                IN EXAM_SUBJECT.EXAM_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN EXAM_SUBJECT.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN EXAM_SUBJECT.SECTION_ID%TYPE,
        P_SUBJECT_ID             IN EXAM_SUBJECT.SUBJECT_ID%TYPE,
        P_EXAM_DATE              IN EXAM_SUBJECT.EXAM_DATE%TYPE,
        P_START_TIME             IN EXAM_SUBJECT.START_TIME%TYPE,
        P_END_TIME               IN EXAM_SUBJECT.END_TIME%TYPE,
        P_DURATION_MINUTES       IN EXAM_SUBJECT.DURATION_MINUTES%TYPE,
        P_FULL_MARKS             IN EXAM_SUBJECT.FULL_MARKS%TYPE,
        P_PASS_MARKS             IN EXAM_SUBJECT.PASS_MARKS%TYPE,
        P_ROOM_NUMBER            IN EXAM_SUBJECT.ROOM_NUMBER%TYPE,
        P_INSTRUCTIONS           IN EXAM_SUBJECT.INSTRUCTIONS%TYPE,
        P_STATUS                 IN EXAM_SUBJECT.STATUS%TYPE,
        P_UPDATED_BY             IN EXAM_SUBJECT.UPDATED_BY%TYPE
    );

    PROCEDURE GET_EXAM_SUBJECT_BY_ID(
        P_EXAM_SUBJECT_ID IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE,
        P_RESULT          OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ALL_EXAM_SUBJECTS(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_SUBJECTS_BY_EXAM(
        P_EXAM_ID IN EXAM_SUBJECT.EXAM_ID%TYPE,
        P_RESULT  OUT SYS_REFCURSOR
    );

    PROCEDURE GET_SUBJECTS_BY_CLASS(
        P_ACADEMIC_YEAR_CLASS_ID
            IN EXAM_SUBJECT.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_SUBJECTS_BY_SECTION(
        P_SECTION_ID IN EXAM_SUBJECT.SECTION_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    );

    PROCEDURE UPDATE_EXAM_SUBJECT_STATUS(
        P_EXAM_SUBJECT_ID IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE,
        P_STATUS          IN EXAM_SUBJECT.STATUS%TYPE,
        P_UPDATED_BY      IN EXAM_SUBJECT.UPDATED_BY%TYPE
    );

    PROCEDURE DELETE_EXAM_SUBJECT(
        P_EXAM_SUBJECT_ID IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE
    );

END EXAM_SUBJECT_PKG;
/

CREATE OR REPLACE PACKAGE BODY EXAM_SUBJECT_PKG AS

    ------------------------------------------------------------------
    -- Validate status
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_STATUS(
        P_STATUS IN EXAM_SUBJECT.STATUS%TYPE
    ) AS
    BEGIN
        IF P_STATUS IS NULL OR P_STATUS NOT IN (
                                                'SCHEDULED',
                                                'ONGOING',
                                                'COMPLETED',
                                                'POSTPONED',
                                                'CANCELLED'
            ) THEN
            RAISE_APPLICATION_ERROR(
                    -20601,
                    'INVALID EXAM SUBJECT STATUS'
            );
        END IF;
    END VALIDATE_STATUS;

    ------------------------------------------------------------------
    -- Validate marks and duration
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_MARKS(
        P_FULL_MARKS       IN EXAM_SUBJECT.FULL_MARKS%TYPE,
        P_PASS_MARKS       IN EXAM_SUBJECT.PASS_MARKS%TYPE,
        P_DURATION_MINUTES IN EXAM_SUBJECT.DURATION_MINUTES%TYPE
    ) AS
    BEGIN
        IF P_FULL_MARKS IS NULL OR P_FULL_MARKS <= 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20602,
                    'FULL MARKS MUST BE GREATER THAN ZERO'
            );
        END IF;

        IF P_PASS_MARKS IS NULL OR P_PASS_MARKS < 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20603,
                    'PASS MARKS CANNOT BE NEGATIVE'
            );
        END IF;

        IF P_PASS_MARKS > P_FULL_MARKS THEN
            RAISE_APPLICATION_ERROR(
                    -20604,
                    'PASS MARKS CANNOT EXCEED FULL MARKS'
            );
        END IF;

        IF P_DURATION_MINUTES IS NOT NULL
            AND P_DURATION_MINUTES <= 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20605,
                    'DURATION MUST BE GREATER THAN ZERO'
            );
        END IF;
    END VALIDATE_MARKS;

    ------------------------------------------------------------------
    -- Validate exam date against parent exam
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_EXAM_DATE(
        P_EXAM_ID   IN EXAM_SUBJECT.EXAM_ID%TYPE,
        P_EXAM_DATE IN EXAM_SUBJECT.EXAM_DATE%TYPE
    ) AS
        V_START_DATE EXAM.START_DATE%TYPE;
        V_END_DATE   EXAM.END_DATE%TYPE;
    BEGIN
        IF P_EXAM_DATE IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20606,
                    'EXAM DATE IS REQUIRED'
            );
        END IF;

        SELECT START_DATE,
               END_DATE
        INTO V_START_DATE,
            V_END_DATE
        FROM EXAM
        WHERE EXAM_ID = P_EXAM_ID;

        IF TRUNC(P_EXAM_DATE) < TRUNC(V_START_DATE)
            OR TRUNC(P_EXAM_DATE) > TRUNC(V_END_DATE) THEN
            RAISE_APPLICATION_ERROR(
                    -20607,
                    'SUBJECT EXAM DATE MUST BE WITHIN EXAM DATE RANGE'
            );
        END IF;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20608, 'EXAM NOT FOUND');
    END VALIDATE_EXAM_DATE;

    ------------------------------------------------------------------
    -- Prevent duplicate assignment, including NULL section
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_DUPLICATE(
        P_EXAM_SUBJECT_ID        IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE,
        P_EXAM_ID                IN EXAM_SUBJECT.EXAM_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN EXAM_SUBJECT.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN EXAM_SUBJECT.SECTION_ID%TYPE,
        P_SUBJECT_ID             IN EXAM_SUBJECT.SUBJECT_ID%TYPE
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM EXAM_SUBJECT
        WHERE EXAM_ID = P_EXAM_ID
          AND ACADEMIC_YEAR_CLASS_ID = P_ACADEMIC_YEAR_CLASS_ID
          AND (
            SECTION_ID = P_SECTION_ID
                OR (SECTION_ID IS NULL AND P_SECTION_ID IS NULL)
            )
          AND SUBJECT_ID = P_SUBJECT_ID
          AND (
            P_EXAM_SUBJECT_ID IS NULL
                OR EXAM_SUBJECT_ID <> P_EXAM_SUBJECT_ID
            );

        IF V_COUNT > 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20609,
                    'SUBJECT IS ALREADY SCHEDULED FOR THIS EXAM, CLASS AND SECTION'
            );
        END IF;
    END VALIDATE_DUPLICATE;

    ------------------------------------------------------------------
    -- Create
    ------------------------------------------------------------------
    PROCEDURE CREATE_EXAM_SUBJECT(
        P_EXAM_ID                IN EXAM_SUBJECT.EXAM_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN EXAM_SUBJECT.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN EXAM_SUBJECT.SECTION_ID%TYPE,
        P_SUBJECT_ID             IN EXAM_SUBJECT.SUBJECT_ID%TYPE,
        P_EXAM_DATE              IN EXAM_SUBJECT.EXAM_DATE%TYPE,
        P_START_TIME             IN EXAM_SUBJECT.START_TIME%TYPE,
        P_END_TIME               IN EXAM_SUBJECT.END_TIME%TYPE,
        P_DURATION_MINUTES       IN EXAM_SUBJECT.DURATION_MINUTES%TYPE,
        P_FULL_MARKS             IN EXAM_SUBJECT.FULL_MARKS%TYPE,
        P_PASS_MARKS             IN EXAM_SUBJECT.PASS_MARKS%TYPE,
        P_ROOM_NUMBER            IN EXAM_SUBJECT.ROOM_NUMBER%TYPE,
        P_INSTRUCTIONS           IN EXAM_SUBJECT.INSTRUCTIONS%TYPE,
        P_STATUS                 IN EXAM_SUBJECT.STATUS%TYPE,
        P_CREATED_BY             IN EXAM_SUBJECT.CREATED_BY%TYPE,
        P_EXAM_SUBJECT_ID        OUT EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE
    ) AS
        V_STATUS EXAM_SUBJECT.STATUS%TYPE;
    BEGIN
        IF P_EXAM_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(-20610, 'EXAM ID IS REQUIRED');
        END IF;

        IF P_ACADEMIC_YEAR_CLASS_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(-20611, 'CLASS ID IS REQUIRED');
        END IF;

        IF P_SUBJECT_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(-20612, 'SUBJECT ID IS REQUIRED');
        END IF;

        V_STATUS := NVL(UPPER(TRIM(P_STATUS)), 'SCHEDULED');

        VALIDATE_STATUS(V_STATUS);

        VALIDATE_MARKS(
                P_FULL_MARKS,
                P_PASS_MARKS,
                P_DURATION_MINUTES
        );

        VALIDATE_EXAM_DATE(
                P_EXAM_ID,
                P_EXAM_DATE
        );

        VALIDATE_DUPLICATE(
                NULL,
                P_EXAM_ID,
                P_ACADEMIC_YEAR_CLASS_ID,
                P_SECTION_ID,
                P_SUBJECT_ID
        );

        INSERT INTO EXAM_SUBJECT (
            EXAM_ID,
            ACADEMIC_YEAR_CLASS_ID,
            SECTION_ID,
            SUBJECT_ID,
            EXAM_DATE,
            START_TIME,
            END_TIME,
            DURATION_MINUTES,
            FULL_MARKS,
            PASS_MARKS,
            ROOM_NUMBER,
            INSTRUCTIONS,
            STATUS,
            CREATED_BY
        ) VALUES (
                     P_EXAM_ID,
                     P_ACADEMIC_YEAR_CLASS_ID,
                     P_SECTION_ID,
                     P_SUBJECT_ID,
                     P_EXAM_DATE,
                     TRIM(P_START_TIME),
                     TRIM(P_END_TIME),
                     P_DURATION_MINUTES,
                     P_FULL_MARKS,
                     P_PASS_MARKS,
                     TRIM(P_ROOM_NUMBER),
                     P_INSTRUCTIONS,
                     V_STATUS,
                     P_CREATED_BY
                 )
        RETURNING EXAM_SUBJECT_ID
        INTO P_EXAM_SUBJECT_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20609,
                    'SUBJECT IS ALREADY SCHEDULED FOR THIS EXAM, CLASS AND SECTION'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20699 AND -20601 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20613,
                    'ERROR CREATING EXAM SUBJECT: ' || SQLERRM
            );
    END CREATE_EXAM_SUBJECT;

    ------------------------------------------------------------------
    -- Update
    ------------------------------------------------------------------
    PROCEDURE UPDATE_EXAM_SUBJECT(
        P_EXAM_SUBJECT_ID        IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE,
        P_EXAM_ID                IN EXAM_SUBJECT.EXAM_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN EXAM_SUBJECT.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN EXAM_SUBJECT.SECTION_ID%TYPE,
        P_SUBJECT_ID             IN EXAM_SUBJECT.SUBJECT_ID%TYPE,
        P_EXAM_DATE              IN EXAM_SUBJECT.EXAM_DATE%TYPE,
        P_START_TIME             IN EXAM_SUBJECT.START_TIME%TYPE,
        P_END_TIME               IN EXAM_SUBJECT.END_TIME%TYPE,
        P_DURATION_MINUTES       IN EXAM_SUBJECT.DURATION_MINUTES%TYPE,
        P_FULL_MARKS             IN EXAM_SUBJECT.FULL_MARKS%TYPE,
        P_PASS_MARKS             IN EXAM_SUBJECT.PASS_MARKS%TYPE,
        P_ROOM_NUMBER            IN EXAM_SUBJECT.ROOM_NUMBER%TYPE,
        P_INSTRUCTIONS           IN EXAM_SUBJECT.INSTRUCTIONS%TYPE,
        P_STATUS                 IN EXAM_SUBJECT.STATUS%TYPE,
        P_UPDATED_BY             IN EXAM_SUBJECT.UPDATED_BY%TYPE
    ) AS
        V_STATUS EXAM_SUBJECT.STATUS%TYPE;
    BEGIN
        IF P_EXAM_SUBJECT_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20614,
                    'EXAM SUBJECT ID IS REQUIRED'
            );
        END IF;

        IF P_EXAM_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(-20610, 'EXAM ID IS REQUIRED');
        END IF;

        IF P_ACADEMIC_YEAR_CLASS_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(-20611, 'CLASS ID IS REQUIRED');
        END IF;

        IF P_SUBJECT_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(-20612, 'SUBJECT ID IS REQUIRED');
        END IF;

        V_STATUS := UPPER(TRIM(P_STATUS));

        VALIDATE_STATUS(V_STATUS);

        VALIDATE_MARKS(
                P_FULL_MARKS,
                P_PASS_MARKS,
                P_DURATION_MINUTES
        );

        VALIDATE_EXAM_DATE(
                P_EXAM_ID,
                P_EXAM_DATE
        );

        VALIDATE_DUPLICATE(
                P_EXAM_SUBJECT_ID,
                P_EXAM_ID,
                P_ACADEMIC_YEAR_CLASS_ID,
                P_SECTION_ID,
                P_SUBJECT_ID
        );

        UPDATE EXAM_SUBJECT
        SET EXAM_ID                  = P_EXAM_ID,
            ACADEMIC_YEAR_CLASS_ID   = P_ACADEMIC_YEAR_CLASS_ID,
            SECTION_ID               = P_SECTION_ID,
            SUBJECT_ID               = P_SUBJECT_ID,
            EXAM_DATE                = P_EXAM_DATE,
            START_TIME               = TRIM(P_START_TIME),
            END_TIME                 = TRIM(P_END_TIME),
            DURATION_MINUTES         = P_DURATION_MINUTES,
            FULL_MARKS               = P_FULL_MARKS,
            PASS_MARKS               = P_PASS_MARKS,
            ROOM_NUMBER              = TRIM(P_ROOM_NUMBER),
            INSTRUCTIONS             = P_INSTRUCTIONS,
            STATUS                   = V_STATUS,
            UPDATED_AT               = SYSTIMESTAMP,
            UPDATED_BY               = P_UPDATED_BY
        WHERE EXAM_SUBJECT_ID = P_EXAM_SUBJECT_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20615, 'EXAM SUBJECT NOT FOUND');
        END IF;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20609,
                    'SUBJECT IS ALREADY SCHEDULED FOR THIS EXAM, CLASS AND SECTION'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20699 AND -20601 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20616,
                    'ERROR UPDATING EXAM SUBJECT: ' || SQLERRM
            );
    END UPDATE_EXAM_SUBJECT;

    ------------------------------------------------------------------
    -- Get by ID
    ------------------------------------------------------------------
    PROCEDURE GET_EXAM_SUBJECT_BY_ID(
        P_EXAM_SUBJECT_ID IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE,
        P_RESULT          OUT SYS_REFCURSOR
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM EXAM_SUBJECT
        WHERE EXAM_SUBJECT_ID = P_EXAM_SUBJECT_ID;

        IF V_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20615, 'EXAM SUBJECT NOT FOUND');
        END IF;

        OPEN P_RESULT FOR
            SELECT ES.*
            FROM EXAM_SUBJECT ES
            WHERE ES.EXAM_SUBJECT_ID = P_EXAM_SUBJECT_ID;
    END GET_EXAM_SUBJECT_BY_ID;

    ------------------------------------------------------------------
    -- Get all
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_EXAM_SUBJECTS(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT ES.*
            FROM EXAM_SUBJECT ES
            ORDER BY ES.EXAM_DATE DESC,
                     ES.START_TIME,
                     ES.EXAM_SUBJECT_ID DESC;
    END GET_ALL_EXAM_SUBJECTS;

    ------------------------------------------------------------------
    -- Get by exam
    ------------------------------------------------------------------
    PROCEDURE GET_SUBJECTS_BY_EXAM(
        P_EXAM_ID IN EXAM_SUBJECT.EXAM_ID%TYPE,
        P_RESULT  OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT ES.*
            FROM EXAM_SUBJECT ES
            WHERE ES.EXAM_ID = P_EXAM_ID
            ORDER BY ES.EXAM_DATE,
                     ES.START_TIME,
                     ES.EXAM_SUBJECT_ID;
    END GET_SUBJECTS_BY_EXAM;

    ------------------------------------------------------------------
    -- Get by class
    ------------------------------------------------------------------
    PROCEDURE GET_SUBJECTS_BY_CLASS(
        P_ACADEMIC_YEAR_CLASS_ID
            IN EXAM_SUBJECT.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT ES.*
            FROM EXAM_SUBJECT ES
            WHERE ES.ACADEMIC_YEAR_CLASS_ID =
                  P_ACADEMIC_YEAR_CLASS_ID
            ORDER BY ES.EXAM_DATE DESC,
                     ES.START_TIME;
    END GET_SUBJECTS_BY_CLASS;

    ------------------------------------------------------------------
    -- Get by section
    ------------------------------------------------------------------
    PROCEDURE GET_SUBJECTS_BY_SECTION(
        P_SECTION_ID IN EXAM_SUBJECT.SECTION_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT ES.*
            FROM EXAM_SUBJECT ES
            WHERE ES.SECTION_ID = P_SECTION_ID
            ORDER BY ES.EXAM_DATE DESC,
                     ES.START_TIME;
    END GET_SUBJECTS_BY_SECTION;

    ------------------------------------------------------------------
    -- Update status
    ------------------------------------------------------------------
    PROCEDURE UPDATE_EXAM_SUBJECT_STATUS(
        P_EXAM_SUBJECT_ID IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE,
        P_STATUS          IN EXAM_SUBJECT.STATUS%TYPE,
        P_UPDATED_BY      IN EXAM_SUBJECT.UPDATED_BY%TYPE
    ) AS
        V_STATUS EXAM_SUBJECT.STATUS%TYPE;
    BEGIN
        V_STATUS := UPPER(TRIM(P_STATUS));
        VALIDATE_STATUS(V_STATUS);

        UPDATE EXAM_SUBJECT
        SET STATUS     = V_STATUS,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE EXAM_SUBJECT_ID = P_EXAM_SUBJECT_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20615, 'EXAM SUBJECT NOT FOUND');
        END IF;
    END UPDATE_EXAM_SUBJECT_STATUS;

    ------------------------------------------------------------------
    -- Delete
    ------------------------------------------------------------------
    PROCEDURE DELETE_EXAM_SUBJECT(
        P_EXAM_SUBJECT_ID IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE
    ) AS
    BEGIN
        DELETE FROM EXAM_SUBJECT
        WHERE EXAM_SUBJECT_ID = P_EXAM_SUBJECT_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20615, 'EXAM SUBJECT NOT FOUND');
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2292 THEN
                RAISE_APPLICATION_ERROR(
                        -20617,
                        'EXAM SUBJECT CANNOT BE DELETED BECAUSE STUDENT MARKS EXIST'
                );
            END IF;

            IF SQLCODE BETWEEN -20699 AND -20601 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20618,
                    'ERROR DELETING EXAM SUBJECT: ' || SQLERRM
            );
    END DELETE_EXAM_SUBJECT;

END EXAM_SUBJECT_PKG;
/