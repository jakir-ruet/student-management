DECLARE
    V_STUDENT_ID NUMBER;
BEGIN
    STUDENT_PKG.ADD_STUDENT(
            P_STUDENT_CODE   => 'STD-001',
            P_FIRST_NAME     => 'Rahim',
            P_LAST_NAME      => 'Uddin',
            P_EMAIL          => 'rahim@student.com',
            P_PHONE          => '01710000001',
            P_GENDER         => 'MALE',
            P_DATE_OF_BIRTH  => DATE '2010-01-15',
            P_ADMISSION_DATE => SYSDATE,
            P_STUDENT_ID     => V_STUDENT_ID
    );

    DBMS_OUTPUT.PUT_LINE('Student ID: ' || V_STUDENT_ID);

    COMMIT;
END;
/

SELECT *
FROM STUDENTS;

SELECT *
FROM STUDENTS
WHERE STUDENT_ID =1;