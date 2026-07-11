package com.jakirbd.student_management.subject.service.impl;

import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import com.jakirbd.student_management.subject.dto.request.SubjectCreateRequest;
import com.jakirbd.student_management.subject.dto.request.SubjectUpdateRequest;
import com.jakirbd.student_management.subject.dto.response.SubjectResponse;
import com.jakirbd.student_management.subject.model.Subject;
import com.jakirbd.student_management.subject.repository.SubjectRepository;
import com.jakirbd.student_management.subject.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(
            SubjectRepository subjectRepository
    ) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional
    public SubjectResponse createSubject(
            SubjectCreateRequest request
    ) {
        Long subjectId = subjectRepository.createSubject(
                request.getSubjectName(),
                request.getSubjectCode(),
                request.getSubjectType(),
                request.getDescription(),
                request.getDisplayOrder(),
                request.getStatus()
        );

        return getSubjectById(subjectId);
    }

    @Override
    @Transactional
    public SubjectResponse updateSubject(
            Long subjectId,
            SubjectUpdateRequest request
    ) {
        getSubjectEntityById(subjectId);

        subjectRepository.updateSubject(
                subjectId,
                request.getSubjectName(),
                request.getSubjectCode(),
                request.getSubjectType(),
                request.getDescription(),
                request.getDisplayOrder(),
                request.getStatus()
        );

        return getSubjectById(subjectId);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponse getSubjectById(Long subjectId) {
        Subject subject = getSubjectEntityById(subjectId);
        return mapToResponse(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAllSubjects()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteSubjectById(Long subjectId) {
        getSubjectEntityById(subjectId);
        subjectRepository.deleteSubjectById(subjectId);
    }

    private Subject getSubjectEntityById(Long subjectId) {
        return subjectRepository.findSubjectById(subjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with ID: "
                                        + subjectId
                        )
                );
    }

    private SubjectResponse mapToResponse(Subject subject) {
        SubjectResponse response = new SubjectResponse();

        response.setSubjectId(subject.getSubjectId());
        response.setSubjectName(subject.getSubjectName());
        response.setSubjectCode(subject.getSubjectCode());
        response.setSubjectType(subject.getSubjectType());
        response.setDescription(subject.getDescription());
        response.setDisplayOrder(subject.getDisplayOrder());
        response.setStatus(subject.getStatus());
        response.setCreatedAt(subject.getCreatedAt());
        response.setUpdatedAt(subject.getUpdatedAt());

        return response;
    }
}