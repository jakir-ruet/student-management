package com.jakirbd.student_management.acss.service;

import com.jakirbd.student_management.acss.dto.request.ShiftCreateRequest;
import com.jakirbd.student_management.acss.dto.request.ShiftUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.ShiftResponse;

import java.util.List;

public interface ShiftService {

    ShiftResponse createShift(
            ShiftCreateRequest request
    );

    ShiftResponse updateShift(
            Long shiftId,
            ShiftUpdateRequest request
    );

    ShiftResponse getShiftById(
            Long shiftId
    );

    List<ShiftResponse> getAllShifts();

    void deleteShift(
            Long shiftId
    );
}
