package com.jakirbd.student_management.subject.controller;

import com.jakirbd.student_management.subject.dto.request.SubjectCreateRequest;
import com.jakirbd.student_management.subject.dto.request.SubjectUpdateRequest;
import com.jakirbd.student_management.subject.dto.response.SubjectResponse;
import com.jakirbd.student_management.subject.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<SubjectResponse> createSubject(
            @Valid @RequestBody SubjectCreateRequest request
    ) {
        SubjectResponse response =
                subjectService.createSubject(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<SubjectResponse> updateSubject(
            @PathVariable Long subjectId,
            @Valid @RequestBody SubjectUpdateRequest request
    ) {
        SubjectResponse response =
                subjectService.updateSubject(subjectId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<SubjectResponse> getSubjectById(
            @PathVariable Long subjectId
    ) {
        return ResponseEntity.ok(
                subjectService.getSubjectById(subjectId)
        );
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
        return ResponseEntity.ok(
                subjectService.getAllSubjects()
        );
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> deleteSubjectById(
            @PathVariable Long subjectId
    ) {
        subjectService.deleteSubjectById(subjectId);
        return ResponseEntity.noContent().build();
    }
}