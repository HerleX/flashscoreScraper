package flashscoreScraper.league;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import flashscoreScraper.MatchInfo;

public class DEL {
	private WebDriver driver;
	
	public DEL() {
		System.setProperty("webdriver.gecko.driver", "C:\\downloads\\geckodriver-v0.21.0-win64\\geckodriver.exe");
		
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		driver = new FirefoxDriver(options);
	}
	
	public void finalize() {
		driver.quit();
	}
	
	public List<MatchInfo> getUpcomingMatches() {		
		// Fetch match information
		List<MatchInfo> result = new ArrayList<MatchInfo>();
		
		try {
			driver.get("https://www.flashscore.de/eishockey/deutschland/del/");
		
			WebElement table = driver.findElement(By.id("fs")).findElement(By.className("hockey")).findElement(By.tagName("tbody"));
			List<WebElement> matches = table.findElements(By.tagName("tr"));
			
			MatchInfo match = null;
			int inc = 0;
			for(WebElement row : matches) {
				if(inc % 2 == 0) {
					match = new MatchInfo();
					try {
						match.startTime = row.findElement(By.cssSelector(".cell_ad.time")).getText();
						
						// Quick and dirty - remove newlines
						match.startTime = match.startTime.replace("\n", "").replace("\r", "");
						
						match.liveTime = row.findElement(By.cssSelector(".cell_aa.timer")).getText();
						match.teamHome = row.findElement(By.cssSelector(".cell_ab.team-home")).getText();
						match.scoreHome = row.findElement(By.cssSelector(".cell_sc.score-home")).getText();
					} catch(NoSuchElementException e) {
						continue;
					}
				} else {
					try {
						match.teamAway = row.findElement(By.cssSelector(".cell_ac.team-away")).getText();
						match.scoreAway = row.findElement(By.cssSelector(".cell_ta.score-away")).getText();
					} catch(NoSuchElementException e) {
						continue;
					}
					
					result.add(match);
				}
				
				++inc;
			}
		} catch(Exception e) {
			return new ArrayList<MatchInfo>();
		}
		
		return result;
	}
}
