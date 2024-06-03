package com.example.entity;

import javax.persistence.*;

import java.util.Date;
import java.util.UUID;

/**
 * Represents an enrollment entity in the university schema.
 */
@Entity
@Table(name = "enrollments", schema = "university")
public class Enrollment {

    @Id
    @GeneratedValue
    private UUID id; // Unique identifier for the enrollment

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student; // The student who is enrolled in the course

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course; // The course in which the student is enrolled

    private Date enrollmentDate; // Date when the student enrolled in the course
    private int grade; // Grade received by the student in the course

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
