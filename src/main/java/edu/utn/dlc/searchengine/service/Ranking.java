package edu.utn.dlc.searchengine.service;

import java.util.Objects;

public class Ranking implements Comparable {
    private String archivo;
    private double peso;
    private String path;

    public Ranking() {
        archivo="";
        peso=0;
        path="";
    }

    public Ranking(String archivo, double peso, String path) {
        this.archivo = archivo;
        this.peso = peso;
        this.path=path;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
     public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(Object o) {
        //if (this.peso>)
        
        
        double comparePeso = ((Ranking)o).getPeso();
       
        return Double.compare(this.peso, comparePeso);
       // return (int) (this.peso - comparePeso);
    }

    @Override
    public String toString() {
        return  "archivo=" + archivo + ", peso=" + peso +", path= "+path+"\n";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.archivo);
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.peso) ^ (Double.doubleToLongBits(this.peso) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ranking other = (Ranking) obj;
        if (!Objects.equals(this.archivo, other.archivo)) {
            return false;
        }
        return true;
    }
    
}
