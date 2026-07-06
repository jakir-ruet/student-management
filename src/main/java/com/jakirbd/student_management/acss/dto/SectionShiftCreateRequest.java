package com.jakirbd.student_management.acss.dto;

public class SectionShiftCreateRequest {

    private Long sectionId;
    private Long shiftId;
    private String status;

    public SectionShiftCreateRequest() {
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
