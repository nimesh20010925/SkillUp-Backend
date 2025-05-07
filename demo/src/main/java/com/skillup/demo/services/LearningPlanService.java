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

    /**
     * Creates a new learning plan and saves it to the database.
     * Sends a notification after successful creation.
     * 
     * @param plan the LearningPlan object to be created
     * @return the saved LearningPlan object
     */
    public LearningPlan createLearningPlan(LearningPlan plan) {
        LearningPlan savedPlan = repository.save(plan);
        sendNotification(savedPlan); // Simulate notification
        return savedPlan;
    }

    /**
     * Retrieves all learning plans from the database.
     * 
     * @return a list of all LearningPlan objects
     */
    public List<LearningPlan> getAllLearningPlans() {
        return repository.findAll();
    }

    /**
     * Retrieves a learning plan by its ID.
     * 
     * @param id the ID of the learning plan to retrieve
     * @return an Optional containing the LearningPlan if found, or empty if not
     *         found
     */
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
