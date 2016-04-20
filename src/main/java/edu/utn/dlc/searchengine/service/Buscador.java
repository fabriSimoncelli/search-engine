package edu.utn.dlc.searchengine.service;

import java.util.List;

import edu.utn.dlc.searchengine.modelo.entity.Palabra;
import edu.utn.dlc.searchengine.modelo.entity.Posteo;

public interface Buscador {

	List<Ranking> buscar(String cadenaInput);

	/**
	 * Recibe el arreglo con las palabras a buscar y devuelve una lista ordenada
	 * de entidades Palabras de menor a mayor Nr
	 *
	 * @param arrayInput
	 * @return
	 */
	List buscarPalabrasBD(String[] arrayInput);

	/**
	 * Recibe una palabra por parámetro y busca su lista de posteo. La cantidad
	 * de posteos recuperados está limitado por la variable límite
	 * @param entidadPalabra
	 * @return
	 */
	List<Posteo> getPosteo(Palabra entidadPalabra);

	/**
	 * Recibe una lista con los posteos del término analizado y lo agrega al
	 * ranking de documentos calculando el peso del término para cada documento.
	 * Devuelve una lista ordenada de documentos segun el peso
	 *
	 * @param posteos
	 * @param nr
	 */
	void agregarRanking(List<Posteo> posteos, int nr);

}