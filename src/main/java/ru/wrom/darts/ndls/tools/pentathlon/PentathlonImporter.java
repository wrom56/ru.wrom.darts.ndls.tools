package ru.wrom.darts.ndls.tools.pentathlon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import ru.wrom.darts.ndls.tools.CellHelper;
import ru.wrom.darts.ndls.tools.FileFinder;

public class PentathlonImporter {

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YY-MM-dd");

	private final static Logger logger = getLogger(PentathlonImporter.class);

	private String templateFile;
	private String loadPath;

	public PentathlonImporter withTemplateFile(String templateFile) {
		this.templateFile = templateFile;
		return this;
	}

	public PentathlonImporter withLoadPath(String loadPath) {
		this.loadPath = loadPath;
		return this;
	}

	public void importData() throws IOException {

		logger.info("Start import data");

		List<File> files = new ArrayList<>();
		new FileFinder().getFiles(loadPath, ".*xls", files);

		logger.info("Found files: {}", files.toString());

		if (files.isEmpty()) {
			logger.info("Files not found");
			return;
		}

		logger.info("Sort files");
		Collections.sort(files, (o1, o2) -> new Long(o1.lastModified()).compareTo(o2.lastModified()));

		List<PentathlonPlayerData> data = new ArrayList<>();

		for (File file : files) {
			PentathlonDataExtractor extractor = new PentathlonDataExtractor();
			data.addAll(extractor.extract(file));
		}

		logger.info("Player data:");
		for (PentathlonPlayerData pentathlonPlayerData : data) {
			logger.info(pentathlonPlayerData.toString());
		}

		logger.info("Save player data to result file");

		InputStream is = new FileInputStream(templateFile);
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheet("БР");

		logger.debug("Find row for save");
		Integer startRow = 0;
		for (int i = 3; i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			String playerCellValue = CellHelper.getCellValue(row.getCell(1));
			if (playerCellValue == null || playerCellValue.length() == 0) {
				logger.debug("Row found: {}", i);
				startRow = i;
				break;
			}
		}

		logger.info("Start with row: {}", startRow);

		for (PentathlonPlayerData playerData : data) {
			sheet = workbook.getSheet("БР");
			HSSFRow row = sheet.getRow(startRow);
			row.createCell(1).setCellValue(playerData.getPlayerName() + " " + simpleDateFormat.format(new Date(playerData.getFile().lastModified())));
			addPlayerGameData(row, playerData.getBigRound());
			addPlayerGameData(workbook.getSheet("Набор").getRow(startRow), playerData.getAllSectors());
			addPlayerGameData(workbook.getSheet("20").getRow(startRow), playerData.getSector20());
			addPlayerGameData(workbook.getSheet("Булл").getRow(startRow), playerData.getBull());
			addPlayerGameData(workbook.getSheet("Крикет").getRow(startRow), playerData.getCricket());
			startRow++;
		}

		HSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);

		try (FileOutputStream outputStream = new FileOutputStream(new File("result.xls"))) {
			workbook.write(outputStream);
		}

	}

	private void addPlayerGameData(HSSFRow row, List<Integer> gameData) {
		for (int i = 0; i < gameData.size(); i++) {
			HSSFCell cell = row.getCell(i + 2);
			if (cell == null) {
				cell = row.createCell(i + 2);
			}
			cell.setCellValue(gameData.get(i));
		}
	}

}
