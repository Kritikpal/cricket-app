package com.fastx.live_score.application.adaptors;

import com.fastx.live_score.application.mapper.ScoreCardMapper;
import com.fastx.live_score.application.mapper.ScoreboardMapper;
import com.fastx.live_score.domain.Scoreboard;
import com.fastx.live_score.domain.in.MatchEventService;
import com.fastx.live_score.domain.out.MatchCardRepository;
import com.fastx.live_score.infra.db.entities.MatchEntity;
import com.fastx.live_score.infra.db.entities.TournamentEntity;
import com.fastx.live_score.infra.db.entities.enums.MatchStatus;
import com.fastx.live_score.infra.db.entities.enums.TournamentStatus;
import com.fastx.live_score.infra.db.jpaRepository.MatchEntityRepository;
import com.fastx.live_score.infra.db.jpaRepository.TournamentJpaRepository;
import jakarta.transaction.Transactional;
import org.fastX.MatchController;
import org.fastX.PrintService;
import org.fastX.enums.DismissType;
import org.fastX.models.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MatchEventServiceImpl implements MatchEventService {

    private final MatchEntityRepository matchEntityRepository;
    private final MatchCardRepository matchCardRepository;
    private final TournamentJpaRepository tournamentJpaRepository;

    @Autowired
    public MatchEventServiceImpl(MatchEntityRepository matchEntityRepository, MatchCardRepository matchCardRepository,
                                 TournamentJpaRepository tournamentJpaRepository) {
        this.matchEntityRepository = matchEntityRepository;
        this.matchCardRepository = matchCardRepository;
        this.tournamentJpaRepository = tournamentJpaRepository;
    }


    @Override
    public Match startMatch(long matchId, List<Long> teamA, List<Long> teamB) {

        MatchEntity matchEntity = matchEntityRepository.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));

        MatchController controller = MatchController.startMatch(matchEntity.getId(),
                ScoreCardMapper.fromTeamEntity("A", teamA, matchEntity.getTeamEntityA()),
                ScoreCardMapper.fromTeamEntity("B", teamB, matchEntity.getTeamEntityB()),
                matchEntity.getTotalOvers(), 2);

        matchEntity.setMatchStatus(MatchStatus.IN_PROGRESS);
        if (matchEntity.getTournament() != null) {
            TournamentEntity tournament = matchEntity.getTournament();
            if (matchEntity.getTournament().getTournamentStatus() != TournamentStatus.LIVE) {
                tournament.setTournamentStatus(TournamentStatus.LIVE);
                tournamentJpaRepository.save(tournament);
            }
        }
        matchEntityRepository.save(matchEntity);
        Match match = controller.getMatch();
        matchCardRepository.cacheMatch(match);
        return match;
    }

    @Override
    public Match startOver(long matchId, long bowlerName) {
        Match cachedMatch = matchCardRepository.getCachedMatch(matchId);
        MatchController matchController = new MatchController(cachedMatch);
        Match match = matchController.startOver(bowlerName).getMatch();
        matchCardRepository.cacheMatch(match);
        return match;
    }

    @Override
    public Scoreboard startInnings(Long matchId, Long teamId, Long strikerId, Long nonStrikerId, Long bowlerId) {
        Match cachedMatch = matchCardRepository.getCachedMatch(matchId);
        MatchController matchController = new MatchController(cachedMatch);
        Match match = matchController.startNewInnings(teamId, strikerId, nonStrikerId, bowlerId).getMatch();
        matchCardRepository.cacheMatch(match);
        return ScoreboardMapper.toDto(match);
    }

    @Override
    public Scoreboard addBallEvent(Long matchId, String input, Long dismissBy, DismissType dismissType, Long dismissPlayer) {
        Match match = matchCardRepository.getCachedMatch(matchId);
        Match updatedMatch = new MatchController(match).completeDelivery
                (input, dismissType, dismissBy, dismissPlayer).getMatch();
        matchCardRepository.cacheMatch(updatedMatch);
        return ScoreboardMapper.toDto(updatedMatch);
    }

    @Override
    public String getScoreBoard(long matchId) {
        Match match = matchCardRepository.getCachedMatch(matchId);
        return new PrintService(match).getMatchSummary();
    }

    @Override
    public Scoreboard getLiveMatchStatus(long id) {

        Match cachedMatch = matchCardRepository.getCachedMatch(id);
        return ScoreboardMapper.toDto(cachedMatch);

    }


}
