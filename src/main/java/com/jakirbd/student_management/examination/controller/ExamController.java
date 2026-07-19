package com.jakirbd.student_management.examination.controller;

import com.jakirbd.student_management.examination.dto.request.ExamCreateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamStatusUpdateRequest;
import com.jakirbd.student_management.examination.dto.request.ExamUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.ExamResponse;
import com.jakirbd.student_management.examination.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<ExamResponse> createExam(
            @Valid @RequestBody ExamCreateRequest request
    ) {
        Long examId = examService.createExam(request);
        ExamResponse response = examService.getExamById(examId);

        return ResponseEntity
                .created(URI.create("/api/exams/" + examId))
                .body(response);
    }

    @PutMapping("/{examId}")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable Long examId,
            @Valid @RequestBody ExamUpdateRequest request
    ) {
        examService.updateExam(examId, request);

        return ResponseEntity.ok(
                examService.getExamById(examId)
        );
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ExamResponse> getExamById(
            @PathVariable Long examId
    ) {
        return ResponseEntity.ok(
                examService.getExamById(examId)
        );
    }

    @GetMapping
    public ResponseEntity<List<ExamResponse>> getAllExams() {
        return ResponseEntity.ok(
                examService.getAllExams()
        );
    }

    @GetMapping("/academic-year/{academicYearId}")
    public ResponseEntity<List<ExamResponse>>
    getExamsByAcademicYear(
            @PathVariable Long academicYearId
    ) {
        return ResponseEntity.ok(
                examService.getExamsByAcademicYear(
                        academicYearId
                )
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ExamResponse>> getExamsByStatus(
            @PathVariable String status
    ) {
        return ResponseEntity.ok(
                examService.getExamsByStatus(status)
        );
    }

    @PatchMapping("/{examId}/status")
    public ResponseEntity<ExamResponse> updateExamStatus(
            @PathVariable Long examId,
            @Valid @RequestBody ExamStatusUpdateRequest request
    ) {
        examService.updateExamStatus(examId, request);

        return ResponseEntity.ok(
                examService.getExamById(examId)
        );
    }

    @DeleteMapping("/{examId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExam(@PathVariable Long examId) {
        examService.deleteExam(examId);
    }
}