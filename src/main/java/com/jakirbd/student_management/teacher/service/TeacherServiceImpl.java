package com.jakirbd.student_management.teacher.service;

import com.jakirbd.student_management.teacher.dto.TeacherCreateRequest;
import com.jakirbd.student_management.teacher.dto.TeacherResponse;
import com.jakirbd.student_management.teacher.dto.TeacherUpdateRequest;
import com.jakirbd.student_management.teacher.model.Teacher;
import com.jakirbd.student_management.teacher.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Long create(TeacherCreateRequest request) {
        Teacher teacher = new Teacher();

        teacher.setTeacherCode(request.getTeacherCode());
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setGender(request.getGender());
        teacher.setDateOfBirth(request.getDateOfBirth());
        teacher.setJoiningDate(request.getJoiningDate());
        teacher.setDesignation(request.getDesignation());
        teacher.setQualification(request.getQualification());
        teacher.setDepartment(request.getDepartment());

        return teacherRepository.create(teacher);
    }

    @Override
    public void update(Long teacherId, TeacherUpdateRequest request) {
        Teacher teacher = new Teacher();

        teacher.setTeacherId(teacherId);
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setGender(request.getGender());
        teacher.setDateOfBirth(request.getDateOfBirth());
        teacher.setJoiningDate(request.getJoiningDate());
        teacher.setDesignation(request.getDesignation());
        teacher.setQualification(request.getQualification());
        teacher.setDepartment(request.getDepartment());
        teacher.setStatus(request.getStatus());

        teacherRepository.update(teacher);
    }

    @Override
    public void delete(Long teacherId) {
        teacherRepository.delete(teacherId);
    }

    @Override
    public TeacherResponse findById(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Teacher not found with id: " + teacherId
                        )
                );

        return toResponse(teacher);
    }

    @Override
    public List<TeacherResponse> findAll() {
        return teacherRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TeacherResponse> search(String searchText) {
        return teacherRepository.search(searchText)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TeacherResponse toResponse(Teacher teacher) {
        TeacherResponse response = new TeacherResponse();

        response.setTeacherId(teacher.getTeacherId());
        response.setTeacherCode(teacher.getTeacherCode());
        response.setFirstName(teacher.getFirstName());
        response.setLastName(teacher.getLastName());
        response.setEmail(teacher.getEmail());
        response.setPhone(teacher.getPhone());
        response.setGender(teacher.getGender());
        response.setDateOfBirth(teacher.getDateOfBirth());
        response.setJoiningDate(teacher.getJoiningDate());
        response.setDesignation(teacher.getDesignation());
        response.setQualification(teacher.getQualification());
        response.setDepartment(teacher.getDepartment());
        response.setStatus(teacher.getStatus());
        response.setCreatedAt(teacher.getCreatedAt());
        response.setUpdatedAt(teacher.getUpdatedAt());

        return response;
    }
}