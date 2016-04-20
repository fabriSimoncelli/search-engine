/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.tsb.vocabulario.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

import edu.utn.tsb.vocabulario.db.DataBaseUtil;
import edu.utn.tsb.vocabulario.db.DataBaseUtilImpl;

/**
 *
 * @author Fabricio Simoncelli
 *
 *         Para que reconozca letras con acento debemos cambiar el encoding
 *         dentro de las propiedades, en la pestaña source, de UTF-8 a
 *         Windows-1252
 */
public class DocumentProcessor implements Processor {

	private Map<String, Integer> map;
	public DataBaseUtil dbUtil = new DataBaseUtilImpl();

	@Override
	public Map<String, Integer> processDocument(String documentName, String path)
			throws IOException, ClassNotFoundException, SQLException {
		map = new HashMap<>();
		File file = new File(path);
		if (!dbUtil.existDocument(documentName)) {
			try {
				dbUtil.addPalabra(documentName);
				FileInputStream fis = new FileInputStream(file);
				Scanner scan = new Scanner(fis).useDelimiter(Pattern.compile("[^áéñóíúüÁÉÑÓÍÚÜa-zA-Z]"));
				// Scanner(fis).useDelimiter(Pattern.compile("[^aábcdeéfghijklmnñoópqrstuúüvwxyzAÁBCDEÉFGHIJKLMNÑOÓPQRSTUÚÜVWXYZ]"));
				while (scan.hasNext()) {

					String aux = scan.next().toUpperCase();
					if (aux.length() > 1) {
						if (!map.containsKey(aux)) {
							map.put(aux, 1);
						} else {
							Integer frequency = map.get(aux);
							frequency += 1;
							map.put(aux, frequency);
						}
					}
				}
				scan.close();
				fis.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			return map;
		}
		JOptionPane.showMessageDialog(null, "Uno de los documentos seleccionados ya fue procesado.", "Error",
				JOptionPane.ERROR_MESSAGE);
		return map;
	}
}
