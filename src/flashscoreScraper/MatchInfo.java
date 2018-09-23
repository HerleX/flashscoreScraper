package flashscoreScraper;

import flashscoreScraper.helperFunctions;

public class MatchInfo {
    public String teamHome;
    public String teamAway;
    public String scoreHome;
    public String scoreAway;
    public String liveTime;
    public String startTime;
    
    public MatchInfo() {
        teamHome = "#teamHome#";
        teamAway = "#teamAway#";
        scoreHome = "null";
        scoreAway = "null";
        liveTime = "0";
        startTime = "0";
    }
    
    public String getLiveTime() {
        if(helperFunctions.isStringNullOrWhiteSpace(liveTime)) {
            return "Vor dem Spiel";
        } else {
            return liveTime;
        }
    }
    
    public String getFormatted() {
        return getLiveTime() + " | " + startTime + " Uhr | " + teamHome + " " + scoreHome + " - " + scoreAway + " " + teamAway;
    }
}
