package com.jakirbd.student_management.subject.controller;

import com.jakirbd.student_management.subject.dto.request.ClassSubjectCreateRequest;
import com.jakirbd.student_management.subject.dto.request.ClassSubjectUpdateRequest;
import com.jakirbd.student_management.subject.dto.response.ClassSubjectResponse;
import com.jakirbd.student_management.subject.service.ClassSubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-subjects")
public class ClassSubjectController {

    private final ClassSubjectService classSubjectService;

    public ClassSubjectController(
            ClassSubjectService classSubjectService
    ) {
        this.classSubjectService = classSubjectService;
    }

    @PostMapping
    public ResponseEntity<ClassSubjectResponse> createClassSubject(
            @Valid @RequestBody ClassSubjectCreateRequest request
    ) {
        ClassSubjectResponse response =
                classSubjectService.createClassSubject(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{classSubjectId}")
    public ResponseEntity<ClassSubjectResponse> updateClassSubject(
            @PathVariable Long classSubjectId,
            @Valid @RequestBody ClassSubjectUpdateRequest request
    ) {
        ClassSubjectResponse response =
                classSubjectService.updateClassSubject(
                        classSubjectId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{classSubjectId}")
    public ResponseEntity<ClassSubjectResponse> getClassSubjectById(
            @PathVariable Long classSubjectId
    ) {
        return ResponseEntity.ok(
                classSubjectService.getClassSubjectById(
                        classSubjectId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<ClassSubjectResponse>>
    getAllClassSubjects() {
        return ResponseEntity.ok(
                classSubjectService.getAllClassSubjects()
        );
    }

    @GetMapping("/academic-year-class/{academicYearClassId}")
    public ResponseEntity<List<ClassSubjectResponse>>
    getSubjectsByAcademicClass(
            @PathVariable Long academicYearClassId
    ) {
        return ResponseEntity.ok(
                classSubjectService.getSubjectsByAcademicClass(
                        academicYearClassId
                )
        );
    }

    @DeleteMapping("/{classSubjectId}")
    public ResponseEntity<Void> deleteClassSubjectById(
            @PathVariable Long classSubjectId
    ) {
        classSubjectService.deleteClassSubjectById(
                classSubjectId
        );

        return ResponseEntity.noContent().build();
    }
}