package com.jakirbd.student_management.acss.controller;

import com.jakirbd.student_management.acss.dto.request.SchoolClassCreateRequest;
import com.jakirbd.student_management.acss.dto.request.SchoolClassUpdateRequest;
import com.jakirbd.student_management.acss.dto.response.SchoolClassResponse;
import com.jakirbd.student_management.acss.service.SchoolClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acss/classes")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    public SchoolClassController(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }

    @PostMapping
    public ResponseEntity<SchoolClassResponse> createClass(
            @RequestBody SchoolClassCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(schoolClassService.createClass(request));
    }

    @PutMapping("/{classId}")
    public ResponseEntity<SchoolClassResponse> updateClass(
            @PathVariable Long classId,
            @RequestBody SchoolClassUpdateRequest request
    ) {
        return ResponseEntity.ok(
                schoolClassService.updateClass(classId, request)
        );
    }

    @GetMapping("/{classId}")
    public ResponseEntity<SchoolClassResponse> getClassById(
            @PathVariable Long classId
    ) {
        return ResponseEntity.ok(
                schoolClassService.getClassById(classId)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllClasses() {
        return ResponseEntity.ok(
                schoolClassService.getAllClasses()
        );
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<Void> deleteClass(
            @PathVariable Long classId
    ) {
        schoolClassService.deleteClass(classId);
        return ResponseEntity.noContent().build();
    }
}
