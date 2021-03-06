package flashscoreScraper.league;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import flashscoreScraper.MatchInfo;

public class DEL {
	private Document webPage;
	private List<String> matchUrls;
	private List<Document> matchPages;
	
	public DEL(String url, String league) {
		matchUrls = new ArrayList<String>();
		matchPages = new ArrayList<Document>();
		
		try {
		      // fetch the document over HTTP
		      webPage = Jsoup.connect(url).get();
		      Elements matches = webPage.select("h4 ~ a");
		      for(Element match : matches) {
		    	  if(findParentLeague(match).equalsIgnoreCase(league)) {
		    		  String matchUrl = match.attr("abs:href");
		    		  matchUrls.add(matchUrl);
		    	  }
		      }
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
    }
    
	private String findParentLeague(Element child) {
		Element parent = child.previousElementSibling();
		while(parent != null) {
			if(parent.tagName().equalsIgnoreCase("h4")) {
				return parent.text();
			}
			
			parent = parent.previousElementSibling();
		}
		
		return "";
	}
	
	private void fetchMatchPages() throws IOException {
		matchPages.clear();
		for(String url : matchUrls) {
			matchPages.add( Jsoup.connect(url).get() );
		}
	}
	
    public List<MatchInfo> getUpcomingMatches() {
    	try {
			fetchMatchPages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        // Fetch match information
        List<MatchInfo> result = new ArrayList<MatchInfo>();
        
        for(Document matchPage : matchPages) {
        	MatchInfo match = new MatchInfo();
        	String participants = matchPage.select("h3").text();
        	match.teamHome = participants.split("-")[0];
        	match.teamAway = participants.split("-")[1];
        	
        	Elements liveData = matchPage.select("div.detail > span.live");
        	if(liveData.size() == 2) {
        		match.isLive = true;
        		String score = liveData.first().text();
        		match.scoreHome = score.split(":")[0];
        		match.scoreAway = score.split(":")[1];
        		
        		match.liveTime = liveData.last().text();
        	} else {
        		match.isLive = false;
        		liveData = matchPage.select("div.detail");
        		match.liveTime = liveData.size() > 1 ? liveData.get(1).text() : "";
        		match.startTime = liveData.size() > 2 ? liveData.get(2).text() : "";
        		
        		String score = matchPage.select("div.detail > b").text();
        		match.scoreHome = score.split(":")[0];
        		match.scoreAway = score.split(":")[1];
        	}
        	
        	match.cleanUp();
        	result.add(match);
        }
        return result;
    }
}
