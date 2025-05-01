package com.fastx.live_score.adapter.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class CreateTournamentRequest {

    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
    private String location;

    private List<Long> participatingTeamIds;

}
