package edu.utn.tsb.vocabulario.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

public interface DataBaseUtil {

	/**
	 * Estable la conexión con la base de datos
	 */
	void connect();

	/**
	 * Cierra la conexión con la base de datos
	 */
	void disconnect();

	/**
	 * Inserta las palabras en la base de datos
	 *
	 * @param mapa Hashmap con las palabras procesadas del archivo
	 * @param doc Nombre del documento procesado
	 * @param barra Barra de progreso
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	void insertPalabras(Map<String, Integer> map, String doc, JProgressBar barra)
			throws ClassNotFoundException, SQLException;

	/**
	 * Verifica si el documento ya fue procesado y existe en la base de datos
	 *
	 * @param doc nombre del documento a buscar
	 * @return retorna true si ya existe y false si no
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	boolean existDocument(String doc) throws ClassNotFoundException, SQLException;

	/**
	 * agrega el archivo a la tabla DOCUMENTOS de la base de datos
	 *
	 * @param nombre nombre del archivo
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	void addPalabra(String fileName) throws ClassNotFoundException, SQLException;

	/**
	 * Crea el statement para agregat la entrada en la tabla PALABRASXDOCUMENTO
	 * y lo agrega al batch
	 *
	 * @param palabra palabra a agregar
	 * @param Doc documento en donde aparece la palabra
	 * @param frecuencia parcial de la palabra a agregar en este documento
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	void addPalabraXDocumento(String palabra, String Doc, int frecuencia) throws ClassNotFoundException, SQLException;

	/**
	 * Hace una consulta a la tabla PALABRAS de la BD y crea un modelo para
	 * luego mostrarlos en la tabla de la aplicación
	 *
	 * @return Devuelve un modelo de tabla
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	DefaultTableModel cargarTabla() throws ClassNotFoundException, SQLException;

	/**
	 * Hace una consulta a la tabla PALABRAS y agrega el resultado en el mapa
	 * mapaPalabras, para luego poder realizar búsquedas de palabras en memoria
	 * y evitar hacer consultas a BD
	 *
	 * @return
	 */
	Set<String> loadPalabras();

	/**
	 * Metodo para borrar y dejar la base de datos sin ningun dato.
	 */
	void dropDB();

	Map<String, Integer> getDocuments(String property);

	ArrayList<String> getProcessedDocuments();

}