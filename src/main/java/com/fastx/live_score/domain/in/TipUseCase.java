package com.fastx.live_score.domain.in;

import com.fastx.live_score.domain.models.tips.Tip;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface TipUseCase {

    void addTipToMatch(Long matchId, @NotEmpty List<String> tipData);

    void updateTip(Long tipId, String tipData);

    List<Tip> getMatchTips(Long matchId);

    void deleteTip(Long tipId);

}
