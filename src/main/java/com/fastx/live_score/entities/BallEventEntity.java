package com.fastx.live_score.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ball_events")
@Data
public class BallEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    private int inningsNumber;
    private int overNumber;
    private int ballNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "striker_id")
    private PlayerEntity batsman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "non_striker_id")
    private PlayerEntity nonStriker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bowler_id")
    private PlayerEntity bowler;

    private int runs;
    private boolean isBoundary;
    private boolean isWicket;
    private String wicketType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dismissedPlayer")
    private PlayerEntity dismissed;


    private String extraType;
    private int extraRuns;

    private String commentary;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
