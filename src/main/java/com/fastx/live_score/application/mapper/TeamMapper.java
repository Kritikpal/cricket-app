package com.fastx.live_score.application.mapper;

import com.fastx.live_score.adapter.admin.response.ListPlayerRes;
import com.fastx.live_score.domain.models.match.Team;
import com.fastx.live_score.infra.db.entities.TeamEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TeamMapper {
    public static Team toResponse(TeamEntity entity) {
        if (entity == null) return null;
        Team.TeamBuilder builder = Team.builder()
                .id(entity.getId())
                .name(entity.getName())
                .shortCode(entity.getShortCode())
                .logoUrl(entity.getLogoUrl())
                .coach(entity.getCoach());

        if (entity.getCaptain() != null) {
            builder.captainId(entity.getCaptain().getId());
            builder.captainName(entity.getCaptain().getFullName());
        }

        if (entity.getPlayers() != null) {
            List<ListPlayerRes> players = entity.getPlayers().stream()
                    .map(PlayerMapper::toPlayer)
                    .map(ListPlayerRes::toShortPlayer)
                    .collect(Collectors.toList());
            builder.players(players);
        }

        return builder.build();
    }
}
