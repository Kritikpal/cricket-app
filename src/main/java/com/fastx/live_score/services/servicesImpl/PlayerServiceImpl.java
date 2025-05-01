package com.fastx.live_score.services.servicesImpl;

import com.fastx.live_score.adapter.models.request.CreatePlayerRequest;
import com.fastx.live_score.adapter.models.response.Player;
import com.fastx.live_score.entities.PlayerEntity;
import com.fastx.live_score.repository.PlayerRepository;
import com.fastx.live_score.repository.TeamRepository;
import com.fastx.live_score.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void createPlayer(List<CreatePlayerRequest> requests) {
        List<PlayerEntity> entities = requests.stream()
                .map(req -> getPlayerEntity(new PlayerEntity(), req))
                .toList();

        playerRepository.saveAll(entities);
    }

    @Override
    public Player getPlayerById(Long playerId) {
        PlayerEntity entity = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        return mapToResponse(entity);
    }

    @Override
    public List<Player> getPlayersByTeamId(Long teamId) {
        return playerRepository.findByTeamId(teamId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Player updatePlayer(Long playerId, CreatePlayerRequest request) {
        PlayerEntity entity = getPlayerEntity(playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId)), request);

        return mapToResponse(playerRepository.save(entity));
    }

    @Override
    public void deletePlayer(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new IllegalArgumentException("Player not found with ID: " + playerId);
        }
        playerRepository.deleteById(playerId);
    }

    private Player mapToResponse(PlayerEntity entity) {
        Player.PlayerBuilder playerBuilder = Player.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .shortName(entity.getShortName())
                .nationality(entity.getNationality())
                .role(entity.getRole())
                .battingStyle(entity.getBattingStyle())
                .bowlingStyle(entity.getBowlingStyle())
                .totalMatches(entity.getTotalMatches())
                .totalRuns(entity.getTotalRuns())
                .totalWickets(entity.getTotalWickets())
                .isActive(entity.isActive());
        List<Player.ShortTeam> list = entity.getTeams().stream().map(
                teamEntity ->
                        new Player.ShortTeam(teamEntity.getId(), teamEntity.getName())
        ).toList();
        playerBuilder.teams(list);
        return playerBuilder.build();
    }


    private static PlayerEntity getPlayerEntity(PlayerEntity playerEntity, CreatePlayerRequest request) {
        playerEntity.setFullName(request.getFullName());
        playerEntity.setRole(request.getRole());
        playerEntity.setBattingStyle(request.getBattingStyle());
        playerEntity.setBowlingStyle(request.getBowlingStyle());
        playerEntity.setNationality(request.getNationality());
        playerEntity.setShortName(request.getShortName());
        playerEntity.setActive(true);
        return playerEntity;
    }

}
