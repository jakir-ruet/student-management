package com.jakirbd.student_management.subject.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.jakirbd.student_management.subject.dto.response.ClassSubjectResponse;

public interface ClassSubjectRepository {
	Long createClassSubject(
		Long academicYearClassId,
		Long subjectId,
		String isMandatory,
		BigDecimal fullMarks,
		Integer displayOrder,
		String status
	);

	void updateClassSubject(
		Long classSubjectId,
		Long academicYearClassId,
		Long subjectId,
		String isMandatory,
		BigDecimal fullMarks,
		Integer displayOrder,
		String status
	);

	Optional<ClassSubjectResponse> findClassSubjectById(Long classSubjectId);

    List<ClassSubjectResponse> findAllClassSubjects();

    List<ClassSubjectResponse> findSubjectsByAcademicClass(Long academicYearClassId);

    void deleteClassSubject(Long classSubjectId);
}
