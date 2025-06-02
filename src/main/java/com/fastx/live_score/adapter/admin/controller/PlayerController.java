package com.fastx.live_score.adapter.admin.controller;

import com.fastx.live_score.adapter.admin.response.ListPlayerRes;
import com.fastx.live_score.core.config.ApiDocsTags;
import com.fastx.live_score.adapter.admin.request.PlayerRequest;
import com.fastx.live_score.domain.models.Player;
import com.fastx.live_score.domain.in.PlayerService;
import com.fastx.live_score.core.utils.AppResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/players")
@Tag(name = "player", description = "All api for players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Tag(name = "import")
    @PostMapping("/import")
    public AppResponse<String> importPlayer(@RequestParam("file") MultipartFile file) {
        playerService.importPlayersFromCsv(file);
        return AppResponse.success(null, "Players imported successfully.");
    }

    @PostMapping("/create")
    public AppResponse<String> savePlayer(@RequestBody PlayerRequest player) {
        playerService.savePlayers(List.of(player));
        return AppResponse.success(null, "Player created successfully.");
    }

    @GetMapping("/getPlayerInfo/{id}")
    public AppResponse<Player> getPlayerById(@PathVariable Long id) {
        return AppResponse.success(playerService.getPlayerById(id));
    }

    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportPlayersCsv() {
        ByteArrayResource csv = playerService.exportPlayersToCsv();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=players.csv")
                .header("Content-Type", "text/csv")
                .body(csv);
    }

    @Tag(name = ApiDocsTags.SEARCH)
    @GetMapping("/searchPlayers")
    public AppResponse<List<ListPlayerRes>> searchPlayer(@RequestParam(value = "q", required = false) String q) {
        return AppResponse.success(playerService.listPlayer(q).stream().map(ListPlayerRes::toShortPlayer).toList());
    }


    @PutMapping("/updatePlayer/{id}")
    public AppResponse<String> updatePlayer(@PathVariable Long id, @RequestBody PlayerRequest request) {
        playerService.updatePlayer(id, request);
        return AppResponse.success("Player updated successfully");
    }

    @DeleteMapping("/deletePlayer/{id}")
    public AppResponse<String> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return AppResponse.success("Player deleted successfully.");
    }
}
