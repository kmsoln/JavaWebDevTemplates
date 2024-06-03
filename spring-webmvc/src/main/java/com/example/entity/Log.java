package com.example.entity;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a log entity in the university schema.
 */
@Entity
@Table(name = "logs", schema = "university")
public class Log {

    @Id
    @GeneratedValue
    private UUID id; // Unique identifier for the log entry

    private String action; // Action that was logged
    private String outcome; // Outcome or result of the action
    private LocalDateTime timestamp; // Timestamp when the action occurred

    // Constructors, getters, and setters
    public Log() {}

    public Log(String action, String outcome, LocalDateTime timestamp) {
        this.action = action;
        this.outcome = outcome;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
