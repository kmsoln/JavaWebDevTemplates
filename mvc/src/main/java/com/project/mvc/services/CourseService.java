package com.project.mvc.services;


import com.project.mvc.data.entities.Course;
import com.project.mvc.data.entities.Enrollment;
import com.project.mvc.repositories.CourseRepository;
import com.project.mvc.repositories.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Course createCourse(Course course) {
        // Logic to create a new course
        return courseRepository.save(course);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Course updateCourse(Course course) {
        // Logic to update an existing course
        return courseRepository.save(course);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Course getCourse(UUID courseId) {
        // Logic to retrieve a course by ID
        return courseRepository.findById(courseId).orElse(null);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Course> getAllCourses() {
        // Logic to retrieve all courses
        return courseRepository.findAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Course> getCoursesByName(String name) {
        // Logic to retrieve courses by name
        return courseRepository.findByName(name);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<Course> getCoursesByInstructor(String instructor) {
        return courseRepository.findByInstructor(instructor);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = false)
    public void updateCourseCapacity(UUID courseId, int newCapacity) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        course.setCapacity(newCapacity);
        courseRepository.save(course);
    }


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = false)
    public void deleteCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        // Delete related enrollments
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        enrollmentRepository.deleteAll(enrollments);

        // Delete the course
        try {
            courseRepository.deleteById(courseId);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to delete course with ID: " + courseId, e);
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Course> getCoursesWithCapacityGreaterThan(int capacity) {
        // Logic to retrieve courses with capacity greater than a specified value
        return courseRepository.findByCapacityGreaterThanEqual(capacity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Course> getOnlineCourses(boolean online) {
        // Logic to retrieve online courses
        return courseRepository.findByOnline(online);
    }

}
