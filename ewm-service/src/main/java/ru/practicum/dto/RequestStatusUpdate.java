package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestStatusUpdate {

    private List<Long> requestIds;

    private String status;
}
