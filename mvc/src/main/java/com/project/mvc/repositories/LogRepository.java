package com.project.mvc.repositories;

import com.project.mvc.data.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, UUID> {}
