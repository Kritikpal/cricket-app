package com.fastx.live_score.application.mapper;

import com.fastx.live_score.infra.db.entities.PlayerEntity;
import com.fastx.live_score.infra.db.entities.TeamEntity;
import org.fastX.models.Player;
import org.fastX.models.Team;

public class ScoreCardMapper {

    public static Team fromTeamEntity(TeamEntity teamEntity) {

        return new Team(teamEntity.getId(),teamEntity.getName(),teamEntity.getPlayers().stream().map(
                ScoreCardMapper::fromPlayerEntity
        ).limit(11).toList());
    }

    public static Player fromPlayerEntity(PlayerEntity playerEntity) {
        return new Player(playerEntity.getId(), playerEntity.getFullName(), playerEntity.getShortName());
    }


}
