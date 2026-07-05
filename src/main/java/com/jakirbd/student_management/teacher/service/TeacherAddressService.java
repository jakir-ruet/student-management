package com.jakirbd.student_management.teacher.service;

import com.jakirbd.student_management.teacher.dto.TeacherAddressCreateRequest;
import com.jakirbd.student_management.teacher.dto.TeacherAddressResponse;
import com.jakirbd.student_management.teacher.dto.TeacherAddressUpdateRequest;

import java.util.List;

public interface TeacherAddressService {

    Long create(TeacherAddressCreateRequest request);

    void update(Long addressId, TeacherAddressUpdateRequest request);

    void delete(Long addressId);

    List<TeacherAddressResponse> findByTeacherId(Long teacherId);
}