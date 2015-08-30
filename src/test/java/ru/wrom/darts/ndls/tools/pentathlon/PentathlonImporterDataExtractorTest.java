package ru.wrom.darts.ndls.tools.pentathlon;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class PentathlonImporterDataExtractorTest {

	private final static Logger logger = getLogger(PentathlonImporterDataExtractorTest.class);

	@Test
	public void testName() throws Exception {
		File file = new File(this.getClass().getClassLoader().getResource("NDLS-00-player.xls").toURI());
		new PentathlonDataExtractor().extract(file);
	}

	@Test
	public void testDuplicatePlayer() throws Exception {
		File file = new File(this.getClass().getClassLoader().getResource("duplicate.xls").toURI());
		logger.info(new PentathlonDataExtractor().extract(file).toString());
	}
}