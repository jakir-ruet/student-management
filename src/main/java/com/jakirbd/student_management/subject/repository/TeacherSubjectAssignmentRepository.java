package com.jakirbd.student_management.subject.repository;

import java.util.List;
import java.util.Optional;

import com.jakirbd.student_management.subject.dto.response.TeacherSubjectAssignmentResponse;

public interface TeacherSubjectAssignmentRepository {
	Long createAssignment(
		Long classSubjectId,
		Long sectionShiftId,
		Long teacherId,
		String assignmentType,
		String status
	);

	void updateAssignment(
		Long assignmentId,
		Long classSubjectId,
		Long sectionShiftId,
		Long teacherId,
		String assignmentType,
		String status
	);

	Optional<TeacherSubjectAssignmentResponse> findAssignmentById(Long assignmentId);

	List<TeacherSubjectAssignmentResponse> findAllAssignments();

	List<TeacherSubjectAssignmentResponse> findAssignmentsByTeacherId(Long teacherId);

	void deleteAssignment(Long assignmentId);
}
