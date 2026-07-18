----------------------------------------------------------------------
-- V16: GRADE SCALE PACKAGE
----------------------------------------------------------------------

CREATE OR REPLACE PACKAGE GRADE_SCALE_PKG AS

    PROCEDURE CREATE_GRADE_SCALE(
        P_GRADE_NAME      IN GRADE_SCALE.GRADE_NAME%TYPE,
        P_LETTER_GRADE    IN GRADE_SCALE.LETTER_GRADE%TYPE,
        P_MIN_PERCENTAGE  IN GRADE_SCALE.MIN_PERCENTAGE%TYPE,
        P_MAX_PERCENTAGE  IN GRADE_SCALE.MAX_PERCENTAGE%TYPE,
        P_GRADE_POINT     IN GRADE_SCALE.GRADE_POINT%TYPE,
        P_DESCRIPTION     IN GRADE_SCALE.DESCRIPTION%TYPE,
        P_DISPLAY_ORDER   IN GRADE_SCALE.DISPLAY_ORDER%TYPE,
        P_STATUS          IN GRADE_SCALE.STATUS%TYPE,
        P_CREATED_BY      IN GRADE_SCALE.CREATED_BY%TYPE,
        P_GRADE_SCALE_ID  OUT GRADE_SCALE.GRADE_SCALE_ID%TYPE
    );

    PROCEDURE UPDATE_GRADE_SCALE(
        P_GRADE_SCALE_ID  IN GRADE_SCALE.GRADE_SCALE_ID%TYPE,
        P_GRADE_NAME      IN GRADE_SCALE.GRADE_NAME%TYPE,
        P_LETTER_GRADE    IN GRADE_SCALE.LETTER_GRADE%TYPE,
        P_MIN_PERCENTAGE  IN GRADE_SCALE.MIN_PERCENTAGE%TYPE,
        P_MAX_PERCENTAGE  IN GRADE_SCALE.MAX_PERCENTAGE%TYPE,
        P_GRADE_POINT     IN GRADE_SCALE.GRADE_POINT%TYPE,
        P_DESCRIPTION     IN GRADE_SCALE.DESCRIPTION%TYPE,
        P_DISPLAY_ORDER   IN GRADE_SCALE.DISPLAY_ORDER%TYPE,
        P_STATUS          IN GRADE_SCALE.STATUS%TYPE,
        P_UPDATED_BY      IN GRADE_SCALE.UPDATED_BY%TYPE
    );

    PROCEDURE GET_GRADE_SCALE_BY_ID(
        P_GRADE_SCALE_ID IN GRADE_SCALE.GRADE_SCALE_ID%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ALL_GRADE_SCALES(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ACTIVE_GRADE_SCALES(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE CALCULATE_GRADE(
        P_PERCENTAGE    IN NUMBER,
        P_LETTER_GRADE  OUT GRADE_SCALE.LETTER_GRADE%TYPE,
        P_GRADE_POINT   OUT GRADE_SCALE.GRADE_POINT%TYPE
    );

    PROCEDURE UPDATE_GRADE_SCALE_STATUS(
        P_GRADE_SCALE_ID IN GRADE_SCALE.GRADE_SCALE_ID%TYPE,
        P_STATUS         IN GRADE_SCALE.STATUS%TYPE,
        P_UPDATED_BY     IN GRADE_SCALE.UPDATED_BY%TYPE
    );

    PROCEDURE DELETE_GRADE_SCALE(
        P_GRADE_SCALE_ID IN GRADE_SCALE.GRADE_SCALE_ID%TYPE
    );

END GRADE_SCALE_PKG;
/

CREATE OR REPLACE PACKAGE BODY GRADE_SCALE_PKG AS

    ------------------------------------------------------------------
    -- Validate status
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_STATUS(
        P_STATUS IN GRADE_SCALE.STATUS%TYPE
    ) AS
    BEGIN
        IF P_STATUS IS NULL
            OR P_STATUS NOT IN ('ACTIVE', 'INACTIVE') THEN
            RAISE_APPLICATION_ERROR(
                    -20701,
                    'INVALID GRADE SCALE STATUS'
            );
        END IF;
    END VALIDATE_STATUS;

    ------------------------------------------------------------------
    -- Validate grade range
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_RANGE(
        P_MIN_PERCENTAGE IN GRADE_SCALE.MIN_PERCENTAGE%TYPE,
        P_MAX_PERCENTAGE IN GRADE_SCALE.MAX_PERCENTAGE%TYPE,
        P_GRADE_POINT    IN GRADE_SCALE.GRADE_POINT%TYPE
    ) AS
    BEGIN
        IF P_MIN_PERCENTAGE IS NULL
            OR P_MAX_PERCENTAGE IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20702,
                    'MINIMUM AND MAXIMUM PERCENTAGES ARE REQUIRED'
            );
        END IF;

        IF P_MIN_PERCENTAGE < 0
            OR P_MAX_PERCENTAGE > 100 THEN
            RAISE_APPLICATION_ERROR(
                    -20703,
                    'GRADE PERCENTAGE MUST BE BETWEEN 0 AND 100'
            );
        END IF;

        IF P_MAX_PERCENTAGE < P_MIN_PERCENTAGE THEN
            RAISE_APPLICATION_ERROR(
                    -20704,
                    'MAXIMUM PERCENTAGE CANNOT BE LESS THAN MINIMUM PERCENTAGE'
            );
        END IF;

        IF P_GRADE_POINT IS NULL OR P_GRADE_POINT < 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20705,
                    'GRADE POINT CANNOT BE NEGATIVE'
            );
        END IF;
    END VALIDATE_RANGE;

    ------------------------------------------------------------------
    -- Prevent overlapping active grade ranges
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_OVERLAPPING_RANGE(
        P_GRADE_SCALE_ID IN GRADE_SCALE.GRADE_SCALE_ID%TYPE,
        P_MIN_PERCENTAGE IN GRADE_SCALE.MIN_PERCENTAGE%TYPE,
        P_MAX_PERCENTAGE IN GRADE_SCALE.MAX_PERCENTAGE%TYPE,
        P_STATUS         IN GRADE_SCALE.STATUS%TYPE
    ) AS
        V_COUNT NUMBER;
    BEGIN
        IF P_STATUS <> 'ACTIVE' THEN
            RETURN;
        END IF;

        SELECT COUNT(*)
        INTO V_COUNT
        FROM GRADE_SCALE
        WHERE STATUS = 'ACTIVE'
          AND P_MIN_PERCENTAGE <= MAX_PERCENTAGE
          AND P_MAX_PERCENTAGE >= MIN_PERCENTAGE
          AND (
            P_GRADE_SCALE_ID IS NULL
                OR GRADE_SCALE_ID <> P_GRADE_SCALE_ID
            );

        IF V_COUNT > 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20706,
                    'GRADE PERCENTAGE RANGE OVERLAPS AN ACTIVE GRADE SCALE'
            );
        END IF;
    END VALIDATE_OVERLAPPING_RANGE;

    ------------------------------------------------------------------
    -- Create
    ------------------------------------------------------------------
    PROCEDURE CREATE_GRADE_SCALE(
        P_GRADE_NAME      IN GRADE_SCALE.GRADE_NAME%TYPE,
        P_LETTER_GRADE    IN GRADE_SCALE.LETTER_GRADE%TYPE,
        P_MIN_PERCENTAGE  IN GRADE_SCALE.MIN_PERCENTAGE%TYPE,
        P_MAX_PERCENTAGE  IN GRADE_SCALE.MAX_PERCENTAGE%TYPE,
        P_GRADE_POINT     IN GRADE_SCALE.GRADE_POINT%TYPE,
        P_DESCRIPTION     IN GRADE_SCALE.DESCRIPTION%TYPE,
        P_DISPLAY_ORDER   IN GRADE_SCALE.DISPLAY_ORDER%TYPE,
        P_STATUS          IN GRADE_SCALE.STATUS%TYPE,
        P_CREATED_BY      IN GRADE_SCALE.CREATED_BY%TYPE,
        P_GRADE_SCALE_ID  OUT GRADE_SCALE.GRADE_SCALE_ID%TYPE
    ) AS
        V_STATUS        GRADE_SCALE.STATUS%TYPE;
        V_DISPLAY_ORDER GRADE_SCALE.DISPLAY_ORDER%TYPE;
    BEGIN
        IF TRIM(P_GRADE_NAME) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20707,
                    'GRADE NAME IS REQUIRED'
            );
        END IF;

        IF TRIM(P_LETTER_GRADE) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20708,
                    'LETTER GRADE IS REQUIRED'
            );
        END IF;

        V_STATUS := NVL(UPPER(TRIM(P_STATUS)), 'ACTIVE');
        V_DISPLAY_ORDER := NVL(P_DISPLAY_ORDER, 1);

        VALIDATE_STATUS(V_STATUS);

        VALIDATE_RANGE(
                P_MIN_PERCENTAGE,
                P_MAX_PERCENTAGE,
                P_GRADE_POINT
        );

        VALIDATE_OVERLAPPING_RANGE(
                NULL,
                P_MIN_PERCENTAGE,
                P_MAX_PERCENTAGE,
                V_STATUS
        );

        INSERT INTO GRADE_SCALE (
            GRADE_NAME,
            LETTER_GRADE,
            MIN_PERCENTAGE,
            MAX_PERCENTAGE,
            GRADE_POINT,
            DESCRIPTION,
            DISPLAY_ORDER,
            STATUS,
            CREATED_BY
        ) VALUES (
                     TRIM(P_GRADE_NAME),
                     UPPER(TRIM(P_LETTER_GRADE)),
                     P_MIN_PERCENTAGE,
                     P_MAX_PERCENTAGE,
                     P_GRADE_POINT,
                     P_DESCRIPTION,
                     V_DISPLAY_ORDER,
                     V_STATUS,
                     P_CREATED_BY
                 )
        RETURNING GRADE_SCALE_ID
        INTO P_GRADE_SCALE_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20709,
                    'LETTER GRADE ALREADY EXISTS'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20799 AND -20701 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20710,
                    'ERROR CREATING GRADE SCALE: ' || SQLERRM
            );
    END CREATE_GRADE_SCALE;

    ------------------------------------------------------------------
    -- Update
    ------------------------------------------------------------------
    PROCEDURE UPDATE_GRADE_SCALE(
        P_GRADE_SCALE_ID  IN GRADE_SCALE.GRADE_SCALE_ID%TYPE,
        P_GRADE_NAME      IN GRADE_SCALE.GRADE_NAME%TYPE,
        P_LETTER_GRADE    IN GRADE_SCALE.LETTER_GRADE%TYPE,
        P_MIN_PERCENTAGE  IN GRADE_SCALE.MIN_PERCENTAGE%TYPE,
        P_MAX_PERCENTAGE  IN GRADE_SCALE.MAX_PERCENTAGE%TYPE,
        P_GRADE_POINT     IN GRADE_SCALE.GRADE_POINT%TYPE,
        P_DESCRIPTION     IN GRADE_SCALE.DESCRIPTION%TYPE,
        P_DISPLAY_ORDER   IN GRADE_SCALE.DISPLAY_ORDER%TYPE,
        P_STATUS          IN GRADE_SCALE.STATUS%TYPE,
        P_UPDATED_BY      IN GRADE_SCALE.UPDATED_BY%TYPE
    ) AS
        V_STATUS GRADE_SCALE.STATUS%TYPE;
    BEGIN
        IF P_GRADE_SCALE_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20711,
                    'GRADE SCALE ID IS REQUIRED'
            );
        END IF;

        IF TRIM(P_GRADE_NAME) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20707,
                    'GRADE NAME IS REQUIRED'
            );
        END IF;

        IF TRIM(P_LETTER_GRADE) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20708,
                    'LETTER GRADE IS REQUIRED'
            );
        END IF;

        V_STATUS := UPPER(TRIM(P_STATUS));

        VALIDATE_STATUS(V_STATUS);

        VALIDATE_RANGE(
                P_MIN_PERCENTAGE,
                P_MAX_PERCENTAGE,
                P_GRADE_POINT
        );

        VALIDATE_OVERLAPPING_RANGE(
                P_GRADE_SCALE_ID,
                P_MIN_PERCENTAGE,
                P_MAX_PERCENTAGE,
                V_STATUS
        );

        UPDATE GRADE_SCALE
        SET GRADE_NAME      = TRIM(P_GRADE_NAME),
            LETTER_GRADE    = UPPER(TRIM(P_LETTER_GRADE)),
            MIN_PERCENTAGE  = P_MIN_PERCENTAGE,
            MAX_PERCENTAGE  = P_MAX_PERCENTAGE,
            GRADE_POINT     = P_GRADE_POINT,
            DESCRIPTION     = P_DESCRIPTION,
            DISPLAY_ORDER   = NVL(P_DISPLAY_ORDER, 1),
            STATUS          = V_STATUS,
            UPDATED_AT      = SYSTIMESTAMP,
            UPDATED_BY      = P_UPDATED_BY
        WHERE GRADE_SCALE_ID = P_GRADE_SCALE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20712,
                    'GRADE SCALE NOT FOUND'
            );
        END IF;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20709,
                    'LETTER GRADE ALREADY EXISTS'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20799 AND -20701 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20713,
                    'ERROR UPDATING GRADE SCALE: ' || SQLERRM
            );
    END UPDATE_GRADE_SCALE;

    ------------------------------------------------------------------
    -- Get by ID
    ------------------------------------------------------------------
    PROCEDURE GET_GRADE_SCALE_BY_ID(
        P_GRADE_SCALE_ID IN GRADE_SCALE.GRADE_SCALE_ID%TYPE,
        P_RESULT         OUT SYS_REFCURSOR
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM GRADE_SCALE
        WHERE GRADE_SCALE_ID = P_GRADE_SCALE_ID;

        IF V_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20712,
                    'GRADE SCALE NOT FOUND'
            );
        END IF;

        OPEN P_RESULT FOR
            SELECT GS.*
            FROM GRADE_SCALE GS
            WHERE GS.GRADE_SCALE_ID = P_GRADE_SCALE_ID;
    END GET_GRADE_SCALE_BY_ID;

    ------------------------------------------------------------------
    -- Get all
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_GRADE_SCALES(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT GS.*
            FROM GRADE_SCALE GS
            ORDER BY GS.DISPLAY_ORDER,
                     GS.MIN_PERCENTAGE DESC,
                     GS.GRADE_SCALE_ID;
    END GET_ALL_GRADE_SCALES;

    ------------------------------------------------------------------
    -- Get active grade scales
    ------------------------------------------------------------------
    PROCEDURE GET_ACTIVE_GRADE_SCALES(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT GS.*
            FROM GRADE_SCALE GS
            WHERE GS.STATUS = 'ACTIVE'
            ORDER BY GS.DISPLAY_ORDER,
                     GS.MIN_PERCENTAGE DESC;
    END GET_ACTIVE_GRADE_SCALES;

    ------------------------------------------------------------------
    -- Calculate grade
    ------------------------------------------------------------------
    PROCEDURE CALCULATE_GRADE(
        P_PERCENTAGE    IN NUMBER,
        P_LETTER_GRADE  OUT GRADE_SCALE.LETTER_GRADE%TYPE,
        P_GRADE_POINT   OUT GRADE_SCALE.GRADE_POINT%TYPE
    ) AS
    BEGIN
        IF P_PERCENTAGE IS NULL
            OR P_PERCENTAGE < 0
            OR P_PERCENTAGE > 100 THEN
            RAISE_APPLICATION_ERROR(
                    -20714,
                    'PERCENTAGE MUST BE BETWEEN 0 AND 100'
            );
        END IF;

        SELECT LETTER_GRADE,
               GRADE_POINT
        INTO P_LETTER_GRADE,
            P_GRADE_POINT
        FROM GRADE_SCALE
        WHERE STATUS = 'ACTIVE'
          AND ROUND(P_PERCENTAGE, 2)
            BETWEEN MIN_PERCENTAGE AND MAX_PERCENTAGE;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(
                    -20715,
                    'NO ACTIVE GRADE SCALE FOUND FOR THE PERCENTAGE'
            );

        WHEN TOO_MANY_ROWS THEN
            RAISE_APPLICATION_ERROR(
                    -20716,
                    'MULTIPLE ACTIVE GRADE SCALES MATCH THE PERCENTAGE'
            );
    END CALCULATE_GRADE;

    ------------------------------------------------------------------
    -- Update status
    ------------------------------------------------------------------
    PROCEDURE UPDATE_GRADE_SCALE_STATUS(
        P_GRADE_SCALE_ID IN GRADE_SCALE.GRADE_SCALE_ID%TYPE,
        P_STATUS         IN GRADE_SCALE.STATUS%TYPE,
        P_UPDATED_BY     IN GRADE_SCALE.UPDATED_BY%TYPE
    ) AS
        V_STATUS        GRADE_SCALE.STATUS%TYPE;
        V_MIN_PERCENTAGE GRADE_SCALE.MIN_PERCENTAGE%TYPE;
        V_MAX_PERCENTAGE GRADE_SCALE.MAX_PERCENTAGE%TYPE;
    BEGIN
        V_STATUS := UPPER(TRIM(P_STATUS));
        VALIDATE_STATUS(V_STATUS);

        BEGIN
            SELECT MIN_PERCENTAGE,
                   MAX_PERCENTAGE
            INTO V_MIN_PERCENTAGE,
                V_MAX_PERCENTAGE
            FROM GRADE_SCALE
            WHERE GRADE_SCALE_ID = P_GRADE_SCALE_ID;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(
                        -20712,
                        'GRADE SCALE NOT FOUND'
                );
        END;

        VALIDATE_OVERLAPPING_RANGE(
                P_GRADE_SCALE_ID,
                V_MIN_PERCENTAGE,
                V_MAX_PERCENTAGE,
                V_STATUS
        );

        UPDATE GRADE_SCALE
        SET STATUS     = V_STATUS,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE GRADE_SCALE_ID = P_GRADE_SCALE_ID;
    END UPDATE_GRADE_SCALE_STATUS;

    ------------------------------------------------------------------
    -- Delete
    ------------------------------------------------------------------
    PROCEDURE DELETE_GRADE_SCALE(
        P_GRADE_SCALE_ID IN GRADE_SCALE.GRADE_SCALE_ID%TYPE
    ) AS
    BEGIN
        DELETE FROM GRADE_SCALE
        WHERE GRADE_SCALE_ID = P_GRADE_SCALE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20712,
                    'GRADE SCALE NOT FOUND'
            );
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20799 AND -20701 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20717,
                    'ERROR DELETING GRADE SCALE: ' || SQLERRM
            );
    END DELETE_GRADE_SCALE;

END GRADE_SCALE_PKG;
/