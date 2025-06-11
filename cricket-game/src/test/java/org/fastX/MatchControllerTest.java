package org.fastX;

import org.fastX.enums.DismissType;
import org.fastX.models.Players;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.fastX.exception.GameException;
import org.fastX.models.*;
import org.junit.jupiter.api.BeforeEach;

class MatchControllerTest {

    private Team teamA;
    private Team teamB;
    private Player striker;
    private Player nonStriker;
    private Player bowler;
    private MatchController controller;

    @BeforeEach
    void setUp() {
        striker = Players.getRCBPlayingXI().get(0);
        nonStriker = Players.getRCBPlayingXI().get(1);
        bowler = Players.getCSKPlayingXI().get(10);

        teamA = Teams.RCB;
        teamB = Teams.CSK;

        controller = MatchController.startMatch(1L, teamA, teamB, 20, 2);
    }

    @Test
    void testStartMatchCreatesMatch() {
        assertNotNull(controller.getMatch());
        assertEquals(teamA, controller.getMatch().matchInfo().teamA());
        assertEquals(teamB, controller.getMatch().matchInfo().teamB());
    }

    @Test
    void testStartNewInningsSetsCorrectPlayers() {
        controller.startNewInnings(teamA.teamId(), striker.playerId(), nonStriker.playerId(), bowler.playerId());

        Innings innings = controller.getMatch().currentInnings();
        assertEquals(striker, innings.striker().player());
        assertEquals(nonStriker, innings.nonStriker().player());
        assertEquals(bowler, innings.currentBowler().player());
    }

    @Test
    void testStartOverIncrementsOverNumber() {
        controller.startNewInnings(teamA.teamId(), striker.playerId(),
                nonStriker.playerId(), bowler.playerId());

        Innings innings = controller.getMatch().currentInnings();
        assertEquals(1, innings.overs().size());
    }

    @Test
    void testAddBatterToInnings() {
        Player newBatter = Players.getRCBPlayingXI().get(1);
        controller.startNewInnings(teamA.teamId(), striker.playerId(), nonStriker.playerId(), bowler.playerId());
        controller.addBatter(newBatter.playerId());

        assertTrue(controller.getMatch().currentInnings().batterInnings().stream()
                .anyMatch(bp -> bp.player().equals(newBatter)));
    }

    @Test
    void testCompleteDeliveryParsesScoreCorrectly() {
        controller.startNewInnings(teamA.teamId(),
                striker.playerId(),
                nonStriker.playerId(), bowler.playerId());
        controller.startOver(bowler.playerId());
        controller.completeDelivery("1");
        controller.completeDelivery("1");
        controller.completeDelivery("1");
        controller.completeDelivery("1");
        assertEquals(4, controller.getMatch().currentInnings().balls().balls().size());
    }

    @Test
    void testCompleteDeliveryWithWicket() {
        controller.startNewInnings(teamA.teamId(), striker.playerId(), nonStriker.playerId(), bowler.playerId());
        controller.completeDelivery("W", DismissType.BOWLED, null, striker.playerId());
        assertEquals(1, controller.getMatch().currentInnings().balls().balls().size());
    }

    @Test
    void testInvalidTeamThrowsException() {
        Exception ex = assertThrows(GameException.class, () ->
                controller.startNewInnings(999L, striker.playerId(), nonStriker.playerId(), bowler.playerId())
        );
        assertEquals("Select valid team", ex.getMessage());
    }
}
