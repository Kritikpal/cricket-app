package com.fastx.live_score.services;


import com.fastx.live_score.adapter.models.request.CreateTournamentRequest;
import com.fastx.live_score.adapter.models.response.Tournament;

import java.util.List;

public interface TournamentService {

    Tournament createTournament(CreateTournamentRequest request);

    Tournament getTournamentById(Long tournamentId);

    List<Tournament> getAllTournaments();

    Tournament updateTournament(Long tournamentId, CreateTournamentRequest request);

    void deleteTournament(Long tournamentId);

    void assignWinner(Long tournamentId, Long teamId);
}