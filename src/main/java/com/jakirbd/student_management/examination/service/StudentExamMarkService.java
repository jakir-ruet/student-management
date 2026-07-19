package com.jakirbd.student_management.examination.service;

import com.jakirbd.student_management.examination.dto.request.StudentExamMarkCreateRequest;
import com.jakirbd.student_management.examination.dto.request.StudentExamMarkPublishRequest;
import com.jakirbd.student_management.examination.dto.request.StudentExamMarkUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.StudentExamMarkResponse;
import com.jakirbd.student_management.examination.dto.response.StudentExamResultResponse;

import java.util.List;

public interface StudentExamMarkService {

    Long createStudentMark(
            StudentExamMarkCreateRequest request
    );

    void updateStudentMark(
            Long studentExamMarkId,
            StudentExamMarkUpdateRequest request
    );

    StudentExamMarkResponse getStudentMarkById(
            Long studentExamMarkId
    );

    List<StudentExamMarkResponse> getAllStudentMarks();

    List<StudentExamMarkResponse> getMarksByExamSubject(
            Long examSubjectId
    );

    List<StudentExamMarkResponse> getMarksByStudent(
            Long studentId
    );

    List<StudentExamResultResponse> getStudentExamResults(
            Long studentId,
            Long examId
    );

    void publishStudentMark(
            Long studentExamMarkId,
            StudentExamMarkPublishRequest request
    );

    void unpublishStudentMark(
            Long studentExamMarkId,
            StudentExamMarkPublishRequest request
    );

    void publishExamSubjectMarks(
            Long examSubjectId,
            StudentExamMarkPublishRequest request
    );

    void deleteStudentMark(Long studentExamMarkId);
}