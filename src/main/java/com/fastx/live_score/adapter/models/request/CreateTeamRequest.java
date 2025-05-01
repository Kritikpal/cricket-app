package com.fastx.live_score.adapter.models.request;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamRequest {

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "logoUrl")
    private String logoUrl;

    @CsvBindByName(column = "coach")
    private String coach;

    @CsvBindByName(column = "shortCode")
    private String shortCode;

    @CsvBindByName(column = "captainId")
    private Long captainId;

    private List<Long> playerIds;

}
