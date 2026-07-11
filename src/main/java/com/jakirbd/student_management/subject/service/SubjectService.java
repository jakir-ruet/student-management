package com.jakirbd.student_management.subject.service;

import com.jakirbd.student_management.subject.dto.request.SubjectCreateRequest;
import com.jakirbd.student_management.subject.dto.request.SubjectUpdateRequest;
import com.jakirbd.student_management.subject.dto.response.SubjectResponse;

import java.util.List;

public interface SubjectService {

    SubjectResponse createSubject(SubjectCreateRequest request);

    SubjectResponse updateSubject(
            Long subjectId,
            SubjectUpdateRequest request
    );

    SubjectResponse getSubjectById(Long subjectId);

    List<SubjectResponse> getAllSubjects();

    void deleteSubjectById(Long subjectId);
}