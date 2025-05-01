package com.fastx.live_score.adapter.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Team {
    private Long id;
    private String name;
    private String shortCode;
    private String logoUrl;
    private String coach;
    private List<TeamPlayer> players;
    private String captainName;
    private Long captainId;


    @AllArgsConstructor
    @Data
    public static class TeamPlayer {
        private Long id;
        private String name;
    }
}
