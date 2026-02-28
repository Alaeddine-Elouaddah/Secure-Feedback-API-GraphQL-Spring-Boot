package com.example.feedback.service;

import com.example.feedback.domain.Feedback;
import com.example.feedback.domain.FeedbackCategory;
import com.example.feedback.domain.FeedbackStatus;
import com.example.feedback.domain.FeedbackResponse;
import com.example.feedback.domain.User;
import com.example.feedback.dto.feedback.AddFeedbackResponseInput;
import com.example.feedback.dto.feedback.CreateFeedbackInput;
import com.example.feedback.dto.feedback.FeedbackFilter;
import com.example.feedback.dto.feedback.FeedbackPageDto;
import com.example.feedback.dto.feedback.UpdateFeedbackStatusInput;
import com.example.feedback.exception.NotFoundException;
import com.example.feedback.mapper.FeedbackMapper;
import com.example.feedback.repository.FeedbackRepository;
import com.example.feedback.repository.FeedbackResponseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackResponseRepository feedbackResponseRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           FeedbackResponseRepository feedbackResponseRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackResponseRepository = feedbackResponseRepository;
    }

    @Transactional
    public Feedback createFeedback(CreateFeedbackInput input, User author) {
        Feedback feedback = new Feedback();
        feedback.setTitle(input.getTitle());
        feedback.setMessage(input.getMessage());
        FeedbackCategory category = input.getCategory() != null ? input.getCategory() : FeedbackCategory.OTHER;
        feedback.setCategory(category);
        feedback.setRating(input.getRating());
        feedback.setAuthor(author);
        return feedbackRepository.save(feedback);
    }

    @Transactional(readOnly = true)
    public FeedbackPageDto findAll(int page, int size, FeedbackFilter filter) {
        Pageable pageable = PageRequest.of(page, size);
        FeedbackStatus status = filter != null ? filter.getStatus() : null;
        FeedbackCategory category = filter != null ? filter.getCategory() : null;
        String searchText = filter != null ? filter.getSearchText() : null;

        Page<Feedback> result = feedbackRepository.search(status, category, searchText, pageable);
        return FeedbackMapper.toFeedbackPageDto(result);
    }

    @Transactional(readOnly = true)
    public FeedbackPageDto findByAuthor(User author, int page, int size, FeedbackFilter filter) {
        Pageable pageable = PageRequest.of(page, size);
        FeedbackStatus status = filter != null ? filter.getStatus() : null;
        FeedbackCategory category = filter != null ? filter.getCategory() : null;
        String searchText = filter != null ? filter.getSearchText() : null;

        Page<Feedback> result = feedbackRepository.searchByAuthor(author, status, category, searchText, pageable);
        return FeedbackMapper.toFeedbackPageDto(result);
    }

    @Transactional
    public Feedback updateStatus(UpdateFeedbackStatusInput input) {
        Feedback feedback = feedbackRepository.findById(input.getFeedbackId())
                .orElseThrow(() -> new NotFoundException("Feedback not found"));
        feedback.setStatus(input.getStatus());
        return feedbackRepository.save(feedback);
    }

    @Transactional
    public FeedbackResponse addResponse(AddFeedbackResponseInput input, User author) {
        Feedback feedback = feedbackRepository.findById(input.getFeedbackId())
                .orElseThrow(() -> new NotFoundException("Feedback not found"));

        FeedbackResponse response = new FeedbackResponse();
        response.setFeedback(feedback);
        response.setAuthor(author);
        response.setMessage(input.getMessage());

        return feedbackResponseRepository.save(response);
    }

    @Transactional(readOnly = true)
    public Feedback findById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Feedback not found"));
    }
}

