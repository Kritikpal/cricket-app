package com.fastx.live_score.adapter.models.request;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlayerRequest {

    @CsvBindByName(column = "fullName")
    private String fullName;

    @CsvBindByName(column = "shortName")
    private String shortName;

    @CsvBindByName(column = "nationality")
    private String nationality;

    @CsvBindByName(column = "battingStyle")
    private String battingStyle;

    @CsvBindByName(column = "bowlingStyle")
    private String bowlingStyle;

    @CsvBindByName(column = "role")
    private String role;

}
