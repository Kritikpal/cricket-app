package com.fastx.live_score.application.services;

import com.fastx.live_score.adapter.web.request.TournamentRequest;
import com.fastx.live_score.adapter.web.response.ShortTournamentRes;
import com.fastx.live_score.adapter.web.response.TournamentRes;
import com.fastx.live_score.domain.in.TournamentService;
import com.fastx.live_score.infra.db.entities.TeamEntity;
import com.fastx.live_score.infra.db.entities.TournamentEntity;
import com.fastx.live_score.application.mapper.TournamentMapper;
import com.fastx.live_score.infra.db.jpaRepository.MatchRepository;
import com.fastx.live_score.infra.db.jpaRepository.TeamRepository;
import com.fastx.live_score.infra.db.jpaRepository.TournamentRepository;
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
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public TournamentServiceImpl(MatchRepository matchRepository, TournamentRepository tournamentRepository, TeamRepository teamRepository) {
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public TournamentRes createNewTournament(TournamentRequest request) {
        TournamentEntity formTournamentRequest = createFormTournamentRequest(request);
        TournamentEntity tournamentEntity = tournamentRepository.save(formTournamentRequest);
        return TournamentMapper.mapToTournament(tournamentEntity);
    }

    @Override
    public TournamentRes updateTournament(Long tournamentId, TournamentRequest request) {
        TournamentEntity tournamentEntity = tournamentRepository.findById(tournamentId).orElseThrow();
        TournamentEntity formTournamentRequest = createFormTournamentRequest(request, tournamentEntity);
        return TournamentMapper.mapToTournament(tournamentRepository.save(formTournamentRequest));
    }

    @Override
    public TournamentRes getTournamentById(Long tournamentId) {
        TournamentEntity tournamentEntity = tournamentRepository.findById(tournamentId).orElseThrow();
        return TournamentMapper.mapToTournament(tournamentEntity);
    }

    @Override
    public List<ShortTournamentRes> getAllTournaments() {
        List<TournamentEntity> tournamentEntityList = tournamentRepository.findAll();
        return tournamentEntityList.stream().map(TournamentMapper::toShortTournament).toList();
    }


    @Override
    public void deleteTournament(Long tournamentId) {
        tournamentRepository.deleteById(tournamentId);
    }

    @Override
    public void assignWinner(Long tournamentId, Long teamId) {
        TeamEntity teamEntity = teamRepository.findById(teamId).orElseThrow();
        TournamentEntity tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        tournament.setWinner(teamEntity);
        tournamentRepository.save(tournament);
    }

    private TournamentEntity createFormTournamentRequest(TournamentRequest request, TournamentEntity entity) {
        Optional.ofNullable(request.getName()).ifPresent(entity::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(entity::setDescription);
        Optional.ofNullable(request.getLocation()).ifPresent(entity::setLocation);

        if (request.getStartDate() != 0) {
            entity.setStartDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getStartDate()), ZoneId.systemDefault()));
        }
        if (request.getEndDate() != 0) {
            entity.setEndDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getEndDate()), ZoneId.systemDefault()));
        }

        if (request.getParticipatingTeamIds() != null) {
            Set<Long> uniqueTeamIds = new HashSet<>(request.getParticipatingTeamIds());
            entity.setParticipatingTeams(teamRepository.findAllById(uniqueTeamIds));        }
        return entity;
    }


    private TournamentEntity createFormTournamentRequest(TournamentRequest request) {
        TournamentEntity entity = new TournamentEntity();
        return createFormTournamentRequest(request, entity);
    }


}
