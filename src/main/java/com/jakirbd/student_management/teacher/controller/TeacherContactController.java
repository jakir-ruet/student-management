package com.jakirbd.student_management.teacher.controller;

import com.jakirbd.student_management.teacher.dto.TeacherContactCreateRequest;
import com.jakirbd.student_management.teacher.dto.TeacherContactResponse;
import com.jakirbd.student_management.teacher.dto.TeacherContactUpdateRequest;
import com.jakirbd.student_management.teacher.service.TeacherContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher-contacts")
public class TeacherContactController {

    private final TeacherContactService teacherContactService;

    public TeacherContactController(TeacherContactService teacherContactService) {
        this.teacherContactService = teacherContactService;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody TeacherContactCreateRequest request) {
        Long contactId = teacherContactService.create(request);
        return ResponseEntity.ok(contactId);
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<String> update(
            @PathVariable Long contactId,
            @RequestBody TeacherContactUpdateRequest request
    ) {
        teacherContactService.update(contactId, request);
        return ResponseEntity.ok("Teacher contact updated successfully");
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<String> delete(@PathVariable Long contactId) {
        teacherContactService.delete(contactId);
        return ResponseEntity.ok("Teacher contact deleted successfully");
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeacherContactResponse>> findByTeacherId(
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(teacherContactService.findByTeacherId(teacherId));
    }
}