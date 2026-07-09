package com.jakirbd.student_management.subject.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.jakirbd.student_management.subject.model.TeacherSubjectAssignment;

public class TeacherSubjectAssignmentRowMapper
        implements RowMapper<TeacherSubjectAssignment> {

    @Override
    public TeacherSubjectAssignment mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        TeacherSubjectAssignment assignment =
                new TeacherSubjectAssignment();

        assignment.setTeacherSubjectAssignmentId(
                rs.getLong("TEACHER_SUBJECT_ASSIGNMENT_ID")
        );

        assignment.setClassSubjectId(
                rs.getLong("CLASS_SUBJECT_ID")
        );

        assignment.setSectionShiftId(
                rs.getLong("SECTION_SHIFT_ID")
        );

        assignment.setTeacherId(
                rs.getLong("TEACHER_ID")
        );

        assignment.setAssignmentType(
                rs.getString("ASSIGNMENT_TYPE")
        );

        assignment.setStatus(
                rs.getString("STATUS")
        );

        Timestamp createdAt =
                rs.getTimestamp("CREATED_AT");

        assignment.setCreatedAt(
                createdAt != null
                        ? createdAt.toLocalDateTime()
                        : null
        );

        Timestamp updatedAt =
                rs.getTimestamp("UPDATED_AT");

        assignment.setUpdatedAt(
                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );

        return assignment;
    }
}
