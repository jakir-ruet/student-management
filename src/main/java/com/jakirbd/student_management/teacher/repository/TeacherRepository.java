package com.jakirbd.student_management.teacher.repository;

import com.jakirbd.student_management.teacher.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository {

    Long create(Teacher teacher);

    void update(Teacher teacher);

    void delete(Long teacherId);

    Optional<Teacher> findById(Long teacherId);

    List<Teacher> findAll();

    List<Teacher> search(String searchText);
}