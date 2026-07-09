package com.jakirbd.student_management.teacher.repository;

import com.jakirbd.student_management.teacher.model.TeacherAddress;

import java.util.List;

public interface TeacherAddressRepository {

    Long create(TeacherAddress address);

    void update(TeacherAddress address);

    void delete(Long addressId);

    List<TeacherAddress> findByTeacherId(Long teacherId);
}