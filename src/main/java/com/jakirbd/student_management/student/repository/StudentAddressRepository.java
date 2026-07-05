package com.jakirbd.student_management.student.repository;

import com.jakirbd.student_management.student.model.StudentAddress;

import java.util.List;
import java.util.Optional;

public interface StudentAddressRepository {

    Long addAddress(StudentAddress address);

    void updateAddress(StudentAddress address);

    void deleteAddress(Long addressId);

    Optional<StudentAddress> getAddressById(Long addressId);

    List<StudentAddress> getAddressesByStudent(Long studentId);
}
