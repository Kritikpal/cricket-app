package com.fastx.live_score.application.mapper;

import com.fastx.live_score.adapter.web.response.ShortMatchRes;
import com.fastx.live_score.adapter.web.response.ShortTeamRes;
import com.fastx.live_score.adapter.web.response.ShortTournamentRes;
import com.fastx.live_score.adapter.web.response.TournamentRes;
import com.fastx.live_score.infra.db.entities.TournamentEntity;

public class TournamentMapper {
    public static TournamentRes mapToTournament(TournamentEntity entity) {
        TournamentRes.TournamentResBuilder builder = TournamentRes.builder();
        builder.id(entity.getId());
        builder.name(entity.getName());
        builder.startDate(entity.getStartDate());
        builder.description(entity.getDescription());
        builder.endDate(entity.getEndDate());
        builder.location(entity.getLocation());
        builder.participatingTeams(
                entity.getParticipatingTeams().stream().map(teamEntity ->
                        new ShortTeamRes(teamEntity.getId(), teamEntity.getName())).toList()
        );
        builder.matches(entity.getMatches().stream().map(matchEntity -> new ShortMatchRes(matchEntity.getId(), matchEntity.getTeamEntityA().getName(), matchEntity.getTeamEntityB().getName())).toList());
        return builder.build();
    }

    public static ShortTournamentRes toShortTournament(TournamentEntity entity) {
        return new ShortTournamentRes(entity.getId(), entity.getName(), entity.getDescription());
    }

}
