---------------------------------------------
-- Test Register User
---------------------------------------------
SET SERVEROUTPUT ON;

DECLARE
    V_USER_ID USERS.USER_ID%TYPE;
BEGIN
    AUTH_PKG.REGISTER_USER(
        P_USERNAME      => 'teacher1',
        P_EMAIL         => 'teacher1@student.com',
        P_PASSWORD_HASH => 'HASH_123',
        P_FULL_NAME     => 'Teacher One',
        P_USER_ID       => V_USER_ID
    );

    DBMS_OUTPUT.PUT_LINE('Created User ID: ' || V_USER_ID);
END;
/

---------------------------------------------
-- Test Successful Login
---------------------------------------------
SET SERVEROUTPUT ON;

DECLARE
    V_USER_ID USERS.USER_ID%TYPE;
    V_STATUS  USERS.STATUS%TYPE;
BEGIN
    AUTH_PKG.LOGIN_USER(
        P_USERNAME      => 'teacher1',
        P_PASSWORD_HASH => 'HASH_123',
        P_IP_ADDRESS    => '127.0.0.1',
        P_USER_ID       => V_USER_ID,
        P_STATUS        => V_STATUS
    );

    DBMS_OUTPUT.PUT_LINE('Login Success');
    DBMS_OUTPUT.PUT_LINE('User ID: ' || V_USER_ID);
    DBMS_OUTPUT.PUT_LINE('Status: ' || V_STATUS);
END;
/

---------------------------------------------
-- Test Failed Login
---------------------------------------------
SET SERVEROUTPUT ON;

DECLARE
    V_USER_ID USERS.USER_ID%TYPE;
    V_STATUS  USERS.STATUS%TYPE;
BEGIN
    AUTH_PKG.LOGIN_USER(
        P_USERNAME      => 'teacher1',
        P_PASSWORD_HASH => 'WRONG_HASH',
        P_IP_ADDRESS    => '127.0.0.1',
        P_USER_ID       => V_USER_ID,
        P_STATUS        => V_STATUS
    );
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;
/

---------------------------------------------
-- Check Failed Login Count
---------------------------------------------
SELECT USER_ID, USERNAME, STATUS, FAILED_LOGIN, LAST_LOGIN_AT
FROM USERS
WHERE USERNAME = 'teacher1';

---------------------------------------------
-- Test Account Lock
---------------------------------------------
SELECT USER_ID, USERNAME, STATUS, FAILED_LOGIN
FROM USERS
WHERE USERNAME = 'teacher1';

---------------------------------------------
-- Check Login History
---------------------------------------------
SELECT LOGIN_ID, USER_ID, USERNAME, LOGIN_STATUS, IP_ADDRESS, LOGIN_AT
FROM LOGIN_HISTORY
ORDER BY LOGIN_ID DESC;

---------------------------------------------
-- Test Unlock User
---------------------------------------------
SET SERVEROUTPUT ON;

BEGIN
    AUTH_PKG.UNLOCK_USER(
        P_USER_ID => 1
    );
END;
/

SELECT USER_ID, USERNAME, STATUS, FAILED_LOGIN
FROM USERS
WHERE USERNAME = 'teacher1';
