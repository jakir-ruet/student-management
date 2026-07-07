package com.jakirbd.student_management.acss.service.impl;

import com.jakirbd.student_management.acss.dto.request.SchoolClassCreateRequest;
import com.jakirbd.student_management.acss.dto.request.SchoolClassUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.SchoolClassResponse;
import com.jakirbd.student_management.acss.model.SchoolClass;
import com.jakirbd.student_management.acss.repository.SchoolClassRepository;
import com.jakirbd.student_management.acss.service.SchoolClassService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolClassServiceImpl implements SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    public SchoolClassServiceImpl(
            SchoolClassRepository schoolClassRepository
    ) {
        this.schoolClassRepository = schoolClassRepository;
    }

    @Override
    public SchoolClassResponse createClass(
            SchoolClassCreateRequest request
    ) {
        Long classId = schoolClassRepository.createClass(
                request.getClassName(),
                request.getClassCode(),
                request.getDisplayOrder(),
                request.getStatus()
        );

        return getClassById(classId);
    }

    @Override
    public SchoolClassResponse updateClass(
            Long classId,
            SchoolClassUpdateRequest request
    ) {
        schoolClassRepository.updateClass(
                classId,
                request.getClassName(),
                request.getClassCode(),
                request.getDisplayOrder(),
                request.getStatus()
        );

        return getClassById(classId);
    }

    @Override
    public SchoolClassResponse getClassById(
            Long classId
    ) {
        SchoolClass schoolClass = schoolClassRepository
                .findClassById(classId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Class not found with ID: " + classId
                        )
                );

        return mapToResponse(schoolClass);
    }

    @Override
    public List<SchoolClassResponse> getAllClasses() {
        return schoolClassRepository
                .findAllClasses()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteClass(
            Long classId
    ) {
        getClassById(classId);

        schoolClassRepository.deleteClass(classId);
    }

    private SchoolClassResponse mapToResponse(
            SchoolClass schoolClass
    ) {
        SchoolClassResponse response = new SchoolClassResponse();

        response.setClassId(
                schoolClass.getClassId()
        );

        response.setClassName(
                schoolClass.getClassName()
        );

        response.setClassCode(
                schoolClass.getClassCode()
        );

        response.setDisplayOrder(
                schoolClass.getDisplayOrder()
        );

        response.setStatus(
                schoolClass.getStatus()
        );

        response.setCreatedAt(
                schoolClass.getCreatedAt()
        );

        response.setUpdatedAt(
                schoolClass.getUpdatedAt()
        );

        return response;
    }
}
