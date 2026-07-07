package com.jakirbd.student_management.acss.service.impl;

import com.jakirbd.student_management.acss.dto.request.ShiftCreateRequest;
import com.jakirbd.student_management.acss.dto.request.ShiftUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.ShiftResponse;
import com.jakirbd.student_management.acss.model.Shift;
import com.jakirbd.student_management.acss.repository.ShiftRepository;
import com.jakirbd.student_management.acss.service.ShiftService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public ShiftResponse createShift(ShiftCreateRequest request) {
        Long shiftId = shiftRepository.createShift(
                request.getShiftName(),
                request.getShiftCode(),
                request.getStartTime(),
                request.getEndTime(),
                request.getDisplayOrder(),
                request.getStatus()
        );

        return getShiftById(shiftId);
    }

    @Override
    public ShiftResponse updateShift(
            Long shiftId,
            ShiftUpdateRequest request
    ) {
        shiftRepository.updateShift(
                shiftId,
                request.getShiftName(),
                request.getShiftCode(),
                request.getStartTime(),
                request.getEndTime(),
                request.getDisplayOrder(),
                request.getStatus()
        );

        return getShiftById(shiftId);
    }

    @Override
    public ShiftResponse getShiftById(Long shiftId) {
        Shift shift = shiftRepository.findShiftById(shiftId)
                .orElseThrow(() ->
                        new RuntimeException("Shift not found with ID: " + shiftId)
                );

        return mapToResponse(shift);
    }

    @Override
    public List<ShiftResponse> getAllShifts() {
        return shiftRepository.findAllShifts()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteShift(Long shiftId) {
        getShiftById(shiftId);
        shiftRepository.deleteShift(shiftId);
    }

    private ShiftResponse mapToResponse(Shift shift) {
        ShiftResponse response = new ShiftResponse();

        response.setShiftId(shift.getShiftId());
        response.setShiftName(shift.getShiftName());
        response.setShiftCode(shift.getShiftCode());
        response.setStartTime(shift.getStartTime());
        response.setEndTime(shift.getEndTime());
        response.setDisplayOrder(shift.getDisplayOrder());
        response.setStatus(shift.getStatus());
        response.setCreatedAt(shift.getCreatedAt());
        response.setUpdatedAt(shift.getUpdatedAt());

        return response;
    }
}
