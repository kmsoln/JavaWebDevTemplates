package com.project.mvc.data.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "logs", schema = "university")
public class Log {
    @Id
    @GeneratedValue
    private UUID id;
    private String action;
    private String outcome;
    private LocalDateTime timestamp;

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
