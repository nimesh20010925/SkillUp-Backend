package com.skillup.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.demo.model.LearningPlan;
import com.skillup.demo.services.LearningPlanService;

import jakarta.validation.Valid;

/**
 * REST controller for managing LearningPlan resources.
 * Provides endpoints for creating, retrieving, updating, and deleting learning
 * plans.
 */
@RestController
@RequestMapping("/api/learning-plans")
public class LearningPlanController {

    @Autowired
    private LearningPlanService service;

    /**
     * Creates a new learning plan.
     * 
     * 
     */

    @PostMapping
    public ResponseEntity<LearningPlan> createLearningPlan(@Valid @RequestBody LearningPlan plan) {
        LearningPlan createdPlan = service.createLearningPlan(plan);
        return ResponseEntity.ok(createdPlan);
    }

    /**
     * Retrieves a list of all learning plans.
     * 
     * @return ResponseEntity containing the list of LearningPlans and HTTP status
     *         200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<LearningPlan>> getAllLearningPlans() {
        return ResponseEntity.ok(service.getAllLearningPlans());
    }

    /**
     * Retrieves a learning plan by its ID.
     * 
     * @param id the ID of the learning plan to retrieve
     * @return ResponseEntity containing the LearningPlan and HTTP status 200 (OK)
     *         if
     *         found, or HTTP status 404 (Not Found) if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<LearningPlan> getLearningPlanById(@PathVariable Long id) {
        Optional<LearningPlan> plan = service.getLearningPlanById(id);
        return plan.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningPlan> updateLearningPlan(@PathVariable Long id,
            @Valid @RequestBody LearningPlan plan) {
        LearningPlan updatedPlan = service.updateLearningPlan(id, plan);
        if (updatedPlan != null) {
            return ResponseEntity.ok(updatedPlan);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningPlan(@PathVariable Long id) {
        service.deleteLearningPlan(id);
        return ResponseEntity.noContent().build();
    }

}
