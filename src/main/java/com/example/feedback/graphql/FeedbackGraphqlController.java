package com.example.feedback.graphql;

import com.example.feedback.domain.User;
import com.example.feedback.dto.feedback.AddFeedbackResponseInput;
import com.example.feedback.dto.feedback.CreateFeedbackInput;
import com.example.feedback.dto.feedback.FeedbackDto;
import com.example.feedback.dto.feedback.FeedbackFilter;
import com.example.feedback.dto.feedback.FeedbackPageDto;
import com.example.feedback.dto.feedback.FeedbackResponseDto;
import com.example.feedback.dto.feedback.UpdateFeedbackStatusInput;
import com.example.feedback.dto.user.UserDto;
import com.example.feedback.mapper.FeedbackMapper;
import com.example.feedback.service.FeedbackService;
import com.example.feedback.service.UserService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class FeedbackGraphqlController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    public FeedbackGraphqlController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @QueryMapping
    public FeedbackPageDto feedbacks(@Argument int page,
                                     @Argument int size,
                                     @Argument(name = "filter", required = false) FeedbackFilter filter) {
        return feedbackService.findAll(page, size, filter);
    }

    @QueryMapping
    public FeedbackPageDto myFeedbacks(@Argument int page,
                                       @Argument int size,
                                       @Argument(name = "filter", required = false) FeedbackFilter filter) {
        User current = getCurrentUser();
        return feedbackService.findByAuthor(current, page, size, filter);
    }

    @QueryMapping
    public UserDto me() {
        User current = getCurrentUser();
        return FeedbackMapper.toUserDto(current);
    }

    @MutationMapping
    public FeedbackDto createFeedback(@Valid @Argument CreateFeedbackInput input) {
        User current = getCurrentUser();
        var created = feedbackService.createFeedback(input, current);
        return FeedbackMapper.toFeedbackDto(created);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public FeedbackDto updateFeedbackStatus(@Valid @Argument UpdateFeedbackStatusInput input) {
        var updated = feedbackService.updateStatus(input);
        return FeedbackMapper.toFeedbackDto(updated);
    }

    @MutationMapping
    public FeedbackResponseDto addFeedbackResponse(@Valid @Argument AddFeedbackResponseInput input) {
        User current = getCurrentUser();
        var response = feedbackService.addResponse(input, current);
        return FeedbackMapper.toFeedbackResponseDto(response);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException("User not authenticated");
        }
        String username = auth.getName();
        return userService.findByUsername(username);
    }
}

