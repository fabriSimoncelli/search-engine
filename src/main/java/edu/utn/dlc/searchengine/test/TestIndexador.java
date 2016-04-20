package edu.utn.dlc.searchengine.test;

import java.io.File;
import java.util.ArrayList;

import edu.utn.dlc.searchengine.service.IndexadorImpl;
import edu.utn.dlc.searchengine.service.Indexador;


public class TestIndexador {

	public static void main(String[] args) {
		Indexador indexador = new IndexadorImpl();
		ArrayList<String> documentos = new ArrayList<>();

		File folder = new File("documentos");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				documentos.add(listOfFiles[i].getName());
			}
		}
		indexador.procesarDocumentos(documentos, "documentos");

	}
}
