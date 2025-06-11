package com.fastx.live_score.application.adaptors;

import com.fastx.live_score.domain.in.PlayerService;
import com.fastx.live_score.adapter.admin.request.PlayerRequest;
import com.fastx.live_score.domain.models.match.Player;
import com.fastx.live_score.infra.db.entities.PlayerEntity;
import com.fastx.live_score.application.exception.PlayerNotFoundException;
import com.fastx.live_score.application.mapper.PlayerMapper;
import com.fastx.live_score.infra.db.jpaRepository.PlayerRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void savePlayers(List<PlayerRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            return;
        }
        List<PlayerEntity> entities = requests.stream()
                .map(request -> {
                    PlayerEntity playerEntity = new PlayerEntity();
                    return this.toEntity(playerEntity, request);
                })
                .collect(Collectors.toList());
        playerRepository.saveAll(entities);
    }

    @Override
    public void importPlayersFromCsv(MultipartFile file) {
        try (
                Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        ) {
            CsvToBean<PlayerRequest> csvToBean = new CsvToBeanBuilder<PlayerRequest>(reader)
                    .withType(PlayerRequest.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<PlayerRequest> players = csvToBean.parse();
            savePlayers(players);
        } catch (Exception e) {
            throw new RuntimeException("CSV import failed: " + e.getMessage(), e);
        }
    }

    private PlayerEntity toEntity(PlayerEntity entity, PlayerRequest request) {
        Optional.ofNullable(request.getFullName()).ifPresent(entity::setFullName);
        Optional.ofNullable(request.getShortName()).ifPresent(entity::setShortName);
        Optional.ofNullable(request.getRole()).ifPresent(entity::setRole);
        Optional.ofNullable(request.getBattingStyle()).ifPresent(entity::setBattingStyle);
        Optional.ofNullable(request.getBowlingStyle()).ifPresent(entity::setBowlingStyle);
        Optional.ofNullable(request.getNationality()).ifPresent(entity::setNationality);
        entity.setActive(true);
        return entity;
    }

    @Override
    public Player getPlayerById(Long playerId) {
        PlayerEntity entity = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + playerId));
        return PlayerMapper.toPlayer(entity);
    }

    @Override
    public ByteArrayResource exportPlayersToCsv() {
        List<PlayerEntity> entities = playerRepository.findAll();
        List<PlayerRequest> players = entities.stream()
                .map(entity -> {
                    PlayerRequest req = new PlayerRequest();
                    req.setFullName(entity.getFullName());
                    req.setShortName(entity.getShortName());
                    req.setRole(entity.getRole());
                    req.setBattingStyle(entity.getBattingStyle());
                    req.setBowlingStyle(entity.getBowlingStyle());
                    req.setNationality(entity.getNationality());
                    return req;
                })
                .toList();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("fullName", "shortName", "role", "battingStyle", "bowlingStyle", "nationality"))) {
            for (PlayerRequest player : players) {
                csvPrinter.printRecord(
                        player.getFullName(),
                        player.getShortName(),
                        player.getRole(),
                        player.getBattingStyle(),
                        player.getBowlingStyle(),
                        player.getNationality()
                );
            }

            csvPrinter.flush();
            return new ByteArrayResource(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to export CSV: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Player> listPlayer(String q) {
        List<PlayerEntity> all;
        if (q == null || q.isEmpty()) all = playerRepository.findAll();
        else {
            all = playerRepository.searchByName(q);
        }
        return all.stream()
                .map(PlayerMapper::toPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public void updatePlayer(Long playerId, PlayerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body cannot be null.");
        }

        PlayerEntity existing = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + playerId));

        PlayerEntity updated = this.toEntity(existing, request);
        playerRepository.save(updated);
    }

    @Override
    public void deletePlayer(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException("Player not found with ID: " + playerId);
        }
        playerRepository.deleteById(playerId);
    }

}
