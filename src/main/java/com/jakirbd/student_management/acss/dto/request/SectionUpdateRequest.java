package com.jakirbd.student_management.acss.dto.request;

public class SectionUpdateRequest {

    private Long academicYearClassId;
    private String sectionName;
    private String sectionCode;
    private Integer capacity;
    private Integer displayOrder;
    private String status;

    public SectionUpdateRequest() {
    }

    public Long getAcademicYearClassId() {
        return academicYearClassId;
    }

    public void setAcademicYearClassId(Long academicYearClassId) {
        this.academicYearClassId = academicYearClassId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
