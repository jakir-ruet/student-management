package com.jakirbd.student_management.subject.service.impl;

import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import com.jakirbd.student_management.subject.dto.request.ClassSubjectCreateRequest;
import com.jakirbd.student_management.subject.dto.request.ClassSubjectUpdateRequest;
import com.jakirbd.student_management.subject.dto.response.ClassSubjectResponse;
import com.jakirbd.student_management.subject.repository.ClassSubjectRepository;
import com.jakirbd.student_management.subject.service.ClassSubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassSubjectServiceImpl
        implements ClassSubjectService {

    private final ClassSubjectRepository classSubjectRepository;

    public ClassSubjectServiceImpl(
            ClassSubjectRepository classSubjectRepository
    ) {
        this.classSubjectRepository = classSubjectRepository;
    }

    @Override
    @Transactional
    public ClassSubjectResponse createClassSubject(
            ClassSubjectCreateRequest request
    ) {
        Long classSubjectId =
                classSubjectRepository.createClassSubject(
                        request.getAcademicYearClassId(),
                        request.getSubjectId(),
                        request.getIsMandatory(),
                        request.getFullMarks(),
                        request.getPassMarks(),
                        request.getDisplayOrder(),
                        request.getStatus()
                );

        return getClassSubjectById(classSubjectId);
    }

    @Override
    @Transactional
    public ClassSubjectResponse updateClassSubject(
            Long classSubjectId,
            ClassSubjectUpdateRequest request
    ) {
        getClassSubjectById(classSubjectId);

        classSubjectRepository.updateClassSubject(
                classSubjectId,
                request.getAcademicYearClassId(),
                request.getSubjectId(),
                request.getIsMandatory(),
                request.getFullMarks(),
                request.getPassMarks(),
                request.getDisplayOrder(),
                request.getStatus()
        );

        return getClassSubjectById(classSubjectId);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassSubjectResponse getClassSubjectById(
            Long classSubjectId
    ) {
        return classSubjectRepository
                .findClassSubjectById(classSubjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Class subject not found with ID: "
                                        + classSubjectId
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassSubjectResponse> getAllClassSubjects() {
        return classSubjectRepository.findAllClassSubjects();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassSubjectResponse> getSubjectsByAcademicClass(
            Long academicYearClassId
    ) {
        return classSubjectRepository
                .findSubjectsByAcademicClass(
                        academicYearClassId
                );
    }

    @Override
    @Transactional
    public void deleteClassSubjectById(
            Long classSubjectId
    ) {
        getClassSubjectById(classSubjectId);

        classSubjectRepository.deleteClassSubjectById(
                classSubjectId
        );
    }
}