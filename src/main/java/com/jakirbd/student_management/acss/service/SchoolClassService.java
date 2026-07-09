package com.jakirbd.student_management.acss.service;

import com.jakirbd.student_management.acss.dto.request.SchoolClassCreateRequest;
import com.jakirbd.student_management.acss.dto.request.SchoolClassUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.SchoolClassResponse;

import java.util.List;

public interface SchoolClassService {

    SchoolClassResponse createClass(
            SchoolClassCreateRequest request
    );

    SchoolClassResponse updateClass(
            Long classId,
            SchoolClassUpdateRequest request
    );

    SchoolClassResponse getClassById(
            Long classId
    );

    List<SchoolClassResponse> getAllClasses();

    void deleteClass(
            Long classId
    );
}
