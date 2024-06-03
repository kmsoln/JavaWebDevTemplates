package com.example.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Represents a student entity in the university schema.
 */
@Entity
@Table(name = "students", schema = "university")
public class Student {

    @Id
    @GeneratedValue
    private UUID id; // Unique identifier for the student

    private String name; // Name of the student

    @NotBlank
    @Email
    private String email; // Email address of the student

    @NotBlank
    private String major; // Major field of study of the student

    @NotNull
    private int year; // Year of study of the student

    @NotNull
    private boolean international; // Indicates if the student is an international student

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Enrollment> enrollments; // List of courses the student is enrolled in

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isInternational() {
        return international;
    }

    public void setInternational(boolean international) {
        this.international = international;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
}
