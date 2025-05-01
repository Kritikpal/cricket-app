package com.fastx.live_score.adapter.controller;

import com.fastx.live_score.adapter.models.request.CreatePlayerRequest;
import com.fastx.live_score.adapter.models.response.Player;
import com.fastx.live_score.csv.CsvImporter;
import com.fastx.live_score.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/import")
    public String createPlayer() {
        CsvImporter importer = new CsvImporter();
        List<CreatePlayerRequest> players = importer.readPlayersFromCsv("static/indian_players.csv", CreatePlayerRequest.class);
        playerService.createPlayer(players);
        return "Success";
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/team/{teamId}")
    public List<Player> getPlayersByTeamId(@PathVariable Long teamId) {
        return playerService.getPlayersByTeamId(teamId);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody CreatePlayerRequest request) {
        return playerService.updatePlayer(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }
}
