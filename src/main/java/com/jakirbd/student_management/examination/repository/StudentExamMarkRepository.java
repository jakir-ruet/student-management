package com.jakirbd.student_management.examination.repository;

import com.jakirbd.student_management.examination.dto.response.StudentExamMarkResponse;
import com.jakirbd.student_management.examination.dto.response.StudentExamResultResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StudentExamMarkRepository {

    Long createStudentMark(
            Long examSubjectId,
            Long studentId,
            BigDecimal obtainedMarks,
            String attendanceStatus,
            String remarks,
            Long createdBy
    );

    void updateStudentMark(
            Long studentExamMarkId,
            BigDecimal obtainedMarks,
            String attendanceStatus,
            String remarks,
            Long updatedBy
    );

    Optional<StudentExamMarkResponse> findStudentMarkById(
            Long studentExamMarkId
    );

    List<StudentExamMarkResponse> findAllStudentMarks();

    List<StudentExamMarkResponse> findMarksByExamSubject(
            Long examSubjectId
    );

    List<StudentExamMarkResponse> findMarksByStudent(
            Long studentId
    );

    List<StudentExamResultResponse> findStudentExamResults(
            Long studentId,
            Long examId
    );

    void publishStudentMark(
            Long studentExamMarkId,
            Long updatedBy
    );

    void unpublishStudentMark(
            Long studentExamMarkId,
            Long updatedBy
    );

    void publishExamSubjectMarks(
            Long examSubjectId,
            Long updatedBy
    );

    void deleteStudentMark(Long studentExamMarkId);
}