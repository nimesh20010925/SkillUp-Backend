package com.skillup.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.demo.model.LearningPlan;
import com.skillup.demo.repository.LearningPlanRepository;

/**
 * Service class for managing business logic related to LearningPlan entities.
 * Interacts with the LearningPlanRepository to perform CRUD operations and
 * additional functionality.
 */
@Service
public class LearningPlanService {

    /**
     * Repository for accessing LearningPlan data in the database.
     */
    @Autowired
    private LearningPlanRepository repository;

    public LearningPlan createLearningPlan(LearningPlan plan) {
        LearningPlan savedPlan = repository.save(plan);
        sendNotification(savedPlan); // Simulate notification
        return savedPlan;
    }

    public List<LearningPlan> getAllLearningPlans() {
        return repository.findAll();
    }

    public Optional<LearningPlan> getLearningPlanById(Long id) {
        return repository.findById(id);
    }

    public LearningPlan updateLearningPlan(Long id, LearningPlan plan) {
        Optional<LearningPlan> existingPlan = repository.findById(id);
        if (existingPlan.isPresent()) {
            LearningPlan updatedPlan = existingPlan.get();
            updatedPlan.setTitle(plan.getTitle());
            updatedPlan.setDescription(plan.getDescription());
            updatedPlan.setStartDate(plan.getStartDate());
            updatedPlan.setEndDate(plan.getEndDate());
            return repository.save(updatedPlan);
        }
        return null;
    }

    public void deleteLearningPlan(Long id) {
        repository.deleteById(id);
    }

    private void sendNotification(LearningPlan plan) {
        // Simulate sending a notification (e.g., email or log)
        System.out.println("Notification: Learning Plan '" + plan.getTitle() + "' created!");
    }

}
