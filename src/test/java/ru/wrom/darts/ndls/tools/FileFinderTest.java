package ru.wrom.darts.ndls.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class FileFinderTest {

	private final static Logger logger = getLogger(FileFinderTest.class);

	@Test
	public void testGetFiles() throws Exception {

		List<File> files = new ArrayList<>();
		new FileFinder().getFiles("d:\\work\\test\\ndls\\1\\", ".*", files);
		logger.info(files.toString());

	}
}