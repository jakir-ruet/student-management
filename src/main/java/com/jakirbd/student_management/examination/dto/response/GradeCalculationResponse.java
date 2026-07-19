package com.jakirbd.student_management.examination.dto.response;

import java.math.BigDecimal;

public class GradeCalculationResponse {

    private BigDecimal percentage;
    private String letterGrade;
    private BigDecimal gradePoint;

    public GradeCalculationResponse() {
    }

    public GradeCalculationResponse(
            BigDecimal percentage,
            String letterGrade,
            BigDecimal gradePoint
    ) {
        this.percentage = percentage;
        this.letterGrade = letterGrade;
        this.gradePoint = gradePoint;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public BigDecimal getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(BigDecimal gradePoint) {
        this.gradePoint = gradePoint;
    }
}