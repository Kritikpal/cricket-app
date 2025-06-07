package com.fastx.live_score.application.adaptors;

import com.fastx.live_score.adapter.admin.request.MatchRequest;
import com.fastx.live_score.domain.models.Match;
import com.fastx.live_score.domain.in.MatchService;
import com.fastx.live_score.infra.db.entities.MatchEntity;
import com.fastx.live_score.infra.db.entities.TeamEntity;
import com.fastx.live_score.infra.db.entities.TournamentEntity;
import com.fastx.live_score.infra.db.entities.enums.MatchStatus;
import com.fastx.live_score.application.exception.AppException;
import com.fastx.live_score.application.mapper.MatchMapper;
import com.fastx.live_score.infra.db.jpaRepository.MatchRepository;
import com.fastx.live_score.infra.db.jpaRepository.TeamRepository;
import com.fastx.live_score.infra.db.jpaRepository.TournamentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final TournamentJpaRepository tournamentJpaRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, TournamentJpaRepository tournamentJpaRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.tournamentJpaRepository = tournamentJpaRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public void saveMatch(MatchRequest request) {
        MatchEntity matchEntity = new MatchEntity();
        if (request.getMatchId() != null) {
            matchEntity = matchRepository.findById(request.getMatchId()).orElse(new MatchEntity());
        }
        matchRepository.save(getMatchEntity(matchEntity, request));
    }

    @Override
    public List<Match> listMatches(MatchStatus matchStatus) {
        if (matchStatus == null) {
            return matchRepository.findAll().stream().map(MatchMapper::toMatch).toList();
        }
        List<MatchEntity> byMatchStatus = matchRepository.findByMatchStatus(matchStatus);
        return byMatchStatus.stream().map(MatchMapper::toMatch).toList();
    }

    @Override
    public List<Match> listMatchesByTourId(long tourId) {
        List<MatchEntity> matchEntities = matchRepository.findByTournament_Id(tourId);
        return matchEntities.stream().map(MatchMapper::toMatch).toList();
    }

    private MatchEntity getMatchEntity(MatchEntity entity, MatchRequest request) {
        if (request.getTournamentId() != null) {
            TournamentEntity tournamentEntity = tournamentJpaRepository.findById(request.getTournamentId()).orElseThrow();
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
        entity.setTotalOvers(request.getTotalOvers());

        if (request.getStartTime() != 0) {
            entity.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getStartTime()), ZoneId.systemDefault()));
        }
        if (request.getEndTime() != 0) {
            entity.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getEndTime()), ZoneId.systemDefault()));
        }

        return entity;
    }

    @Override
    public Match getMatchById(Long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId).orElseThrow();
        return MatchMapper.toMatch(matchEntity);
    }

    @Override

    public void deleteMatch(Long matchId){

        matchRepository.deleteById(matchId);
    }

    @Override
    public void startMatch(Long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId).orElseThrow();
        if (matchEntity.getMatchStatus().equals(MatchStatus.COMPLETED)) {
            throw new AppException("Match is completed");
        }
        matchEntity.setMatchStatus(MatchStatus.IN_PROGRESS);
        matchEntity.setStartTime(LocalDateTime.now());
        matchRepository.save(matchEntity);
    }

    @Override
    public void endMatch(Long matchId, int winningTeam) {
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
        matchRepository.save(matchEntity);
    }


}
