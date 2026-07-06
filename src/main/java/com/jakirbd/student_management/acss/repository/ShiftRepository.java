package com.jakirbd.student_management.acss.repository;

import com.jakirbd.student_management.acss.model.Shift;

import java.util.List;
import java.util.Optional;

public interface ShiftRepository {

    Long createShift(
            String shiftName,
            String shiftCode,
            String startTime,
            String endTime,
            Integer displayOrder,
            String status
    );

    void updateShift(
            Long shiftId,
            String shiftName,
            String shiftCode,
            String startTime,
            String endTime,
            Integer displayOrder,
            String status
    );

    Optional<Shift> findShiftById(Long shiftId);

    List<Shift> findAllShifts();

    void deleteShift(Long shiftId);
}
