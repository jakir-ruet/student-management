package com.jakirbd.student_management.student.repository;

import com.jakirbd.student_management.student.model.StudentGuardian;

import java.util.List;
import java.util.Optional;

public interface StudentGuardianRepository {

    Long addGuardian(StudentGuardian guardian);

    void updateGuardian(StudentGuardian guardian);

    void deleteGuardian(Long guardianId);

    Optional<StudentGuardian> getGuardianById(Long guardianId);

    List<StudentGuardian> getGuardiansByStudent(Long studentId);
}
