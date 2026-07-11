package com.jakirbd.student_management.subject.repository;

import com.jakirbd.student_management.subject.dto.response.TeacherSubjectAssignmentResponse;

import java.util.List;
import java.util.Optional;

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

	Optional<TeacherSubjectAssignmentResponse> findAssignmentById(
			Long assignmentId
	);

	List<TeacherSubjectAssignmentResponse> findAllAssignments();

	List<TeacherSubjectAssignmentResponse> findAssignmentsByTeacherId(
			Long teacherId
	);

	void deleteAssignmentById(Long assignmentId);
}