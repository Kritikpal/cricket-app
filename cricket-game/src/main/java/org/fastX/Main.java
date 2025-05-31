package org.fastX;

import org.fastX.models.*;

public class Main {
    public static void main(String[] args) {

        try {
            MatchController matchController = MatchController.startMatch(1L, Teams.CSK, Teams.RCB, 20, 2)
                    .startNewInnings(Teams.RCB)
                    .addBatter(Players.getRCBPlayingXI().get(0).getPlayerId())
                    .addBatter(Players.getRCBPlayingXI().get(1).getPlayerId())
                    .startOver(Players.getCSKPlayingXI().get(10).getPlayerId())
                    .completeDelivery("1")
                    .completeDelivery("1")
                    .completeDelivery("1")
                    .completeDelivery("1")
                    .completeDelivery("1")
                    .completeDelivery("1")
                    .startOver(Players.getCSKPlayingXI().get(9).getPlayerId())
                    .completeDelivery("1w")
                    .completeDelivery("1")
                    .completeDelivery("1")
                    .completeDelivery("1")
                    .completeDelivery("1")
                    .completeDelivery("1")
                    .completeDelivery("1")
//                    .startOver(12L)
//                    .completeDelivery("1")
//                    .completeDelivery("2")
                    ;
            Match match = matchController.getMatch();
            new PrintService(match).printMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}