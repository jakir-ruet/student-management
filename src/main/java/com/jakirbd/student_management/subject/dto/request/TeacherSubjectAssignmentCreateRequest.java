package com.jakirbd.student_management.subject.dto.request;

public class TeacherSubjectAssignmentCreateRequest {

	private Long classSubjectId;
	private Long sectionShiftId;
	private Long teacherId;
	private String assignmentType;
	private String status;

	public Long getClassSubjectId() {
		return classSubjectId;
	}
	public void setClassSubjectId(Long classSubjectId) {
		this.classSubjectId = classSubjectId;
	}
	public Long getSectionShiftId() {
		return sectionShiftId;
	}
	public void setSectionShiftId(Long sectionShiftId) {
		this.sectionShiftId = sectionShiftId;
	}
	public Long getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
	public String getAssignmentType() {
		return assignmentType;
	}
	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


}
