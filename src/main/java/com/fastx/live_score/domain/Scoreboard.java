package com.fastx.live_score.domain;

import org.fastX.models.Innings;
import org.fastX.models.Match;
import org.fastX.models.Team;

import java.util.List;

public class Scoreboard {
    public String matchTitle;
    public String matchStatus;

    public Team teamA;
    public Team teamB;

    public List<ScorecardInnings> inningsList;

    public static class ScorecardInnings {
        public Long teamId;
        public Innings.ScoreCardState inningsStatus;
        public TeamScore teamScore;
        public List<BatterStats> battingStats;
        public List<BowlerStats> bowlingStats;
        public List<String> oversSummary;
        public Extras extras;
    }

    public static class TeamScore {
        public String teamName;
        public String scoreSummary;
        public String overs;
    }

    public static class BatterStats {
        public String name;
        public int runs;
        public int balls;
        public int fours;
        public int sixes;
        public String dismissal;
        public boolean isStriker;
    }

    public static class BowlerStats {
        public String name;
        public String overs;
        public int runs;
        public int wickets;
        public int extras;
    }

    public static class Extras {
        public int wides;
        public int noBalls;
        public int byes;
        public int legByes;
        public int total;
    }
}
