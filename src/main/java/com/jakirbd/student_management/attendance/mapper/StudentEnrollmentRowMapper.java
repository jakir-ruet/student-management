package com.jakirbd.student_management.attendance.mapper;

import com.jakirbd.student_management.attendance.model.StudentEnrollment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class StudentEnrollmentRowMapper
        implements RowMapper<StudentEnrollment> {

    @Override
    public StudentEnrollment mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        StudentEnrollment enrollment =
                new StudentEnrollment();

        enrollment.setEnrollmentId(
                rs.getLong("ENROLLMENT_ID")
        );

        enrollment.setStudentId(
                rs.getLong("STUDENT_ID")
        );

        enrollment.setAcademicYearClassId(
                rs.getLong("ACADEMIC_YEAR_CLASS_ID")
        );

        enrollment.setSectionShiftId(
                rs.getLong("SECTION_SHIFT_ID")
        );

        enrollment.setRollNumber(
                rs.getString("ROLL_NUMBER")
        );

        Date enrollmentDate =
                rs.getDate("ENROLLMENT_DATE");

        enrollment.setEnrollmentDate(
                enrollmentDate != null
                        ? enrollmentDate.toLocalDate()
                        : null
        );

        enrollment.setStatus(
                rs.getString("STATUS")
        );

        enrollment.setRemarks(
                rs.getString("REMARKS")
        );

        Timestamp createdAt =
                rs.getTimestamp("CREATED_AT");

        enrollment.setCreatedAt(
                createdAt != null
                        ? createdAt.toLocalDateTime()
                        : null
        );

        Timestamp updatedAt =
                rs.getTimestamp("UPDATED_AT");

        enrollment.setUpdatedAt(
                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );

        return enrollment;
    }
}