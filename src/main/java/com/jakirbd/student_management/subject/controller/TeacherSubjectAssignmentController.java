package com.jakirbd.student_management.subject.controller;

import com.jakirbd.student_management.subject.dto.request.TeacherSubjectAssignmentCreateRequest;
import com.jakirbd.student_management.subject.dto.request.TeacherSubjectAssignmentUpdateRequest;
import com.jakirbd.student_management.subject.dto.response.TeacherSubjectAssignmentResponse;
import com.jakirbd.student_management.subject.service.TeacherSubjectAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher-subject-assignments")
public class TeacherSubjectAssignmentController {

    private final TeacherSubjectAssignmentService assignmentService;

    public TeacherSubjectAssignmentController(
            TeacherSubjectAssignmentService assignmentService
    ) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<TeacherSubjectAssignmentResponse>
    createAssignment(
            @Valid
            @RequestBody
            TeacherSubjectAssignmentCreateRequest request
    ) {
        TeacherSubjectAssignmentResponse response =
                assignmentService.createAssignment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{assignmentId}")
    public ResponseEntity<TeacherSubjectAssignmentResponse>
    updateAssignment(
            @PathVariable Long assignmentId,
            @Valid
            @RequestBody
            TeacherSubjectAssignmentUpdateRequest request
    ) {
        TeacherSubjectAssignmentResponse response =
                assignmentService.updateAssignment(
                        assignmentId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<TeacherSubjectAssignmentResponse>
    getAssignmentById(
            @PathVariable Long assignmentId
    ) {
        return ResponseEntity.ok(
                assignmentService.getAssignmentById(
                        assignmentId
                )
        );
    }

    @GetMapping
    public ResponseEntity<
            List<TeacherSubjectAssignmentResponse>
            > getAllAssignments() {

        return ResponseEntity.ok(
                assignmentService.getAllAssignments()
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<
            List<TeacherSubjectAssignmentResponse>
            > getAssignmentsByTeacherId(
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(
                assignmentService.getAssignmentsByTeacherId(
                        teacherId
                )
        );
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable Long assignmentId
    ) {
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }
}