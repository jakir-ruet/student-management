package com.jakirbd.student_management.subject.service.impl;

import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import com.jakirbd.student_management.subject.dto.request.TeacherSubjectAssignmentCreateRequest;
import com.jakirbd.student_management.subject.dto.request.TeacherSubjectAssignmentUpdateRequest;
import com.jakirbd.student_management.subject.dto.response.TeacherSubjectAssignmentResponse;
import com.jakirbd.student_management.subject.repository.TeacherSubjectAssignmentRepository;
import com.jakirbd.student_management.subject.service.TeacherSubjectAssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherSubjectAssignmentServiceImpl
        implements TeacherSubjectAssignmentService {

    private final TeacherSubjectAssignmentRepository assignmentRepository;

    public TeacherSubjectAssignmentServiceImpl(
            TeacherSubjectAssignmentRepository assignmentRepository
    ) {
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    @Transactional
    public TeacherSubjectAssignmentResponse createAssignment(
            TeacherSubjectAssignmentCreateRequest request
    ) {
        Long assignmentId =
                assignmentRepository.createAssignment(
                        request.getClassSubjectId(),
                        request.getSectionShiftId(),
                        request.getTeacherId(),
                        request.getAssignmentType(),
                        request.getStatus()
                );

        return getAssignmentById(assignmentId);
    }

    @Override
    @Transactional
    public TeacherSubjectAssignmentResponse updateAssignment(
            Long assignmentId,
            TeacherSubjectAssignmentUpdateRequest request
    ) {
        getAssignmentById(assignmentId);

        assignmentRepository.updateAssignment(
                assignmentId,
                request.getClassSubjectId(),
                request.getSectionShiftId(),
                request.getTeacherId(),
                request.getAssignmentType(),
                request.getStatus()
        );

        return getAssignmentById(assignmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherSubjectAssignmentResponse getAssignmentById(
            Long assignmentId
    ) {
        return assignmentRepository
                .findAssignmentById(assignmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher subject assignment not found with ID: "
                                        + assignmentId
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherSubjectAssignmentResponse> getAllAssignments() {
        return assignmentRepository.findAllAssignments();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherSubjectAssignmentResponse> getAssignmentsByTeacherId(
            Long teacherId
    ) {
        return assignmentRepository
                .findAssignmentsByTeacherId(teacherId);
    }

    @Override
    @Transactional
    public void deleteAssignment(Long assignmentId) {

        getAssignmentById(assignmentId);

        assignmentRepository.deleteAssignmentById(assignmentId);
    }
}

