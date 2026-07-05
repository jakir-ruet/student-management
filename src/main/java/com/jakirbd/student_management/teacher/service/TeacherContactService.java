package com.jakirbd.student_management.teacher.service;

import com.jakirbd.student_management.teacher.dto.TeacherContactCreateRequest;
import com.jakirbd.student_management.teacher.dto.TeacherContactResponse;
import com.jakirbd.student_management.teacher.dto.TeacherContactUpdateRequest;

import java.util.List;

public interface TeacherContactService {

    Long create(TeacherContactCreateRequest request);

    void update(Long contactId, TeacherContactUpdateRequest request);

    void delete(Long contactId);

    List<TeacherContactResponse> findByTeacherId(Long teacherId);
}