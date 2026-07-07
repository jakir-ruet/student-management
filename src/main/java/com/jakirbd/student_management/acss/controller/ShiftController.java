package com.jakirbd.student_management.acss.controller;

import com.jakirbd.student_management.acss.dto.request.ShiftCreateRequest;
import com.jakirbd.student_management.acss.dto.request.ShiftUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.ShiftResponse;
import com.jakirbd.student_management.acss.service.ShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acss/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @PostMapping
    public ResponseEntity<ShiftResponse> createShift(
            @RequestBody ShiftCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shiftService.createShift(request));
    }

    @PutMapping("/{shiftId}")
    public ResponseEntity<ShiftResponse> updateShift(
            @PathVariable Long shiftId,
            @RequestBody ShiftUpdateRequest request
    ) {
        return ResponseEntity.ok(
                shiftService.updateShift(shiftId, request)
        );
    }

    @GetMapping("/{shiftId}")
    public ResponseEntity<ShiftResponse> getShiftById(
            @PathVariable Long shiftId
    ) {
        return ResponseEntity.ok(
                shiftService.getShiftById(shiftId)
        );
    }

    @GetMapping
    public ResponseEntity<List<ShiftResponse>> getAllShifts() {
        return ResponseEntity.ok(
                shiftService.getAllShifts()
        );
    }

    @DeleteMapping("/{shiftId}")
    public ResponseEntity<Void> deleteShift(
            @PathVariable Long shiftId
    ) {
        shiftService.deleteShift(shiftId);
        return ResponseEntity.noContent().build();
    }
}
