package ru.practicum.mapper;

import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.Request;

import java.time.format.DateTimeFormatter;

public final class RequestMapper {

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private RequestMapper() {}

    public static ParticipationRequestDto toRequestDto(Request request) {
         return ParticipationRequestDto.builder()
                 .id(request.getId())
                 .requester(request.getRequester())
                 .created(request.getCreated().format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS)))
                 .event(request.getEvent())
                 .status(request.getStatus())
                 .build();
    }

}
