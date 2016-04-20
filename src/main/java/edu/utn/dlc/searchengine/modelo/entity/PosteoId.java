/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.dlc.searchengine.modelo.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author
 */
@Embeddable
public class PosteoId implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "palabra")
    private String palabra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "archivo")
    private String archivo;

    public PosteoId() {
    }

    public PosteoId(String palabra, String archivo) {
        this.palabra = palabra;
        this.archivo = archivo;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (palabra != null ? palabra.hashCode() : 0);
        hash += (archivo != null ? archivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PosteoId)) {
            return false;
        }
        PosteoId other = (PosteoId) object;
        if ((this.palabra == null && other.palabra != null) || (this.palabra != null && !this.palabra.equals(other.palabra))) {
            return false;
        }
        if ((this.archivo == null && other.archivo != null) || (this.archivo != null && !this.archivo.equals(other.archivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PosteoId[ palabra=" + palabra + ", archivo=" + archivo + " ]";
    }
    
}
