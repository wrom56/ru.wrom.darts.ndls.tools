package ru.wrom.darts.ndls.tools;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import ru.wrom.darts.ndls.tools.pentathlon.PentathlonImporter;

public class Start {

	private final static Logger logger = getLogger(Start.class);

	private String loadPath;
	private String templateFile;

	public static void main(String[] args) {
		logger.debug("Start tools");
		try {
			Start start = new Start();
			start.initConfig();
			new PentathlonImporter().withLoadPath(start.loadPath).withTemplateFile(start.templateFile).importData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("End tools");
	}

	public void initConfig() throws IOException {
		Properties properties = new Properties();
		properties.load(new FileReader("config.properties"));
		loadPath = String.valueOf(properties.get("loadPath"));
		templateFile = String.valueOf(properties.get("templateFile"));
	}
}
