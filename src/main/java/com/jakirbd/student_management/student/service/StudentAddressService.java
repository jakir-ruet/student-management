package com.jakirbd.student_management.student.service;

import com.jakirbd.student_management.student.dto.StudentAddressCreateRequest;
import com.jakirbd.student_management.student.dto.StudentAddressResponse;
import com.jakirbd.student_management.student.dto.StudentAddressUpdateRequest;

import java.util.List;

public interface StudentAddressService {

    Long addAddress(StudentAddressCreateRequest request);

    void updateAddress(Long addressId, StudentAddressUpdateRequest request);

    void deleteAddress(Long addressId);

    StudentAddressResponse getAddressById(Long addressId);

    List<StudentAddressResponse> getAddressesByStudent(Long studentId);
}
