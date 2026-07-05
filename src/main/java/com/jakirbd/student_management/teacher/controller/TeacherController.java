package com.jakirbd.student_management.teacher.controller;

import com.jakirbd.student_management.teacher.dto.TeacherCreateRequest;
import com.jakirbd.student_management.teacher.dto.TeacherResponse;
import com.jakirbd.student_management.teacher.dto.TeacherUpdateRequest;
import com.jakirbd.student_management.teacher.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody TeacherCreateRequest request) {
        Long teacherId = teacherService.create(request);
        return ResponseEntity.ok(teacherId);
    }

    @PutMapping("/{teacherId}")
    public ResponseEntity<String> update(
            @PathVariable Long teacherId,
            @RequestBody TeacherUpdateRequest request
    ) {
        teacherService.update(teacherId, request);
        return ResponseEntity.ok("Teacher updated successfully");
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<String> delete(@PathVariable Long teacherId) {
        teacherService.delete(teacherId);
        return ResponseEntity.ok("Teacher deleted successfully");
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<TeacherResponse> findById(@PathVariable Long teacherId) {
        return ResponseEntity.ok(teacherService.findById(teacherId));
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponse>> findAll() {
        return ResponseEntity.ok(teacherService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeacherResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(teacherService.search(q));
    }
}