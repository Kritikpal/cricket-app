package org.fastX;

import org.fastX.enums.DismissType;
import org.fastX.models.Match;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchControllerTest {

//    private MatchController setupMatch() {
//        return MatchController.startMatch(1L, Teams.CSK, Teams.RCB, 20, 2)
//                .startNewInnings(
//                        Teams.RCB.getTeamId(),
//                        Players.getRCBPlayingXI().get(0).getPlayerId(),
//                        Players.getRCBPlayingXI().get(1).getPlayerId()
//                );
//    }

//
//    @Test
//    void fullMatchFlowTest() {
//        try {
//            MatchController matchController = setupMatch()
//                    .startOver(Players.getCSKPlayingXI().get(10).getPlayerId())
//                    .completeDelivery("2")
//                    .completeDelivery("2")
//                    .completeDelivery("4")
//                    .completeDelivery("1")
//                    .completeDelivery("4")
//                    .completeDelivery("1")
//
//                    .startOver(Players.getCSKPlayingXI().get(9).getPlayerId())
//                    .completeDelivery("1w")
//                    .completeDelivery("1")
//                    .completeDelivery("1")
//                    .completeDelivery("1")
//                    .completeDelivery("1")
//                    .completeDelivery("1")
//                    .completeDelivery("1")
//
//                    .startOver(Players.getCSKPlayingXI().get(10).getPlayerId())
//                    .completeDelivery("1")
//                    .completeDelivery("2");
//
//            matchController = matchController.completeDelivery(
//                    DismissType.RUN_OUT,
//                    "1W",
//                    Players.getCSKPlayingXI().get(0).getPlayerId(),
//                    true
//            );
//
//            matchController = matchController
//                    .addBatter(Players.getRCBPlayingXI().get(2).getPlayerId())
//                    .completeDelivery("1")
//                    .completeDelivery("6")
//                    .completeDelivery("4");
//
//            // Assertions: You can check score, players, wickets, etc.
//            Match match = matchController.getMatch();
//            assertEquals(1, match.getInningsList().size());
//            assertEquals(3, match.getCurrentInnings().getBatterInnings().size());
//            assertTrue(match.getCurrentInnings().getBalls().size() > 0);
//
//        } catch (Exception e) {
//            fail("Should not throw exception: " + e.getMessage());
//        }
//    }

}