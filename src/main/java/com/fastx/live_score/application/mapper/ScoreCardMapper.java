package com.fastx.live_score.application.mapper;

import com.fastx.live_score.infra.db.entities.PlayerEntity;
import com.fastx.live_score.infra.db.entities.TeamEntity;
import org.fastX.models.Player;
import org.fastX.models.Team;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreCardMapper {

    public static Team fromTeamEntity(String label, List<Long> selectedPlayerIds, TeamEntity teamEntity) {
        validatePlayersInTeam(selectedPlayerIds, teamEntity.getPlayers(), label);

        List<Player> selectedPlayers = teamEntity.getPlayers().stream()
                .filter(p -> selectedPlayerIds.contains(p.getId()))
                .map(ScoreCardMapper::fromPlayerEntity)
                .toList();
        return new Team(teamEntity.getId(), teamEntity.getName(), selectedPlayers);
    }

    private static void validatePlayersInTeam(List<Long> selectedPlayerIds, List<PlayerEntity> actualPlayers, String teamLabel) {

        Set<Long> actualPlayerIds = actualPlayers.stream()
                .map(PlayerEntity::getId)
                .collect(Collectors.toSet());

        for (Long playerId : selectedPlayerIds) {
            if (!actualPlayerIds.contains(playerId)) {
                throw new IllegalArgumentException("Player " + playerId + " does not belong to Team " + teamLabel);
            }
        }

        if (selectedPlayerIds.size() != 11) {
            throw new IllegalArgumentException("Team " + teamLabel + " must have exactly 11 players");
        }
    }

    public static Player fromPlayerEntity(PlayerEntity playerEntity) {
        return new Player(playerEntity.getId(), playerEntity.getFullName(), playerEntity.getShortName());
    }
}
