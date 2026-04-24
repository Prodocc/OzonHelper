package com.example.OzonHelper.dto.response.fbs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetFbsPostingListResult {
    @JsonProperty("postings")
    private List<PostingDto> postings;
}
