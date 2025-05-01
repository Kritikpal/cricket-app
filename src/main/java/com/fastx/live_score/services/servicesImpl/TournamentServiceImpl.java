package com.fastx.live_score.services.servicesImpl;

import com.fastx.live_score.adapter.models.request.CreateTournamentRequest;
import com.fastx.live_score.adapter.models.response.Tournament;
import com.fastx.live_score.entities.MatchEntity;
import com.fastx.live_score.entities.TeamEntity;
import com.fastx.live_score.entities.TournamentEntity;
import com.fastx.live_score.repository.MatchRepository;
import com.fastx.live_score.repository.TeamRepository;
import com.fastx.live_score.repository.TournamentRepository;
import com.fastx.live_score.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService {
    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public TournamentServiceImpl(MatchRepository matchRepository, TournamentRepository tournamentRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Tournament createTournament(CreateTournamentRequest request) {
        TournamentEntity formTournamentRequest = createFormTournamentRequest(request);
        TournamentEntity tournamentEntity = tournamentRepository.save(formTournamentRequest);
        return mapToTournaMent(tournamentEntity);
    }

    @Override
    public Tournament getTournamentById(Long tournamentId) {
        TournamentEntity tournamentEntity = tournamentRepository.findById(tournamentId).orElseThrow();
        return mapToTournaMent(tournamentEntity);
    }

    @Override
    public List<Tournament> getAllTournaments() {
        List<TournamentEntity> tournamentEntityList = tournamentRepository.findAll();
        return tournamentEntityList.stream().map(this::mapToTournaMent).toList();
    }

    @Override
    public Tournament updateTournament(Long tournamentId, CreateTournamentRequest request) {
        return null;
    }

    @Override
    public void deleteTournament(Long tournamentId) {
        tournamentRepository.deleteById(tournamentId);
    }

    @Override
    public void assignWinner(Long tournamentId, Long teamId) {
        TeamEntity teamEntity = teamRepository.findById(teamId).orElseThrow();
        TournamentEntity tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        tournament.setWinner(teamEntity);
        tournamentRepository.save(tournament);
    }


    private TournamentEntity createFormTournamentRequest(CreateTournamentRequest request) {
        TournamentEntity entity = new TournamentEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setLocation(request.getLocation());
        if (request.getParticipatingTeamIds() != null) {
            List<Long> participatingTeamIds = request.getParticipatingTeamIds();
            for (Long participatingTeamId : participatingTeamIds) {
                MatchEntity matchEntity = matchRepository.findById(participatingTeamId).orElseThrow();
                entity.getMatches().add(matchEntity);
            }
        }
        return entity;
    }

    private Tournament mapToTournaMent(TournamentEntity entity) {
        Tournament.TournamentBuilder builder = Tournament.builder();
        builder.id(entity.getId());
        builder.name(entity.getName());
        builder.startDate(entity.getStartDate());
        builder.endDate(entity.getEndDate());
        builder.location(entity.getLocation());
        builder.matches(
                entity.getMatches().stream().map(
                        matchEntity -> new Tournament.ShortMatch(
                                matchEntity.getId(),
                                matchEntity.getTeamEntityA().getName(),
                                matchEntity.getTeamEntityB().getName()
                        )
                ).toList()
        );
        return builder.build();
    }

}
