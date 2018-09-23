package flashscoreScraper.league;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import flashscoreScraper.LeagueTable;
import flashscoreScraper.LeagueTableEntry;
import flashscoreScraper.MatchInfo;

public class DEL {
    private WebDriver driver;
    
    public DEL(String geckoDriverPath, boolean showBrowser) {
        System.setProperty("webdriver.gecko.driver", geckoDriverPath);
        
        FirefoxOptions options = new FirefoxOptions();
        
        if(!showBrowser) {
        	options.setHeadless(true);
        }
        
        driver = new FirefoxDriver(options);
        driver.get("https://www.flashscore.de/eishockey/deutschland/del/");
    }
    
    public void finalize() {
        driver.quit();
    }
    
    public WebElement fluentWait(final By locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });

        return foo;
    };
    
    public List<MatchInfo> getUpcomingMatches() {
        // Fetch match information
        List<MatchInfo> result = new ArrayList<MatchInfo>();
        
        try {
            WebElement table = fluentWait(By.cssSelector("table.hockey:nth-child(4) > tbody:nth-child(3)"));           
            List<WebElement> matches = table.findElements(By.tagName("tr"));
            
            MatchInfo match = null;
            int inc = 0;
            for(WebElement row : matches) {
                if(inc % 2 == 0) {
                    match = new MatchInfo();
                    try {
                        match.startTime = row.findElement(By.cssSelector(".cell_ad.time")).getText();
                        match.liveTime = row.findElement(By.cssSelector(".cell_aa.timer")).getText();
                        
                        // Quick and dirty - remove newlines
                        match.startTime = match.startTime.replace("\n", "").replace("\r", "");
                        match.liveTime = match.liveTime.replace("\n",  "").replace("\r", "");
                        
                        String[] liveTime = match.liveTime.split("(?<=Drittel) ");
                        if(liveTime.length != 0) {
                            match.liveTime = liveTime[0];
                        }
                        
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
    
    public LeagueTable getTable() {
        // Fetch league table information
        LeagueTable result = new LeagueTable();
        
        try {
            driver.get("https://www.flashscore.de/eishockey/deutschland/del/");
        
            WebElement table = driver.findElement(By.className("glib-stats-box-table-overall")).findElement(By.id("table-type-1")).findElement(By.tagName("tbody"));
            List<WebElement> clubs = table.findElements(By.tagName("tr"));
            
            LeagueTableEntry club = null;
            for(WebElement row : clubs) {
                club = new LeagueTableEntry();
                try {
                    club.teamName = row.findElement(By.cssSelector(".team_name_span")).getText();
                    club.defeats = row.findElement(By.cssSelector(".losses_regular")).getText();
                    club.gamesPlayed = row.findElement(By.cssSelector(".matches_played")).getText();
                    club.otDefeats = row.findElement(By.cssSelector(".losses_ot")).getText();
                    club.otWins = row.findElement(By.cssSelector(".wins_ot")).getText();
                    club.wins = row.findElement(By.cssSelector(".wins_regular")).getText();
                    
                    // Special
                    List<WebElement> goalFields = row.findElements(By.cssSelector(".goals"));
                    if(!goalFields.isEmpty()) {
                        club.points = goalFields.get(goalFields.size() - 1).getText();
                    }
                } catch(NoSuchElementException e) {
                    continue;
                }
                result.addEntry(club);
            }
        } catch(Exception e) {
            return new LeagueTable();
        }
        
        return result;
    }
}
