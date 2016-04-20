package edu.utn.dlc.searchengine.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.utn.dlc.searchengine.modelo.dao.ArchivoDao;
import edu.utn.dlc.searchengine.modelo.dao.PalabraDao;
import edu.utn.dlc.searchengine.modelo.dao.PosteoDao;
import edu.utn.dlc.searchengine.modelo.entity.Palabra;
import edu.utn.dlc.searchengine.modelo.entity.Posteo;

public class BuscadorImpl implements Buscador {

	private PosteoDao posteoDao = new PosteoDao();

	private PalabraDao palabraDao = new PalabraDao();

	private ArchivoDao archivoDao = new ArchivoDao();

    int limite; // cantidad de documentos a mostrar
    double N; // cantidad de documentos indexados
    ArrayList<Ranking> ranking;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public BuscadorImpl() {

        limite = 30;
        ranking = new ArrayList<>();

    }

	@Override
    public List<Ranking> buscar(String cadenaInput) {
        System.out.println("Working Directory = "
                + System.getProperty("user.dir"));
        N = archivoDao.count();
        ranking.clear();
        System.out.println("Cantidad de archivos indexados: " + N);
        ArrayList<Palabra> listaPalabras = new ArrayList<>();
        String[] arregloInput = cadenaInput.toUpperCase().trim().split("[^AÁBCDEÉFGHIÍJKLMNÑOÓPQRSTUÚÜVWXYZ]"); // crea un arreglo de las palabras a buscar
        listaPalabras = (ArrayList<Palabra>) buscarPalabrasBD(arregloInput);
        for (Palabra pal : listaPalabras) {
            agregarRanking(getPosteo(pal), pal.getNr());
        }
        System.out.println(listaPalabras.toString());
        System.out.println("Ranking: Cantidad de archivos: " + ranking.size() + " \n" + ranking.toString());
        listaPalabras.clear();
        Collections.sort(ranking, Collections.reverseOrder());
        return ranking;
    }

    
	@Override
    public List buscarPalabrasBD(String[] arrayInput) {
        ArrayList<Palabra> listaPalabras = new ArrayList<>();
        for (int i = 0; i < arrayInput.length; i++) {
            Palabra aux = palabraDao.buscarPalabra(arrayInput[i]);
            if (aux != null) {
                listaPalabras.add(aux);
            }
        }
        Collections.sort(listaPalabras);
        return listaPalabras;
    }

	@Override
    public List<Posteo> getPosteo(Palabra entidadPalabra) {
        List posteoPalabra = posteoDao.buscarPosteo(entidadPalabra.getPalabra(), limite);
        return posteoPalabra;
    }

	@Override
    public void agregarRanking(List<Posteo> posteos, int nr) {

        for (Posteo posteo : posteos) {

            double peso = posteo.getTf() * Math.log10(N / nr); //calcula el peso
            String archivo = posteo.getArchivo().getNombre(); // obtiene el nombre del archivo
            String path = posteo.getArchivo().getPath();
            Ranking auxRanking = new Ranking(archivo, peso, path);

            int index = ranking.indexOf(auxRanking); // posición del archivo en el ranking. Si no está devuelve -1
            if (index != -1) { //si el archivo ya está en el ranking sumo los pesos
                ranking.get(index).setPeso(ranking.get(index).getPeso() + auxRanking.getPeso());
            } else { // si el archivo no está en el ranking
                if (ranking.size() < limite) { // Si el ranking aún no se llenó
                    ranking.add(auxRanking);
                } else { // si el ranking ya esta lleno
                    if (ranking.get(0).getPeso() < auxRanking.getPeso())//Si el archivo actual tiene mas peso que el último del ranking lo reemplaza
                    {
                        ranking.set(0, auxRanking);
                    }
                }
            }
            Collections.sort(ranking); // ordena el ranking segun el peso
        }
    }

}