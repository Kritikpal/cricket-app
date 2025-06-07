package com.fastx.live_score.infra.file;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {

    private final String baseFolder;

    public FileService() {
        this.baseFolder = "src/main/resources/static/";
        File folder = new File(baseFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * Write byte[] content into a file inside /{baseFolder}/{matchId}/{yyyy-MM-dd}/ directory.
     * Automatically creates directories if they don't exist.
     * Returns absolute file path.
     */
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

    /**
     * Read file content by absolute path.
     */
    public byte[] readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    /**
     * Delete file by absolute path.
     */
    public void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            // Log if needed
        }
    }
}