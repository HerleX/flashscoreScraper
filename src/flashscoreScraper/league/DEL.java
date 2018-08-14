package flashscoreScraper.league;

import java.util.ArrayList;
import java.util.List;

import flashscoreScraper.MatchInfo;

public class DEL {
	public static List<MatchInfo> getUpcomingMatches() {
		MatchInfo match = new MatchInfo();
		
		// Fetch match information
		List<MatchInfo> result = new ArrayList<MatchInfo>();
		result.add(match);
		
		return result;
	}
}
