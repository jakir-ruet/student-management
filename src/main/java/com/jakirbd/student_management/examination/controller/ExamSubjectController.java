package com.jakirbd.student_management.examination.controller;

import com.jakirbd.student_management.examination.dto.request.ExamSubjectCreateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamSubjectStatusUpdateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamSubjectUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.ExamSubjectResponse;
import com.jakirbd.student_management.examination.service.ExamSubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/exam-subjects")
public class ExamSubjectController {

    private final ExamSubjectService examSubjectService;

    public ExamSubjectController(
            ExamSubjectService examSubjectService
    ) {
        this.examSubjectService = examSubjectService;
    }

    @PostMapping
    public ResponseEntity<ExamSubjectResponse> createExamSubject(
            @Valid @RequestBody ExamSubjectCreateRequest request
    ) {
        Long examSubjectId =
                examSubjectService.createExamSubject(request);

        ExamSubjectResponse response =
                examSubjectService.getExamSubjectById(
                        examSubjectId
                );

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/exam-subjects/"
                                        + examSubjectId
                        )
                )
                .body(response);
    }

    @PutMapping("/{examSubjectId}")
    public ResponseEntity<ExamSubjectResponse> updateExamSubject(
            @PathVariable Long examSubjectId,
            @Valid @RequestBody ExamSubjectUpdateRequest request
    ) {
        examSubjectService.updateExamSubject(
                examSubjectId,
                request
        );

        return ResponseEntity.ok(
                examSubjectService.getExamSubjectById(
                        examSubjectId
                )
        );
    }

    @GetMapping("/{examSubjectId}")
    public ResponseEntity<ExamSubjectResponse>
    getExamSubjectById(
            @PathVariable Long examSubjectId
    ) {
        return ResponseEntity.ok(
                examSubjectService.getExamSubjectById(
                        examSubjectId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<ExamSubjectResponse>>
    getAllExamSubjects() {
        return ResponseEntity.ok(
                examSubjectService.getAllExamSubjects()
        );
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamSubjectResponse>>
    getExamSubjectsByExam(
            @PathVariable Long examId
    ) {
        return ResponseEntity.ok(
                examSubjectService.getExamSubjectsByExam(
                        examId
                )
        );
    }

    @GetMapping("/class/{academicYearClassId}")
    public ResponseEntity<List<ExamSubjectResponse>>
    getExamSubjectsByClass(
            @PathVariable Long academicYearClassId
    ) {
        return ResponseEntity.ok(
                examSubjectService.getExamSubjectsByClass(
                        academicYearClassId
                )
        );
    }

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<ExamSubjectResponse>>
    getExamSubjectsBySection(
            @PathVariable Long sectionId
    ) {
        return ResponseEntity.ok(
                examSubjectService.getExamSubjectsBySection(
                        sectionId
                )
        );
    }

    @PatchMapping("/{examSubjectId}/status")
    public ResponseEntity<ExamSubjectResponse>
    updateExamSubjectStatus(
            @PathVariable Long examSubjectId,
            @Valid
            @RequestBody ExamSubjectStatusUpdateRequest request
    ) {
        examSubjectService.updateExamSubjectStatus(
                examSubjectId,
                request
        );

        return ResponseEntity.ok(
                examSubjectService.getExamSubjectById(
                        examSubjectId
                )
        );
    }

    @DeleteMapping("/{examSubjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExamSubject(
            @PathVariable Long examSubjectId
    ) {
        examSubjectService.deleteExamSubject(examSubjectId);
    }
}