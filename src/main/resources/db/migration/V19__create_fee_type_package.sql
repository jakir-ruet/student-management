----------------------------------------------------------------------
-- V19: FEE TYPE PACKAGE
----------------------------------------------------------------------

CREATE OR REPLACE PACKAGE FEE_TYPE_PKG AS

    PROCEDURE CREATE_FEE_TYPE(
        P_FEE_TYPE_NAME   IN FEE_TYPE.FEE_TYPE_NAME%TYPE,
        P_FEE_TYPE_CODE   IN FEE_TYPE.FEE_TYPE_CODE%TYPE,
        P_DESCRIPTION     IN FEE_TYPE.DESCRIPTION%TYPE,
        P_DISPLAY_ORDER   IN FEE_TYPE.DISPLAY_ORDER%TYPE,
        P_STATUS          IN FEE_TYPE.STATUS%TYPE,
        P_CREATED_BY      IN FEE_TYPE.CREATED_BY%TYPE,
        P_FEE_TYPE_ID     OUT FEE_TYPE.FEE_TYPE_ID%TYPE
    );

    PROCEDURE UPDATE_FEE_TYPE(
        P_FEE_TYPE_ID     IN FEE_TYPE.FEE_TYPE_ID%TYPE,
        P_FEE_TYPE_NAME   IN FEE_TYPE.FEE_TYPE_NAME%TYPE,
        P_FEE_TYPE_CODE   IN FEE_TYPE.FEE_TYPE_CODE%TYPE,
        P_DESCRIPTION     IN FEE_TYPE.DESCRIPTION%TYPE,
        P_DISPLAY_ORDER   IN FEE_TYPE.DISPLAY_ORDER%TYPE,
        P_STATUS          IN FEE_TYPE.STATUS%TYPE,
        P_UPDATED_BY      IN FEE_TYPE.UPDATED_BY%TYPE
    );

    PROCEDURE GET_FEE_TYPE_BY_ID(
        P_FEE_TYPE_ID IN FEE_TYPE.FEE_TYPE_ID%TYPE,
        P_RESULT      OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ALL_FEE_TYPES(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_ACTIVE_FEE_TYPES(
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE UPDATE_FEE_TYPE_STATUS(
        P_FEE_TYPE_ID IN FEE_TYPE.FEE_TYPE_ID%TYPE,
        P_STATUS      IN FEE_TYPE.STATUS%TYPE,
        P_UPDATED_BY  IN FEE_TYPE.UPDATED_BY%TYPE
    );

    PROCEDURE DELETE_FEE_TYPE(
        P_FEE_TYPE_ID IN FEE_TYPE.FEE_TYPE_ID%TYPE
    );

END FEE_TYPE_PKG;
/

CREATE OR REPLACE PACKAGE BODY FEE_TYPE_PKG AS

    ------------------------------------------------------------------
    -- Validate status
    ------------------------------------------------------------------
    PROCEDURE VALIDATE_STATUS(
        P_STATUS IN FEE_TYPE.STATUS%TYPE
    ) AS
    BEGIN
        IF P_STATUS IS NULL
            OR P_STATUS NOT IN ('ACTIVE', 'INACTIVE') THEN
            RAISE_APPLICATION_ERROR(
                    -20901,
                    'INVALID FEE TYPE STATUS'
            );
        END IF;
    END VALIDATE_STATUS;

    ------------------------------------------------------------------
    -- Create fee type
    ------------------------------------------------------------------
    PROCEDURE CREATE_FEE_TYPE(
        P_FEE_TYPE_NAME   IN FEE_TYPE.FEE_TYPE_NAME%TYPE,
        P_FEE_TYPE_CODE   IN FEE_TYPE.FEE_TYPE_CODE%TYPE,
        P_DESCRIPTION     IN FEE_TYPE.DESCRIPTION%TYPE,
        P_DISPLAY_ORDER   IN FEE_TYPE.DISPLAY_ORDER%TYPE,
        P_STATUS          IN FEE_TYPE.STATUS%TYPE,
        P_CREATED_BY      IN FEE_TYPE.CREATED_BY%TYPE,
        P_FEE_TYPE_ID     OUT FEE_TYPE.FEE_TYPE_ID%TYPE
    ) AS
        V_STATUS        FEE_TYPE.STATUS%TYPE;
        V_DISPLAY_ORDER FEE_TYPE.DISPLAY_ORDER%TYPE;
    BEGIN
        IF TRIM(P_FEE_TYPE_NAME) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20902,
                    'FEE TYPE NAME IS REQUIRED'
            );
        END IF;

        IF TRIM(P_FEE_TYPE_CODE) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20903,
                    'FEE TYPE CODE IS REQUIRED'
            );
        END IF;

        V_STATUS :=
                NVL(UPPER(TRIM(P_STATUS)), 'ACTIVE');

        V_DISPLAY_ORDER :=
                NVL(P_DISPLAY_ORDER, 1);

        IF V_DISPLAY_ORDER <= 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20904,
                    'DISPLAY ORDER MUST BE GREATER THAN ZERO'
            );
        END IF;

        VALIDATE_STATUS(V_STATUS);

        INSERT INTO FEE_TYPE (
            FEE_TYPE_NAME,
            FEE_TYPE_CODE,
            DESCRIPTION,
            DISPLAY_ORDER,
            STATUS,
            CREATED_BY
        ) VALUES (
                     TRIM(P_FEE_TYPE_NAME),
                     UPPER(TRIM(P_FEE_TYPE_CODE)),
                     P_DESCRIPTION,
                     V_DISPLAY_ORDER,
                     V_STATUS,
                     P_CREATED_BY
                 )
        RETURNING FEE_TYPE_ID
        INTO P_FEE_TYPE_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20905,
                    'FEE TYPE NAME OR CODE ALREADY EXISTS'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20906,
                    'ERROR CREATING FEE TYPE: ' || SQLERRM
            );
    END CREATE_FEE_TYPE;

    ------------------------------------------------------------------
    -- Update fee type
    ------------------------------------------------------------------
    PROCEDURE UPDATE_FEE_TYPE(
        P_FEE_TYPE_ID     IN FEE_TYPE.FEE_TYPE_ID%TYPE,
        P_FEE_TYPE_NAME   IN FEE_TYPE.FEE_TYPE_NAME%TYPE,
        P_FEE_TYPE_CODE   IN FEE_TYPE.FEE_TYPE_CODE%TYPE,
        P_DESCRIPTION     IN FEE_TYPE.DESCRIPTION%TYPE,
        P_DISPLAY_ORDER   IN FEE_TYPE.DISPLAY_ORDER%TYPE,
        P_STATUS          IN FEE_TYPE.STATUS%TYPE,
        P_UPDATED_BY      IN FEE_TYPE.UPDATED_BY%TYPE
    ) AS
        V_STATUS FEE_TYPE.STATUS%TYPE;
    BEGIN
        IF P_FEE_TYPE_ID IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20907,
                    'FEE TYPE ID IS REQUIRED'
            );
        END IF;

        IF TRIM(P_FEE_TYPE_NAME) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20902,
                    'FEE TYPE NAME IS REQUIRED'
            );
        END IF;

        IF TRIM(P_FEE_TYPE_CODE) IS NULL THEN
            RAISE_APPLICATION_ERROR(
                    -20903,
                    'FEE TYPE CODE IS REQUIRED'
            );
        END IF;

        IF P_DISPLAY_ORDER IS NULL
            OR P_DISPLAY_ORDER <= 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20904,
                    'DISPLAY ORDER MUST BE GREATER THAN ZERO'
            );
        END IF;

        V_STATUS := UPPER(TRIM(P_STATUS));

        VALIDATE_STATUS(V_STATUS);

        UPDATE FEE_TYPE
        SET FEE_TYPE_NAME = TRIM(P_FEE_TYPE_NAME),
            FEE_TYPE_CODE = UPPER(TRIM(P_FEE_TYPE_CODE)),
            DESCRIPTION   = P_DESCRIPTION,
            DISPLAY_ORDER = P_DISPLAY_ORDER,
            STATUS        = V_STATUS,
            UPDATED_AT    = SYSTIMESTAMP,
            UPDATED_BY    = P_UPDATED_BY
        WHERE FEE_TYPE_ID = P_FEE_TYPE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20908,
                    'FEE TYPE NOT FOUND'
            );
        END IF;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                    -20905,
                    'FEE TYPE NAME OR CODE ALREADY EXISTS'
            );

        WHEN OTHERS THEN
            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20909,
                    'ERROR UPDATING FEE TYPE: ' || SQLERRM
            );
    END UPDATE_FEE_TYPE;

    ------------------------------------------------------------------
    -- Get fee type by ID
    ------------------------------------------------------------------
    PROCEDURE GET_FEE_TYPE_BY_ID(
        P_FEE_TYPE_ID IN FEE_TYPE.FEE_TYPE_ID%TYPE,
        P_RESULT      OUT SYS_REFCURSOR
    ) AS
        V_COUNT NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM FEE_TYPE
        WHERE FEE_TYPE_ID = P_FEE_TYPE_ID;

        IF V_COUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20908,
                    'FEE TYPE NOT FOUND'
            );
        END IF;

        OPEN P_RESULT FOR
            SELECT FT.*
            FROM FEE_TYPE FT
            WHERE FT.FEE_TYPE_ID = P_FEE_TYPE_ID;
    END GET_FEE_TYPE_BY_ID;

    ------------------------------------------------------------------
    -- Get all fee types
    ------------------------------------------------------------------
    PROCEDURE GET_ALL_FEE_TYPES(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FT.*
            FROM FEE_TYPE FT
            ORDER BY FT.DISPLAY_ORDER,
                     FT.FEE_TYPE_NAME,
                     FT.FEE_TYPE_ID;
    END GET_ALL_FEE_TYPES;

    ------------------------------------------------------------------
    -- Get active fee types
    ------------------------------------------------------------------
    PROCEDURE GET_ACTIVE_FEE_TYPES(
        P_RESULT OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT FT.*
            FROM FEE_TYPE FT
            WHERE FT.STATUS = 'ACTIVE'
            ORDER BY FT.DISPLAY_ORDER,
                     FT.FEE_TYPE_NAME,
                     FT.FEE_TYPE_ID;
    END GET_ACTIVE_FEE_TYPES;

    ------------------------------------------------------------------
    -- Update fee type status
    ------------------------------------------------------------------
    PROCEDURE UPDATE_FEE_TYPE_STATUS(
        P_FEE_TYPE_ID IN FEE_TYPE.FEE_TYPE_ID%TYPE,
        P_STATUS      IN FEE_TYPE.STATUS%TYPE,
        P_UPDATED_BY  IN FEE_TYPE.UPDATED_BY%TYPE
    ) AS
        V_STATUS FEE_TYPE.STATUS%TYPE;
    BEGIN
        V_STATUS := UPPER(TRIM(P_STATUS));

        VALIDATE_STATUS(V_STATUS);

        UPDATE FEE_TYPE
        SET STATUS     = V_STATUS,
            UPDATED_AT = SYSTIMESTAMP,
            UPDATED_BY = P_UPDATED_BY
        WHERE FEE_TYPE_ID = P_FEE_TYPE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20908,
                    'FEE TYPE NOT FOUND'
            );
        END IF;
    END UPDATE_FEE_TYPE_STATUS;

    ------------------------------------------------------------------
    -- Delete fee type
    ------------------------------------------------------------------
    PROCEDURE DELETE_FEE_TYPE(
        P_FEE_TYPE_ID IN FEE_TYPE.FEE_TYPE_ID%TYPE
    ) AS
    BEGIN
        DELETE FROM FEE_TYPE
        WHERE FEE_TYPE_ID = P_FEE_TYPE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                    -20908,
                    'FEE TYPE NOT FOUND'
            );
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -2292 THEN
                RAISE_APPLICATION_ERROR(
                        -20910,
                        'FEE TYPE CANNOT BE DELETED BECAUSE FEE STRUCTURES EXIST'
                );
            END IF;

            IF SQLCODE BETWEEN -20999 AND -20901 THEN
                RAISE;
            END IF;

            RAISE_APPLICATION_ERROR(
                    -20911,
                    'ERROR DELETING FEE TYPE: ' || SQLERRM
            );
    END DELETE_FEE_TYPE;

END FEE_TYPE_PKG;
/