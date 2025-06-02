package com.fastx.live_score.adapter.admin.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRequest {


    @NotNull(message = "Provide a name for the tournament")
    private String name;

    @NotNull(message = "Provide a description for the tournament")
    private String description;

    @NotNull(message = "Provide a start date for the tournament")
    private long startDate;

    @NotNull(message = "Provide an end date for the tournament")
    private long endDate;

    private String location;

    private String logoUrl;

    private List<Long> participatingTeamIds;

}
