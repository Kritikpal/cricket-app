package com.fastx.live_score.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
@Data
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String shortCode;
    private String logoUrl;
    private String coach;

    @ManyToMany(mappedBy = "teams", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<PlayerEntity> players = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_id")
    private PlayerEntity captain;

    @ManyToMany(mappedBy = "participatingTeams")
    private List<TournamentEntity> tournaments = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
