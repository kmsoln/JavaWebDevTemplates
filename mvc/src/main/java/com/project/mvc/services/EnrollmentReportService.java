package com.project.mvc.services;

import com.project.mvc.data.entities.Enrollment;
import com.project.mvc.repositories.EnrollmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EnrollmentReportService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentReportService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStudentId(UUID studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByCourseId(UUID courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByEnrollmentDate(Date enrollmentDate) {
        return enrollmentRepository.findByEnrollmentDate(enrollmentDate);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByGrade(int grade) {
        return enrollmentRepository.findByGrade(grade);
    }

    @Transactional(readOnly = true)
    public Page<Enrollment> getEnrollmentsByStudentId(UUID studentId, Pageable pageable) {
        return enrollmentRepository.findByStudentId(studentId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Enrollment> getEnrollmentsByCourseId(UUID courseId, Pageable pageable) {
        return enrollmentRepository.findByCourseId(courseId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Enrollment> getEnrollmentsByEnrollmentDate(Date enrollmentDate, Pageable pageable) {
        return enrollmentRepository.findByEnrollmentDate(enrollmentDate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Enrollment> getEnrollmentsByGrade(int grade, Pageable pageable) {
        return enrollmentRepository.findByGrade(grade, pageable);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStudentId(UUID studentId, Sort sort) {
        return enrollmentRepository.findByStudentId(studentId, sort);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByCourseId(UUID courseId, Sort sort) {
        return enrollmentRepository.findByCourseId(courseId, sort);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByEnrollmentDate(Date enrollmentDate, Sort sort) {
        return enrollmentRepository.findByEnrollmentDate(enrollmentDate, sort);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByGrade(int grade, Sort sort) {
        return enrollmentRepository.findByGrade(grade, sort);
    }
}
