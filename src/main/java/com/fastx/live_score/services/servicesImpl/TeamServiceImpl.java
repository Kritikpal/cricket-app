package com.fastx.live_score.services.servicesImpl;

import com.fastx.live_score.adapter.models.request.CreateTeamRequest;
import com.fastx.live_score.adapter.models.response.Team;
import com.fastx.live_score.entities.PlayerEntity;
import com.fastx.live_score.entities.TeamEntity;
import com.fastx.live_score.entities.TournamentEntity;
import com.fastx.live_score.repository.PlayerRepository;
import com.fastx.live_score.repository.TeamRepository;
import com.fastx.live_score.repository.TournamentRepository;
import com.fastx.live_score.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void createTeam(List<CreateTeamRequest> request) {

        List<TeamEntity> teamEntities = request.stream().map(
                player -> getTeamEntity(new TeamEntity(), player)
        ).toList();
        teamRepository.saveAll(teamEntities);
    }


    @Override
    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTeam(Long teamId, CreateTeamRequest request) {
        Optional<TeamEntity> optionalTeam = teamRepository.findById(teamId);
        if (optionalTeam.isEmpty()) return;
        TeamEntity teamEntity = getTeamEntity(optionalTeam.get(), request);
        TeamEntity updatedEntity = teamRepository.save(teamEntity);
        mapToResponse(updatedEntity);
    }

    private TeamEntity getTeamEntity(TeamEntity entity, CreateTeamRequest request) {
        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getShortCode() != null) {
            entity.setShortCode(request.getShortCode());
        }
        if (request.getLogoUrl() != null) {
            entity.setLogoUrl(request.getLogoUrl());
        }
        if (request.getCoach() != null) {
            entity.setCoach(request.getCoach());
        }

        if (request.getPlayerIds() != null) {
            List<PlayerEntity> players = playerRepository.findAllById(request.getPlayerIds());
            entity.setPlayers(players);
            for (PlayerEntity player : players) {
                if (player.getTeams() == null) {
                    player.setTeams(List.of(entity));
                } else {
                    player.getTeams().add(entity);
                }
            }
        }
        if (request.getCaptainId() != null) {
            playerRepository.findById(request.getCaptainId()).ifPresent(entity::setCaptain);
        }
        return entity;
    }

    @Override
    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
    }

    @Override
    public List<Team> getTeamsByTournamentId(Long tournamentId) {

        TournamentEntity tournamentEntity = tournamentRepository.findById(tournamentId).orElseThrow();
        List<TeamEntity> participatingTeams = tournamentEntity.getParticipatingTeams();
        return participatingTeams.stream().map(this::mapToResponse).toList();
    }

    private Team mapToResponse(TeamEntity entity) {
        Team team = Team.builder()
                .id(entity.getId())
                .name(entity.getName())
                .shortCode(entity.getShortCode())
                .logoUrl(entity.getLogoUrl())
                .coach(entity.getCoach())
                .captainId(entity.getCaptain() != null ? entity.getCaptain().getId() : null)
                .captainName(entity.getCaptain() != null ? entity.getCaptain().getFullName() : null)
                .build();
        if (entity.getPlayers() != null) {
            team.setPlayers(entity.getPlayers().stream()
                    .map(player -> new Team.TeamPlayer(player.getId(), player.getFullName()))
                    .collect(Collectors.toList()));
        }

        return team;
    }

}
