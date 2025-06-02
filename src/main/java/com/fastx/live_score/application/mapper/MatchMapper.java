package com.fastx.live_score.application.mapper;

import com.fastx.live_score.adapter.admin.response.ListTeamRes;
import com.fastx.live_score.domain.models.Match;
import com.fastx.live_score.infra.db.entities.MatchEntity;

public class MatchMapper {
    public static Match toMatch(MatchEntity matchEntity) {
        Match.MatchBuilder builder = Match.builder();

        builder.id(matchEntity.getId());
        if (matchEntity.getTeamEntityA() != null) {
            builder.teamA(ListTeamRes.from(TeamMapper.toResponse(matchEntity.getTeamEntityA())));
        }
        if (matchEntity.getTeamEntityB() != null) {
            builder.teamB(ListTeamRes.from(TeamMapper.toResponse(matchEntity.getTeamEntityB())));
        }
        builder.startTime(matchEntity.getStartTime());
        builder.venue(matchEntity.getVenue());
        builder.totalOvers(matchEntity.getTotalOvers());
        builder.endTime(matchEntity.getEndTime());
        builder.winningTeam(matchEntity.getWinningTeam());
        builder.electedTo(matchEntity.getElectedTo());
        builder.matchStatus(matchEntity.getMatchStatus());
        builder.tossWinner(matchEntity.getTossWinner());
        builder.tournament(matchEntity.getTournament().getId());
        return builder.build();
    }
}

