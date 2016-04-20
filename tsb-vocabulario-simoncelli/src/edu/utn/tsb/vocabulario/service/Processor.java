package edu.utn.tsb.vocabulario.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public interface Processor {

	Map<String, Integer> processDocument(String documentName, String path)
			throws IOException, ClassNotFoundException, SQLException;

}