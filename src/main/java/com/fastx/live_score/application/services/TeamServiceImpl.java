package com.fastx.live_score.application.services;

import com.fastx.live_score.domain.models.Player;
import com.fastx.live_score.domain.models.Team;
import com.fastx.live_score.domain.in.TeamService;
import com.fastx.live_score.adapter.admin.request.TeamRequest;
import com.fastx.live_score.infra.db.entities.PlayerEntity;
import com.fastx.live_score.infra.db.entities.TeamEntity;
import com.fastx.live_score.application.mapper.PlayerMapper;
import com.fastx.live_score.application.mapper.TeamMapper;
import com.fastx.live_score.infra.db.jpaRepository.PlayerRepository;
import com.fastx.live_score.infra.db.jpaRepository.TeamRepository;
import com.fastx.live_score.infra.db.jpaRepository.TournamentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TournamentJpaRepository tournamentJpaRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           TournamentJpaRepository tournamentJpaRepository,
                           PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.tournamentJpaRepository = tournamentJpaRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void saveTeams(List<TeamRequest> requestList) {
        List<TeamEntity> teams = requestList.stream()
                .peek(this::validate)
                .map(this::buildTeamEntity)
                .collect(Collectors.toList());
        teamRepository.saveAll(teams);
    }

    @Override
    public void updateTeam(Long teamId, TeamRequest request) {
        TeamEntity teamEntity = teamRepository.findById(teamId).orElseThrow();
        teamRepository.save(buildTeamEntity(teamEntity, request));
    }

    @Override
    public Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) throw new IllegalArgumentException("Invalid team ID");

        return teamRepository.findById(teamId)
                .map(TeamMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Team not found with ID: " + teamId));
    }

    @Override
    public List<Team> listTeams(String q) {
        List<TeamEntity> teams;
        if (q == null || q.isEmpty()) {
            teams = teamRepository.findAll();
        } else
            teams = teamRepository.findByNameContainingIgnoreCase(q);
        return teams.stream()
                .map(TeamMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void validate(TeamRequest request) {
        if (request == null) throw new IllegalArgumentException("Team request cannot be null.");
        if (!StringUtils.hasText(request.getName())) throw new IllegalArgumentException("Team name is required.");
        if (!StringUtils.hasText(request.getShortCode())) throw new IllegalArgumentException("Short code is required.");
    }


    @Override
    public void deleteTeam(Long teamId) {
        if (teamId == null || teamId <= 0) throw new IllegalArgumentException("Invalid team ID");
        if (!teamRepository.existsById(teamId)) {
            throw new NoSuchElementException("Team not found with ID: " + teamId);
        }
        teamRepository.deleteById(teamId);
    }

    @Override
    public List<Player> getAllPlayerFromTeam(Long teamId) {
        TeamEntity teamEntity = teamRepository.findById(teamId).orElseThrow();
        return teamEntity.getPlayers().stream().map(PlayerMapper::toPlayer).toList();
    }

    private TeamEntity buildTeamEntity(TeamRequest request) {
        return buildTeamEntity(new TeamEntity(), request);
    }

    private TeamEntity buildTeamEntity(TeamEntity entity, TeamRequest request) {
        Optional.ofNullable(request.getName()).ifPresent(entity::setName);
        Optional.ofNullable(request.getShortCode()).ifPresent(entity::setShortCode);
        Optional.ofNullable(request.getLogoUrl()).ifPresent(entity::setLogoUrl);
        Optional.ofNullable(request.getCoach()).ifPresent(entity::setCoach);

        if(!request.getPlayers().isEmpty()){
            Set<Long> uniquePlayerIds = new HashSet<>(request.getPlayers());
            List<PlayerEntity> allById = playerRepository.findAllById(uniquePlayerIds);
            entity.setPlayers(allById);
            if (request.getCaptainId() != null) {
                playerRepository.findById(request.getCaptainId())
                        .ifPresent(entity::setCaptain);
            }
        }
        return entity;
    }

}
