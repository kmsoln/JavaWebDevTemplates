package com.project.mvc.repositories;

import com.project.mvc.data.entities.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    List<Enrollment> findByStudentId(UUID studentId);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    List<Enrollment> findByCourseId(UUID courseId);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    List<Enrollment> findByEnrollmentDate(Date enrollmentDate);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    List<Enrollment> findByGrade(int grade);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    Page<Enrollment> findByStudentId(UUID studentId, Pageable pageable);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    Page<Enrollment> findByCourseId(UUID courseId, Pageable pageable);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    Page<Enrollment> findByEnrollmentDate(Date enrollmentDate, Pageable pageable);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    Page<Enrollment> findByGrade(int grade, Pageable pageable);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    List<Enrollment> findByStudentId(UUID studentId, Sort sort);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    List<Enrollment> findByCourseId(UUID courseId, Sort sort);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    List<Enrollment> findByEnrollmentDate(Date enrollmentDate, Sort sort);

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    List<Enrollment> findByGrade(int grade, Sort sort);
}
