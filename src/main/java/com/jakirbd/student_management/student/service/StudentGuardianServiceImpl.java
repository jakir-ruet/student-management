package com.jakirbd.student_management.student.service;

import com.jakirbd.student_management.student.dto.StudentGuardianCreateRequest;
import com.jakirbd.student_management.student.dto.StudentGuardianResponse;
import com.jakirbd.student_management.student.dto.StudentGuardianUpdateRequest;
import com.jakirbd.student_management.student.model.StudentGuardian;
import com.jakirbd.student_management.student.repository.StudentGuardianRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentGuardianServiceImpl implements StudentGuardianService {

    private final StudentGuardianRepository studentGuardianRepository;

    public StudentGuardianServiceImpl(StudentGuardianRepository studentGuardianRepository) {
        this.studentGuardianRepository = studentGuardianRepository;
    }

    @Override
    public Long addGuardian(StudentGuardianCreateRequest request) {
        StudentGuardian guardian = new StudentGuardian();

        guardian.setStudentId(request.getStudentId());
        guardian.setGuardianName(request.getGuardianName());
        guardian.setRelationship(request.getRelationship());
        guardian.setPhone(request.getPhone());
        guardian.setEmail(request.getEmail());
        guardian.setOccupation(request.getOccupation());
        guardian.setIsPrimary(request.getIsPrimary());

        return studentGuardianRepository.addGuardian(guardian);
    }

    @Override
    public void updateGuardian(Long guardianId, StudentGuardianUpdateRequest request) {
        StudentGuardian guardian = new StudentGuardian();

        guardian.setGuardianId(guardianId);
        guardian.setGuardianName(request.getGuardianName());
        guardian.setRelationship(request.getRelationship());
        guardian.setPhone(request.getPhone());
        guardian.setEmail(request.getEmail());
        guardian.setOccupation(request.getOccupation());
        guardian.setIsPrimary(request.getIsPrimary());

        studentGuardianRepository.updateGuardian(guardian);
    }

    @Override
    public void deleteGuardian(Long guardianId) {
        studentGuardianRepository.deleteGuardian(guardianId);
    }

    @Override
    public StudentGuardianResponse getGuardianById(Long guardianId) {
        StudentGuardian guardian = studentGuardianRepository.getGuardianById(guardianId)
                .orElseThrow(() -> new RuntimeException("Student guardian not found"));

        return mapToResponse(guardian);
    }

    @Override
    public List<StudentGuardianResponse> getGuardiansByStudent(Long studentId) {
        return studentGuardianRepository.getGuardiansByStudent(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private StudentGuardianResponse mapToResponse(StudentGuardian guardian) {
        StudentGuardianResponse response = new StudentGuardianResponse();

        response.setGuardianId(guardian.getGuardianId());
        response.setStudentId(guardian.getStudentId());
        response.setGuardianName(guardian.getGuardianName());
        response.setRelationship(guardian.getRelationship());
        response.setPhone(guardian.getPhone());
        response.setEmail(guardian.getEmail());
        response.setOccupation(guardian.getOccupation());
        response.setIsPrimary(guardian.getIsPrimary());
        response.setCreatedAt(guardian.getCreatedAt());
        response.setUpdatedAt(guardian.getUpdatedAt());

        return response;
    }
}
