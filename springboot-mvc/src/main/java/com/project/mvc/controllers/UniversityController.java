package com.project.mvc.controllers;

import com.project.mvc.data.entities.Course;
import com.project.mvc.data.entities.Student;
import com.project.mvc.services.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/university")
public class UniversityController {

    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    // Enroll a student in a course
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollStudentInCourse(@RequestParam UUID studentId, @RequestParam UUID courseId) {
        try {
            universityService.enrollStudentInCourse(studentId, courseId);
            return ResponseEntity.ok("Student enrolled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to enroll student: " + e.getMessage());
        }
    }

    // Remove a student's enrollment from a course
    @DeleteMapping("/enrollments")
    public ResponseEntity<String> removeStudentEnrollmentFromCourse(@RequestParam UUID studentId, @RequestParam UUID courseId) {
        try {
            universityService.removeStudentEnrollmentFromCourse(studentId, courseId);
            return ResponseEntity.ok("Student enrollment removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to remove student enrollment: " + e.getMessage());
        }
    }

    // Retrieve all students for a specific course
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudentsForCourse(@RequestParam UUID courseId) {
        try {
            List<Student> students = universityService.getStudentsForCourse(courseId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Add a new course
    @PostMapping("/courses")
    public ResponseEntity<Course> addNewCourse(@RequestParam String title) {
        try {
            Course course = universityService.addNewCourse(title);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Update course and enrollments
    @PutMapping("/courses")
    public ResponseEntity<String> updateCourseAndEnrollments(@RequestParam UUID courseId, @RequestParam String newTitle) {
        try {
            universityService.updateCourseAndEnrollments(courseId, newTitle);
            return ResponseEntity.ok("Course and enrollments updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update course and enrollments: " + e.getMessage());
        }
    }

    // Perform non-transactional operation
    @PostMapping("/non-transactional-operation")
    public ResponseEntity<String> performNonTransactionalOperation() {
        universityService.performNonTransactionalOperation();
        return ResponseEntity.ok("Non-transactional operation performed successfully");
    }
}
