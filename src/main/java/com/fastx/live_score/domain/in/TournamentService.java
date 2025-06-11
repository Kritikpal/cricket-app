package com.fastx.live_score.domain.in;


import com.fastx.live_score.adapter.admin.request.TournamentRequest;
import com.fastx.live_score.domain.models.match.Tournament;

import java.util.List;

public interface TournamentService {

    Tournament createNewTournament(TournamentRequest request);

    Tournament updateTournament(Long tournamentId, TournamentRequest request);

    Tournament getTournamentById(Long tournamentId);

    List<Tournament> getAllTournaments();

    void deleteTournament(Long tournamentId);

    void assignWinner(Long tournamentId, Long teamId);
}