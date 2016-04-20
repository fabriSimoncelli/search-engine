/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.dlc.searchengine.modelo.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author 
 */
@Entity
@Table(name = "archivo")
@NamedQueries({ @NamedQuery(name = "Archivo.findAll", query = "SELECT a FROM Archivo a"),
		@NamedQuery(name = "Archivo.findByNombre", query = "SELECT a FROM Archivo a WHERE a.nombre = :nombre"),
		@NamedQuery(name = "Archivo.findByPath", query = "SELECT a FROM Archivo a WHERE a.path = :path") })
public class Archivo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "nombre")
	private String nombre;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "path")
	private String path;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "archivo")
	private List<Posteo> posteoList;

	public Archivo() {
	}

	public Archivo(String nombre) {
		this.nombre = nombre;
	}

	public Archivo(String nombre, String path) {
		this.nombre = nombre;
		this.path = path;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Posteo> getPosteoList() {
		return posteoList;
	}

	public void setPosteoList(List<Posteo> posteoList) {
		this.posteoList = posteoList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (nombre != null ? nombre.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Archivo)) {
			return false;
		}
		Archivo other = (Archivo) object;
		if ((this.nombre == null && other.nombre != null)
				|| (this.nombre != null && !this.nombre.equals(other.nombre))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Archivo[ nombre=" + nombre + ", path= " + path + "]";
	}

}
