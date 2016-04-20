/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.dlc.searchengine.modelo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author
 * @param <T>
 */
public abstract class GenericDao<T> {
	private Class<T> entityClass;
	private EntityManager entityManager;

	public GenericDao(Class<T> entityClass) {
		this.entityClass = entityClass;
		entityManager = EntityManagerUtil.getEntityManager();
		entityManager.getTransaction().begin();
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	public void create(T entity) {
		try {
			entityManager.persist(entity);
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
		}
	}

	public void edit(T entity) {
		try {
			entityManager.merge(entity);
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
		}
	}

	public void remove(T entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	public T find(Object id) {
		T object = null;
		try {
			object = entityManager.find(entityClass, id);
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
		}
		return object;
	}

	public List<T> findAll() {
		CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return entityManager.createQuery(cq).getResultList();
	}

	public int count() {
		CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
		Root<T> rt = cq.from(entityClass);
		cq.select(entityManager.getCriteriaBuilder().count(rt));
		javax.persistence.Query q = entityManager.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

	public void commitAndClose() {
		try {
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
		} finally {
			entityManager.close();
		}
	}
}
