package com.jakirbd.student_management.examination.service.impl;

import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import com.jakirbd.student_management.examination.dto.request.ExamSubjectCreateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamSubjectStatusUpdateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamSubjectUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.ExamSubjectResponse;
import com.jakirbd.student_management.examination.repository.ExamSubjectRepository;
import com.jakirbd.student_management.examination.service.ExamSubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class ExamSubjectServiceImpl
        implements ExamSubjectService {

    private final ExamSubjectRepository examSubjectRepository;

    public ExamSubjectServiceImpl(
            ExamSubjectRepository examSubjectRepository
    ) {
        this.examSubjectRepository = examSubjectRepository;
    }

    @Override
    @Transactional
    public Long createExamSubject(
            ExamSubjectCreateRequest request
    ) {
        validateRequiredIds(
                request.getExamId(),
                request.getAcademicYearClassId(),
                request.getSubjectId()
        );

        validateSectionId(request.getSectionId());

        validateMarks(
                request.getFullMarks(),
                request.getPassMarks()
        );

        validateDuration(request.getDurationMinutes());

        validateTimes(
                request.getStartTime(),
                request.getEndTime()
        );

        String status = request.getStatus() == null
                ? "SCHEDULED"
                : normalize(request.getStatus());

        return examSubjectRepository.createExamSubject(
                request.getExamId(),
                request.getAcademicYearClassId(),
                request.getSectionId(),
                request.getSubjectId(),
                request.getExamDate(),
                trimToNull(request.getStartTime()),
                trimToNull(request.getEndTime()),
                request.getDurationMinutes(),
                request.getFullMarks(),
                request.getPassMarks(),
                trimToNull(request.getRoomNumber()),
                trimToNull(request.getInstructions()),
                status,
                request.getCreatedBy()
        );
    }

    @Override
    @Transactional
    public void updateExamSubject(
            Long examSubjectId,
            ExamSubjectUpdateRequest request
    ) {
        validateExamSubjectId(examSubjectId);

        validateRequiredIds(
                request.getExamId(),
                request.getAcademicYearClassId(),
                request.getSubjectId()
        );

        validateSectionId(request.getSectionId());

        validateMarks(
                request.getFullMarks(),
                request.getPassMarks()
        );

        validateDuration(request.getDurationMinutes());

        validateTimes(
                request.getStartTime(),
                request.getEndTime()
        );

        examSubjectRepository.updateExamSubject(
                examSubjectId,
                request.getExamId(),
                request.getAcademicYearClassId(),
                request.getSectionId(),
                request.getSubjectId(),
                request.getExamDate(),
                trimToNull(request.getStartTime()),
                trimToNull(request.getEndTime()),
                request.getDurationMinutes(),
                request.getFullMarks(),
                request.getPassMarks(),
                trimToNull(request.getRoomNumber()),
                trimToNull(request.getInstructions()),
                normalize(request.getStatus()),
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ExamSubjectResponse getExamSubjectById(
            Long examSubjectId
    ) {
        validateExamSubjectId(examSubjectId);

        return examSubjectRepository
                .findExamSubjectById(examSubjectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exam subject not found with ID: "
                                + examSubjectId
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSubjectResponse> getAllExamSubjects() {
        return examSubjectRepository.findAllExamSubjects();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSubjectResponse> getExamSubjectsByExam(
            Long examId
    ) {
        validatePositiveId(examId, "Exam ID");

        return examSubjectRepository.findExamSubjectsByExam(
                examId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSubjectResponse> getExamSubjectsByClass(
            Long academicYearClassId
    ) {
        validatePositiveId(
                academicYearClassId,
                "Academic year class ID"
        );

        return examSubjectRepository.findExamSubjectsByClass(
                academicYearClassId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSubjectResponse> getExamSubjectsBySection(
            Long sectionId
    ) {
        validatePositiveId(sectionId, "Section ID");

        return examSubjectRepository.findExamSubjectsBySection(
                sectionId
        );
    }

    @Override
    @Transactional
    public void updateExamSubjectStatus(
            Long examSubjectId,
            ExamSubjectStatusUpdateRequest request
    ) {
        validateExamSubjectId(examSubjectId);

        examSubjectRepository.updateExamSubjectStatus(
                examSubjectId,
                normalize(request.getStatus()),
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional
    public void deleteExamSubject(Long examSubjectId) {
        validateExamSubjectId(examSubjectId);
        examSubjectRepository.deleteExamSubject(examSubjectId);
    }

    private void validateExamSubjectId(Long examSubjectId) {
        validatePositiveId(
                examSubjectId,
                "Exam subject ID"
        );
    }

    private void validateRequiredIds(
            Long examId,
            Long academicYearClassId,
            Long subjectId
    ) {
        validatePositiveId(examId, "Exam ID");

        validatePositiveId(
                academicYearClassId,
                "Academic year class ID"
        );

        validatePositiveId(subjectId, "Subject ID");
    }

    private void validateSectionId(Long sectionId) {
        if (sectionId != null && sectionId <= 0) {
            throw new IllegalArgumentException(
                    "Section ID must be greater than zero"
            );
        }
    }

    private void validateMarks(
            BigDecimal fullMarks,
            BigDecimal passMarks
    ) {
        if (fullMarks == null
                || fullMarks.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Full marks must be greater than zero"
            );
        }

        if (passMarks == null
                || passMarks.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Pass marks cannot be negative"
            );
        }

        if (passMarks.compareTo(fullMarks) > 0) {
            throw new IllegalArgumentException(
                    "Pass marks cannot exceed full marks"
            );
        }
    }

    private void validateDuration(Integer durationMinutes) {
        if (durationMinutes != null && durationMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Duration must be greater than zero"
            );
        }
    }

    private void validateTimes(
            String startTime,
            String endTime
    ) {
        if ((startTime == null || startTime.isBlank())
                && (endTime == null || endTime.isBlank())) {
            return;
        }

        if (startTime == null || startTime.isBlank()
                || endTime == null || endTime.isBlank()) {
            throw new IllegalArgumentException(
                    "Both start time and end time are required"
            );
        }

        /*
         * HH:mm uses a fixed-width 24-hour format, so lexical
         * comparison is valid after DTO pattern validation.
         */
        if (endTime.trim().compareTo(startTime.trim()) <= 0) {
            throw new IllegalArgumentException(
                    "End time must be after start time"
            );
        }
    }

    private void validatePositiveId(
            Long id,
            String fieldName
    ) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be greater than zero"
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