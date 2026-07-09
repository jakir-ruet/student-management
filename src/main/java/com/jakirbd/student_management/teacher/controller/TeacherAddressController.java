package com.jakirbd.student_management.teacher.controller;

import com.jakirbd.student_management.teacher.dto.TeacherAddressCreateRequest;
import com.jakirbd.student_management.teacher.dto.TeacherAddressResponse;
import com.jakirbd.student_management.teacher.dto.TeacherAddressUpdateRequest;
import com.jakirbd.student_management.teacher.service.TeacherAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher-addresses")
public class TeacherAddressController {

    private final TeacherAddressService teacherAddressService;

    public TeacherAddressController(
            TeacherAddressService teacherAddressService
    ) {
        this.teacherAddressService = teacherAddressService;
    }

    @PostMapping
    public ResponseEntity<Long> create(
            @RequestBody TeacherAddressCreateRequest request
    ) {
        Long addressId = teacherAddressService.create(request);
        return ResponseEntity.ok(addressId);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<String> update(
            @PathVariable Long addressId,
            @RequestBody TeacherAddressUpdateRequest request
    ) {
        teacherAddressService.update(addressId, request);

        return ResponseEntity.ok(
                "Teacher address updated successfully"
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> delete(
            @PathVariable Long addressId
    ) {
        teacherAddressService.delete(addressId);

        return ResponseEntity.ok(
                "Teacher address deleted successfully"
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeacherAddressResponse>> findByTeacherId(
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(
                teacherAddressService.findByTeacherId(teacherId)
        );
    }
}