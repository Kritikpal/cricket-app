package com.fastx.live_score.application.mapper;

import com.fastx.live_score.adapter.web.response.PlayerRes;
import com.fastx.live_score.adapter.web.response.ShortPlayerRes;
import com.fastx.live_score.infra.db.entities.PlayerEntity;

public class PlayerMapper {

    public static PlayerRes toPlayer(PlayerEntity entity) {
        if (entity == null) return null;

        return PlayerRes.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .shortName(entity.getShortName())
                .nationality(entity.getNationality())
                .role(entity.getRole())
                .battingStyle(entity.getBattingStyle())
                .bowlingStyle(entity.getBowlingStyle())
                .totalMatches(entity.getTotalMatches())
                .totalRuns(entity.getTotalRuns())
                .totalWickets(entity.getTotalWickets())
                .isActive(entity.isActive())
                .build();
    }

    public static ShortPlayerRes toShortPlayer(PlayerEntity playerEntity) {
        return new ShortPlayerRes(playerEntity.getId(), playerEntity.getShortName());
    }
}
