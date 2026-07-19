package com.jakirbd.student_management.examination.service.impl;

import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import com.jakirbd.student_management.examination.dto.request.ExamCreateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamStatusUpdateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.ExamResponse;
import com.jakirbd.student_management.examination.repository.ExamRepository;
import com.jakirbd.student_management.examination.service.ExamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    @Transactional
    public Long createExam(ExamCreateRequest request) {
        validateDateRange(
                request.getStartDate(),
                request.getEndDate()
        );

        String status = request.getStatus() == null
                ? "DRAFT"
                : normalize(request.getStatus());

        return examRepository.createExam(
                request.getAcademicYearId(),
                request.getExamName().trim(),
                normalize(request.getExamCode()),
                normalize(request.getExamType()),
                request.getStartDate(),
                request.getEndDate(),
                trimToNull(request.getDescription()),
                status,
                request.getCreatedBy()
        );
    }

    @Override
    @Transactional
    public void updateExam(
            Long examId,
            ExamUpdateRequest request
    ) {
        validateExamId(examId);

        validateDateRange(
                request.getStartDate(),
                request.getEndDate()
        );

        examRepository.updateExam(
                examId,
                request.getAcademicYearId(),
                request.getExamName().trim(),
                normalize(request.getExamCode()),
                normalize(request.getExamType()),
                request.getStartDate(),
                request.getEndDate(),
                trimToNull(request.getDescription()),
                normalize(request.getStatus()),
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponse getExamById(Long examId) {
        validateExamId(examId);

        return examRepository.findExamById(examId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exam not found with ID: " + examId
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getAllExams() {
        return examRepository.findAllExams();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getExamsByAcademicYear(
            Long academicYearId
    ) {
        if (academicYearId == null || academicYearId <= 0) {
            throw new IllegalArgumentException(
                    "Academic year ID must be greater than zero"
            );
        }

        return examRepository.findExamsByAcademicYear(
                academicYearId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getExamsByStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Exam status is required"
            );
        }

        return examRepository.findExamsByStatus(
                normalize(status)
        );
    }

    @Override
    @Transactional
    public void updateExamStatus(
            Long examId,
            ExamStatusUpdateRequest request
    ) {
        validateExamId(examId);

        examRepository.updateExamStatus(
                examId,
                normalize(request.getStatus()),
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional
    public void deleteExam(Long examId) {
        validateExamId(examId);
        examRepository.deleteExam(examId);
    }

    private void validateExamId(Long examId) {
        if (examId == null || examId <= 0) {
            throw new IllegalArgumentException(
                    "Exam ID must be greater than zero"
            );
        }
    }

    private void validateDateRange(
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    ) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Exam start date and end date are required"
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "Exam end date cannot be before start date"
            );
        }
    }

    private String normalize(String value) {
        return value == null
                ? null
                : value.trim().toUpperCase(Locale.ROOT);
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}