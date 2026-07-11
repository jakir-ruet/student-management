package com.jakirbd.student_management.subject.service;

import com.jakirbd.student_management.subject.dto.request.ClassSubjectCreateRequest;
import com.jakirbd.student_management.subject.dto.request.ClassSubjectUpdateRequest;
import com.jakirbd.student_management.subject.dto.response.ClassSubjectResponse;

import java.util.List;

public interface ClassSubjectService {

    ClassSubjectResponse createClassSubject(
            ClassSubjectCreateRequest request
    );

    ClassSubjectResponse updateClassSubject(
            Long classSubjectId,
            ClassSubjectUpdateRequest request
    );

    ClassSubjectResponse getClassSubjectById(
            Long classSubjectId
    );

    List<ClassSubjectResponse> getAllClassSubjects();

    List<ClassSubjectResponse> getSubjectsByAcademicClass(
            Long academicYearClassId
    );

    void deleteClassSubjectById(Long classSubjectId);
}