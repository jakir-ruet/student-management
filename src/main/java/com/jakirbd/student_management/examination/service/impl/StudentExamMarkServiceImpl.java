package com.jakirbd.student_management.examination.service.impl;

import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import com.jakirbd.student_management.examination.dto.request.StudentExamMarkCreateRequest;
import com.jakirbd.student_management.examination.dto.request.StudentExamMarkPublishRequest;
import com.jakirbd.student_management.examination.dto.request.StudentExamMarkUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.StudentExamMarkResponse;
import com.jakirbd.student_management.examination.dto.response.StudentExamResultResponse;
import com.jakirbd.student_management.examination.repository.StudentExamMarkRepository;
import com.jakirbd.student_management.examination.service.StudentExamMarkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class StudentExamMarkServiceImpl
        implements StudentExamMarkService {

    private final StudentExamMarkRepository studentExamMarkRepository;

    public StudentExamMarkServiceImpl(
            StudentExamMarkRepository studentExamMarkRepository
    ) {
        this.studentExamMarkRepository =
                studentExamMarkRepository;
    }

    @Override
    @Transactional
    public Long createStudentMark(
            StudentExamMarkCreateRequest request
    ) {
        validatePositiveId(
                request.getExamSubjectId(),
                "Exam subject ID"
        );

        validatePositiveId(
                request.getStudentId(),
                "Student ID"
        );

        String attendanceStatus =
                request.getAttendanceStatus() == null
                        ? "PRESENT"
                        : normalize(
                        request.getAttendanceStatus()
                );

        validateMarkByAttendance(
                request.getObtainedMarks(),
                attendanceStatus
        );

        return studentExamMarkRepository.createStudentMark(
                request.getExamSubjectId(),
                request.getStudentId(),
                request.getObtainedMarks(),
                attendanceStatus,
                trimToNull(request.getRemarks()),
                request.getCreatedBy()
        );
    }

    @Override
    @Transactional
    public void updateStudentMark(
            Long studentExamMarkId,
            StudentExamMarkUpdateRequest request
    ) {
        validatePositiveId(
                studentExamMarkId,
                "Student exam mark ID"
        );

        String attendanceStatus =
                normalize(request.getAttendanceStatus());

        validateMarkByAttendance(
                request.getObtainedMarks(),
                attendanceStatus
        );

        studentExamMarkRepository.updateStudentMark(
                studentExamMarkId,
                request.getObtainedMarks(),
                attendanceStatus,
                trimToNull(request.getRemarks()),
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public StudentExamMarkResponse getStudentMarkById(
            Long studentExamMarkId
    ) {
        validatePositiveId(
                studentExamMarkId,
                "Student exam mark ID"
        );

        return studentExamMarkRepository
                .findStudentMarkById(studentExamMarkId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student exam mark not found with ID: "
                                + studentExamMarkId
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentExamMarkResponse> getAllStudentMarks() {
        return studentExamMarkRepository.findAllStudentMarks();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentExamMarkResponse> getMarksByExamSubject(
            Long examSubjectId
    ) {
        validatePositiveId(
                examSubjectId,
                "Exam subject ID"
        );

        return studentExamMarkRepository
                .findMarksByExamSubject(examSubjectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentExamMarkResponse> getMarksByStudent(
            Long studentId
    ) {
        validatePositiveId(studentId, "Student ID");

        return studentExamMarkRepository
                .findMarksByStudent(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentExamResultResponse> getStudentExamResults(
            Long studentId,
            Long examId
    ) {
        validatePositiveId(studentId, "Student ID");
        validatePositiveId(examId, "Exam ID");

        return studentExamMarkRepository
                .findStudentExamResults(studentId, examId);
    }

    @Override
    @Transactional
    public void publishStudentMark(
            Long studentExamMarkId,
            StudentExamMarkPublishRequest request
    ) {
        validatePositiveId(
                studentExamMarkId,
                "Student exam mark ID"
        );

        studentExamMarkRepository.publishStudentMark(
                studentExamMarkId,
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional
    public void unpublishStudentMark(
            Long studentExamMarkId,
            StudentExamMarkPublishRequest request
    ) {
        validatePositiveId(
                studentExamMarkId,
                "Student exam mark ID"
        );

        studentExamMarkRepository.unpublishStudentMark(
                studentExamMarkId,
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional
    public void publishExamSubjectMarks(
            Long examSubjectId,
            StudentExamMarkPublishRequest request
    ) {
        validatePositiveId(
                examSubjectId,
                "Exam subject ID"
        );

        studentExamMarkRepository.publishExamSubjectMarks(
                examSubjectId,
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional
    public void deleteStudentMark(Long studentExamMarkId) {
        validatePositiveId(
                studentExamMarkId,
                "Student exam mark ID"
        );

        studentExamMarkRepository.deleteStudentMark(
                studentExamMarkId
        );
    }

    private void validateMarkByAttendance(
            BigDecimal obtainedMarks,
            String attendanceStatus
    ) {
        if (attendanceStatus == null) {
            throw new IllegalArgumentException(
                    "Attendance status is required"
            );
        }

        switch (attendanceStatus) {
            case "PRESENT" -> {
                if (obtainedMarks == null) {
                    throw new IllegalArgumentException(
                            "Obtained marks are required for " +
                                    "a present student"
                    );
                }

                if (obtainedMarks.compareTo(
                        BigDecimal.ZERO
                ) < 0) {
                    throw new IllegalArgumentException(
                            "Obtained marks cannot be negative"
                    );
                }
            }

            case "ABSENT" -> {
                if (obtainedMarks != null
                        && obtainedMarks.compareTo(
                        BigDecimal.ZERO
                ) != 0) {
                    throw new IllegalArgumentException(
                            "Absent student cannot have " +
                                    "obtained marks"
                    );
                }
            }

            case "EXCUSED" -> {
                if (obtainedMarks != null) {
                    throw new IllegalArgumentException(
                            "Excused student cannot have " +
                                    "obtained marks"
                    );
                }
            }

            default -> throw new IllegalArgumentException(
                    "Attendance status must be PRESENT, " +
                            "ABSENT, or EXCUSED"
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