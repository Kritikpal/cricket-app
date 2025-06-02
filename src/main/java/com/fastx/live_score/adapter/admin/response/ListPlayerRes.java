package com.fastx.live_score.adapter.admin.response;

import com.fastx.live_score.domain.models.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ListPlayerRes {
    private Long id;
    private String name;

    public static ListPlayerRes toShortPlayer(Player player) {
        return new ListPlayerRes(player.getId(), player.getShortName());
    }

}
