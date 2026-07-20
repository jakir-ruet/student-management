package com.jakirbd.student_management.fee.repository;

import com.jakirbd.student_management.fee.dto.response.StudentFeeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentFeeRepository {

    Long assignStudentFee(
            Long feeStructureId,
            Long studentId,
            String billingPeriod,
            LocalDate dueDate,
            String remarks,
            Long createdBy
    );

    void updateStudentFee(
            Long studentFeeId,
            LocalDate dueDate,
            String remarks,
            Long updatedBy
    );

    Optional<StudentFeeResponse> findStudentFeeById(
            Long studentFeeId
    );

    List<StudentFeeResponse> findAllStudentFees();

    List<StudentFeeResponse> findFeesByStudent(
            Long studentId
    );

    List<StudentFeeResponse> findFeesByStructure(
            Long feeStructureId
    );

    List<StudentFeeResponse> findFeesByStatus(
            String status
    );

    List<StudentFeeResponse> findOutstandingFees(
            Long studentId
    );

    void applyLateFine(
            Long studentFeeId,
            Long updatedBy
    );

    void refreshFeeStatus(
            Long studentFeeId,
            Long updatedBy
    );

    void waiveStudentFee(
            Long studentFeeId,
            String remarks,
            Long updatedBy
    );

    void cancelStudentFee(
            Long studentFeeId,
            String remarks,
            Long updatedBy
    );

    void deleteStudentFee(Long studentFeeId);
}