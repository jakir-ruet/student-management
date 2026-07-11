package com.jakirbd.student_management.subject.service;

import com.jakirbd.student_management.subject.dto.request.TeacherSubjectAssignmentCreateRequest;
import com.jakirbd.student_management.subject.dto.request.TeacherSubjectAssignmentUpdateRequest;
import com.jakirbd.student_management.subject.dto.response.TeacherSubjectAssignmentResponse;

import java.util.List;

public interface TeacherSubjectAssignmentService {

    TeacherSubjectAssignmentResponse createAssignment(
            TeacherSubjectAssignmentCreateRequest request
    );

    TeacherSubjectAssignmentResponse updateAssignment(
            Long assignmentId,
            TeacherSubjectAssignmentUpdateRequest request
    );

    TeacherSubjectAssignmentResponse getAssignmentById(
            Long assignmentId
    );

    List<TeacherSubjectAssignmentResponse> getAllAssignments();

    List<TeacherSubjectAssignmentResponse> getAssignmentsByTeacherId(
            Long teacherId
    );

    void deleteAssignment(Long assignmentId);
}