package com.fastx.live_score.adapter.models.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BowlerStats {

    private Long id;

    private Player player;

    private int oversBowled;
    private int runsConceded;
    private int wicketsTaken;
    private int wides;
    private int noBalls;

    private double economyRate;
}