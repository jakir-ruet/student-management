package com.jakirbd.student_management.student.service;

import com.jakirbd.student_management.student.dto.StudentGuardianCreateRequest;
import com.jakirbd.student_management.student.dto.StudentGuardianResponse;
import com.jakirbd.student_management.student.dto.StudentGuardianUpdateRequest;

import java.util.List;

public interface StudentGuardianService {

    Long addGuardian(StudentGuardianCreateRequest request);

    void updateGuardian(Long guardianId, StudentGuardianUpdateRequest request);

    void deleteGuardian(Long guardianId);

    StudentGuardianResponse getGuardianById(Long guardianId);

    List<StudentGuardianResponse> getGuardiansByStudent(Long studentId);
}
