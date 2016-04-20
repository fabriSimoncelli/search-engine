package edu.utn.dlc.searchengine.service;

import java.util.ArrayList;

/**
 *
 * @author 
 */
public interface Indexador {

    void indexarDocumento(String absolutePath);

    void procesarDocumentos(ArrayList<String> documentos, String path);

    void insertData();

  

}
