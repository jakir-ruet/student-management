package com.jakirbd.student_management.examination.controller;

import com.jakirbd.student_management.examination.dto.request.StudentExamMarkCreateRequest;
import com.jakirbd.student_management.examination.dto.request.StudentExamMarkPublishRequest;
import com.jakirbd.student_management.examination.dto.request.StudentExamMarkUpdateRequest;
import com.jakirbd.student_management.examination.dto.response.StudentExamMarkResponse;
import com.jakirbd.student_management.examination.dto.response.StudentExamResultResponse;
import com.jakirbd.student_management.examination.service.StudentExamMarkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/student-exam-marks")
public class StudentExamMarkController {

    private final StudentExamMarkService studentExamMarkService;

    public StudentExamMarkController(
            StudentExamMarkService studentExamMarkService
    ) {
        this.studentExamMarkService =
                studentExamMarkService;
    }

    @PostMapping
    public ResponseEntity<StudentExamMarkResponse>
    createStudentMark(
            @Valid
            @RequestBody StudentExamMarkCreateRequest request
    ) {
        Long studentExamMarkId =
                studentExamMarkService.createStudentMark(
                        request
                );

        StudentExamMarkResponse response =
                studentExamMarkService.getStudentMarkById(
                        studentExamMarkId
                );

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/student-exam-marks/"
                                        + studentExamMarkId
                        )
                )
                .body(response);
    }

    @PutMapping("/{studentExamMarkId}")
    public ResponseEntity<StudentExamMarkResponse>
    updateStudentMark(
            @PathVariable Long studentExamMarkId,
            @Valid
            @RequestBody StudentExamMarkUpdateRequest request
    ) {
        studentExamMarkService.updateStudentMark(
                studentExamMarkId,
                request
        );

        return ResponseEntity.ok(
                studentExamMarkService.getStudentMarkById(
                        studentExamMarkId
                )
        );
    }

    @GetMapping("/{studentExamMarkId}")
    public ResponseEntity<StudentExamMarkResponse>
    getStudentMarkById(
            @PathVariable Long studentExamMarkId
    ) {
        return ResponseEntity.ok(
                studentExamMarkService.getStudentMarkById(
                        studentExamMarkId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<StudentExamMarkResponse>>
    getAllStudentMarks() {
        return ResponseEntity.ok(
                studentExamMarkService.getAllStudentMarks()
        );
    }

    @GetMapping("/exam-subject/{examSubjectId}")
    public ResponseEntity<List<StudentExamMarkResponse>>
    getMarksByExamSubject(
            @PathVariable Long examSubjectId
    ) {
        return ResponseEntity.ok(
                studentExamMarkService.getMarksByExamSubject(
                        examSubjectId
                )
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentExamMarkResponse>>
    getMarksByStudent(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                studentExamMarkService.getMarksByStudent(
                        studentId
                )
        );
    }

    @GetMapping("/student/{studentId}/exam/{examId}")
    public ResponseEntity<List<StudentExamResultResponse>>
    getStudentExamResults(
            @PathVariable Long studentId,
            @PathVariable Long examId
    ) {
        return ResponseEntity.ok(
                studentExamMarkService.getStudentExamResults(
                        studentId,
                        examId
                )
        );
    }

    @PatchMapping("/{studentExamMarkId}/publish")
    public ResponseEntity<StudentExamMarkResponse>
    publishStudentMark(
            @PathVariable Long studentExamMarkId,
            @Valid
            @RequestBody StudentExamMarkPublishRequest request
    ) {
        studentExamMarkService.publishStudentMark(
                studentExamMarkId,
                request
        );

        return ResponseEntity.ok(
                studentExamMarkService.getStudentMarkById(
                        studentExamMarkId
                )
        );
    }

    @PatchMapping("/{studentExamMarkId}/unpublish")
    public ResponseEntity<StudentExamMarkResponse>
    unpublishStudentMark(
            @PathVariable Long studentExamMarkId,
            @Valid
            @RequestBody StudentExamMarkPublishRequest request
    ) {
        studentExamMarkService.unpublishStudentMark(
                studentExamMarkId,
                request
        );

        return ResponseEntity.ok(
                studentExamMarkService.getStudentMarkById(
                        studentExamMarkId
                )
        );
    }

    @PatchMapping("/exam-subject/{examSubjectId}/publish")
    public ResponseEntity<List<StudentExamMarkResponse>>
    publishExamSubjectMarks(
            @PathVariable Long examSubjectId,
            @Valid
            @RequestBody StudentExamMarkPublishRequest request
    ) {
        studentExamMarkService.publishExamSubjectMarks(
                examSubjectId,
                request
        );

        return ResponseEntity.ok(
                studentExamMarkService.getMarksByExamSubject(
                        examSubjectId
                )
        );
    }

    @DeleteMapping("/{studentExamMarkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentMark(
            @PathVariable Long studentExamMarkId
    ) {
        studentExamMarkService.deleteStudentMark(
                studentExamMarkId
        );
    }
}