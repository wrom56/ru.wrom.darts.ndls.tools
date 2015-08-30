package ru.wrom.darts.ndls.tools.pentathlon;

import org.junit.Test;

public class PentathlonImporterTest {

	@Test
	public void testName() throws Exception {

		PentathlonImporter pentathlonImporter = new PentathlonImporter();
		pentathlonImporter.withLoadPath("d:/work/test/ndls/pentathlon/load").withTemplateFile("d:/work/test/ndls/pentathlon/template.xls").importData();

	}
}