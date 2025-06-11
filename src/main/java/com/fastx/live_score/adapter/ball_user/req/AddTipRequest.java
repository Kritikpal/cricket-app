package com.fastx.live_score.adapter.ball_user.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddTipRequest {

    List<String> tipData;
}
