package com.jakirbd.student_management.common.exception;

public class OracleExceptionTranslator extends RuntimeException{
    public OracleExceptionTranslator(String message){
        super(message);
    }
}
