package com.fastx.live_score.application.adaptors;

import com.fastx.live_score.adapter.admin.request.TournamentRequest;
import com.fastx.live_score.domain.models.match.Tournament;
import com.fastx.live_score.domain.in.TournamentService;
import com.fastx.live_score.infra.db.entities.TeamEntity;
import com.fastx.live_score.infra.db.entities.TournamentEntity;
import com.fastx.live_score.application.mapper.TournamentMapper;
import com.fastx.live_score.infra.db.jpaRepository.MatchEntityRepository;
import com.fastx.live_score.infra.db.jpaRepository.TeamRepository;
import com.fastx.live_score.infra.db.jpaRepository.TournamentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TournamentServiceImpl implements TournamentService {
    private final TournamentJpaRepository tournamentJpaRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public TournamentServiceImpl(MatchEntityRepository matchEntityRepository, TournamentJpaRepository tournamentJpaRepository, TeamRepository teamRepository) {
        this.tournamentJpaRepository = tournamentJpaRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Tournament createNewTournament(TournamentRequest request) {
        TournamentEntity formTournamentRequest = createFormTournamentRequest(request);
        TournamentEntity tournamentEntity = tournamentJpaRepository.save(formTournamentRequest);
        return TournamentMapper.mapToTournament(tournamentEntity);
    }

    @Override
    public Tournament updateTournament(Long tournamentId, TournamentRequest request) {
        TournamentEntity tournamentEntity = tournamentJpaRepository.findById(tournamentId).orElseThrow();
        TournamentEntity formTournamentRequest = createFormTournamentRequest(request, tournamentEntity);
        return TournamentMapper.mapToTournament(tournamentJpaRepository.save(formTournamentRequest));
    }

    @Override
    public Tournament getTournamentById(Long tournamentId) {
        TournamentEntity tournamentEntity = tournamentJpaRepository.findById(tournamentId).orElseThrow();
        return TournamentMapper.mapToTournament(tournamentEntity);
    }

    @Override
    public List<Tournament> getAllTournaments() {
        List<TournamentEntity> tournamentEntityList = tournamentJpaRepository.findAll();
        return tournamentEntityList.stream().map(TournamentMapper::mapToTournament).toList();
    }


    @Override
    public void deleteTournament(Long tournamentId) {
        tournamentJpaRepository.deleteById(tournamentId);
    }

    @Override
    public void assignWinner(Long tournamentId, Long teamId) {
        TeamEntity teamEntity = teamRepository.findById(teamId).orElseThrow();
        TournamentEntity tournament = tournamentJpaRepository.findById(tournamentId).orElseThrow();
        tournament.setWinner(teamEntity);
        tournamentJpaRepository.save(tournament);
    }

    private TournamentEntity createFormTournamentRequest(TournamentRequest request, TournamentEntity entity) {
        Optional.ofNullable(request.getName()).ifPresent(entity::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(entity::setDescription);
        Optional.ofNullable(request.getLocation()).ifPresent(entity::setLocation);
        Optional.ofNullable(request.getLogoUrl()).ifPresent(entity::setLogoUrl);

        if (request.getStartDate() != 0) {
            entity.setStartDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getStartDate()), ZoneId.systemDefault()));
        }
        if (request.getEndDate() != 0) {
            entity.setEndDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getEndDate()), ZoneId.systemDefault()));
        }

        if (request.getParticipatingTeamIds() != null) {
            Set<Long> uniqueTeamIds = new HashSet<>(request.getParticipatingTeamIds());
            entity.setParticipatingTeams(teamRepository.findAllById(uniqueTeamIds));
        }
        return entity;
    }


    private TournamentEntity createFormTournamentRequest(TournamentRequest request) {
        TournamentEntity entity = new TournamentEntity();
        return createFormTournamentRequest(request, entity);
    }


}
