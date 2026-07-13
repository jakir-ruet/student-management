package com.jakirbd.student_management.attendance.service.impl;

import com.jakirbd.student_management.attendance.dto.request.AttendanceSessionCreateRequest;
import com.jakirbd.student_management.attendance.dto.request.AttendanceSessionUpdateRequest;
import com.jakirbd.student_management.attendance.dto.response.AttendanceSessionResponse;
import com.jakirbd.student_management.attendance.model.AttendanceSession;
import com.jakirbd.student_management.attendance.repository.AttendanceSessionRepository;
import com.jakirbd.student_management.attendance.service.AttendanceSessionService;
import com.jakirbd.student_management.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceSessionServiceImpl
        implements AttendanceSessionService {

    private final AttendanceSessionRepository sessionRepository;

    public AttendanceSessionServiceImpl(
            AttendanceSessionRepository sessionRepository
    ) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public AttendanceSessionResponse createSession(
            AttendanceSessionCreateRequest request
    ) {
        Long attendanceSessionId =
                sessionRepository.createSession(
                        request.getSectionShiftId(),
                        request.getClassSubjectId(),
                        request.getAttendanceDate(),
                        request.getAttendanceType(),
                        request.getPeriodNumber(),
                        request.getMarkedByTeacherId(),
                        request.getStatus(),
                        request.getRemarks()
                );

        return getSessionById(attendanceSessionId);
    }

    @Override
    @Transactional
    public AttendanceSessionResponse updateSession(
            Long attendanceSessionId,
            AttendanceSessionUpdateRequest request
    ) {
        getSessionEntityById(attendanceSessionId);

        sessionRepository.updateSession(
                attendanceSessionId,
                request.getSectionShiftId(),
                request.getClassSubjectId(),
                request.getAttendanceDate(),
                request.getAttendanceType(),
                request.getPeriodNumber(),
                request.getMarkedByTeacherId(),
                request.getStatus(),
                request.getRemarks()
        );

        return getSessionById(attendanceSessionId);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceSessionResponse getSessionById(
            Long attendanceSessionId
    ) {
        return mapToResponse(
                getSessionEntityById(attendanceSessionId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceSessionResponse> getAllSessions() {
        return sessionRepository.findAllSessions()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceSessionResponse>
    getSessionsBySectionAndDate(
            Long sectionShiftId,
            LocalDate attendanceDate
    ) {
        return sessionRepository
                .findSessionsBySectionAndDate(
                        sectionShiftId,
                        attendanceDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public AttendanceSessionResponse changeSessionStatus(
            Long attendanceSessionId,
            String status
    ) {
        getSessionEntityById(attendanceSessionId);

        sessionRepository.changeSessionStatus(
                attendanceSessionId,
                status
        );

        return getSessionById(attendanceSessionId);
    }

    @Override
    @Transactional
    public void deleteSessionById(Long attendanceSessionId) {
        getSessionEntityById(attendanceSessionId);

        sessionRepository.deleteSessionById(
                attendanceSessionId
        );
    }

    private AttendanceSession getSessionEntityById(
            Long attendanceSessionId
    ) {
        return sessionRepository
                .findSessionById(attendanceSessionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance session not found with ID: "
                                        + attendanceSessionId
                        )
                );
    }

    private AttendanceSessionResponse mapToResponse(
            AttendanceSession session
    ) {
        AttendanceSessionResponse response =
                new AttendanceSessionResponse();

        response.setAttendanceSessionId(
                session.getAttendanceSessionId()
        );
        response.setSectionShiftId(
                session.getSectionShiftId()
        );
        response.setClassSubjectId(
                session.getClassSubjectId()
        );
        response.setAttendanceDate(
                session.getAttendanceDate()
        );
        response.setAttendanceType(
                session.getAttendanceType()
        );
        response.setPeriodNumber(
                session.getPeriodNumber()
        );
        response.setMarkedByTeacherId(
                session.getMarkedByTeacherId()
        );
        response.setStatus(session.getStatus());
        response.setRemarks(session.getRemarks());
        response.setCreatedAt(session.getCreatedAt());
        response.setUpdatedAt(session.getUpdatedAt());

        return response;
    }
}