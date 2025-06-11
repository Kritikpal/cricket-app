package com.fastx.live_score.domain.models.match;

import com.fastx.live_score.adapter.admin.response.ListPlayerRes;
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
    private List<ListPlayerRes> players;
    private String captainName;
    private Long captainId;

}
