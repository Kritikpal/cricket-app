package com.fastx.live_score.application.mapper;

import com.fastx.live_score.adapter.web.response.MatchRes;
import com.fastx.live_score.adapter.web.response.ShortTeamRes;
import com.fastx.live_score.infra.db.entities.MatchEntity;

public class MatchMapper {
    public static MatchRes toMatch(MatchEntity matchEntity) {
        MatchRes.MatchResBuilder builder = MatchRes.builder();

        if (matchEntity.getTeamEntityA() != null) {
            builder.teamA(new ShortTeamRes(matchEntity.getId(), matchEntity.getTeamEntityA().getName()));
        }
        if (matchEntity.getTeamEntityA() != null) {
            builder.teamB(new ShortTeamRes(matchEntity.getId(), matchEntity.getTeamEntityB().getName()));
        }
        builder.startTime(matchEntity.getStartTime());
        builder.venue(matchEntity.getVenue());
        builder.endTime(matchEntity.getEndTime());
        builder.winningTeam(matchEntity.getWinningTeam());
        builder.electedTo(matchEntity.getElectedTo());
        builder.matchStatus(matchEntity.getMatchStatus());
        builder.tossWinner(matchEntity.getTossWinner());
        return builder.build();
    }
}

