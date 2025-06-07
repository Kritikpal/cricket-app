package org.fastX.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.fastX.enums.MatchStatus;
import org.fastX.exception.GameException;
import org.fastX.models.events.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder(toBuilder = true)
public record Match(Long matchId, MatchInfo matchInfo, MatchStatus matchStatus, MatchResult matchResult,
                    Innings currentInnings,
                    List<Innings> inningsList) implements MatchEventTrigger<Match>, Serializable {

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", matchStatus=" + matchStatus +
                ", matchInfo=" + matchInfo +
                ", matchResult=" + matchResult +
                ", currentInnings=" + currentInnings +
                ", inningsList=" + inningsList +
                '}';
    }


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


    @JsonIgnore
    private List<Innings> updateInningsList(List<Innings> list, Innings updated) {
        List<Innings> result = new ArrayList<>();
        for (Innings innings : list) {
            if (Objects.equals(innings.team().teamId(), updated.team().teamId())) {
                result.add(updated);
            } else {
                result.add(innings);
            }
        }
        return result;
    }

    @JsonIgnore
    public Team getBattingTeam() {
        return getTeamFromInnings(true);
    }

    @JsonIgnore
    public Team getBowlingTeam() {
        return getTeamFromInnings(false);
    }

    @JsonIgnore
    private Team getTeamFromInnings(boolean isBattingTeam) {
        if (currentInnings == null || matchInfo.teamA() == null || matchInfo.teamB() == null) {
            return null;
        }
        boolean isTeamA = currentInnings.team().teamId().equals(matchInfo.teamA().teamId());
        return isBattingTeam ? (isTeamA ? matchInfo.teamA() : matchInfo.teamB()) : (isTeamA ? matchInfo.teamB() : matchInfo.teamA());
    }
}
