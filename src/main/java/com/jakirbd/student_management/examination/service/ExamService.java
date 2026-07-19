package com.jakirbd.student_management.examination.service;

import com.jakirbd.student_management.examination.dto.request.ExamCreateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamStatusUpdateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.ExamResponse;

import java.util.List;

public interface ExamService {

    Long createExam(ExamCreateRequest request);

    void updateExam(
            Long examId,
            ExamUpdateRequest request
    );

    ExamResponse getExamById(Long examId);

    List<ExamResponse> getAllExams();

    List<ExamResponse> getExamsByAcademicYear(
            Long academicYearId
    );

    List<ExamResponse> getExamsByStatus(String status);

    void updateExamStatus(
            Long examId,
            ExamStatusUpdateRequest request
    );

    void deleteExam(Long examId);
}