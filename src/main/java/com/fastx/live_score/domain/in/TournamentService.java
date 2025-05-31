package com.fastx.live_score.domain.in;


import com.fastx.live_score.adapter.web.request.TournamentRequest;
import com.fastx.live_score.adapter.web.response.ShortTournamentRes;
import com.fastx.live_score.adapter.web.response.TournamentRes;

import java.util.List;

public interface TournamentService {

    TournamentRes createNewTournament(TournamentRequest request);

    TournamentRes updateTournament(Long tournamentId, TournamentRequest request);

    TournamentRes getTournamentById(Long tournamentId);

    List<ShortTournamentRes> getAllTournaments();

    void deleteTournament(Long tournamentId);

    void assignWinner(Long tournamentId, Long teamId);
}