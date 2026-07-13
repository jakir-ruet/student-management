package com.jakirbd.student_management.attendance.controller;

import com.jakirbd.student_management.attendance.dto.request.AttendanceSessionCreateRequest;
import com.jakirbd.student_management.attendance.dto.request.AttendanceSessionStatusUpdateRequest;
import com.jakirbd.student_management.attendance.dto.request.AttendanceSessionUpdateRequest;
import com.jakirbd.student_management.attendance.dto.response.AttendanceSessionResponse;
import com.jakirbd.student_management.attendance.service.AttendanceSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance-sessions")
public class AttendanceSessionController {

    private final AttendanceSessionService sessionService;

    public AttendanceSessionController(
            AttendanceSessionService sessionService
    ) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<AttendanceSessionResponse> createSession(
            @Valid @RequestBody AttendanceSessionCreateRequest request
    ) {
        AttendanceSessionResponse response =
                sessionService.createSession(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{attendanceSessionId}")
    public ResponseEntity<AttendanceSessionResponse> updateSession(
            @PathVariable Long attendanceSessionId,
            @Valid @RequestBody AttendanceSessionUpdateRequest request
    ) {
        return ResponseEntity.ok(
                sessionService.updateSession(
                        attendanceSessionId,
                        request
                )
        );
    }

    @GetMapping("/{attendanceSessionId}")
    public ResponseEntity<AttendanceSessionResponse> getSessionById(
            @PathVariable Long attendanceSessionId
    ) {
        return ResponseEntity.ok(
                sessionService.getSessionById(
                        attendanceSessionId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<AttendanceSessionResponse>>
    getAllSessions() {
        return ResponseEntity.ok(
                sessionService.getAllSessions()
        );
    }

    @GetMapping("/section-shift/{sectionShiftId}")
    public ResponseEntity<List<AttendanceSessionResponse>>
    getSessionsBySectionAndDate(
            @PathVariable Long sectionShiftId,
            @RequestParam LocalDate attendanceDate
    ) {
        return ResponseEntity.ok(
                sessionService.getSessionsBySectionAndDate(
                        sectionShiftId,
                        attendanceDate
                )
        );
    }

    @PatchMapping("/{attendanceSessionId}/status")
    public ResponseEntity<AttendanceSessionResponse>
    changeSessionStatus(
            @PathVariable Long attendanceSessionId,
            @Valid
            @RequestBody
            AttendanceSessionStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(
                sessionService.changeSessionStatus(
                        attendanceSessionId,
                        request.getStatus()
                )
        );
    }

    @DeleteMapping("/{attendanceSessionId}")
    public ResponseEntity<Void> deleteSessionById(
            @PathVariable Long attendanceSessionId
    ) {
        sessionService.deleteSessionById(
                attendanceSessionId
        );

        return ResponseEntity.noContent().build();
    }
}