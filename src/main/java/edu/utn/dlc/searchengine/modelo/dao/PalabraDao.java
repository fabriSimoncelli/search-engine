/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.dlc.searchengine.modelo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.utn.dlc.searchengine.modelo.entity.Palabra;

/**
 *
 * @author
 */
public class PalabraDao extends GenericDao<Palabra> {
	public PalabraDao() {
		super(Palabra.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return super.getEntityManager();
	}

	public Palabra buscarPalabra(String palabra) {
		Query query = getEntityManager().createNamedQuery("Palabra.findByPalabra");
		query.setParameter("palabra", palabra);
		List<Palabra> result = query.getResultList();
		if (!result.isEmpty()) {
			return (Palabra) result.get(0);
		}
		return null;
	}
}
