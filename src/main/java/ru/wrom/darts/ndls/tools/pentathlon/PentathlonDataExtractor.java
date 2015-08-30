package ru.wrom.darts.ndls.tools.pentathlon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import ru.wrom.darts.ndls.tools.CellHelper;

public class PentathlonDataExtractor {

	private final static Logger logger = getLogger(PentathlonDataExtractor.class);

	private HSSFWorkbook workbook;
	private File file;

	public List<PentathlonPlayerData> extract(File file) throws IOException {

		this.file = file;

		List<PentathlonPlayerData> result = new ArrayList<>();
		logger.debug("Extract data from file: " + file);

		try (InputStream is = new FileInputStream(file)) {
			workbook = new HSSFWorkbook(is);
			HSSFSheet sheet = workbook.getSheet("БР");

			logger.debug("Find players data");

			for (int i = 3; i <= sheet.getLastRowNum(); i++) {
				HSSFRow row = sheet.getRow(i);
				HSSFCell positionCell = row.getCell(24);
				String positionCellValue = CellHelper.getCellValue(positionCell);
				if (positionCellValue != null && !positionCellValue.equals("1000")) {
					logger.debug(MessageFormat.format("Player found. Row num: {0}, position: {1}", i, positionCellValue));
					result.add(getPlayerData(i));
				}
			}

			if (!result.isEmpty() && checkDuplicatePlayer(result)) {
				return Collections.singletonList(result.get(result.size() - 1));
			}

			return result;
		}
	}

	private boolean checkDuplicatePlayer(List<PentathlonPlayerData> result) {
		logger.info("Check for duplicate player");
		String previousPlayerName = null;
		for (PentathlonPlayerData data : result) {
			if (previousPlayerName != null) {
				if (checkDuplicatePlayerName(previousPlayerName, data.getPlayerName())) {
					logger.debug("Find duplicate player. {} and {}", previousPlayerName, data.getPlayerName());
					return true;
				}
			}
			previousPlayerName = data.getPlayerName();
		}
		return false;
	}

	private boolean checkDuplicatePlayerName(String previousPlayerName, String playerName) {
		logger.trace("Check duplicate player. {} and {}", previousPlayerName, playerName);
		if (previousPlayerName == null || playerName == null || previousPlayerName.isEmpty() || playerName.isEmpty()) {
			return false;
		}
		return previousPlayerName.substring(0, Math.min(10, previousPlayerName.length())).equalsIgnoreCase(playerName.substring(0, Math.min(10, playerName.length())));
	}

	private PentathlonPlayerData getPlayerData(Integer rowNum) {
		PentathlonPlayerData data = new PentathlonPlayerData();
		data.setFile(file);
		HSSFSheet sheet = workbook.getSheet("БР");
		HSSFRow row = sheet.getRow(rowNum);
		data.setPlayerName(CellHelper.getCellValue(row.getCell(1)));

		fillGameData(row, 21, data.getBigRound());
		fillGameData(workbook.getSheet("Набор").getRow(rowNum), 10, data.getAllSectors());
		fillGameData(workbook.getSheet("20").getRow(rowNum), 10, data.getSector20());
		fillGameData(workbook.getSheet("Булл").getRow(rowNum), 10, data.getBull());
		fillGameData(workbook.getSheet("Крикет").getRow(rowNum), 22, data.getCricket());

		logger.debug("Player data: " + data.toString());
		return data;
	}

	private void fillGameData(HSSFRow row, Integer count, List<Integer> gameData) {
		for (int i = 0; i < count; i++) {
			gameData.add(CellHelper.getCellIntValue(row.getCell(i + 2)));
		}
	}

}
