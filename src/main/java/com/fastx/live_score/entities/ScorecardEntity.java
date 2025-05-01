package com.fastx.live_score.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "scorecards")
@Data
public class ScorecardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @OneToMany(mappedBy = "scorecard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BatsmanStatsEntity> batsmanStats;

    @OneToMany(mappedBy = "scorecard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BowlerStatsEntity> bowlerStats;

    private int totalRuns;
    private int totalWickets;
    private int totalExtras;

    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}