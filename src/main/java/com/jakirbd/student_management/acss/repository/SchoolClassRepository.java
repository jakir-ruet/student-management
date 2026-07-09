package com.jakirbd.student_management.acss.repository;

import com.jakirbd.student_management.acss.model.SchoolClass;

import java.util.List;
import java.util.Optional;

public interface SchoolClassRepository {

    Long createClass(
            String className,
            String classCode,
            Integer displayOrder,
            String status
    );

    void updateClass(
            Long classId,
            String className,
            String classCode,
            Integer displayOrder,
            String status
    );

    Optional<SchoolClass> findClassById(Long classId);

    List<SchoolClass> findAllClasses();

    void deleteClass(Long classId);
}
