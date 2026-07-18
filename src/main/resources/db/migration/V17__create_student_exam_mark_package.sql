----------------------------------------------------------------------
-- V17: STUDENT EXAM MARK PACKAGE
----------------------------------------------------------------------

CREATE OR REPLACE PACKAGE STUDENT_EXAM_MARK_PKG AS

    PROCEDURE CREATE_STUDENT_MARK(
        P_EXAM_SUBJECT_ID      IN STUDENT_EXAM_MARK.EXAM_SUBJECT_ID%TYPE,
        P_STUDENT_ID           IN STUDENT_EXAM_MARK.STUDENT_ID%TYPE,
        P_OBTAINED_MARKS       IN STUDENT_EXAM_MARK.OBTAINED_MARKS%TYPE,
        P_ATTENDANCE_STATUS    IN STUDENT_EXAM_MARK.ATTENDANCE_STATUS%TYPE,
        P_REMARKS              IN STUDENT_EXAM_MARK.REMARKS%TYPE,
        P_CREATED_BY           IN STUDENT_EXAM_MARK.CREATED_BY%TYPE,
        P_STUDENT_EXAM_MARK_ID OUT STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE
    );

    PROCEDURE UPDATE_STUDENT_MARK(
        P_STUDENT_EXAM_MARK_ID IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE,
        P_OBTAINED_MARKS       IN STUDENT_EXAM_MARK.OBTAINED_MARKS%TYPE,
        P_ATTENDANCE_STATUS    IN STUDENT_EXAM_MARK.ATTENDANCE_STATUS%TYPE,
        P_REMARKS              IN STUDENT_EXAM_MARK.REMARKS%TYPE,
        P_UPDATED_BY           IN STUDENT_EXAM_MARK.UPDATED_BY%TYPE
    );

    PROCEDURE GET_STUDENT_MARK_BY_ID(
        P_STUDENT_EXAM_MARK_ID
            IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ALL_STUDENT_MARKS(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_MARKS_BY_EXAM_SUBJECT(
        P_EXAM_SUBJECT_ID IN STUDENT_EXAM_MARK.EXAM_SUBJECT_ID%TYPE,
        P_RESULT          OUT SYS_REFCURSOR
    );

    PROCEDURE GET_MARKS_BY_STUDENT(
        P_STUDENT_ID IN STUDENT_EXAM_MARK.STUDENT_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    );

    PROCEDURE GET_STUDENT_EXAM_RESULTS(
        P_STUDENT_ID IN STUDENT_EXAM_MARK.STUDENT_ID%TYPE,
        P_EXAM_ID    IN EXAM.EXAM_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    );

    PROCEDURE PUBLISH_STUDENT_MARK(
        P_STUDENT_EXAM_MARK_ID
            IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE,
        P_UPDATED_BY IN STUDENT_EXAM_MARK.UPDATED_BY%TYPE
    );

    PROCEDURE UNPUBLISH_STUDENT_MARK(
        P_STUDENT_EXAM_MARK_ID
            IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE,
        P_UPDATED_BY IN STUDENT_EXAM_MARK.UPDATED_BY%TYPE
    );

    PROCEDURE PUBLISH_EXAM_SUBJECT_MARKS(
        P_EXAM_SUBJECT_ID IN STUDENT_EXAM_MARK.EXAM_SUBJECT_ID%TYPE,
        P_UPDATED_BY      IN STUDENT_EXAM_MARK.UPDATED_BY%TYPE
    );

    PROCEDURE DELETE_STUDENT_MARK(
        P_STUDENT_EXAM_MARK_ID
            IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE
    );

END STUDENT_EXAM_MARK_PKG;
/

CREATE OR REPLACE PACKAGE BODY STUDENT_EXAM_MARK_PKG AS

    ------------------------------------------------------------------
    -- Validate attendance status
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_ATTENDANCE_STATUS(
        P_ATTENDANCE_STATUS
            IN STUDENT_EXAM_MARK.ATTENDANCE_STATUS%TYPE
    ) AS
    BEGIN
        IF P_ATTENDANCE_STATUS IS NULL
            OR P_ATTENDANCE_STATUS NOT IN (
                                           'PRESENT',
                                           'ABSENT',
                                           'EXCUSED'
                ) THEN
            RAISE_APPLICATION_ERROR(
                    -20801,
                    'INVALID EXAM ATTENDANCE STATUS'
            );
        END IF;
    END VALIDATE_ATTENDANCE_STATUS;

    ------------------------------------------------------------------
    -- Calculate result
    ------------------------------------------------------------------
    PROCEDURE CALCULATE_RESULT(
        P_EXAM_SUBJECT_ID   IN EXAM_SUBJECT.EXAM_SUBJECT_ID%TYPE,
        P_OBTAINED_MARKS    IN STUDENT_EXAM_MARK.OBTAINED_MARKS%TYPE,
        P_ATTENDANCE_STATUS IN STUDENT_EXAM_MARK.ATTENDANCE_STATUS%TYPE,
        P_PERCENTAGE        OUT STUDENT_EXAM_MARK.PERCENTAGE%TYPE,
        P_LETTER_GRADE      OUT STUDENT_EXAM_MARK.LETTER_GRADE%TYPE,
        P_GRADE_POINT       OUT STUDENT_EXAM_MARK.GRADE_POINT%TYPE,
        P_RESULT_STATUS     OUT STUDENT_EXAM_MARK.RESULT_STATUS%TYPE
    ) AS
        V_FULL_MARKS EXAM_SUBJECT.FULL_MARKS%TYPE;
        V_PASS_MARKS EXAM_SUBJECT.PASS_MARKS%TYPE;
    BEGIN
        SELECT FULL_MARKS,
               PASS_MARKS
        INTO V_FULL_MARKS,
            V_PASS_MARKS
        FROM EXAM_SUBJECT
        WHERE EXAM_SUBJECT_ID = P_EXAM_SUBJECT_ID;

        IF P_ATTENDANCE_STATUS = 'PRESENT' THEN
            IF P_OBTAINED_MARKS IS NULL THEN
                RAISE_APPLICATION_ERROR(
                        -20802,
                        'OBTAINED MARKS ARE REQUIRED FOR A PRESENT STUDENT'
                );
            END IF;

            IF P_OBTAINED_MARKS < 0 THEN
                RAISE_APPLICATION_ERROR(
                        -20803,
                        'OBTAINED MARKS CANNOT BE NEGATIVE'
                );
            END IF;

            IF P_OBTAINED_MARKS > V_FULL_MARKS THEN
                RAISE_APPLICATION_ERROR(
                        -20804,
                        'OBTAINED MARKS CANNOT EXCEED FULL MARKS'
                );
            END IF;

            P_PERCENTAGE :=
                    ROUND((P_OBTAINED_MARKS / V_FULL_MARKS) * 100, 2);

            GRADE_SCALE_PKG.CALCULATE_GRADE(
                    P_PERCENTAGE   => P_PERCENTAGE,
                    P_LETTER_GRADE => P_LETTER_GRADE,
                    P_GRADE_POINT  => P_GRADE_POINT
            );

            IF P_OBTAINED_MARKS >= V_PASS_MARKS THEN
                P_RESULT_STATUS := 'PASS';
            ELSE
                P_RESULT_STATUS := 'FAIL';
            END IF;

        ELSIF P_ATTENDANCE_STATUS = 'ABSENT' THEN
            IF P_OBTAINED_MARKS IS NOT NULL
                AND P_OBTAINED_MARKS <> 0 THEN
                RAISE_APPLICATION_ERROR(
                        -20805,
                        'ABSENT STUDENT CANNOT HAVE OBTAINED MARKS'
                );
            END IF;

            P_PERCENTAGE    := 0;
            P_LETTER_GRADE  := 'F';
            P_GRADE_POINT   := 0;
            P_RESULT_STATUS := 'FAIL';

        ELSIF P_ATTENDANCE_STATUS = 'EXCUSED' THEN
            IF P_OBTAINED_MARKS IS NOT NULL THEN
                RAISE_APPLICATION_ERROR(
                        -20806,
                        'EXCUSED STUDENT CANNOT HAVE OBTAINED MARKS'
                );
            END IF;

            P_PERCENTAGE    := NULL;
            P_LETTER_GRADE  := NULL;
            P_GRADE_POINT   := NULL;
            P_RESULT_STATUS := 'WITHHELD';
        END IF;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(
                    -20807,
                    'EXAM SUBJECT NOT FOUND'
            );
    END CALCULATE_RESULT;

    ------------------------------------------------------------------
    -- Create student mark
    ------------------------------------------------------------------
    PROCEDURE CREATE_STUDENT_MARK(
        P_EXAM_SUBJECT_ID      IN STUDENT_EXAM_MARK.EXAM_SUBJECT_ID%TYPE,
        P_STUDENT_ID           IN STUDENT_EXAM_MARK.STUDENT_ID%TYPE,
        P_OBTAINED_MARKS       IN STUDENT_EXAM_MARK.OBTAINED_MARKS%TYPE,
        P_ATTENDANCE_STATUS    IN STUDENT_EXAM_MARK.ATTENDANCE_STATUS%TYPE,
        P_REMARKS              IN STUDENT_EXAM_MARK.REMARKS%TYPE,
        P_CREATED_BY           IN STUDENT_EXAM_MARK.CREATED_BY%TYPE,
        P_STUDENT_EXAM_MARK_ID OUT STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE
    ) AS
        V_ATTENDANCE_STATUS
            STUDENT_EXAM_MARK.ATTENDANCE_STATUS%TYPE;
        V_PERCENTAGE
            STUDENT_EXAM_MARK.PERCENTAGE%TYPE;
        V_LETTER_GRADE
            STUDENT_EXAM_MARK.LETTER_GRADE%TYPE;
        V_GRADE_POINT
            STUDENT_EXAM_MARK.GRADE_POINT%TYPE;
        V_RESULT_STATUS
            STUDENT_EXAM_MARK.RESULT_STATUS%TYPE;
    BEGIN
        IF P_EXAM_SUBJECT_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20808,
                    'EXAM SUBJECT ID IS REQUIRED'
            );
        END IF;

        IF P_STUDENT_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20809,
                    'STUDENT ID IS REQUIRED'
            );
        END IF;

        V_ATTENDANCE_STATUS :=
                NVL(UPPER(TRIM(P_ATTENDANCE_STATUS)), 'PRESENT');

        VALIDATE_ATTENDANCE_STATUS(V_ATTENDANCE_STATUS);

        CALCULATE_RESULT(
                P_EXAM_SUBJECT_ID   => P_EXAM_SUBJECT_ID,
                P_OBTAINED_MARKS    => P_OBTAINED_MARKS,
                P_ATTENDANCE_STATUS => V_ATTENDANCE_STATUS,
                P_PERCENTAGE        => V_PERCENTAGE,
                P_LETTER_GRADE      => V_LETTER_GRADE,
                P_GRADE_POINT       => V_GRADE_POINT,
                P_RESULT_STATUS     => V_RESULT_STATUS
        );

        INSERT INTO STUDENT_EXAM_MARK (
            EXAM_SUBJECT_ID,
            STUDENT_ID,
            OBTAINED_MARKS,
            PERCENTAGE,
            LETTER_GRADE,
            GRADE_POINT,
            RESULT_STATUS,
            ATTENDANCE_STATUS,
            REMARKS,
            PUBLISHED_FLAG,
            MARKED_AT,
            CREATED_BY
        ) VALUES (
                     P_EXAM_SUBJECT_ID,
                     P_STUDENT_ID,
                     CASE
                         WHEN V_ATTENDANCE_STATUS = 'ABSENT' THEN 0
                         ELSE P_OBTAINED_MARKS
                         END,
                     V_PERCENTAGE,
                     V_LETTER_GRADE,
                     V_GRADE_POINT,
                     V_RESULT_STATUS,
                     V_ATTENDANCE_STATUS,
                     P_REMARKS,
                     'N',
                     SYSTIMESTAMP,
                     P_CREATED_BY
                 )
        RETURNING STUDENT_EXAM_MARK_ID
        INTO P_STUDENT_EXAM_MARK_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20810,
                    'MARK ALREADY EXISTS FOR THIS STUDENT AND EXAM SUBJECT'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20899 AND -20801 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20811,
                    'ERROR CREATING STUDENT EXAM MARK: ' || SQLERRM
            );
    END CREATE_STUDENT_MARK;

    ------------------------------------------------------------------
    -- Update student mark
    ------------------------------------------------------------------
    PROCEDURE UPDATE_STUDENT_MARK(
        P_STUDENT_EXAM_MARK_ID IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE,
        P_OBTAINED_MARKS       IN STUDENT_EXAM_MARK.OBTAINED_MARKS%TYPE,
        P_ATTENDANCE_STATUS    IN STUDENT_EXAM_MARK.ATTENDANCE_STATUS%TYPE,
        P_REMARKS              IN STUDENT_EXAM_MARK.REMARKS%TYPE,
        P_UPDATED_BY           IN STUDENT_EXAM_MARK.UPDATED_BY%TYPE
    ) AS
        V_EXAM_SUBJECT_ID
            STUDENT_EXAM_MARK.EXAM_SUBJECT_ID%TYPE;
        V_ATTENDANCE_STATUS
            STUDENT_EXAM_MARK.ATTENDANCE_STATUS%TYPE;
        V_PERCENTAGE
            STUDENT_EXAM_MARK.PERCENTAGE%TYPE;
        V_LETTER_GRADE
            STUDENT_EXAM_MARK.LETTER_GRADE%TYPE;
        V_GRADE_POINT
            STUDENT_EXAM_MARK.GRADE_POINT%TYPE;
        V_RESULT_STATUS
            STUDENT_EXAM_MARK.RESULT_STATUS%TYPE;
    BEGIN
        IF P_STUDENT_EXAM_MARK_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20812,
                    'STUDENT EXAM MARK ID IS REQUIRED'
            );
        END IF;

        BEGIN
            SELECT EXAM_SUBJECT_ID
            INTO V_EXAM_SUBJECT_ID
            FROM STUDENT_EXAM_MARK
            WHERE STUDENT_EXAM_MARK_ID =
                  P_STUDENT_EXAM_MARK_ID;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20813,
                        'STUDENT EXAM MARK NOT FOUND'
                );
        END;

        V_ATTENDANCE_STATUS :=
                UPPER(TRIM(P_ATTENDANCE_STATUS));

        VALIDATE_ATTENDANCE_STATUS(V_ATTENDANCE_STATUS);

        CALCULATE_RESULT(
                P_EXAM_SUBJECT_ID   => V_EXAM_SUBJECT_ID,
                P_OBTAINED_MARKS    => P_OBTAINED_MARKS,
                P_ATTENDANCE_STATUS => V_ATTENDANCE_STATUS,
                P_PERCENTAGE        => V_PERCENTAGE,
                P_LETTER_GRADE      => V_LETTER_GRADE,
                P_GRADE_POINT       => V_GRADE_POINT,
                P_RESULT_STATUS     => V_RESULT_STATUS
        );

        UPDATE STUDENT_EXAM_MARK
        SET OBTAINED_MARKS = CASE
                                 WHEN V_ATTENDANCE_STATUS = 'ABSENT'
                                     THEN 0
                                 ELSE P_OBTAINED_MARKS
            END,
            PERCENTAGE = V_PERCENTAGE,
            LETTER_GRADE = V_LETTER_GRADE,
            GRADE_POINT = V_GRADE_POINT,
            RESULT_STATUS = V_RESULT_STATUS,
            ATTENDANCE_STATUS = V_ATTENDANCE_STATUS,
            REMARKS = P_REMARKS,
            MARKED_AT = SYSTIMESTAMP,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE STUDENT_EXAM_MARK_ID =
              P_STUDENT_EXAM_MARK_ID;
    END UPDATE_STUDENT_MARK;

    ------------------------------------------------------------------
    -- Get mark by ID
    ------------------------------------------------------------------
    PROCEDURE GET_STUDENT_MARK_BY_ID(
        P_STUDENT_EXAM_MARK_ID
            IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM STUDENT_EXAM_MARK
        WHERE STUDENT_EXAM_MARK_ID =
              P_STUDENT_EXAM_MARK_ID;

        IF V_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20813,
                    'STUDENT EXAM MARK NOT FOUND'
            );
        END IF;

        OPEN P_RESULT FOR
            SELECT SEM.*
            FROM STUDENT_EXAM_MARK SEM
            WHERE SEM.STUDENT_EXAM_MARK_ID =
                  P_STUDENT_EXAM_MARK_ID;
    END GET_STUDENT_MARK_BY_ID;

    ------------------------------------------------------------------
    -- Get all marks
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_STUDENT_MARKS(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SEM.*
            FROM STUDENT_EXAM_MARK SEM
            ORDER BY SEM.STUDENT_EXAM_MARK_ID DESC;
    END GET_ALL_STUDENT_MARKS;

    ------------------------------------------------------------------
    -- Get marks by exam subject
    ------------------------------------------------------------------
    PROCEDURE GET_MARKS_BY_EXAM_SUBJECT(
        P_EXAM_SUBJECT_ID IN STUDENT_EXAM_MARK.EXAM_SUBJECT_ID%TYPE,
        P_RESULT          OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SEM.*
            FROM STUDENT_EXAM_MARK SEM
            WHERE SEM.EXAM_SUBJECT_ID = P_EXAM_SUBJECT_ID
            ORDER BY SEM.STUDENT_ID;
    END GET_MARKS_BY_EXAM_SUBJECT;

    ------------------------------------------------------------------
    -- Get marks by student
    ------------------------------------------------------------------
    PROCEDURE GET_MARKS_BY_STUDENT(
        P_STUDENT_ID IN STUDENT_EXAM_MARK.STUDENT_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SEM.*
            FROM STUDENT_EXAM_MARK SEM
            WHERE SEM.STUDENT_ID = P_STUDENT_ID
            ORDER BY SEM.STUDENT_EXAM_MARK_ID DESC;
    END GET_MARKS_BY_STUDENT;

    ------------------------------------------------------------------
    -- Get one student's results for an exam
    ------------------------------------------------------------------
    PROCEDURE GET_STUDENT_EXAM_RESULTS(
        P_STUDENT_ID IN STUDENT_EXAM_MARK.STUDENT_ID%TYPE,
        P_EXAM_ID    IN EXAM.EXAM_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT SEM.STUDENT_EXAM_MARK_ID,
                   SEM.EXAM_SUBJECT_ID,
                   ES.EXAM_ID,
                   ES.SUBJECT_ID,
                   ES.FULL_MARKS,
                   ES.PASS_MARKS,
                   SEM.STUDENT_ID,
                   SEM.OBTAINED_MARKS,
                   SEM.PERCENTAGE,
                   SEM.LETTER_GRADE,
                   SEM.GRADE_POINT,
                   SEM.RESULT_STATUS,
                   SEM.ATTENDANCE_STATUS,
                   SEM.REMARKS,
                   SEM.PUBLISHED_FLAG,
                   SEM.MARKED_AT,
                   SEM.PUBLISHED_AT,
                   SEM.CREATED_AT,
                   SEM.UPDATED_AT,
                   SEM.CREATED_BY,
                   SEM.UPDATED_BY
            FROM STUDENT_EXAM_MARK SEM
                     JOIN EXAM_SUBJECT ES
                          ON ES.EXAM_SUBJECT_ID =
                             SEM.EXAM_SUBJECT_ID
            WHERE SEM.STUDENT_ID = P_STUDENT_ID
              AND ES.EXAM_ID = P_EXAM_ID
            ORDER BY ES.EXAM_DATE,
                     ES.SUBJECT_ID;
    END GET_STUDENT_EXAM_RESULTS;

    ------------------------------------------------------------------
    -- Publish individual mark
    ------------------------------------------------------------------
    PROCEDURE PUBLISH_STUDENT_MARK(
        P_STUDENT_EXAM_MARK_ID
            IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE,
        P_UPDATED_BY IN STUDENT_EXAM_MARK.UPDATED_BY%TYPE
    ) AS
    BEGIN
        UPDATE STUDENT_EXAM_MARK
        SET PUBLISHED_FLAG = 'Y',
            PUBLISHED_AT = SYSTIMESTAMP,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE STUDENT_EXAM_MARK_ID =
              P_STUDENT_EXAM_MARK_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20813,
                    'STUDENT EXAM MARK NOT FOUND'
            );
        END IF;
    END PUBLISH_STUDENT_MARK;

    ------------------------------------------------------------------
    -- Unpublish individual mark
    ------------------------------------------------------------------
    PROCEDURE UNPUBLISH_STUDENT_MARK(
        P_STUDENT_EXAM_MARK_ID
            IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE,
        P_UPDATED_BY IN STUDENT_EXAM_MARK.UPDATED_BY%TYPE
    ) AS
    BEGIN
        UPDATE STUDENT_EXAM_MARK
        SET PUBLISHED_FLAG = 'N',
            PUBLISHED_AT = NULL,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE STUDENT_EXAM_MARK_ID =
              P_STUDENT_EXAM_MARK_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20813,
                    'STUDENT EXAM MARK NOT FOUND'
            );
        END IF;
    END UNPUBLISH_STUDENT_MARK;

    ------------------------------------------------------------------
    -- Publish all marks for an exam subject
    ------------------------------------------------------------------
    PROCEDURE PUBLISH_EXAM_SUBJECT_MARKS(
        P_EXAM_SUBJECT_ID IN STUDENT_EXAM_MARK.EXAM_SUBJECT_ID%TYPE,
        P_UPDATED_BY      IN STUDENT_EXAM_MARK.UPDATED_BY%TYPE
    ) AS
    BEGIN
        UPDATE STUDENT_EXAM_MARK
        SET PUBLISHED_FLAG = 'Y',
            PUBLISHED_AT = SYSTIMESTAMP,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE EXAM_SUBJECT_ID = P_EXAM_SUBJECT_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20814,
                    'NO STUDENT MARKS FOUND FOR THE EXAM SUBJECT'
            );
        END IF;
    END PUBLISH_EXAM_SUBJECT_MARKS;

    ------------------------------------------------------------------
    -- Delete student mark
    ------------------------------------------------------------------
    PROCEDURE DELETE_STUDENT_MARK(
        P_STUDENT_EXAM_MARK_ID
            IN STUDENT_EXAM_MARK.STUDENT_EXAM_MARK_ID%TYPE
    ) AS
    BEGIN
        DELETE FROM STUDENT_EXAM_MARK
        WHERE STUDENT_EXAM_MARK_ID =
              P_STUDENT_EXAM_MARK_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20813,
                    'STUDENT EXAM MARK NOT FOUND'
            );
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20899 AND -20801 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20815,
                    'ERROR DELETING STUDENT EXAM MARK: ' || SQLERRM
            );
    END DELETE_STUDENT_MARK;

END STUDENT_EXAM_MARK_PKG;
/