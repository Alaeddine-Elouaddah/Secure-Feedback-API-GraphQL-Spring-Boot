package com.example.feedback.dto.feedback;

import com.example.feedback.domain.FeedbackStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateFeedbackStatusInput {

    @NotNull
    private Long feedbackId;

    @NotNull
    private FeedbackStatus status;

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }
}

