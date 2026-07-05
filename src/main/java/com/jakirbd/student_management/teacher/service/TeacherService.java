package com.jakirbd.student_management.teacher.service;

import com.jakirbd.student_management.teacher.dto.TeacherCreateRequest;
import com.jakirbd.student_management.teacher.dto.TeacherResponse;
import com.jakirbd.student_management.teacher.dto.TeacherUpdateRequest;

import java.util.List;

public interface TeacherService {

    Long create(TeacherCreateRequest request);

    void update(Long teacherId, TeacherUpdateRequest request);

    void delete(Long teacherId);

    TeacherResponse findById(Long teacherId);

    List<TeacherResponse> findAll();

    List<TeacherResponse> search(String searchText);
}