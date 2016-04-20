/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.dlc.searchengine.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import edu.utn.dlc.searchengine.modelo.dao.ArchivoDao;
import edu.utn.dlc.searchengine.modelo.dao.PalabraDao;
import edu.utn.dlc.searchengine.modelo.dao.PosteoDao;
import edu.utn.dlc.searchengine.modelo.entity.Archivo;
import edu.utn.dlc.searchengine.modelo.entity.Palabra;
import edu.utn.dlc.searchengine.modelo.entity.Posteo;
import edu.utn.dlc.searchengine.modelo.entity.PosteoId;

/**
 *
 * @author
 */
public class IndexadorImpl implements Indexador {

	private PosteoDao posteoDao = new PosteoDao();

	private PalabraDao palabraDao = new PalabraDao();

	private ArchivoDao archivoDao = new ArchivoDao();

	private Archivo archivo; // entity archivo
	private ArrayList<String> listaDocumentos; // lista de documentos a procesar
	private String directorio;
	private Map<String, Palabra> vocabulario;
	private ArrayList<Posteo> listaPosteo;
	List<Palabra> palabrasBD;

	public IndexadorImpl() {
	}

	/**
	 * Recibe una lista con los documentos a procesar y el path en donde se
	 * encuentran. Procesa los documentos de la lista agregando las palabras
	 * lista de posteo y archivos a la BD
	 * 
	 * @param documentos
	 * @param path
	 */
	@Override
	public void procesarDocumentos(ArrayList documentos, String path) {

		try {
			listaDocumentos = new ArrayList<>();
			listaDocumentos = documentos;
			vocabulario = new HashMap<>();
			listaPosteo = new ArrayList<>();
			directorio = path + "\\";
			System.out.println(directorio);
			System.out.println(listaDocumentos.toString() + listaDocumentos.size());
			if (!listaDocumentos.isEmpty()) {
				System.out.println("Procesando Documentos");
				for (int i = 0; i < listaDocumentos.size(); i++) {
					archivo = new Archivo();
					archivo.setNombre(listaDocumentos.get(i));
					archivo.setPath(directorio + archivo.getNombre());
					if (archivoDao.find(listaDocumentos.get(i)) == null) {
						archivoDao.create(archivo);
						indexarDocumento(directorio + listaDocumentos.get(i));
						System.out.println("El archivo " + listaDocumentos.get(i) + " no estaba en la bd y se agregó");
					} else {
						System.out.println("El archivo " + listaDocumentos.get(i)
								+ " ya estaba en la bd por lo tanto no se procesó");
					}
				}
			}
			listaDocumentos.clear(); 
			archivoDao.commitAndClose();
			insertData();
		} catch (Exception ex) {
			Logger.getLogger(IndexadorImpl.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Recibe la direccion de un archivo, lo lee, extrae las palabras del mismo
	 * y las agrega a los mapas correspondientes
	 * 
	 * @param absolutePath
	 */
	@Override
	public void indexarDocumento(String absolutePath) {
		Map<String, Integer> mapaDocumento = new HashMap<>();
		try {
			FileInputStream fis = null;
			File file = new File(absolutePath);
			fis = new FileInputStream(file); 
			Scanner scan = new Scanner(fis).useDelimiter(
					Pattern.compile("[^aábcdeéfghiíjklmnñoópqrstuúüvwxyzAÁBCDEÉFGHIÍJKLMNÑOÓPQRSTUÚÜVWXYZ]"));

			while (scan.hasNext()) { // recorre el archivo
				String palabraScanner = scan.next().toUpperCase();
//				System.out.print("\nPalabra procesada: " + palabraScanner);
				if (palabraScanner.length() > 1 && palabraScanner.length() < 51) { 
					if (!mapaDocumento.containsKey(palabraScanner)) {
						mapaDocumento.put(palabraScanner, 1); 
					} else { 
						mapaDocumento.put(palabraScanner, mapaDocumento.get(palabraScanner) + 1); 
					}
				}

			} 
			Posteo posteoAux = new Posteo();
			PosteoId posteoPKAux;
			for (Map.Entry<String, Integer> entry : mapaDocumento.entrySet()) {
				// agrega el posteo a la lista posteo
				Palabra palAux = new Palabra();
				posteoPKAux = new PosteoId(entry.getKey(), file.getName());
				posteoAux = new Posteo(posteoPKAux, entry.getValue());
				listaPosteo.add(posteoAux);
				// agrega las palabras al mapaPalabras
				palAux.setPalabra(entry.getKey());
				if (!vocabulario.containsKey(entry.getKey())) {
					palAux.setNr(1);
					palAux.setMaxTf(entry.getValue());

				} else {
					palAux.setNr(vocabulario.get(entry.getKey()).getNr() + 1);
					if (vocabulario.get(entry.getKey()).getMaxTf() > entry.getValue()) {
						palAux.setMaxTf(vocabulario.get(entry.getKey()).getMaxTf());
					} else {
						palAux.setMaxTf(entry.getValue());
					}
				}
				vocabulario.put(entry.getKey(), palAux);
			}
			
			scan.close();
			fis.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(IndexadorImpl.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(IndexadorImpl.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Realiza todas las inserciones en la bd de todos los documentos procesados, junto con sus
	 * palabras y lista de posteo
	 */
	@Override
	public void insertData() {
		Palabra palBD;
		// Recupero las palabras que ya estaban insertadas en la base de datos
		palabrasBD = palabraDao.findAll();
		Map<String, Palabra> mapaBD = new HashMap<>();
		for (Palabra palabrasBD1 : palabrasBD) {
			mapaBD.put(palabrasBD1.getPalabra(), palabrasBD1);
		}
		System.out.println("Agregando palabras a la BD");
		for (Map.Entry<String, Palabra> entry : vocabulario.entrySet()) {
			if (!mapaBD.containsKey(entry.getKey())) {
				palabraDao.create(entry.getValue());
			} else { // la palabra ya estaba en la base de datos
				palBD = mapaBD.get(entry.getKey());
				entry.getValue().setNr(palBD.getNr() + entry.getValue().getNr()); 
				if (palBD.getMaxTf() > entry.getValue().getMaxTf()) {
					entry.getValue().setMaxTf(palBD.getMaxTf());
				}
				palabraDao.edit(entry.getValue());
			}
		}
		palabraDao.commitAndClose();
		System.out.println("Empieza a agregar posteos...");
//		for (Posteo auxPosteo : listaPosteo) {
//			System.out.println(auxPosteo.toString());
//		}
		for (Posteo auxPosteo : listaPosteo) {
			posteoDao.create(auxPosteo);
		}
		posteoDao.commitAndClose();
		System.out.println("Se completo la indexación...");
	}

}
