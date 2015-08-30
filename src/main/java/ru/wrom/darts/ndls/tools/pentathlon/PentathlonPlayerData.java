package ru.wrom.darts.ndls.tools.pentathlon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PentathlonPlayerData {
	private String playerName;
	private File file;
	private List<Integer> bigRound = new ArrayList<>();
	private List<Integer> allSectors = new ArrayList<>();
	private List<Integer> sector20 = new ArrayList<>();
	private List<Integer> bull = new ArrayList<>();
	private List<Integer> cricket = new ArrayList<>();

	public String getPlayerName() {
		return playerName;
	}

	public List<Integer> getBigRound() {
		return bigRound;
	}

	public List<Integer> getAllSectors() {
		return allSectors;
	}

	public List<Integer> getSector20() {
		return sector20;
	}

	public List<Integer> getBull() {
		return bull;
	}

	public List<Integer> getCricket() {
		return cricket;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PentathlonPlayerData{");
		sb.append("playerName='").append(playerName).append('\'');
		sb.append(", file=").append(file);
		sb.append(", bigRound=").append(bigRound);
		sb.append(", allSectors=").append(allSectors);
		sb.append(", sector20=").append(sector20);
		sb.append(", bull=").append(bull);
		sb.append(", cricket=").append(cricket);
		sb.append('}');
		return sb.toString();
	}
}
