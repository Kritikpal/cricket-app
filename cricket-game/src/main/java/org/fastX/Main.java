//package org.fastX;
//
//import org.fastX.enums.DismissType;
//import org.fastX.models.*;
//
//public class Main {
//    public static void main(String[] args) {
//
//        try {
//            MatchController matchController = MatchController.startMatch(1L, Teams.CSK, Teams.RCB, 20, 2)
////                    .startNewInnings(Teams.RCB.getTeamId())
////                    .addBatter(Players.getRCBPlayingXI().get(0).getPlayerId())
////                    .addBatter(Players.getRCBPlayingXI().get(1).getPlayerId())
////                    .startOver(Players.getCSKPlayingXI().get(10).getPlayerId());
////                    .completeDelivery("2")
////                    .completeDelivery("2")
////                    .completeDelivery("4")
////                    .completeDelivery("1")
////                    .completeDelivery("4")
////                    .completeDelivery("1")
////                    .startOver(Players.getCSKPlayingXI().get(9).getPlayerId())
////                    .completeDelivery("1w")
////                    .completeDelivery("1")
////                    .completeDelivery("1")
////                    .completeDelivery("1")
////                    .completeDelivery("1")
////                    .completeDelivery("1")
////                    .completeDelivery("1")
////
////                    .startOver(Players.getCSKPlayingXI().get(10).getPlayerId())
////                    .completeDelivery("1")
////                    .completeDelivery("2");
//
////            matchController = matchController.completeDelivery(DismissType.RUN_OUT,
////                    "1W", Players.getCSKPlayingXI().get(0).getPlayerId(),
////                    true);
////
////            matchController = matchController.addBatter(Players.getRCBPlayingXI().get(2).getPlayerId())
////                    .completeDelivery("1")
////                    .completeDelivery("6")
////                    .completeDelivery("4");
//            new PrintService(matchController.getMatch()).printMethod();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//}