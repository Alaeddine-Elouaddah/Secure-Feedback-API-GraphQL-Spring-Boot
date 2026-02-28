package com.example.feedback.dto.feedback;

import com.example.feedback.dto.common.PageInfoDto;
import java.util.List;

public class FeedbackPageDto {

    private List<FeedbackDto> items;
    private PageInfoDto pageInfo;

    public List<FeedbackDto> getItems() {
        return items;
    }

    public void setItems(List<FeedbackDto> items) {
        this.items = items;
    }

    public PageInfoDto getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoDto pageInfo) {
        this.pageInfo = pageInfo;
    }
}

