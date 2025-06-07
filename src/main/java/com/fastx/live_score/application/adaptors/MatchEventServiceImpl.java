package com.fastx.live_score.application.adaptors;

import com.fastx.live_score.application.mapper.ScoreCardMapper;
import com.fastx.live_score.application.mapper.ScoreboardMapper;
import com.fastx.live_score.domain.Scoreboard;
import com.fastx.live_score.domain.in.MatchEventService;
import com.fastx.live_score.domain.out.MatchCardRepository;
import com.fastx.live_score.infra.db.entities.MatchEntity;
import com.fastx.live_score.infra.db.entities.enums.MatchStatus;
import com.fastx.live_score.infra.db.jpaRepository.MatchRepository;
import jakarta.transaction.Transactional;
import org.fastX.MatchController;
import org.fastX.PrintService;
import org.fastX.models.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MatchEventServiceImpl implements MatchEventService {

    private final MatchRepository matchRepository;
    private final MatchCardRepository matchCardRepository;

    @Autowired
    public MatchEventServiceImpl(
            MatchRepository matchRepository,
            MatchCardRepository matchCardRepository
    ) {
        this.matchRepository = matchRepository;
        this.matchCardRepository = matchCardRepository;
    }

    @Override
    public Match startMatch(long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
        MatchController controller = MatchController.startMatch(
                matchEntity.getId(),
                ScoreCardMapper.fromTeamEntity(matchEntity.getTeamEntityA()),
                ScoreCardMapper.fromTeamEntity(matchEntity.getTeamEntityB()),
                matchEntity.getTotalOvers(), 2
        );
        matchEntity.setMatchStatus(MatchStatus.IN_PROGRESS);
        matchRepository.save(matchEntity);
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
        Match match = matchController.startNewInnings(teamId, strikerId, nonStrikerId, bowlerId)
                .getMatch();
        matchCardRepository.cacheMatch(match);
        return ScoreboardMapper.toDto(match);
    }

    @Override
    public Scoreboard addBallEvent(long matchId, String input) {
        Match match = matchCardRepository.getCachedMatch(matchId);
        Match updatedMatch = new MatchController(match)
                .completeDelivery(input)
                .getMatch();
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
