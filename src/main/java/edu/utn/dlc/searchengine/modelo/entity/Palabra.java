/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.dlc.searchengine.modelo.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author
 */
@Entity
@Table(name = "palabra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Palabra.findAll", query = "SELECT p FROM Palabra p"),
    @NamedQuery(name = "Palabra.findByPalabra", query = "SELECT p FROM Palabra p WHERE p.palabra = :palabra"),
    @NamedQuery(name = "Palabra.findByNr", query = "SELECT p FROM Palabra p WHERE p.nr = :nr"),
    @NamedQuery(name = "Palabra.findByMaxTf", query = "SELECT p FROM Palabra p WHERE p.maxTf = :maxTf")})
public class Palabra implements Serializable, Comparable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "palabra")
    private String palabra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nr")
    private int nr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "maxTf")
    private int maxTf;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "palabra1")
    private List<Posteo> posteoList;

    public Palabra() {
    }

    public Palabra(String palabra) {
        this.palabra = palabra;
    }

    public Palabra(String palabra, int nr, int maxTf) {
        this.palabra = palabra;
        this.nr = nr;
        this.maxTf = maxTf;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public int getMaxTf() {
        return maxTf;
    }

    public void setMaxTf(int maxTf) {
        this.maxTf = maxTf;
    }

    @XmlTransient
    public List<Posteo> getPosteoList() {
        return posteoList;
    }

    public void setPosteoList(List<Posteo> posteoList) {
        this.posteoList = posteoList;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (palabra != null ? palabra.hashCode() : 0);
//        return hash;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.palabra);
        hash = 29 * hash + this.nr;
        hash = 29 * hash + this.maxTf;
        return hash;
    }

//    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Palabra)) {
//            return false;
//        }
//        Palabra other = (Palabra) object;
//        if ((this.palabra == null && other.palabra != null) || (this.palabra != null && !this.palabra.equals(other.palabra))) {
//            return false;
//        }
//        return true;
        if(object == null)return false;
        if(!(object instanceof Palabra)) return false;
        Palabra other = (Palabra)object;
        
        return this.palabra.equals(other.palabra);
    }
    
    @Override
    public int compareTo(Object o) {
        int compareNr = ((Palabra)o).getNr();
        return this.nr-compareNr;
    }
    

    @Override
    public String toString() {
        return "Palabra[ palabra=" + palabra + " nr=" + nr + " maxTf=" + maxTf + "]";
    }  
}
