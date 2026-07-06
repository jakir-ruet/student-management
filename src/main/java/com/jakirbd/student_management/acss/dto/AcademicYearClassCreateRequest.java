package com.jakirbd.student_management.acss.dto;

public class AcademicYearClassCreateRequest {

    private Long academicYearId;
    private Long classId;
    private String status;

    public AcademicYearClassCreateRequest() {
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Long academicYearId) {
        this.academicYearId = academicYearId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
