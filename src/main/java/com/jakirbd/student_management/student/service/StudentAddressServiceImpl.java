package com.jakirbd.student_management.student.service;

import com.jakirbd.student_management.student.dto.StudentAddressCreateRequest;
import com.jakirbd.student_management.student.dto.StudentAddressResponse;
import com.jakirbd.student_management.student.dto.StudentAddressUpdateRequest;
import com.jakirbd.student_management.student.model.StudentAddress;
import com.jakirbd.student_management.student.repository.StudentAddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentAddressServiceImpl implements StudentAddressService {

    private final StudentAddressRepository studentAddressRepository;

    public StudentAddressServiceImpl(StudentAddressRepository studentAddressRepository) {
        this.studentAddressRepository = studentAddressRepository;
    }

    @Override
    public Long addAddress(StudentAddressCreateRequest request) {
        StudentAddress address = new StudentAddress();

        address.setStudentId(request.getStudentId());
        address.setAddressType(request.getAddressType());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        return studentAddressRepository.addAddress(address);
    }

    @Override
    public void updateAddress(Long addressId, StudentAddressUpdateRequest request) {
        StudentAddress address = new StudentAddress();

        address.setAddressId(addressId);
        address.setAddressType(request.getAddressType());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        studentAddressRepository.updateAddress(address);
    }

    @Override
    public void deleteAddress(Long addressId) {
        studentAddressRepository.deleteAddress(addressId);
    }

    @Override
    public StudentAddressResponse getAddressById(Long addressId) {
        StudentAddress address = studentAddressRepository.getAddressById(addressId)
                .orElseThrow(() -> new RuntimeException("Student address not found"));

        return mapToResponse(address);
    }

    @Override
    public List<StudentAddressResponse> getAddressesByStudent(Long studentId) {
        return studentAddressRepository.getAddressesByStudent(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private StudentAddressResponse mapToResponse(StudentAddress address) {
        StudentAddressResponse response = new StudentAddressResponse();

        response.setAddressId(address.getAddressId());
        response.setStudentId(address.getStudentId());
        response.setAddressType(address.getAddressType());
        response.setAddressLine(address.getAddressLine());
        response.setCity(address.getCity());
        response.setDistrict(address.getDistrict());
        response.setPostalCode(address.getPostalCode());
        response.setCountry(address.getCountry());
        response.setCreatedAt(address.getCreatedAt());
        response.setUpdatedAt(address.getUpdatedAt());

        return response;
    }
}
