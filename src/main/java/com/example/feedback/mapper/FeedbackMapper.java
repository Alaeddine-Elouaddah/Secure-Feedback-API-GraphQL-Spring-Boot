package com.example.feedback.mapper;

import com.example.feedback.domain.Feedback;
import com.example.feedback.domain.FeedbackResponse;
import com.example.feedback.domain.User;
import com.example.feedback.dto.common.PageInfoDto;
import com.example.feedback.dto.feedback.FeedbackDto;
import com.example.feedback.dto.feedback.FeedbackPageDto;
import com.example.feedback.dto.feedback.FeedbackResponseDto;
import com.example.feedback.dto.user.UserDto;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.domain.Page;

public final class FeedbackMapper {

    private static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    private FeedbackMapper() {
    }

    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        if (user.getCreatedAt() != null) {
            dto.setCreatedAt(ISO_FORMATTER.format(user.getCreatedAt()));
        }
        if (user.getUpdatedAt() != null) {
            dto.setUpdatedAt(ISO_FORMATTER.format(user.getUpdatedAt()));
        }
        return dto;
    }

    public static FeedbackDto toFeedbackDto(Feedback feedback) {
        if (feedback == null) {
            return null;
        }
        FeedbackDto dto = new FeedbackDto();
        dto.setId(feedback.getId());
        dto.setTitle(feedback.getTitle());
        dto.setMessage(feedback.getMessage());
        dto.setStatus(feedback.getStatus());
        dto.setCategory(feedback.getCategory());
        dto.setRating(feedback.getRating());
        dto.setAuthor(toUserDto(feedback.getAuthor()));
        if (feedback.getCreatedAt() != null) {
            dto.setCreatedAt(ISO_FORMATTER.format(feedback.getCreatedAt()));
        }
        if (feedback.getUpdatedAt() != null) {
            dto.setUpdatedAt(ISO_FORMATTER.format(feedback.getUpdatedAt()));
        }
        return dto;
    }

    public static FeedbackResponseDto toFeedbackResponseDto(FeedbackResponse response) {
        if (response == null) {
            return null;
        }
        FeedbackResponseDto dto = new FeedbackResponseDto();
        dto.setId(response.getId());
        dto.setMessage(response.getMessage());
        dto.setAuthor(toUserDto(response.getAuthor()));
        if (response.getFeedback() != null) {
            dto.setFeedbackId(response.getFeedback().getId());
        }
        if (response.getCreatedAt() != null) {
            dto.setCreatedAt(ISO_FORMATTER.format(response.getCreatedAt()));
        }
        return dto;
    }

    public static FeedbackPageDto toFeedbackPageDto(Page<Feedback> page) {
        FeedbackPageDto dto = new FeedbackPageDto();
        List<FeedbackDto> items = page.getContent().stream()
                .map(FeedbackMapper::toFeedbackDto)
                .toList();
        dto.setItems(items);

        PageInfoDto pageInfo = new PageInfoDto();
        pageInfo.setPage(page.getNumber());
        pageInfo.setSize(page.getSize());
        pageInfo.setTotalElements((int) page.getTotalElements());
        pageInfo.setTotalPages(page.getTotalPages());

        dto.setPageInfo(pageInfo);
        return dto;
    }
}

