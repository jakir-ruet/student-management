package com.jakirbd.student_management.attendance.controller;

import com.jakirbd.student_management.attendance.dto.request.AttendanceRecordCreateRequest;
import com.jakirbd.student_management.attendance.dto.request.AttendanceRecordUpdateRequest;
import com.jakirbd.student_management.attendance.dto.response.AttendanceRecordResponse;
import com.jakirbd.student_management.attendance.service.AttendanceRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance-records")
public class AttendanceRecordController {

    private final AttendanceRecordService recordService;

    public AttendanceRecordController(
            AttendanceRecordService recordService
    ) {
        this.recordService = recordService;
    }

    @PostMapping
    public ResponseEntity<AttendanceRecordResponse> createRecord(
            @Valid @RequestBody AttendanceRecordCreateRequest request
    ) {
        AttendanceRecordResponse response =
                recordService.createRecord(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{attendanceRecordId}")
    public ResponseEntity<AttendanceRecordResponse> updateRecord(
            @PathVariable Long attendanceRecordId,
            @Valid @RequestBody AttendanceRecordUpdateRequest request
    ) {
        return ResponseEntity.ok(
                recordService.updateRecord(
                        attendanceRecordId,
                        request
                )
        );
    }

    @GetMapping("/{attendanceRecordId}")
    public ResponseEntity<AttendanceRecordResponse> getRecordById(
            @PathVariable Long attendanceRecordId
    ) {
        return ResponseEntity.ok(
                recordService.getRecordById(
                        attendanceRecordId
                )
        );
    }

    @GetMapping("/session/{attendanceSessionId}")
    public ResponseEntity<List<AttendanceRecordResponse>>
    getRecordsBySessionId(
            @PathVariable Long attendanceSessionId
    ) {
        return ResponseEntity.ok(
                recordService.getRecordsBySessionId(
                        attendanceSessionId
                )
        );
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<AttendanceRecordResponse>>
    getRecordsByEnrollmentId(
            @PathVariable Long enrollmentId
    ) {
        return ResponseEntity.ok(
                recordService.getRecordsByEnrollmentId(
                        enrollmentId
                )
        );
    }

    @DeleteMapping("/{attendanceRecordId}")
    public ResponseEntity<Void> deleteRecordById(
            @PathVariable Long attendanceRecordId
    ) {
        recordService.deleteRecordById(
                attendanceRecordId
        );

        return ResponseEntity.noContent().build();
    }
}