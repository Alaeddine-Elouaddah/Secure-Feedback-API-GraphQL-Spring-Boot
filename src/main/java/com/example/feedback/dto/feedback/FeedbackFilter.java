package com.example.feedback.dto.feedback;

import com.example.feedback.domain.FeedbackCategory;
import com.example.feedback.domain.FeedbackStatus;

public class FeedbackFilter {

    private FeedbackStatus status;
    private FeedbackCategory category;
    private String searchText;

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }

    public FeedbackCategory getCategory() {
        return category;
    }

    public void setCategory(FeedbackCategory category) {
        this.category = category;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}

