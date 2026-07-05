package com.jakirbd.student_management.student.controller;

import com.jakirbd.student_management.student.dto.StudentAddressCreateRequest;
import com.jakirbd.student_management.student.dto.StudentAddressResponse;
import com.jakirbd.student_management.student.dto.StudentAddressUpdateRequest;
import com.jakirbd.student_management.student.service.StudentAddressService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-addresses")
public class StudentAddressController {

    private final StudentAddressService studentAddressService;

    public StudentAddressController(
            StudentAddressService studentAddressService
    ) {
        this.studentAddressService = studentAddressService;
    }

    @PostMapping
    public ResponseEntity<Long> addAddress(
            @RequestBody StudentAddressCreateRequest request
    ) {
        Long addressId = studentAddressService.addAddress(request);

        return ResponseEntity.ok(addressId);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<String> updateAddress(
            @PathVariable Long addressId,
            @RequestBody StudentAddressUpdateRequest request
    ) {
        studentAddressService.updateAddress(addressId, request);

        return ResponseEntity.ok(
                "Student address updated successfully"
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long addressId
    ) {
        studentAddressService.deleteAddress(addressId);

        return ResponseEntity.ok(
                "Student address deleted successfully"
        );
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<StudentAddressResponse> getAddressById(
            @PathVariable Long addressId
    ) {
        StudentAddressResponse response =
                studentAddressService.getAddressById(addressId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentAddressResponse>> getAddressesByStudent(
            @PathVariable Long studentId
    ) {
        List<StudentAddressResponse> responses =
                studentAddressService.getAddressesByStudent(studentId);

        return ResponseEntity.ok(responses);
    }
}
