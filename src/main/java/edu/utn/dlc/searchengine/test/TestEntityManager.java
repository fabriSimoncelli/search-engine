package edu.utn.dlc.searchengine.test;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.utn.dlc.searchengine.modelo.entity.Palabra;

public class TestEntityManager {
	// nota: 144 segundos tardó en insertar 26000 palabras SIN implementar jpa batch
	// nota: 30 segundos tardó en insertar 26000 palabras CON la implementación (ver persistence.xml)
	public static void main(String[] args) {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("motorBusqueda");
		EntityManager em = factory.createEntityManager();

		em.getTransaction().begin();

		String[] randomWords = generateRandomWords(26000);
		Palabra palabra;

		System.out.println("Start the insert in db....");
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < randomWords.length; i++) {
			palabra = new Palabra(randomWords[i], new Random().nextInt(10) + 1, new Random().nextInt(10) + 1);
			if (em.find(Palabra.class, palabra.getPalabra()) == null) {
				em.persist(palabra);
			}
			if (i % 500 == 0) { //tiene que ser el mismo al batch que defini en el persistence.xml
				em.flush();
				em.clear();
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) / 1000 + " seconds");
		em.getTransaction().commit();
		em.close();
	}

	public static String[] generateRandomWords(int numberOfWords) {
		String[] randomStrings = new String[numberOfWords];
		Random random = new Random();
		for (int i = 0; i < numberOfWords; i++) {
			char[] word = new char[random.nextInt(8) + 3];
			for (int j = 0; j < word.length; j++) {
				word[j] = (char) ('a' + random.nextInt(26));
			}
			randomStrings[i] = new String(word);
		}
		return randomStrings;
	}
}
