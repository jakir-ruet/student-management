package com.jakirbd.student_management.subject.repository;

import com.jakirbd.student_management.subject.dto.response.ClassSubjectResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ClassSubjectRepository {

	Long createClassSubject(
			Long academicYearClassId,
			Long subjectId,
			String isMandatory,
			BigDecimal fullMarks,
			BigDecimal passMarks,
			Integer displayOrder,
			String status
	);

	void updateClassSubject(
			Long classSubjectId,
			Long academicYearClassId,
			Long subjectId,
			String isMandatory,
			BigDecimal fullMarks,
			BigDecimal passMarks,
			Integer displayOrder,
			String status
	);

	Optional<ClassSubjectResponse> findClassSubjectById(
			Long classSubjectId
	);

	List<ClassSubjectResponse> findAllClassSubjects();

	List<ClassSubjectResponse> findSubjectsByAcademicClass(
			Long academicYearClassId
	);

	void deleteClassSubjectById(Long classSubjectId);
}