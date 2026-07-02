---------------------------------------------------------
-- Create Role_Pkg's Specification
---------------------------------------------------------
CREATE OR REPLACE PACKAGE ROLE_PKG AS

    PROCEDURE ADD_ROLE (
        P_ROLE_NAME   IN ROLES.ROLE_NAME%TYPE,
        P_DESCRIPTION IN ROLES.DESCRIPTION%TYPE,
        P_ROLE_ID     OUT ROLES.ROLE_ID%TYPE
    );

    PROCEDURE UPDATE_ROLE (
        P_ROLE_ID     IN ROLES.ROLE_ID%TYPE,
        P_ROLE_NAME   IN ROLES.ROLE_NAME%TYPE,
        P_DESCRIPTION IN ROLES.DESCRIPTION%TYPE,
        P_STATUS      IN ROLES.STATUS%TYPE
    );

    PROCEDURE DELETE_ROLE (
        P_ROLE_ID IN ROLES.ROLE_ID%TYPE
    );

    PROCEDURE ASSIGN_ROLE_TO_USER (
        P_USER_ID IN USERS.USER_ID%TYPE,
        P_ROLE_ID IN ROLES.ROLE_ID%TYPE
    );

    PROCEDURE REMOVE_ROLE_FROM_USER (
        P_USER_ID IN USERS.USER_ID%TYPE,
        P_ROLE_ID IN ROLES.ROLE_ID%TYPE
    );

    PROCEDURE GET_USER_ROLES (
        P_USER_ID IN USERS.USER_ID%TYPE,
        P_RESULT  OUT SYS_REFCURSOR
    );

END ROLE_PKG;
/

---------------------------------------------------------
-- Create Role_Pkg's Body
---------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY ROLE_PKG AS

    ------------------------------------------------------------------
    -- ADD ROLE
    ------------------------------------------------------------------
    PROCEDURE ADD_ROLE (
        P_ROLE_NAME   IN ROLES.ROLE_NAME%TYPE,
        P_DESCRIPTION IN ROLES.DESCRIPTION%TYPE,
        P_ROLE_ID     OUT ROLES.ROLE_ID%TYPE
    ) AS
    BEGIN
        INSERT INTO ROLES (
            ROLE_NAME,
            DESCRIPTION
        )
        VALUES (
            UPPER(TRIM(P_ROLE_NAME)),
            P_DESCRIPTION
        )
        RETURNING ROLE_ID INTO P_ROLE_ID;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(-20101, 'ROLE ALREADY EXISTS.');
        WHEN OTHERS THEN
            RAISE_APPLICATION_ERROR(-20102, 'ERROR ADDING ROLE: ' || SQLERRM);
    END ADD_ROLE;

    ------------------------------------------------------------------
    -- UPDATE ROLE
    ------------------------------------------------------------------
    PROCEDURE UPDATE_ROLE (
        P_ROLE_ID     IN ROLES.ROLE_ID%TYPE,
        P_ROLE_NAME   IN ROLES.ROLE_NAME%TYPE,
        P_DESCRIPTION IN ROLES.DESCRIPTION%TYPE,
        P_STATUS      IN ROLES.STATUS%TYPE
    ) AS
    BEGIN
        UPDATE ROLES
        SET ROLE_NAME   = UPPER(TRIM(P_ROLE_NAME)),
            DESCRIPTION = P_DESCRIPTION,
            STATUS      = UPPER(TRIM(P_STATUS)),
            UPDATED_AT  = SYSTIMESTAMP
        WHERE ROLE_ID = P_ROLE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20103, 'ROLE NOT FOUND.');
        END IF;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(-20104, 'ROLE NAME ALREADY EXISTS.');
        WHEN OTHERS THEN
            RAISE_APPLICATION_ERROR(-20105, 'ERROR UPDATING ROLE: ' || SQLERRM);
    END UPDATE_ROLE;

    ------------------------------------------------------------------
    -- DELETE ROLE
    ------------------------------------------------------------------
    PROCEDURE DELETE_ROLE (
        P_ROLE_ID IN ROLES.ROLE_ID%TYPE
    ) AS
        V_COUNT NUMBER;
    BEGIN
        -- Prevent deletion if assigned to users
        SELECT COUNT(*)
        INTO V_COUNT
        FROM USER_ROLES
        WHERE ROLE_ID = P_ROLE_ID;

        IF V_COUNT > 0 THEN
            RAISE_APPLICATION_ERROR(
                -20106,
                'ROLE IS ASSIGNED TO USERS AND CANNOT BE DELETED.'
            );
        END IF;

        DELETE FROM ROLE_PERMISSIONS
        WHERE ROLE_ID = P_ROLE_ID;

        DELETE FROM ROLES
        WHERE ROLE_ID = P_ROLE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20107, 'ROLE NOT FOUND.');
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            RAISE;
    END DELETE_ROLE;

    ------------------------------------------------------------------
    -- ASSIGN ROLE
    ------------------------------------------------------------------
    PROCEDURE ASSIGN_ROLE_TO_USER (
        P_USER_ID IN USERS.USER_ID%TYPE,
        P_ROLE_ID IN ROLES.ROLE_ID%TYPE
    ) AS
    BEGIN
        INSERT INTO USER_ROLES (
            USER_ID,
            ROLE_ID
        )
        VALUES (
            P_USER_ID,
            P_ROLE_ID
        );

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            RAISE_APPLICATION_ERROR(
                -20108,
                'ROLE IS ALREADY ASSIGNED TO THIS USER.'
            );
        WHEN OTHERS THEN
            RAISE_APPLICATION_ERROR(
                -20109,
                'ERROR ASSIGNING ROLE: ' || SQLERRM
            );
    END ASSIGN_ROLE_TO_USER;

    ------------------------------------------------------------------
    -- REMOVE ROLE
    ------------------------------------------------------------------
    PROCEDURE REMOVE_ROLE_FROM_USER (
        P_USER_ID IN USERS.USER_ID%TYPE,
        P_ROLE_ID IN ROLES.ROLE_ID%TYPE
    ) AS
    BEGIN
        DELETE FROM USER_ROLES
        WHERE USER_ID = P_USER_ID
          AND ROLE_ID = P_ROLE_ID;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(
                -20110,
                'USER DOES NOT HAVE THIS ROLE.'
            );
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            RAISE;
    END REMOVE_ROLE_FROM_USER;

    ------------------------------------------------------------------
    -- GET USER ROLES
    ------------------------------------------------------------------
    PROCEDURE GET_USER_ROLES (
        P_USER_ID IN USERS.USER_ID%TYPE,
        P_RESULT  OUT SYS_REFCURSOR
    ) AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT
                R.ROLE_ID,
                R.ROLE_NAME,
                R.DESCRIPTION,
                UR.ASSIGNED_AT
            FROM USER_ROLES UR
            INNER JOIN ROLES R
                ON UR.ROLE_ID = R.ROLE_ID
            WHERE UR.USER_ID = P_USER_ID
            ORDER BY R.ROLE_NAME;
    END GET_USER_ROLES;

END ROLE_PKG;
/
