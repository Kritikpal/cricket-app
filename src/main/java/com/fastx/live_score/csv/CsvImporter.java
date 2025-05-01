package com.fastx.live_score.csv;

import com.fastx.live_score.adapter.models.request.CreatePlayerRequest;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class CsvImporter {
    public <T> List<T> readPlayersFromCsv(String filePath, Class<T> clazz) {
        ClassPathResource resource = new ClassPathResource(filePath);
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
