package com.jakirbd.student_management.teacher.service;

import com.jakirbd.student_management.teacher.dto.TeacherContactCreateRequest;
import com.jakirbd.student_management.teacher.dto.TeacherContactResponse;
import com.jakirbd.student_management.teacher.dto.TeacherContactUpdateRequest;
import com.jakirbd.student_management.teacher.model.TeacherContact;
import com.jakirbd.student_management.teacher.repository.TeacherContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherContactServiceImpl implements TeacherContactService {

    private final TeacherContactRepository teacherContactRepository;

    public TeacherContactServiceImpl(TeacherContactRepository teacherContactRepository) {
        this.teacherContactRepository = teacherContactRepository;
    }

    @Override
    public Long create(TeacherContactCreateRequest request) {
        TeacherContact contact = new TeacherContact();

        contact.setTeacherId(request.getTeacherId());
        contact.setContactName(request.getContactName());
        contact.setRelationship(request.getRelationship());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setIsPrimary(request.getIsPrimary());

        return teacherContactRepository.create(contact);
    }

    @Override
    public void update(Long contactId, TeacherContactUpdateRequest request) {
        TeacherContact contact = new TeacherContact();

        contact.setContactId(contactId);
        contact.setContactName(request.getContactName());
        contact.setRelationship(request.getRelationship());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setIsPrimary(request.getIsPrimary());

        teacherContactRepository.update(contact);
    }

    @Override
    public void delete(Long contactId) {
        teacherContactRepository.delete(contactId);
    }

    @Override
    public List<TeacherContactResponse> findByTeacherId(Long teacherId) {
        return teacherContactRepository.findByTeacherId(teacherId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TeacherContactResponse toResponse(TeacherContact contact) {
        TeacherContactResponse response = new TeacherContactResponse();

        response.setContactId(contact.getContactId());
        response.setTeacherId(contact.getTeacherId());
        response.setContactName(contact.getContactName());
        response.setRelationship(contact.getRelationship());
        response.setPhone(contact.getPhone());
        response.setEmail(contact.getEmail());
        response.setIsPrimary(contact.getIsPrimary());
        response.setCreatedAt(contact.getCreatedAt());
        response.setUpdatedAt(contact.getUpdatedAt());

        return response;
    }
}