package com.skillup.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillup.demo.model.LearningPlan;

public interface LearningPlanRepository extends JpaRepository<LearningPlan, Long> {

}
