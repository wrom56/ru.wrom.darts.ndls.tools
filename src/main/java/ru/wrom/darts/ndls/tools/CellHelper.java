package ru.wrom.darts.ndls.tools;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class CellHelper {

	private final static Logger logger = getLogger(CellHelper.class);

	public static String getCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		try {
			return cell.getStringCellValue();
		} catch (java.lang.IllegalStateException e) {
			return String.valueOf(cell.getNumericCellValue());
		}
	}

	public static int getCellIntValue(HSSFCell cell) {
		Integer result = getCellIntegerValue(cell);
		return result != null ? result : 0;
	}

	public static Integer getCellIntegerValue(HSSFCell cell) {
		if (cell == null) {
			return 0;
		}
		try {
			return Double.valueOf(cell.getNumericCellValue()).intValue();
		} catch (java.lang.IllegalStateException e) {
			try {
				return Integer.valueOf(cell.getStringCellValue());
			} catch (NumberFormatException nfe) {
				logger.warn("Incorrect number: " + cell.getStringCellValue());
				return null;
			}
		}

	}

}
