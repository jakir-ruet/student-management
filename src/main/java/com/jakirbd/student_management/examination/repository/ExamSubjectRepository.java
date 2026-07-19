package com.jakirbd.student_management.examination.repository;

import com.jakirbd.student_management.examination.dto.response.ExamSubjectResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExamSubjectRepository {

    Long createExamSubject(
            Long examId,
            Long academicYearClassId,
            Long sectionId,
            Long subjectId,
            LocalDate examDate,
            String startTime,
            String endTime,
            Integer durationMinutes,
            BigDecimal fullMarks,
            BigDecimal passMarks,
            String roomNumber,
            String instructions,
            String status,
            Long createdBy
    );

    void updateExamSubject(
            Long examSubjectId,
            Long examId,
            Long academicYearClassId,
            Long sectionId,
            Long subjectId,
            LocalDate examDate,
            String startTime,
            String endTime,
            Integer durationMinutes,
            BigDecimal fullMarks,
            BigDecimal passMarks,
            String roomNumber,
            String instructions,
            String status,
            Long updatedBy
    );

    Optional<ExamSubjectResponse> findExamSubjectById(
            Long examSubjectId
    );

    List<ExamSubjectResponse> findAllExamSubjects();

    List<ExamSubjectResponse> findExamSubjectsByExam(
            Long examId
    );

    List<ExamSubjectResponse> findExamSubjectsByClass(
            Long academicYearClassId
    );

    List<ExamSubjectResponse> findExamSubjectsBySection(
            Long sectionId
    );

    void updateExamSubjectStatus(
            Long examSubjectId,
            String status,
            Long updatedBy
    );

    void deleteExamSubject(Long examSubjectId);
}