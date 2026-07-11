package com.jakirbd.student_management.subject.repository;

import com.jakirbd.student_management.subject.model.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository {

	Long createSubject(
			String subjectName,
			String subjectCode,
			String subjectType,
			String description,
			Integer displayOrder,
			String status
	);

	void updateSubject(
			Long subjectId,
			String subjectName,
			String subjectCode,
			String subjectType,
			String description,
			Integer displayOrder,
			String status
	);

	Optional<Subject> findSubjectById(Long subjectId);

	List<Subject> findAllSubjects();

	void deleteSubjectById(Long subjectId);
}