--------------------------------------------
-- Check default roles and permissions
--------------------------------------------
SELECT ROLE_ID, ROLE_NAME, STATUS
FROM ROLES
ORDER BY ROLE_ID;

SELECT PERMISSION_ID, PERMISSION_CODE, STATUS
FROM PERMISSIONS
ORDER BY PERMISSION_ID;

--------------------------------------------
-- Assign TEACHER role to teacher1
--------------------------------------------
DECLARE
    V_USER_ID USERS.USER_ID%TYPE;
    V_ROLE_ID ROLES.ROLE_ID%TYPE;
BEGIN
    SELECT USER_ID INTO V_USER_ID
    FROM USERS
    WHERE USERNAME = 'teacher1';

    SELECT ROLE_ID INTO V_ROLE_ID
    FROM ROLES
    WHERE ROLE_NAME = 'TEACHER';

    ROLE_PKG.ASSIGN_ROLE_TO_USER(V_USER_ID, V_ROLE_ID);

    COMMIT;
END;
/

--------------------------------------------
-- Assign permissions to TEACHER role
--------------------------------------------
DECLARE
    V_ROLE_ID ROLES.ROLE_ID%TYPE;
BEGIN
    SELECT ROLE_ID INTO V_ROLE_ID
    FROM ROLES
    WHERE ROLE_NAME = 'TEACHER';

    INSERT INTO ROLE_PERMISSIONS (ROLE_ID, PERMISSION_ID)
    SELECT V_ROLE_ID, PERMISSION_ID
    FROM PERMISSIONS
    WHERE PERMISSION_CODE IN (
        'STUDENT_VIEW',
        'ATTENDANCE_MANAGE',
        'EXAM_MANAGE',
        'REPORT_VIEW'
    );

    COMMIT;
END;
/

--------------------------------------------
-- View user roles
--------------------------------------------
VAR RC REFCURSOR;

BEGIN
    ROLE_PKG.GET_USER_ROLES(
        P_USER_ID => 2,
        P_RESULT  => :RC
    );
END;
/

PRINT RC;

--------------------------------------------
-- View user permissions
--------------------------------------------
VAR RC REFCURSOR;

BEGIN
    PERMISSION_PKG.GET_USER_PERMISSIONS(
        P_USER_ID => 2,
        P_RESULT  => :RC
    );
END;
/

PRINT RC;
