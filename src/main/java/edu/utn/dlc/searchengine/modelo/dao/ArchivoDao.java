/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.dlc.searchengine.modelo.dao;

import javax.persistence.EntityManager;

import edu.utn.dlc.searchengine.modelo.entity.Archivo;

/**
 *
 * @author
 */
public class ArchivoDao extends GenericDao<Archivo> {

	public ArchivoDao() {
		 super(Archivo.class);
	}

	public EntityManager getEntityManager() {
		return super.getEntityManager();
	}
}
