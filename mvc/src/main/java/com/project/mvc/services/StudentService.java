package com.project.mvc.services;

import com.project.mvc.data.entities.Enrollment;
import com.project.mvc.data.entities.Student;
import com.project.mvc.repositories.EnrollmentRepository;
import com.project.mvc.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;

import java.util.UUID;
import java.util.List;


@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major);
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public Student getStudent(UUID studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE, readOnly = false)
    public void updateStudentMajor(UUID studentId, String newMajor) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        student.setMajor(newMajor);
        studentRepository.save(student);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, isolation = Isolation.READ_COMMITTED)
    public void deleteStudent(UUID studentId) {
        // Retrieve the student by ID
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        // Delete all enrollments related to the student
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        enrollmentRepository.deleteAll(enrollments);

        // Delete the student
        try {
            studentRepository.deleteById(studentId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete student with ID: " + studentId, e);
        }
    }

}
