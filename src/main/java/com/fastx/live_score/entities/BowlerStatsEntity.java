package com.fastx.live_score.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bowler_stats")
@Data
public class BowlerStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scorecard_id", nullable = false)
    private ScorecardEntity scorecard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player;

    private int oversBowled;
    private int runsConceded;
    private int wicketsTaken;
    private int wides;
    private int noBalls;

    private double economyRate;
}