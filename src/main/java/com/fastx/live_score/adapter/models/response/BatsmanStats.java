package com.fastx.live_score.adapter.models.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatsmanStats {

    private Long id;

    private Player player;

    private int runs;
    private int ballsFaced;
    private int boundaries;
    private int sixes;
    private int ballsBowled;
    private int wicketsTaken;

    private boolean isOut;
    private String dismissalType;
}