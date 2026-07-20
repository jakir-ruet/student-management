----------------------------------------------------------------------
-- V20: FEE STRUCTURE PACKAGE
----------------------------------------------------------------------

CREATE OR REPLACE PACKAGE FEE_STRUCTURE_PKG AS

    PROCEDURE CREATE_FEE_STRUCTURE(
        P_ACADEMIC_YEAR_ID       IN FEE_STRUCTURE.ACADEMIC_YEAR_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN FEE_STRUCTURE.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN FEE_STRUCTURE.SECTION_ID%TYPE,
        P_FEE_TYPE_ID            IN FEE_STRUCTURE.FEE_TYPE_ID%TYPE,
        P_STRUCTURE_NAME         IN FEE_STRUCTURE.STRUCTURE_NAME%TYPE,
        P_AMOUNT                 IN FEE_STRUCTURE.AMOUNT%TYPE,
        P_FREQUENCY              IN FEE_STRUCTURE.FREQUENCY%TYPE,
        P_DUE_DAY                IN FEE_STRUCTURE.DUE_DAY%TYPE,
        P_EFFECTIVE_FROM         IN FEE_STRUCTURE.EFFECTIVE_FROM%TYPE,
        P_EFFECTIVE_TO           IN FEE_STRUCTURE.EFFECTIVE_TO%TYPE,
        P_FINE_TYPE              IN FEE_STRUCTURE.FINE_TYPE%TYPE,
        P_FINE_VALUE             IN FEE_STRUCTURE.FINE_VALUE%TYPE,
        P_DESCRIPTION            IN FEE_STRUCTURE.DESCRIPTION%TYPE,
        P_STATUS                 IN FEE_STRUCTURE.STATUS%TYPE,
        P_CREATED_BY             IN FEE_STRUCTURE.CREATED_BY%TYPE,
        P_FEE_STRUCTURE_ID       OUT FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE
    );

    PROCEDURE UPDATE_FEE_STRUCTURE(
        P_FEE_STRUCTURE_ID       IN FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE,
        P_ACADEMIC_YEAR_ID       IN FEE_STRUCTURE.ACADEMIC_YEAR_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN FEE_STRUCTURE.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN FEE_STRUCTURE.SECTION_ID%TYPE,
        P_FEE_TYPE_ID            IN FEE_STRUCTURE.FEE_TYPE_ID%TYPE,
        P_STRUCTURE_NAME         IN FEE_STRUCTURE.STRUCTURE_NAME%TYPE,
        P_AMOUNT                 IN FEE_STRUCTURE.AMOUNT%TYPE,
        P_FREQUENCY              IN FEE_STRUCTURE.FREQUENCY%TYPE,
        P_DUE_DAY                IN FEE_STRUCTURE.DUE_DAY%TYPE,
        P_EFFECTIVE_FROM         IN FEE_STRUCTURE.EFFECTIVE_FROM%TYPE,
        P_EFFECTIVE_TO           IN FEE_STRUCTURE.EFFECTIVE_TO%TYPE,
        P_FINE_TYPE              IN FEE_STRUCTURE.FINE_TYPE%TYPE,
        P_FINE_VALUE             IN FEE_STRUCTURE.FINE_VALUE%TYPE,
        P_DESCRIPTION            IN FEE_STRUCTURE.DESCRIPTION%TYPE,
        P_STATUS                 IN FEE_STRUCTURE.STATUS%TYPE,
        P_UPDATED_BY             IN FEE_STRUCTURE.UPDATED_BY%TYPE
    );

    PROCEDURE GET_FEE_STRUCTURE_BY_ID(
        P_FEE_STRUCTURE_ID IN FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE,
        P_RESULT           OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ALL_FEE_STRUCTURES(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_STRUCTURES_BY_YEAR(
        P_ACADEMIC_YEAR_ID IN FEE_STRUCTURE.ACADEMIC_YEAR_ID%TYPE,
        P_RESULT           OUT SYS_REFCURSOR
    );

    PROCEDURE GET_STRUCTURES_BY_CLASS(
        P_ACADEMIC_YEAR_CLASS_ID
            IN FEE_STRUCTURE.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_STRUCTURES_BY_SECTION(
        P_SECTION_ID IN FEE_STRUCTURE.SECTION_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ACTIVE_FEE_STRUCTURES(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE UPDATE_FEE_STRUCTURE_STATUS(
        P_FEE_STRUCTURE_ID IN FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE,
        P_STATUS           IN FEE_STRUCTURE.STATUS%TYPE,
        P_UPDATED_BY       IN FEE_STRUCTURE.UPDATED_BY%TYPE
    );

    PROCEDURE DELETE_FEE_STRUCTURE(
        P_FEE_STRUCTURE_ID IN FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE
    );

END FEE_STRUCTURE_PKG;
/

CREATE OR REPLACE PACKAGE BODY FEE_STRUCTURE_PKG AS

    ------------------------------------------------------------------
    -- Validate status
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_STATUS(
        P_STATUS IN FEE_STRUCTURE.STATUS%TYPE
    ) AS
    BEGIN
        IF P_STATUS IS NULL
            OR P_STATUS NOT IN ('ACTIVE', 'INACTIVE') THEN
            RAISE_APPLICATION_ERROR(
                    -20920,
                    'INVALID FEE STRUCTURE STATUS'
            );
        END IF;
    END VALIDATE_STATUS;

    ------------------------------------------------------------------
    -- Validate frequency
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_FREQUENCY(
        P_FREQUENCY IN FEE_STRUCTURE.FREQUENCY%TYPE
    ) AS
    BEGIN
        IF P_FREQUENCY IS NULL
            OR P_FREQUENCY NOT IN (
                                   'ONE_TIME',
                                   'MONTHLY',
                                   'QUARTERLY',
                                   'HALF_YEARLY',
                                   'ANNUAL'
                ) THEN
            RAISE_APPLICATION_ERROR(
                    -20921,
                    'INVALID FEE FREQUENCY'
            );
        END IF;
    END VALIDATE_FREQUENCY;

    ------------------------------------------------------------------
    -- Validate fine configuration
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_FINE(
        P_FINE_TYPE  IN FEE_STRUCTURE.FINE_TYPE%TYPE,
        P_FINE_VALUE IN FEE_STRUCTURE.FINE_VALUE%TYPE
    ) AS
    BEGIN
        IF P_FINE_TYPE IS NULL
            OR P_FINE_TYPE NOT IN (
                                   'NONE',
                                   'FIXED',
                                   'PERCENTAGE'
                ) THEN
            RAISE_APPLICATION_ERROR(
                    -20922,
                    'INVALID FINE TYPE'
            );
        END IF;

        IF P_FINE_VALUE IS NULL OR P_FINE_VALUE < 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20923,
                    'FINE VALUE CANNOT BE NEGATIVE'
            );
        END IF;

        IF P_FINE_TYPE = 'NONE'
            AND P_FINE_VALUE <> 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20924,
                    'FINE VALUE MUST BE ZERO WHEN FINE TYPE IS NONE'
            );
        END IF;

        IF P_FINE_TYPE = 'PERCENTAGE'
            AND P_FINE_VALUE > 100 THEN
            RAISE_APPLICATION_ERROR(
                    -20925,
                    'FINE PERCENTAGE CANNOT EXCEED 100'
            );
        END IF;
    END VALIDATE_FINE;

    ------------------------------------------------------------------
    -- Validate basic values
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_VALUES(
        P_STRUCTURE_NAME IN FEE_STRUCTURE.STRUCTURE_NAME%TYPE,
        P_AMOUNT         IN FEE_STRUCTURE.AMOUNT%TYPE,
        P_DUE_DAY        IN FEE_STRUCTURE.DUE_DAY%TYPE,
        P_EFFECTIVE_FROM IN FEE_STRUCTURE.EFFECTIVE_FROM%TYPE,
        P_EFFECTIVE_TO   IN FEE_STRUCTURE.EFFECTIVE_TO%TYPE
    ) AS
    BEGIN
        IF TRIM(P_STRUCTURE_NAME) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20926,
                    'FEE STRUCTURE NAME IS REQUIRED'
            );
        END IF;

        IF P_AMOUNT IS NULL OR P_AMOUNT <= 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20927,
                    'FEE AMOUNT MUST BE GREATER THAN ZERO'
            );
        END IF;

        IF P_DUE_DAY IS NOT NULL
            AND (P_DUE_DAY < 1 OR P_DUE_DAY > 31) THEN
            RAISE_APPLICATION_ERROR(
                    -20928,
                    'DUE DAY MUST BE BETWEEN 1 AND 31'
            );
        END IF;

        IF P_EFFECTIVE_FROM IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20929,
                    'EFFECTIVE FROM DATE IS REQUIRED'
            );
        END IF;

        IF P_EFFECTIVE_TO IS NOT NULL
            AND P_EFFECTIVE_TO < P_EFFECTIVE_FROM THEN
            RAISE_APPLICATION_ERROR(
                    -20930,
                    'EFFECTIVE TO DATE CANNOT BE BEFORE EFFECTIVE FROM DATE'
            );
        END IF;
    END VALIDATE_VALUES;

    ------------------------------------------------------------------
    -- Prevent duplicate structures, including NULL section
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_DUPLICATE(
        P_FEE_STRUCTURE_ID       IN FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE,
        P_ACADEMIC_YEAR_ID       IN FEE_STRUCTURE.ACADEMIC_YEAR_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN FEE_STRUCTURE.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN FEE_STRUCTURE.SECTION_ID%TYPE,
        P_FEE_TYPE_ID            IN FEE_STRUCTURE.FEE_TYPE_ID%TYPE,
        P_STRUCTURE_NAME         IN FEE_STRUCTURE.STRUCTURE_NAME%TYPE
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM FEE_STRUCTURE
        WHERE ACADEMIC_YEAR_ID = P_ACADEMIC_YEAR_ID
          AND ACADEMIC_YEAR_CLASS_ID =
              P_ACADEMIC_YEAR_CLASS_ID
          AND (
            SECTION_ID = P_SECTION_ID
                OR (
                SECTION_ID IS NULL
                    AND P_SECTION_ID IS NULL
                )
            )
          AND FEE_TYPE_ID = P_FEE_TYPE_ID
          AND UPPER(TRIM(STRUCTURE_NAME)) =
              UPPER(TRIM(P_STRUCTURE_NAME))
          AND (
            P_FEE_STRUCTURE_ID IS NULL
                OR FEE_STRUCTURE_ID <> P_FEE_STRUCTURE_ID
            );

        IF V_COUNT > 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20931,
                    'FEE STRUCTURE ALREADY EXISTS'
            );
        END IF;
    END VALIDATE_DUPLICATE;

    ------------------------------------------------------------------
    -- Create
    ------------------------------------------------------------------
    PROCEDURE CREATE_FEE_STRUCTURE(
        P_ACADEMIC_YEAR_ID       IN FEE_STRUCTURE.ACADEMIC_YEAR_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN FEE_STRUCTURE.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN FEE_STRUCTURE.SECTION_ID%TYPE,
        P_FEE_TYPE_ID            IN FEE_STRUCTURE.FEE_TYPE_ID%TYPE,
        P_STRUCTURE_NAME         IN FEE_STRUCTURE.STRUCTURE_NAME%TYPE,
        P_AMOUNT                 IN FEE_STRUCTURE.AMOUNT%TYPE,
        P_FREQUENCY              IN FEE_STRUCTURE.FREQUENCY%TYPE,
        P_DUE_DAY                IN FEE_STRUCTURE.DUE_DAY%TYPE,
        P_EFFECTIVE_FROM         IN FEE_STRUCTURE.EFFECTIVE_FROM%TYPE,
        P_EFFECTIVE_TO           IN FEE_STRUCTURE.EFFECTIVE_TO%TYPE,
        P_FINE_TYPE              IN FEE_STRUCTURE.FINE_TYPE%TYPE,
        P_FINE_VALUE             IN FEE_STRUCTURE.FINE_VALUE%TYPE,
        P_DESCRIPTION            IN FEE_STRUCTURE.DESCRIPTION%TYPE,
        P_STATUS                 IN FEE_STRUCTURE.STATUS%TYPE,
        P_CREATED_BY             IN FEE_STRUCTURE.CREATED_BY%TYPE,
        P_FEE_STRUCTURE_ID       OUT FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE
    ) AS
        V_STATUS     FEE_STRUCTURE.STATUS%TYPE;
        V_FREQUENCY  FEE_STRUCTURE.FREQUENCY%TYPE;
        V_FINE_TYPE  FEE_STRUCTURE.FINE_TYPE%TYPE;
        V_FINE_VALUE FEE_STRUCTURE.FINE_VALUE%TYPE;
    BEGIN
        IF P_ACADEMIC_YEAR_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20932,
                    'ACADEMIC YEAR ID IS REQUIRED'
            );
        END IF;

        IF P_ACADEMIC_YEAR_CLASS_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20933,
                    'ACADEMIC YEAR CLASS ID IS REQUIRED'
            );
        END IF;

        IF P_FEE_TYPE_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20934,
                    'FEE TYPE ID IS REQUIRED'
            );
        END IF;

        V_STATUS :=
                NVL(UPPER(TRIM(P_STATUS)), 'ACTIVE');

        V_FREQUENCY :=
                UPPER(TRIM(P_FREQUENCY));

        V_FINE_TYPE :=
                NVL(UPPER(TRIM(P_FINE_TYPE)), 'NONE');

        V_FINE_VALUE :=
                NVL(P_FINE_VALUE, 0);

        VALIDATE_STATUS(V_STATUS);
        VALIDATE_FREQUENCY(V_FREQUENCY);

        VALIDATE_FINE(
                V_FINE_TYPE,
                V_FINE_VALUE
        );

        VALIDATE_VALUES(
                P_STRUCTURE_NAME,
                P_AMOUNT,
                P_DUE_DAY,
                P_EFFECTIVE_FROM,
                P_EFFECTIVE_TO
        );

        VALIDATE_DUPLICATE(
                NULL,
                P_ACADEMIC_YEAR_ID,
                P_ACADEMIC_YEAR_CLASS_ID,
                P_SECTION_ID,
                P_FEE_TYPE_ID,
                P_STRUCTURE_NAME
        );

        INSERT INTO FEE_STRUCTURE (
            ACADEMIC_YEAR_ID,
            ACADEMIC_YEAR_CLASS_ID,
            SECTION_ID,
            FEE_TYPE_ID,
            STRUCTURE_NAME,
            AMOUNT,
            FREQUENCY,
            DUE_DAY,
            EFFECTIVE_FROM,
            EFFECTIVE_TO,
            FINE_TYPE,
            FINE_VALUE,
            DESCRIPTION,
            STATUS,
            CREATED_BY
        ) VALUES (
                     P_ACADEMIC_YEAR_ID,
                     P_ACADEMIC_YEAR_CLASS_ID,
                     P_SECTION_ID,
                     P_FEE_TYPE_ID,
                     TRIM(P_STRUCTURE_NAME),
                     P_AMOUNT,
                     V_FREQUENCY,
                     P_DUE_DAY,
                     P_EFFECTIVE_FROM,
                     P_EFFECTIVE_TO,
                     V_FINE_TYPE,
                     V_FINE_VALUE,
                     P_DESCRIPTION,
                     V_STATUS,
                     P_CREATED_BY
                 )
        RETURNING FEE_STRUCTURE_ID
        INTO P_FEE_STRUCTURE_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20931,
                    'FEE STRUCTURE ALREADY EXISTS'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20935,
                    'ERROR CREATING FEE STRUCTURE: ' || SQLERRM
            );
    END CREATE_FEE_STRUCTURE;

    ------------------------------------------------------------------
    -- Update
    ------------------------------------------------------------------
    PROCEDURE UPDATE_FEE_STRUCTURE(
        P_FEE_STRUCTURE_ID       IN FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE,
        P_ACADEMIC_YEAR_ID       IN FEE_STRUCTURE.ACADEMIC_YEAR_ID%TYPE,
        P_ACADEMIC_YEAR_CLASS_ID IN FEE_STRUCTURE.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_SECTION_ID             IN FEE_STRUCTURE.SECTION_ID%TYPE,
        P_FEE_TYPE_ID            IN FEE_STRUCTURE.FEE_TYPE_ID%TYPE,
        P_STRUCTURE_NAME         IN FEE_STRUCTURE.STRUCTURE_NAME%TYPE,
        P_AMOUNT                 IN FEE_STRUCTURE.AMOUNT%TYPE,
        P_FREQUENCY              IN FEE_STRUCTURE.FREQUENCY%TYPE,
        P_DUE_DAY                IN FEE_STRUCTURE.DUE_DAY%TYPE,
        P_EFFECTIVE_FROM         IN FEE_STRUCTURE.EFFECTIVE_FROM%TYPE,
        P_EFFECTIVE_TO           IN FEE_STRUCTURE.EFFECTIVE_TO%TYPE,
        P_FINE_TYPE              IN FEE_STRUCTURE.FINE_TYPE%TYPE,
        P_FINE_VALUE             IN FEE_STRUCTURE.FINE_VALUE%TYPE,
        P_DESCRIPTION            IN FEE_STRUCTURE.DESCRIPTION%TYPE,
        P_STATUS                 IN FEE_STRUCTURE.STATUS%TYPE,
        P_UPDATED_BY             IN FEE_STRUCTURE.UPDATED_BY%TYPE
    ) AS
        V_STATUS     FEE_STRUCTURE.STATUS%TYPE;
        V_FREQUENCY  FEE_STRUCTURE.FREQUENCY%TYPE;
        V_FINE_TYPE  FEE_STRUCTURE.FINE_TYPE%TYPE;
        V_FINE_VALUE FEE_STRUCTURE.FINE_VALUE%TYPE;
    BEGIN
        IF P_FEE_STRUCTURE_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20936,
                    'FEE STRUCTURE ID IS REQUIRED'
            );
        END IF;

        IF P_ACADEMIC_YEAR_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20932,
                    'ACADEMIC YEAR ID IS REQUIRED'
            );
        END IF;

        IF P_ACADEMIC_YEAR_CLASS_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20933,
                    'ACADEMIC YEAR CLASS ID IS REQUIRED'
            );
        END IF;

        IF P_FEE_TYPE_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20934,
                    'FEE TYPE ID IS REQUIRED'
            );
        END IF;

        V_STATUS := UPPER(TRIM(P_STATUS));
        V_FREQUENCY := UPPER(TRIM(P_FREQUENCY));
        V_FINE_TYPE := UPPER(TRIM(P_FINE_TYPE));
        V_FINE_VALUE := NVL(P_FINE_VALUE, 0);

        VALIDATE_STATUS(V_STATUS);
        VALIDATE_FREQUENCY(V_FREQUENCY);

        VALIDATE_FINE(
                V_FINE_TYPE,
                V_FINE_VALUE
        );

        VALIDATE_VALUES(
                P_STRUCTURE_NAME,
                P_AMOUNT,
                P_DUE_DAY,
                P_EFFECTIVE_FROM,
                P_EFFECTIVE_TO
        );

        VALIDATE_DUPLICATE(
                P_FEE_STRUCTURE_ID,
                P_ACADEMIC_YEAR_ID,
                P_ACADEMIC_YEAR_CLASS_ID,
                P_SECTION_ID,
                P_FEE_TYPE_ID,
                P_STRUCTURE_NAME
        );

        UPDATE FEE_STRUCTURE
        SET ACADEMIC_YEAR_ID       = P_ACADEMIC_YEAR_ID,
            ACADEMIC_YEAR_CLASS_ID =
                P_ACADEMIC_YEAR_CLASS_ID,
            SECTION_ID             = P_SECTION_ID,
            FEE_TYPE_ID            = P_FEE_TYPE_ID,
            STRUCTURE_NAME         = TRIM(P_STRUCTURE_NAME),
            AMOUNT                 = P_AMOUNT,
            FREQUENCY              = V_FREQUENCY,
            DUE_DAY                = P_DUE_DAY,
            EFFECTIVE_FROM         = P_EFFECTIVE_FROM,
            EFFECTIVE_TO           = P_EFFECTIVE_TO,
            FINE_TYPE              = V_FINE_TYPE,
            FINE_VALUE             = V_FINE_VALUE,
            DESCRIPTION            = P_DESCRIPTION,
            STATUS                 = V_STATUS,
            UPDATED_AT             = SYSTIMESTAMP,
            UPDATED_BY             = P_UPDATED_BY
        WHERE FEE_STRUCTURE_ID = P_FEE_STRUCTURE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20937,
                    'FEE STRUCTURE NOT FOUND'
            );
        END IF;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20931,
                    'FEE STRUCTURE ALREADY EXISTS'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20938,
                    'ERROR UPDATING FEE STRUCTURE: ' || SQLERRM
            );
    END UPDATE_FEE_STRUCTURE;

    ------------------------------------------------------------------
    -- Get by ID
    ------------------------------------------------------------------
    PROCEDURE GET_FEE_STRUCTURE_BY_ID(
        P_FEE_STRUCTURE_ID IN FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE,
        P_RESULT           OUT SYS_REFCURSOR
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM FEE_STRUCTURE
        WHERE FEE_STRUCTURE_ID = P_FEE_STRUCTURE_ID;

        IF V_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20937,
                    'FEE STRUCTURE NOT FOUND'
            );
        END IF;

        OPEN P_RESULT FOR
            SELECT FS.*
            FROM FEE_STRUCTURE FS
            WHERE FS.FEE_STRUCTURE_ID =
                  P_FEE_STRUCTURE_ID;
    END GET_FEE_STRUCTURE_BY_ID;

    ------------------------------------------------------------------
    -- Get all
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_FEE_STRUCTURES(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FS.*
            FROM FEE_STRUCTURE FS
            ORDER BY FS.EFFECTIVE_FROM DESC,
                     FS.STRUCTURE_NAME,
                     FS.FEE_STRUCTURE_ID;
    END GET_ALL_FEE_STRUCTURES;

    ------------------------------------------------------------------
    -- Get by academic year
    ------------------------------------------------------------------
    PROCEDURE GET_STRUCTURES_BY_YEAR(
        P_ACADEMIC_YEAR_ID IN FEE_STRUCTURE.ACADEMIC_YEAR_ID%TYPE,
        P_RESULT           OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FS.*
            FROM FEE_STRUCTURE FS
            WHERE FS.ACADEMIC_YEAR_ID =
                  P_ACADEMIC_YEAR_ID
            ORDER BY FS.STRUCTURE_NAME,
                     FS.FEE_STRUCTURE_ID;
    END GET_STRUCTURES_BY_YEAR;

    ------------------------------------------------------------------
    -- Get by class
    ------------------------------------------------------------------
    PROCEDURE GET_STRUCTURES_BY_CLASS(
        P_ACADEMIC_YEAR_CLASS_ID
            IN FEE_STRUCTURE.ACADEMIC_YEAR_CLASS_ID%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FS.*
            FROM FEE_STRUCTURE FS
            WHERE FS.ACADEMIC_YEAR_CLASS_ID =
                  P_ACADEMIC_YEAR_CLASS_ID
            ORDER BY FS.STRUCTURE_NAME,
                     FS.FEE_STRUCTURE_ID;
    END GET_STRUCTURES_BY_CLASS;

    ------------------------------------------------------------------
    -- Get by section
    ------------------------------------------------------------------
    PROCEDURE GET_STRUCTURES_BY_SECTION(
        P_SECTION_ID IN FEE_STRUCTURE.SECTION_ID%TYPE,
        P_RESULT     OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FS.*
            FROM FEE_STRUCTURE FS
            WHERE FS.SECTION_ID = P_SECTION_ID
            ORDER BY FS.STRUCTURE_NAME,
                     FS.FEE_STRUCTURE_ID;
    END GET_STRUCTURES_BY_SECTION;

    ------------------------------------------------------------------
    -- Get active
    ------------------------------------------------------------------
    PROCEDURE GET_ACTIVE_FEE_STRUCTURES(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FS.*
            FROM FEE_STRUCTURE FS
            WHERE FS.STATUS = 'ACTIVE'
            ORDER BY FS.ACADEMIC_YEAR_ID,
                     FS.ACADEMIC_YEAR_CLASS_ID,
                     FS.STRUCTURE_NAME;
    END GET_ACTIVE_FEE_STRUCTURES;

    ------------------------------------------------------------------
    -- Update status
    ------------------------------------------------------------------
    PROCEDURE UPDATE_FEE_STRUCTURE_STATUS(
        P_FEE_STRUCTURE_ID IN FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE,
        P_STATUS           IN FEE_STRUCTURE.STATUS%TYPE,
        P_UPDATED_BY       IN FEE_STRUCTURE.UPDATED_BY%TYPE
    ) AS
        V_STATUS FEE_STRUCTURE.STATUS%TYPE;
    BEGIN
        V_STATUS := UPPER(TRIM(P_STATUS));

        VALIDATE_STATUS(V_STATUS);

        UPDATE FEE_STRUCTURE
        SET STATUS     = V_STATUS,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE FEE_STRUCTURE_ID = P_FEE_STRUCTURE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20937,
                    'FEE STRUCTURE NOT FOUND'
            );
        END IF;
    END UPDATE_FEE_STRUCTURE_STATUS;

    ------------------------------------------------------------------
    -- Delete
    ------------------------------------------------------------------
    PROCEDURE DELETE_FEE_STRUCTURE(
        P_FEE_STRUCTURE_ID IN FEE_STRUCTURE.FEE_STRUCTURE_ID%TYPE
    ) AS
    BEGIN
        DELETE FROM FEE_STRUCTURE
        WHERE FEE_STRUCTURE_ID = P_FEE_STRUCTURE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20937,
                    'FEE STRUCTURE NOT FOUND'
            );
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2292 THEN
                RAISE_APPLICATION_ERROR(
                        -20939,
                        'FEE STRUCTURE CANNOT BE DELETED BECAUSE STUDENT FEES EXIST'
                );
            END IF;

            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE;
    END DELETE_FEE_STRUCTURE;

END FEE_STRUCTURE_PKG;
/