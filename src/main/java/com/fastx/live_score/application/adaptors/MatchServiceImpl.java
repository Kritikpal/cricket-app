package com.fastx.live_score.application.adaptors;

import com.fastx.live_score.adapter.admin.request.MatchRequest;
import com.fastx.live_score.domain.models.match.Match;
import com.fastx.live_score.domain.in.MatchService;
import com.fastx.live_score.infra.db.entities.MatchEntity;
import com.fastx.live_score.infra.db.entities.TeamEntity;
import com.fastx.live_score.infra.db.entities.TournamentEntity;
import com.fastx.live_score.infra.db.entities.enums.MatchStatus;
import com.fastx.live_score.application.exception.AppException;
import com.fastx.live_score.application.mapper.MatchMapper;
import com.fastx.live_score.infra.db.jpaRepository.MatchEntityRepository;
import com.fastx.live_score.infra.db.jpaRepository.TeamRepository;
import com.fastx.live_score.infra.db.jpaRepository.TournamentJpaRepository;
import org.fastX.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchEntityRepository matchEntityRepository;
    private final TournamentJpaRepository tournamentJpaRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public MatchServiceImpl(MatchEntityRepository matchEntityRepository, TournamentJpaRepository tournamentJpaRepository, TeamRepository teamRepository) {
        this.matchEntityRepository = matchEntityRepository;
        this.tournamentJpaRepository = tournamentJpaRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public void saveMatch(MatchRequest request) {
        MatchEntity matchEntity = new MatchEntity();
        if (request.getMatchId() != null) {
            matchEntity = matchEntityRepository.findById(request.getMatchId()).orElse(new MatchEntity());
        }
        matchEntityRepository.save(getMatchEntity(matchEntity, request));
    }

    @Override
    public List<Match> listMatches(MatchStatus matchStatus) {
        if (matchStatus == null) {
            return matchEntityRepository.findAll().stream().map(MatchMapper::toMatch).toList();
        }
        List<MatchEntity> byMatchStatus = matchEntityRepository.findByMatchStatus(matchStatus);
        return byMatchStatus.stream().map(MatchMapper::toMatch).toList();
    }

    @Override
    public List<Match> listMatchesByTourId(long tourId) {
        List<MatchEntity> matchEntities = matchEntityRepository.findByTournament_Id(tourId);
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
        if (Objects.equals(request.getTeamAId(), request.getTeamBId())) {
            throw new GameException("Cant have 2 same teams", 400);
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
        MatchEntity matchEntity = matchEntityRepository.findById(matchId).orElseThrow();
        return MatchMapper.toMatch(matchEntity);
    }

    @Override

    public void deleteMatch(Long matchId) {

        matchEntityRepository.deleteById(matchId);
    }

    @Override
    public void startMatch(Long matchId) {
        MatchEntity matchEntity = matchEntityRepository.findById(matchId).orElseThrow();
        if (matchEntity.getMatchStatus().equals(MatchStatus.COMPLETED)) {
            throw new AppException("Match is completed");
        }
        matchEntity.setMatchStatus(MatchStatus.IN_PROGRESS);
        matchEntity.setStartTime(LocalDateTime.now());
        matchEntityRepository.save(matchEntity);
    }

    @Override
    public void endMatch(Long matchId, int winningTeam) {
        MatchEntity matchEntity = matchEntityRepository.findById(matchId).orElseThrow();
        if (matchEntity.getMatchStatus().equals(MatchStatus.COMPLETED)) {
            throw new AppException("Match is completed");
        }
        if (winningTeam == 0) {
            throw new AppException("Please Provide WInner");
        }
        matchEntity.setWinningTeam(winningTeam);
        matchEntity.setMatchStatus(MatchStatus.COMPLETED);
        matchEntity.setEndTime(LocalDateTime.now());
        matchEntityRepository.save(matchEntity);
    }


}
