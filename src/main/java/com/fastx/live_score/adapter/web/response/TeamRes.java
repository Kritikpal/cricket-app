package com.fastx.live_score.adapter.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TeamRes {
    private Long id;
    private String name;
    private String shortCode;
    private String logoUrl;
    private String coach;
    private List<ShortPlayerRes> players;
    private String captainName;
    private Long captainId;

}
