package com.jakirbd.student_management.teacher.service;

import com.jakirbd.student_management.teacher.dto.TeacherAddressCreateRequest;
import com.jakirbd.student_management.teacher.dto.TeacherAddressResponse;
import com.jakirbd.student_management.teacher.dto.TeacherAddressUpdateRequest;
import com.jakirbd.student_management.teacher.model.TeacherAddress;
import com.jakirbd.student_management.teacher.repository.TeacherAddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherAddressServiceImpl implements TeacherAddressService {

    private final TeacherAddressRepository teacherAddressRepository;

    public TeacherAddressServiceImpl(
            TeacherAddressRepository teacherAddressRepository
    ) {
        this.teacherAddressRepository = teacherAddressRepository;
    }

    @Override
    public Long create(TeacherAddressCreateRequest request) {
        TeacherAddress address = new TeacherAddress();

        address.setTeacherId(request.getTeacherId());
        address.setAddressType(request.getAddressType());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        return teacherAddressRepository.create(address);
    }

    @Override
    public void update(
            Long addressId,
            TeacherAddressUpdateRequest request
    ) {
        TeacherAddress address = new TeacherAddress();

        address.setAddressId(addressId);
        address.setAddressType(request.getAddressType());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        teacherAddressRepository.update(address);
    }

    @Override
    public void delete(Long addressId) {
        teacherAddressRepository.delete(addressId);
    }

    @Override
    public List<TeacherAddressResponse> findByTeacherId(Long teacherId) {
        return teacherAddressRepository.findByTeacherId(teacherId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TeacherAddressResponse toResponse(TeacherAddress address) {
        TeacherAddressResponse response = new TeacherAddressResponse();

        response.setAddressId(address.getAddressId());
        response.setTeacherId(address.getTeacherId());
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