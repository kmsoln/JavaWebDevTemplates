package com.project.mvc.services;


import com.project.mvc.data.entities.Course;
import com.project.mvc.data.entities.Enrollment;
import com.project.mvc.data.entities.Student;
import com.project.mvc.repositories.CourseRepository;
import com.project.mvc.repositories.EnrollmentRepository;
import com.project.mvc.repositories.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
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
        Student student = getStudentIfExists(studentId);
        Course course = getCourseIfExists(courseId);

        if (course.getCapacity() > 0) {
            validateStudentEnrollment(studentId, courseId);

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollmentRepository.save(enrollment);

            course.setCapacity(course.getCapacity() - 1);
            courseRepository.save(course);
        } else {
            throw new IllegalStateException("Course has reached maximum capacity");
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void removeStudentEnrollmentFromCourse(UUID studentId, UUID courseId) {
        Student student = getStudentIfExists(studentId);
        Course course = getCourseIfExists(courseId);

        List<Enrollment> studentEnrollments = enrollmentRepository.findByStudentId(studentId);
        validateStudentEnrollmentForCourse(studentId, courseId, studentEnrollments);

        List<Enrollment> courseEnrollments = filterEnrollmentsForCourse(courseId, studentEnrollments);
        enrollmentRepository.deleteAll(courseEnrollments);
    }

    private Student getStudentIfExists(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with ID " + studentId + " not found"));
    }

    private Course getCourseIfExists(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course with ID " + courseId + " not found"));
    }

    private void validateStudentEnrollment(UUID studentId, UUID courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalStateException("Student is already enrolled in the course");
        }
    }

    private void validateStudentEnrollmentForCourse(UUID studentId, UUID courseId, List<Enrollment> studentEnrollments) {
        if (studentEnrollments.isEmpty()) {
            throw new IllegalStateException("No enrollments found for student with ID " + studentId);
        }

        if (filterEnrollmentsForCourse(courseId, studentEnrollments).isEmpty()) {
            throw new IllegalStateException("Student with ID " + studentId + " is not enrolled in course with ID " + courseId);
        }
    }

    private List<Enrollment> filterEnrollmentsForCourse(UUID courseId, List<Enrollment> studentEnrollments) {
        return studentEnrollments.stream()
                .filter(enrollment -> enrollment.getCourse().getId().equals(courseId))
                .collect(Collectors.toList());
    }
}
