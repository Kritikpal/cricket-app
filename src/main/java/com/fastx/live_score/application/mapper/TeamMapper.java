package com.fastx.live_score.application.mapper;

import com.fastx.live_score.adapter.web.response.ShortPlayerRes;
import com.fastx.live_score.adapter.web.response.TeamRes;
import com.fastx.live_score.infra.db.entities.TeamEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TeamMapper {
    public static TeamRes toResponse(TeamEntity entity) {
        if (entity == null) return null;
        TeamRes.TeamResBuilder builder = TeamRes.builder()
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
            List<ShortPlayerRes> players = entity.getPlayers().stream()
                    .map(PlayerMapper::toShortPlayer)
                    .collect(Collectors.toList());
            builder.players(players);
        }

        return builder.build();
    }
}
