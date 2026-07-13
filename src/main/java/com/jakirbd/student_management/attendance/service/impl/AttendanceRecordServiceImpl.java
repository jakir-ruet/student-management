package com.jakirbd.student_management.attendance.service.impl;

import com.jakirbd.student_management.attendance.dto.request.AttendanceRecordCreateRequest;
import com.jakirbd.student_management.attendance.dto.request.AttendanceRecordUpdateRequest;
import com.jakirbd.student_management.attendance.dto.response.AttendanceRecordResponse;
import com.jakirbd.student_management.attendance.model.AttendanceRecord;
import com.jakirbd.student_management.attendance.repository.AttendanceRecordRepository;
import com.jakirbd.student_management.attendance.service.AttendanceRecordService;
import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttendanceRecordServiceImpl
        implements AttendanceRecordService {

    private final AttendanceRecordRepository recordRepository;

    public AttendanceRecordServiceImpl(
            AttendanceRecordRepository recordRepository
    ) {
        this.recordRepository = recordRepository;
    }

    @Override
    @Transactional
    public AttendanceRecordResponse createRecord(
            AttendanceRecordCreateRequest request
    ) {
        Long attendanceRecordId =
                recordRepository.createRecord(
                        request.getAttendanceSessionId(),
                        request.getEnrollmentId(),
                        request.getAttendanceStatus(),
                        request.getCheckInTime(),
                        request.getRemarks()
                );

        return getRecordById(attendanceRecordId);
    }

    @Override
    @Transactional
    public AttendanceRecordResponse updateRecord(
            Long attendanceRecordId,
            AttendanceRecordUpdateRequest request
    ) {
        getRecordEntityById(attendanceRecordId);

        recordRepository.updateRecord(
                attendanceRecordId,
                request.getAttendanceStatus(),
                request.getCheckInTime(),
                request.getRemarks()
        );

        return getRecordById(attendanceRecordId);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceRecordResponse getRecordById(
            Long attendanceRecordId
    ) {
        return mapToResponse(
                getRecordEntityById(attendanceRecordId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecordResponse> getRecordsBySessionId(
            Long attendanceSessionId
    ) {
        return recordRepository.findRecordsBySessionId(
                attendanceSessionId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecordResponse> getRecordsByEnrollmentId(
            Long enrollmentId
    ) {
        return recordRepository
                .findRecordsByEnrollmentId(enrollmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteRecordById(Long attendanceRecordId) {
        getRecordEntityById(attendanceRecordId);

        recordRepository.deleteRecordById(
                attendanceRecordId
        );
    }

    private AttendanceRecord getRecordEntityById(
            Long attendanceRecordId
    ) {
        return recordRepository
                .findRecordById(attendanceRecordId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance record not found with ID: "
                                        + attendanceRecordId
                        )
                );
    }

    private AttendanceRecordResponse mapToResponse(
            AttendanceRecord record
    ) {
        AttendanceRecordResponse response =
                new AttendanceRecordResponse();

        response.setAttendanceRecordId(
                record.getAttendanceRecordId()
        );
        response.setAttendanceSessionId(
                record.getAttendanceSessionId()
        );
        response.setEnrollmentId(
                record.getEnrollmentId()
        );
        response.setAttendanceStatus(
                record.getAttendanceStatus()
        );
        response.setCheckInTime(
                record.getCheckInTime()
        );
        response.setRemarks(record.getRemarks());
        response.setMarkedAt(record.getMarkedAt());
        response.setUpdatedAt(record.getUpdatedAt());

        return response;
    }
}