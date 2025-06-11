package com.fastx.live_score.application.mapper;

import com.fastx.live_score.adapter.admin.response.ListTeamRes;
import com.fastx.live_score.domain.models.match.Tournament;
import com.fastx.live_score.infra.db.entities.TournamentEntity;

public class TournamentMapper {
    public static Tournament mapToTournament(TournamentEntity entity) {
        Tournament.TournamentBuilder builder = Tournament.builder();
        builder.id(entity.getId());
        builder.name(entity.getName());
        builder.startDate(entity.getStartDate());
        builder.description(entity.getDescription());
        builder.endDate(entity.getEndDate());
        builder.location(entity.getLocation());
        builder.tournamentStatus(entity.getTournamentStatus());
        builder.logoUrl(entity.getLogoUrl());
        builder.participatingTeams(
                entity.getParticipatingTeams().stream().map(teamEntity ->
                        ListTeamRes.from(TeamMapper.toResponse(teamEntity))).toList()
        );
        builder.winner(TeamMapper.toResponse(entity.getWinner()));
        builder.matches(entity.getMatches().stream().map(MatchMapper::toMatch).toList());
        return builder.build();
    }

}
