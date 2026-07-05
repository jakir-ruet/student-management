package com.jakirbd.student_management.teacher.repository;

import com.jakirbd.student_management.teacher.model.TeacherContact;

import java.util.List;

public interface TeacherContactRepository {

    Long create(TeacherContact contact);

    void update(TeacherContact contact);

    void delete(Long contactId);

    List<TeacherContact> findByTeacherId(Long teacherId);
}