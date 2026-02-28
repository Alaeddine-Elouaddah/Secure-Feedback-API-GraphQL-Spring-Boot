package com.example.feedback.repository;

import com.example.feedback.domain.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackResponseRepository extends JpaRepository<FeedbackResponse, Long> {
}

