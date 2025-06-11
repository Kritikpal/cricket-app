package com.fastx.live_score.application.adaptors;

import com.fastx.live_score.domain.in.TipUseCase;
import com.fastx.live_score.domain.models.tips.Tip;
import com.fastx.live_score.domain.out.TipsRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class TipsServiceImpl implements TipUseCase {

    private final TipsRepository tipsRepository;

    @Override
    public void addTipToMatch(Long matchId, @NotEmpty List<String> tipData) {
        for (String string : tipData) {
            Tip tip = new Tip(null, string, matchId);
        }
        List<Tip> list = tipData.stream().map(s -> new Tip(null, s, matchId)).toList();
        tipsRepository.saveTips(list);
    }

    @Override
    public void updateTip(Long tipId, String tipData) {
        Tip tip = tipsRepository.getTip(tipId);
        tipsRepository.saveTips(List.of(tip));
    }

    @Override
    public List<Tip> getMatchTips(Long matchId) {
        return tipsRepository.getTipsPerMatch(matchId);
    }

    @Override
    public void deleteTip(Long tipId) {
        tipsRepository.deleteTip(tipId);
    }
}
