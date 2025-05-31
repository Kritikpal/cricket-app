package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.fastX.enums.MatchStatus;
import org.fastX.exception.GameException;
import org.fastX.models.events.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Match implements MatchEventTrigger<Match> {

    private final Long matchId;
    private final MatchInfo matchInfo;
    private final MatchStatus matchStatus;
    private final MatchResult matchResult;
    private final Innings currentInnings;
    private final List<Innings> inningsList;

    public static Match createMatch(MatchStartEvent matchStartEvent) {
        return Match.builder()
                .matchId(matchStartEvent.matchId())
                .matchInfo(MatchInfo.createNewMatch(
                        matchStartEvent.teamA(),
                        matchStartEvent.teamB(),
                        matchStartEvent.totalOvers(),
                        matchStartEvent.totalInnings()
                ))
                .matchStatus(MatchStatus.ONGOING)
                .inningsList(new ArrayList<>())
                .build();

    }

    @Override
    public Match triggerEvent(MatchEvent event) {

        if (!matchStatus.equals(MatchStatus.ONGOING)) {
            throw new GameException("Match is not live", 400);
        }

        if (event instanceof StartInningsEvent startInningsEvent) {
            Innings newInnings = Innings.createNewInnings(startInningsEvent);

            List<Innings> newInningsList = new ArrayList<>(this.inningsList);
            newInningsList.add(newInnings);

            return this.toBuilder()
                    .currentInnings(newInnings)
                    .inningsList(newInningsList)
                    .build();
        }

        if (event instanceof BallCompleteEvent && this.currentInnings == null) {
            throw new GameException("No innings in progress", 400);
        }


        Innings updatedInnings = this.currentInnings.triggerEvent(event);
        List<Innings> updatedInningsList = updateInningsList(this.inningsList, updatedInnings);

        return this.toBuilder()
                .currentInnings(updatedInnings)
                .inningsList(updatedInningsList)
                .build();
    }

    private List<Innings> updateInningsList(List<Innings> list, Innings updated) {
        List<Innings> result = new ArrayList<>();
        for (Innings innings : list) {
            if (Objects.equals(innings.getTeam().getTeamId(), updated.getTeam().getTeamId())) {
                result.add(updated);
            } else {
                result.add(innings);
            }
        }
        return result;
    }

    public Team getBattingTeam() {
        return getTeamFromInnings(true);
    }

    public Team getBowlingTeam() {
        return getTeamFromInnings(false);
    }

    public Innings getFirstInnings() {
        return inningsList != null && !inningsList.isEmpty() ? inningsList.get(0) : null;
    }

    public Innings getSecondInnings() {
        return inningsList != null && inningsList.size() > 1 ? inningsList.get(1) : null;
    }

    private Team getTeamFromInnings(boolean isBattingTeam) {
        if (currentInnings == null || matchInfo.getTeamA() == null || matchInfo.getTeamB() == null) {
            throw new GameException("Select Team", 400);
        }
        boolean isTeamA = currentInnings.getTeam().getTeamId().equals(matchInfo.getTeamA().getTeamId());
        return isBattingTeam ? (isTeamA ? matchInfo.getTeamA() : matchInfo.getTeamB()) : (isTeamA ? matchInfo.getTeamB() : matchInfo.getTeamA());
    }
}
