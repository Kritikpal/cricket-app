//package org.fastX.service;
//
//import org.fastX.enums.MatchStatus;
//import org.fastX.exception.GameException;
//import org.fastX.models.*;
//
//import java.util.*;
//
//public class ApplyBallEventService {
//
////    public Match applyBallEvent(Match match, BallEvent ballEvent) {
////
////        List<Scorecard> scorecardList = match.getScorecardList();
////        int scoreCardNumber = 0;
////        Scorecard scorecard = scorecardList.get(scoreCardNumber);
////        Long strikerId = scorecard.getStrikerId();
////        Long bowlerId = scorecard.getCurrentBowlerId();
////
////        BallEvent.EventType eventType = ballEvent.getType();
////        int runs = ballEvent.getRuns();
////        boolean isLegalBall = isLegalDelivery(eventType);
////        boolean rotateStrike = false;
////
////        updateTeamScores(scorecard, eventType, runs);
////        processBatsmanStats(scorecard, strikerId, eventType, runs, ballEvent, isLegalBall);
////        processBowlerStats(scorecard, bowlerId, eventType, runs, isLegalBall);
////
////        if (isLegalBall) {
////            updateOverAndBall(scorecard);
////            rotateStrike = shouldRotateStrike(eventType, runs);
////            if (scorecard.getBallNumber() == 0) rotateStrike = !rotateStrike; // end of over
////        }
////
////        if (rotateStrike) {
////            swapStrikers(scorecard);
////        }
////        match.getScorecardList().set(scoreCardNumber, scorecard);
////        return match;
////    }
//
//    public Match startGame(Match match, Player battingTeamId, Player strikerId, Player nonStrikerId, Player bowlerId) {
//
//    }
//
//
//    private void validateMatchOngoing(Match match) {
//        if (match.getMatchStatus() != MatchStatus.ONGOING) {
//            throw new GameException("Game is not live", 401);
//        }
//    }
//
//    private boolean isLegalDelivery(BallEvent.EventType type) {
//        return type != BallEvent.EventType.NO_BALL && type != BallEvent.EventType.WIDE;
//    }
//
//    private boolean shouldRotateStrike(BallEvent.EventType type, int runs) {
//        return switch (type) {
//            case RUN, BYE, LEG_BYE -> runs % 2 == 1;
//            default -> false;
//        };
//    }
//
//    private void updateTeamScores(Scorecard scorecard, BallEvent.EventType type, int runs) {
//        scorecard.setTeamScore(scorecard.getTeamScore() + runs);
//        if (type == BallEvent.EventType.WIDE || type == BallEvent.EventType.NO_BALL
//                || type == BallEvent.EventType.BYE || type == BallEvent.EventType.LEG_BYE) {
//            scorecard.setTotalExtras(scorecard.getTotalExtras() + runs);
//        }
//        if (type == BallEvent.EventType.WICKET) {
//            scorecard.setTotalWickets(scorecard.getTotalWickets() + 1);
//        }
//    }
//
//    private void processBatsmanStats(Scorecard scorecard, Long strikerId, BallEvent.EventType type,
//                                     int runs, BallEvent event, boolean isLegalBall) {
//        BatsmanStats batsman = findOrCreateBatsman(scorecard, strikerId);
//
//        if (isLegalBall) {
//            batsman.setBallsFaced(batsman.getBallsFaced() + 1);
//        }
//
//        if (type == BallEvent.EventType.RUN) {
//            batsman.setRuns(batsman.getRuns() + runs);
//            if (runs == 4) batsman.setBoundaries(batsman.getBoundaries() + 1);
//            if (runs == 6) batsman.setSixes(batsman.getSixes() + 1);
//        } else if (type == BallEvent.EventType.WICKET) {
//            batsman.setOut(true);
//            batsman.setDismissalType(event.getDisMissalType());
//        }
//
//    }
//
//    private void processBowlerStats(Scorecard scorecard, Long bowlerId, BallEvent.EventType type,
//                                    int runs, boolean isLegalBall) {
//        BowlerStats bowler = findOrCreateBowler(scorecard, bowlerId);
//
//        if (isLegalBall) {
//            bowler.setTotalBallsBowled(bowler.getTotalBallsBowled() + 1);
//        }
//
//        bowler.setRunsConceded(bowler.getRunsConceded() + runs);
//
//        switch (type) {
//            case WICKET -> bowler.setWicketsTaken(bowler.getWicketsTaken() + 1);
//            case WIDE -> bowler.setWides(bowler.getWides() + 1);
//            case NO_BALL -> bowler.setNoBalls(bowler.getNoBalls() + 1);
//        }
//
//        if (bowler.getTotalBallsBowled() > 0) {
//            double economy = bowler.getRunsConceded() / (bowler.getTotalBallsBowled() / 6.0);
//            bowler.setEconomyRate(Math.round(economy * 100.0) / 100.0);
//        }
//
//    }
//
//    private void updateOverAndBall(Scorecard scorecard) {
//        scorecard.setBallNumber(scorecard.getBallNumber() + 1);
//        if (scorecard.getBallNumber() == 6) {
//            scorecard.setBallNumber(0);
//            scorecard.setOverNo(scorecard.getOverNo() + 1);
//        }
//    }
//
//    private void swapStrikers(Scorecard scorecard) {
//        Long temp = scorecard.getStrikerId();
//        scorecard.setStrikerId(scorecard.getNonStrikerId());
//        scorecard.setNonStrikerId(temp);
//    }
//
//    private void validateMatchSetup(Match match, Long strikerId, Long nonStrikerId, Long bowlerId) {
//        if (match == null || match.getBattingTeam() == null || match.getBowlingTeam() == null) {
//            throw new GameException("Match or teams not properly configured", 400);
//        }
//
//        if (match.getBattingTeam().getTeamId().equals(match.getBowlingTeam().getTeamId())) {
//            throw new GameException("Choose different teams", 400);
//        }
//
//        if (match.getBattingTeam().getLinUp().size() != 11 || match.getBowlingTeam().getLinUp().size() != 11) {
//            throw new GameException("Each team must have exactly 11 players", 400);
//        }
//
//        if (isNotPlayerInTeam(bowlerId, match.getBowlingTeam().getLinUp())) {
//            throw new GameException("Invalid Bowler", 400);
//        }
//
//        if (isNotPlayerInTeam(strikerId, match.getBattingTeam().getLinUp()) ||
//                isNotPlayerInTeam(nonStrikerId, match.getBattingTeam().getLinUp())) {
//            throw new GameException("Invalid Striker or Non-Striker", 400);
//        }
//    }
//
//    private void validateScoreBoard(Match match) {
//        Team battingTeam = match.getBattingTeam();
//        Team bowlingTeam = match.getBowlingTeam();
//        Scorecard scorecard = match.getScorecardList().get(0);
//
//        if (!Set.of(battingTeam.getTeamId(), bowlingTeam.getTeamId()).contains(scorecard.getTeamId())) {
//            throw new GameException("Invalid TeamId selected", 400);
//        }
//
//        validateTeamSquad(battingTeam.getLinUp(), "Team A");
//        validateTeamSquad(bowlingTeam.getLinUp(), "Team B");
//
//        if (scorecard.getStrikerId() == null || scorecard.getNonStrikerId() == null || scorecard.getCurrentBowlerId() == null) {
//            throw new GameException("Striker, Non-Striker, or Bowler is not set", 400);
//        }
//
//        if (isNotPlayerInTeam(scorecard.getStrikerId(), battingTeam.getLinUp()) ||
//                isNotPlayerInTeam(scorecard.getNonStrikerId(), battingTeam.getLinUp())) {
//            throw new GameException("Striker and Non-Striker must belong to the batting team", 400);
//        }
//
//        if (isNotPlayerInTeam(scorecard.getCurrentBowlerId(), bowlingTeam.getLinUp())) {
//            throw new GameException("Bowler must belong to the bowling team", 400);
//        }
//
//        if (Set.of(scorecard.getStrikerId(), scorecard.getNonStrikerId()).contains(scorecard.getCurrentBowlerId())) {
//            throw new GameException("Bowler cannot be striker or non-striker", 400);
//        }
//    }
//
//    private void validateTeamSquad(List<Player> squad, String teamName) {
//        if (squad.size() != 11) {
//            throw new GameException(teamName + " must have exactly 11 players", 400);
//        }
//
//        Set<Long> ids = new HashSet<>();
//        for (Player player : squad) {
//            if (!ids.add(player.getPlayerId())) {
//                throw new GameException("Duplicate player ID " + player.getPlayerId() + " found in " + teamName, 400);
//            }
//        }
//    }
//
//    private boolean isNotPlayerInTeam(Long playerId, List<Player> team) {
//        return team.stream().noneMatch(p -> p.getPlayerId().equals(playerId));
//    }
//
//    private BatsmanStats findOrCreateBatsman(Scorecard scorecard, Long playerId) {
//        return scorecard.getBatsmanStats().stream()
//                .filter(b -> b.getPlayerId().equals(playerId))
//                .findFirst()
//                .orElseGet(() -> {
//                    BatsmanStats b = BatsmanStats.builder()
//                            .playerId(playerId)
//                            .runs(0)
//                            .ballsFaced(0)
//                            .boundaries(0)
//                            .sixes(0)
//                            .isOut(false)
//                            .dismissalType(null)
//                            .build();
//                    scorecard.getBatsmanStats().add(b);
//                    return b;
//                });
//    }
//
//    private BowlerStats findOrCreateBowler(Scorecard scorecard, Long playerId) {
//        return scorecard.getBowlerStats().stream()
//                .filter(b -> b.getPlayerId().equals(playerId))
//                .findFirst()
//                .orElseGet(() -> {
//                    BowlerStats b = BowlerStats.builder()
//                            .playerId(playerId)
//                            .totalBallsBowled(0)
//                            .runsConceded(0)
//                            .wicketsTaken(0)
//                            .wides(0)
//                            .noBalls(0)
//                            .economyRate(0)
//                            .build();
//                    scorecard.getBowlerStats().add(b);
//                    return b;
//                });
//    }
//}
