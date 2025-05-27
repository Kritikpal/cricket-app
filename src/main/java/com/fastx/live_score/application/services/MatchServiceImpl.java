package com.fastx.live_score.application.services;

import com.fastx.live_score.adapter.web.request.CreateMatchRequest;
import com.fastx.live_score.adapter.web.response.MatchRes;
import com.fastx.live_score.domain.in.MatchService;
import com.fastx.live_score.infra.db.entities.MatchEntity;
import com.fastx.live_score.infra.db.entities.TeamEntity;
import com.fastx.live_score.infra.db.entities.TournamentEntity;
import com.fastx.live_score.infra.db.entities.enums.MatchStatus;
import com.fastx.live_score.application.exception.AppException;
import com.fastx.live_score.application.mapper.MatchMapper;
import com.fastx.live_score.infra.db.jpaRepository.MatchRepository;
import com.fastx.live_score.infra.db.jpaRepository.TeamRepository;
import com.fastx.live_score.infra.db.jpaRepository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, TournamentRepository tournamentRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public MatchRes saveMatch(CreateMatchRequest request) {
        MatchEntity matchEntity = new MatchEntity();
        if (request.getMatchId() != null) {
            matchEntity = matchRepository.findById(request.getMatchId()).orElse(new MatchEntity());
        }
        return MatchMapper.toMatch(matchRepository.save(getMatchEntity(matchEntity, request)));
    }


    private MatchEntity getMatchEntity(MatchEntity entity, CreateMatchRequest request) {
        if (request.getTournamentId() != null) {
            TournamentEntity tournamentEntity = tournamentRepository.findById(request.getTournamentId()).orElseThrow();
            entity.setTournament(tournamentEntity);
        }
        if (request.getTeamAId() != null) {
            TeamEntity teamEntity = teamRepository.findById(request.getTeamAId()).orElseThrow();
            entity.setTeamEntityA(teamEntity);
        }
        if (request.getTeamBId() != null) {
            TeamEntity teamEntity = teamRepository.findById(request.getTeamBId()).orElseThrow();
            entity.setTeamEntityB(teamEntity);
        }
        Optional.ofNullable(request.getVenue()).ifPresent(entity::setVenue);
        if (request.getStartTime() != 0) {
            entity.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getStartTime()), ZoneId.systemDefault()));
        }
        if (request.getEndTime() != 0) {
            entity.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getEndTime()), ZoneId.systemDefault()));
        }
        return entity;
    }


    @Override
    public MatchRes getMatchById(Long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId).orElseThrow();
        return MatchMapper.toMatch(matchEntity);
    }

    @Override
    public MatchRes startMatch(Long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId).orElseThrow();
        if (matchEntity.getMatchStatus().equals(MatchStatus.COMPLETED)) {
            throw new AppException("Match is completed");
        }
        matchEntity.setMatchStatus(MatchStatus.IN_PROGRESS);
        matchEntity.setStartTime(LocalDateTime.now());
        return MatchMapper.toMatch(matchRepository.save(matchEntity));
    }

    @Override
    public MatchRes endMatch(Long matchId, int winningTeam) {
        MatchEntity matchEntity = matchRepository.findById(matchId).orElseThrow();
        if (matchEntity.getMatchStatus().equals(MatchStatus.COMPLETED)) {
            throw new AppException("Match is completed");
        }
        if (winningTeam == 0) {
            throw new AppException("Please Provide WInner");
        }
        matchEntity.setWinningTeam(winningTeam);
        matchEntity.setMatchStatus(MatchStatus.COMPLETED);
        matchEntity.setEndTime(LocalDateTime.now());
        return MatchMapper.toMatch(matchRepository.save(matchEntity));
    }


}
