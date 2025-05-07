package com.skillup.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillup.demo.model.LearningPlan;

/**
 * Repository interface for accessing and managing LearningPlan entities in the
 * database.
 * Extends JpaRepository to provide CRUD operations and other data access
 * methods.
 */
public interface LearningPlanRepository extends JpaRepository<LearningPlan, Long> {

}
