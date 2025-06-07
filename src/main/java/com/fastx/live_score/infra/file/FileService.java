package com.fastx.live_score.infra.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileService {

    private final String baseFolder;

    public FileService(String baseFolder) {
        this.baseFolder = baseFolder;
        File folder = new File(baseFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public String writeMatchCacheFile(long matchId, byte[] content) throws IOException {
        String dateFolder = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        Path dirPath = Paths.get(baseFolder, String.valueOf(matchId), dateFolder);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        String fileName = "match_cache_" + matchId + ".bin";
        Path filePath = dirPath.resolve(fileName);

        Files.write(filePath, content);
        return filePath.toAbsolutePath().toString();
    }

    public byte[] readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    public void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException ignored) {
        }
    }
}
