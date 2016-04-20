package edu.utn.dlc.searchengine.test;

import java.util.List;

import edu.utn.dlc.searchengine.service.Buscador;
import edu.utn.dlc.searchengine.service.BuscadorImpl;
import edu.utn.dlc.searchengine.service.Ranking;

public class TestRanking {
	public static void main(String[] args) {

		Buscador buscador = new BuscadorImpl();
		String cadena = "Tu infancia fué un paraíso";

		List<Ranking> ranking = buscador.buscar(cadena);

		for (Ranking rankingEntry : ranking) {
			System.out.println("Ranking: " + rankingEntry);
		}
	}
}
