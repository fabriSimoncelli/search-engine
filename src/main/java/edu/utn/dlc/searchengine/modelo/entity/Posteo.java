package edu.utn.dlc.searchengine.modelo.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author
 */
@Entity
@Table(name = "posteo")
@NamedQueries({ @NamedQuery(name = "Posteo.findAll", query = "SELECT p FROM Posteo p"),
		@NamedQuery(name = "Posteo.findByPalabra", query = "SELECT p FROM Posteo p WHERE p.posteoId.palabra = :palabra"),
		@NamedQuery(name = "Posteo.findByPalabraOrdered", query = "SELECT p FROM Posteo p WHERE p.posteoId.palabra = :palabra ORDER BY p.tf DESC"),
		@NamedQuery(name = "Posteo.findByArchivo", query = "SELECT p FROM Posteo p WHERE p.posteoId.archivo = :archivo"),
		@NamedQuery(name = "Posteo.findByTf", query = "SELECT p FROM Posteo p WHERE p.tf = :tf") })
public class Posteo implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected PosteoId posteoId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "tf")
	private int tf;
	@JoinColumn(name = "archivo", referencedColumnName = "nombre", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Archivo archivo;
	@JoinColumn(name = "palabra", referencedColumnName = "palabra", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Palabra palabra1;

	public Posteo() {
	}

	public Posteo(PosteoId posteoId) {
		this.posteoId = posteoId;
	}

	public Posteo(PosteoId posteoId, int tf) {
		this.posteoId = posteoId;
		this.tf = tf;
	}

	public Posteo(String palabra, String archivo) {
		this.posteoId = new PosteoId(palabra, archivo);
	}

	public PosteoId getPosteoId() {
		return posteoId;
	}

	public void setPosteoId(PosteoId posteoId) {
		this.posteoId = posteoId;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public Archivo getArchivo() {
		return archivo;
	}

	public void setArchivo(Archivo archivo1) {
		this.archivo = archivo1;
	}

	public Palabra getPalabra() {
		return palabra1;
	}

	public void setPalabra(Palabra palabra1) {
		this.palabra1 = palabra1;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (posteoId != null ? posteoId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Posteo)) {
			return false;
		}
		Posteo other = (Posteo) object;
		if ((this.posteoId == null && other.posteoId != null)
				|| (this.posteoId != null && !this.posteoId.equals(other.posteoId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Posteo[ posteoId=" + posteoId + " ]";
	}

}
