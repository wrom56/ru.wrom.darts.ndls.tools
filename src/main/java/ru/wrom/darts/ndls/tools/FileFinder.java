package ru.wrom.darts.ndls.tools;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class FileFinder {

	private final static Logger logger = getLogger(FileFinder.class);

	public void getFiles(String path, String mask, List<File> files) {
		logger.trace("Found files. Path: {}, filename mask: {}", path, mask);

		File folder = new File(path);

		if (!folder.exists()) {
			logger.debug("Path '{}' doesn't exists", path);
			return;
		}

		File[] result = folder.listFiles((dir, name) -> {
			if (new File(dir, name).isDirectory()) {
				logger.trace("{} is Directory", name);
				getFiles(dir.getPath() + "/" + name, mask, files);
				return false;
			}
			logger.trace("Find file. Name: {}", name);
			boolean isMatched = name.matches(mask);
			logger.trace("File matches the regular expression result. Filemane: '{}', mask: '{}', result: {}", name, mask, isMatched);
			return isMatched;
		});

		if (result.length == 0) {
			logger.trace("Files not found");
			return;
		}

		files.addAll(Arrays.asList(result));
	}

}
