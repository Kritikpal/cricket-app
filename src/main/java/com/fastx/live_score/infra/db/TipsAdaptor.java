package com.fastx.live_score.infra.db;

import com.fastx.live_score.domain.models.tips.Tip;
import com.fastx.live_score.domain.out.TipsRepository;
import com.fastx.live_score.infra.db.entities.TipEntity;
import com.fastx.live_score.infra.db.jpaRepository.MatchEntityRepository;
import com.fastx.live_score.infra.db.jpaRepository.TipEntityRepository;
import lombok.RequiredArgsConstructor;
import org.fastX.exception.GameException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipsAdaptor implements TipsRepository {

    private final TipEntityRepository tipEntityRepository;

    private final MatchEntityRepository matchEntityRepository;

    @Override
    public void saveTips(List<Tip> tips) {
        List<TipEntity> list = tips.stream().map(tip -> {
            TipEntity tipEntity = new TipEntity();
            tipEntity.setTipData(tip.tipData());
            tipEntity.setMatchEntity(matchEntityRepository.findById(tip.matchId()).orElseThrow(() -> new GameException("No match available for this match id", 400)));
            return tipEntity;
        }).toList();
        tipEntityRepository.saveAll(list);

    }

    @Override
    public List<Tip> getTipsPerMatch(Long matchId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<TipEntity> tipsPerMatch = tipEntityRepository.getTipsPerMatch(matchId, sort);
        return tipsPerMatch.stream().map(TipsAdaptor::getTipFromEntity).toList();
    }

    @Override
    public Tip getTip(Long tipId) {

        TipEntity tipEntity = tipEntityRepository.findById(tipId).orElseThrow(() ->
                new GameException("Tip not found", 400));

        return getTipFromEntity(tipEntity);
    }

    private static Tip getTipFromEntity(TipEntity tipEntity) {
        return new Tip(
                tipEntity.getId(),
                tipEntity.getTipData(),
                tipEntity.getMatchEntity().getId()
        );
    }

    @Override
    public void deleteTip(Long tipId) {
        tipEntityRepository.deleteById(tipId);
    }
}
