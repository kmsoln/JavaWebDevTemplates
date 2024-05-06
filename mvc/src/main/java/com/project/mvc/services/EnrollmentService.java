package com.project.mvc.services;


import com.project.mvc.data.entities.Course;
import com.project.mvc.data.entities.Enrollment;
import com.project.mvc.data.entities.Student;
import com.project.mvc.repositories.CourseRepository;
import com.project.mvc.repositories.EnrollmentRepository;
import com.project.mvc.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class EnrollmentService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public EnrollmentService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Enrollment createEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Enrollment updateEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteEnrollment(UUID enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }


    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Page<Enrollment> getEnrollmentsByCourse(UUID courseId, Pageable pageable) {
        return enrollmentRepository.findByCourseId(courseId, pageable);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE, readOnly = false)
    public void enrollStudentToCourse(UUID studentId, UUID courseId) {
        // Retrieve student and course
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Check if course has available capacity
        if (course.getCapacity() > 0) {
            // Perform enrollment
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollmentRepository.save(enrollment);

            // Update course capacity
            course.setCapacity(course.getCapacity() - 1);
            courseRepository.save(course);
        } else {
            throw new IllegalStateException("Course has reached maximum capacity");
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void removeStudentEnrollmentFromCourse(UUID studentId, UUID courseId) {
        // Retrieve all enrollments for the specified student
        List<Enrollment> studentEnrollments = enrollmentRepository.findByStudentId(studentId);

        // Filter enrollments for the specified course
        List<Enrollment> courseEnrollments = studentEnrollments.stream()
                .filter(enrollment -> enrollment.getCourse().getId().equals(courseId))
                .collect(Collectors.toList());

        // Delete the filtered enrollments
        enrollmentRepository.deleteAll(courseEnrollments);
    }
}
