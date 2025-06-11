package com.fastx.live_score.domain.out;

import com.fastx.live_score.domain.models.tips.Tip;

import java.util.List;

public interface TipsRepository {

    void saveTips(List<Tip> tip);

    List<Tip> getTipsPerMatch(Long matchId);

    Tip getTip(Long tipId);

    void deleteTip(Long tipId);

}
