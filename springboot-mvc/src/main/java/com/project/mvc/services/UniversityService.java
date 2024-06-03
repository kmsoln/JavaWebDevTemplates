package com.project.mvc.services;

import com.project.mvc.data.entities.Course;
import com.project.mvc.data.entities.Enrollment;
import com.project.mvc.data.entities.Log;
import com.project.mvc.data.entities.Student;
import com.project.mvc.repositories.CourseRepository;
import com.project.mvc.repositories.EnrollmentRepository;
import com.project.mvc.repositories.LogRepository;
import com.project.mvc.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UniversityService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LogRepository logRepository;

    @Autowired
    public UniversityService(StudentRepository studentRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, LogRepository logRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.logRepository = logRepository;
    }

    /**
     * Scenario 1: Enrolling a Student in a Course
     * Method: enrollStudentInCourse
     * <p>
     * When a student is enrolled in a course, it is critical to ensure that the enrollment operation is completely isolated to maintain data consistency.
     * This method runs with Propagation.REQUIRED and Isolation.SERIALIZABLE, ensuring that no other transactions can interfere during the enrollment process.
     * Additionally, we need to log this enrollment operation separately, regardless of whether the main transaction succeeds or fails.
     * For this, we call logAction with Propagation.REQUIRES_NEW, which suspends the main transaction and starts a new one for logging.
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void enrollStudentInCourse(UUID studentId, UUID courseId) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            int capacity = course.getCapacity();
            if (capacity <= 0) throw new RuntimeException("Course is already full.");

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);

            enrollmentRepository.save(enrollment);

            // Log successful enrollment
            logAction("Enroll Student", "Success");
        } catch (Exception e) {
            // Log failure
            logAction("Enroll Student", "Failure: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Scenario 2: Removing a Student Enrollment from a Course
     * Method: removeStudentEnrollmentFromCourse
     * <p>
     * This method ensures the removal of a student's enrollment in a course.
     * It uses Propagation.MANDATORY to ensure that this method is always executed within an existing transaction.
     * If called outside of a transaction, an exception will be thrown.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeStudentEnrollmentFromCourse(UUID studentId, UUID courseId) {
        try {

            studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            List<Enrollment> studentEnrollments = enrollmentRepository.findByStudentId(studentId);
            if (studentEnrollments.isEmpty()) {
                throw new IllegalStateException("No enrollments found for student with ID " + studentId);
            }

            List<Enrollment> filteredEnrollmentsForCourse = studentEnrollments.stream()
                    .filter(enrollment -> enrollment.getCourse().getId().equals(courseId))
                    .toList();

            if (filteredEnrollmentsForCourse.isEmpty()) {
                throw new IllegalStateException("Student with ID " + studentId + " is not enrolled in course with ID " + courseId);
            }

            enrollmentRepository.deleteAll(filteredEnrollmentsForCourse);

            // Log successful deletion
            logAction("Delete Enrollment", "Success");
        } catch (Exception e) {
            logAction("Delete Enrollment", "Failure: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Scenario 3: Retrieving Students for a Course
     * Method: getStudentsForCourse
     * <p>
     * To fetch all students enrolled in a specific course, this method uses Propagation.REQUIRED and Isolation.READ_COMMITTED.
     * This ensures that the method either joins an existing transaction or creates a new one, reading only committed data to prevent dirty reads
     * while allowing a balance between consistency and performance.
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public List<Student> getStudentsForCourse(UUID courseId) {
        try {
            List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

            List<Student> students = new ArrayList<>();
            for (Enrollment enrollment : enrollments) {
                students.add(enrollment.getStudent());
            }

            logAction("Retrieve Students for Course", "Success");
            return students;
        } catch (Exception e) {
            logAction("Retrieve Students for Course", "Failure: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Scenario 4: Adding a New Course
     * Method: addNewCourse
     * <p>
     * Adding a new course is a simpler operation that does not require transactional guarantees.
     * Thus, this method runs with Propagation.NOT_SUPPORTED, suspending any existing transactions and executing non-transactionally.
     * This avoids unnecessary transactional overhead for a straightforward insert operation.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Course addNewCourse(String title) {
        try {
            Course course = new Course();
            course.setName(title);
            Course savedCourse = courseRepository.save(course);
            logAction("Add New Course", "Success");
            return savedCourse;
        } catch (Exception e) {
            logAction("Add New Course", "Failure: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Scenario 5: Updating Course and Enrollments with Nested Transactions
     * Method: updateCourseAndEnrollments
     * <p>
     * When updating course details along with related enrollments, we need to ensure consistency within nested transactions.
     * This method uses Propagation.REQUIRED and Isolation.REPEATABLE_READ, ensuring it runs in the context of an existing transaction or starts a new one,
     * and it prevents dirty and non-repeatable reads.
     * Within this method, updateEnrollments is called with Propagation.NESTED, allowing it to run within a nested transaction.
     * This setup enables partial rollbacks within the parent transaction, ensuring robust error handling and data integrity.
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void updateCourseAndEnrollments(UUID courseId, String newTitle) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            course.setName(newTitle);
            courseRepository.save(course);

            // Nested transaction to update enrollments
            updateEnrollments(courseId);
            logAction("Update Course and Enrollments", "Success");
        } catch (Exception e) {
            logAction("Update Course and Enrollments", "Failure: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Scenario 6: Updating Enrollments
     * Method: updateEnrollments
     * <p>
     * This method is responsible for updating enrollment records.
     * Running with Propagation.NESTED and Isolation.REPEATABLE_READ, it ensures that these operations are part of a nested transaction,
     * providing the ability to roll back changes independently of the main transaction if necessary.
     * The isolation level ensures consistent data reads during the transaction.
     */
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.REPEATABLE_READ)
    public void updateEnrollments(UUID courseId) {
        try {
            List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
            for (Enrollment enrollment : enrollments) {
                // Update enrollment in some way
                // For example, adjust grades (this is just a placeholder)
                enrollment.setGrade(enrollment.getGrade() + 1);
                enrollmentRepository.save(enrollment);
            }
            logAction("Update Enrollments", "Success");
        } catch (Exception e) {
            logAction("Update Enrollments", "Failure: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Scenario 7: Non-Transactional Operation
     * Method: performNonTransactionalOperation
     * <p>
     * For operations that do not require transactional context, such as non-critical tasks or logging that doesn't need transactional guarantees,
     * this method runs non-transactionally. This avoids the overhead of transaction management and is used for operations where data consistency is not a concern.
     */
    public void performNonTransactionalOperation() {
        // This operation runs without any transactional context
        logAction("Perform Non-Transactional Operation", "Success");
    }

    /**
     * Log an action
     * <p>
     * This method handles logging actions with Propagation.REQUIRES_NEW, ensuring that logging occurs in a new transaction.
     * This makes the logging operation independent of the outcome of the main transaction.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String action, String outcome) {
        Log log = new Log(action, outcome, LocalDateTime.now());
        logRepository.save(log);
    }
}
