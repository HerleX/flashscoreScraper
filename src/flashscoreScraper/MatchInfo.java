package flashscoreScraper;

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
	
	public String formatted() {
		return liveTime + " min | " + startTime + " Uhr | " + teamHome + " " + scoreHome + " - " + scoreAway + " " + teamAway;
	}
}
