package com.jakirbd.student_management.examination.repository;

import com.jakirbd.student_management.examination.dto.response.ExamResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExamRepository {

    Long createExam(
            Long academicYearId,
            String examName,
            String examCode,
            String examType,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            String status,
            Long createdBy
    );

    void updateExam(
            Long examId,
            Long academicYearId,
            String examName,
            String examCode,
            String examType,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            String status,
            Long updatedBy
    );

    Optional<ExamResponse> findExamById(Long examId);

    List<ExamResponse> findAllExams();

    List<ExamResponse> findExamsByAcademicYear(
            Long academicYearId
    );

    List<ExamResponse> findExamsByStatus(String status);

    void updateExamStatus(
            Long examId,
            String status,
            Long updatedBy
    );

    void deleteExam(Long examId);
}