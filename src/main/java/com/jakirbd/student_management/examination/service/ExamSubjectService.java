package com.jakirbd.student_management.examination.service;

import com.jakirbd.student_management.examination.dto.request.ExamSubjectCreateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamSubjectStatusUpdateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamSubjectUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.ExamSubjectResponse;

import java.util.List;

public interface ExamSubjectService {

    Long createExamSubject(
            ExamSubjectCreateRequest request
    );

    void updateExamSubject(
            Long examSubjectId,
            ExamSubjectUpdateRequest request
    );

    ExamSubjectResponse getExamSubjectById(
            Long examSubjectId
    );

    List<ExamSubjectResponse> getAllExamSubjects();

    List<ExamSubjectResponse> getExamSubjectsByExam(
            Long examId
    );

    List<ExamSubjectResponse> getExamSubjectsByClass(
            Long academicYearClassId
    );

    List<ExamSubjectResponse> getExamSubjectsBySection(
            Long sectionId
    );

    void updateExamSubjectStatus(
            Long examSubjectId,
            ExamSubjectStatusUpdateRequest request
    );

    void deleteExamSubject(Long examSubjectId);
}