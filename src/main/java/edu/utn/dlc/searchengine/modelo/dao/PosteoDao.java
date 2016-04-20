/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.dlc.searchengine.modelo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.utn.dlc.searchengine.modelo.entity.Posteo;

/**
 *
 * @author
 */
public class PosteoDao extends GenericDao<Posteo> {

	public PosteoDao() {
		super(Posteo.class);
	}

	public EntityManager getEntityManager() {
		return super.getEntityManager();
	}

	public List<Posteo> buscarPosteo(String palabra, int limite) {
		Query query = getEntityManager().createNamedQuery("Posteo.findByPalabraOrdered");
		query.setParameter("palabra", palabra);
		query.setMaxResults(limite);
		List<Posteo> results = query.getResultList();
		return results;
	}
}
