package com.fastx.live_score.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "batsman_stats")
@Data
public class BatsmanStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scorecard_id", nullable = false)
    private ScorecardEntity scorecard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player;

    private int runs;
    private int ballsFaced;
    private int boundaries;
    private int sixes;
    private int ballsBowled;
    private int wicketsTaken;

    private boolean isOut;
    private String dismissalType;


}