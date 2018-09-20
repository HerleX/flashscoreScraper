package flashscoreScraper;

import java.util.List;
import java.util.ArrayList;

import flashscoreScraper.LeagueTableEntry;

public class LeagueTable {
	private List<LeagueTableEntry> rows = new ArrayList<LeagueTableEntry>();
	
	
	public LeagueTable() {
		//
	}
	
	public List<LeagueTableEntry> getTable() {
		return rows;
	}
	
	public void addEntry(LeagueTableEntry entry) {
		rows.add(entry);
	}
}
