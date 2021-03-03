package com.example.restexample.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDo {

    @JsonProperty(value = "id")
    private int todoId;
    private int userId;
    private String title;
    private boolean completed;
}
