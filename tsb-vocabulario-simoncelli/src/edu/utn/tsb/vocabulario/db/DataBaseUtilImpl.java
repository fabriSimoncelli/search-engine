/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.tsb.vocabulario.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fabricio Simoncelli
 */
public class DataBaseUtilImpl implements DataBaseUtil {

    private Set<String> setPalabras;
    Connection conn;
    PreparedStatement insertPalabraXDocumentoTable;
    PreparedStatement insertPalabraTable;
    PreparedStatement genericQuery;
    int contadorinsert;
    int contadorquery;

    @Override
	public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:vocabulario-fsimoncelli");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
	public void disconnect() {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
	public void insertPalabras(Map<String, Integer> mapa, String doc, JProgressBar barra) throws ClassNotFoundException, SQLException {

        JProgressBar progreso = barra;
        progreso.setMaximum(mapa.size()); // Establece el tamaño de la barra según el tamaño del mapa de palabras
        progreso.setStringPainted(true); // muestra el porcentaje de proceso en la barra
        connect(); //conecta con la BD
        this.loadPalabras(); // Lee la base de datos y la guarda el mapa mapaPalabra para luego comparar
        Set keySet = mapa.entrySet();
        Iterator it = keySet.iterator(); //iterador que recorre el mapa de palabras
        int i = 0; //contador de palabras que se utiliza para llenar la barra
        final int batchSize = 100; //tamaño del batch
        try {
            // Inicializan los statement y contadores
            insertPalabraTable = conn.prepareStatement("insert into palabra (nombre) values(?)");
            insertPalabraXDocumentoTable = conn.prepareStatement("insert into palabraXdocumento (palabra, documento, frecuencia) values (?,?,?)");
            contadorinsert = contadorquery = 1;
            conn.setAutoCommit(false); // comienza la transacción
            while (it.hasNext()) {

                Map.Entry<String, Integer> entrymap = (Map.Entry) it.next();
                agregarADB(entrymap.getKey()); //llama al metodo agregarADB para agregar la palabra a la tabla PALABRAS
                addPalabraXDocumento(entrymap.getKey(), doc, entrymap.getValue());//llama al metodo addPalabraXDocumento Para agregar la palabra a la tabla PALABRASXDOCUMENTO

                //Cada vez que un batch llega al límite del tamaño definido lo ejecuta
                if (contadorinsert % batchSize == 0) {
                    insertPalabraTable.executeBatch();
                }
                if (contadorquery % batchSize == 0) {
                    insertPalabraXDocumentoTable.executeBatch();
                }
                progreso.setValue(i);//actualiza la barra de progreso
                i++;
            }
            // insertan los elementos sobrantes de los batchs
            insertPalabraXDocumentoTable.executeBatch();
            insertPalabraTable.executeBatch();
            conn.commit(); // hace el commit y finaliza la transacción
        } catch (SQLException e) {

            System.out.println(e.getMessage());
            conn.rollback(); //si se produce un error hace un rollback en la BD

        }
        //Cierre de los statement y de la conexión
        insertPalabraXDocumentoTable.close();
        insertPalabraTable.close();
        disconnect();
        progreso.setValue(i + 1); //Termina de completar la barra de progreso

    }

    /**
     * Crea los statements según sea una inserción o actualización y los agrega
     * a los respectivos batch
     *
     * @param palabra palabra que debe ser agregada
     * @param aparicion objeto que contiene la cantidad de apariciones y los
     * documentos en lo que lo hace
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void agregarADB(String palabra) throws ClassNotFoundException, SQLException {

        if (!existePalabra(palabra)) { //Verifica si la palabra existe o no en la BD
            insertPalabraTable.setString(1, palabra.toUpperCase());
            insertPalabraTable.addBatch();
            contadorinsert++;
        }

    }

    @Override
	public boolean existDocument(String doc) throws ClassNotFoundException, SQLException {

        connect();
        genericQuery = conn.prepareStatement("SELECT * FROM documento WHERE nombre=?"); //hace una consulta a la tabla DOCUMENTOS
        genericQuery.setString(1, doc);
        ResultSet rs = genericQuery.executeQuery();
        boolean exist = false;
        if (rs.next()) { //Si el documento ya existe en la BD entonces rs va a tener una entrada, sino estará vacío
            exist = true;
        }
        rs.close();
        genericQuery.close();
        disconnect();
        return exist;
    }

    /**
     * Busca una palabra en mapaPalabra (mapa que contiene las palabras
     * almacenadas en la tabla PALABRAS de la BD) para ver si la misma ya existe
     * en la BD o no.
     *
     * @param palabra palabra a buscar
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private boolean existePalabra(String palabra) throws ClassNotFoundException, SQLException {

        boolean exist = false;
        if (setPalabras.contains(palabra)) {
            exist = true;
        }
        return exist;
    }

    @Override
	public void addPalabra(String nombre) throws ClassNotFoundException, SQLException {
        connect();
        genericQuery = conn.prepareStatement("insert into documento (nombre) values(?)");
        genericQuery.setString(1, nombre);
        genericQuery.executeUpdate();
        genericQuery.close();
        disconnect();
    }

    @Override
	public void addPalabraXDocumento(String palabra, String Doc, int frecuencia) throws ClassNotFoundException, SQLException {

        insertPalabraXDocumentoTable.setString(1, palabra.toUpperCase());
        insertPalabraXDocumentoTable.setString(2, Doc);
        insertPalabraXDocumentoTable.setInt(3, frecuencia);
        insertPalabraXDocumentoTable.addBatch();
        contadorquery++;
    }
    
    @Override
	@SuppressWarnings("empty-statement")
    public DefaultTableModel cargarTabla() throws ClassNotFoundException, SQLException { //Refactor 1 
        DefaultTableModel modelo = new DefaultTableModel(){
            /**
             * Sobreescribimos el método getColumnClass para que devuelva
             * el tipo de dato almacenado en cada columna así el sorter puede
             * ordenar los datos de la tabla. Si no lo sobreescribimos las
             * columnas con valores numéricos no se ordenan correctamente ya
             * que los compara como si fueran cadenas de caracteres y no 
             * como números
             * @param column
             * @return 
             */
            @Override
            public Class getColumnClass(int column) {
                if (column==0){
                    return String.class;
                }else{
                    return Integer.class;
                }
            }
        };
        
        connect();
        modelo.addColumn("Palabras");
        modelo.addColumn("Frecuencia");
        modelo.addColumn("Documentos");
        

        genericQuery = conn.prepareStatement("select palabra, count(*) as cantidad, sum(frecuencia) as cantXdoc from palabraXdocumento group by(palabra)");
        
        try (ResultSet rs = genericQuery.executeQuery()) {
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString("palabra");
                fila[1] = rs.getInt("cantidad");
                fila[2] = rs.getInt("cantXdoc");
                modelo.addRow(fila);
            }
        }
        genericQuery.close();
        disconnect();

        return modelo;
    }

    @Override
	public Set<String> loadPalabras() {
        setPalabras = new HashSet<>();
        try {

            genericQuery = conn.prepareStatement("select * from palabra");
            ResultSet rs = genericQuery.executeQuery();
            while (rs.next()) {
                setPalabras.add(rs.getString("nombre"));

            }
            rs.close();
            genericQuery.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataBaseUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return setPalabras;
    }

    @Override
	public void dropDB() {
        connect();
        String[] tablas = {"palabra", "palabraXdocumento", "documento"};
        try {
            for (String tb : tablas) {
                genericQuery = conn.prepareStatement("delete from " + tb);
                genericQuery.execute();
                genericQuery.close();
            } 

        } catch (SQLException ex) {
            Logger.getLogger(DataBaseUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        disconnect();
    }

    @Override
	public Map<String,Integer> getDocuments(String property) { //Refactor segunda consulta sql
        Map<String,Integer> info = new HashMap<>();
        connect();
        try {
            genericQuery = conn.prepareStatement("select documento, frecuencia from palabraXdocumento where palabra = ?");
            genericQuery.setString(1, property);
            ResultSet rs = genericQuery.executeQuery();
            while (rs.next()) {
               info.put(rs.getString(1), rs.getInt(2));
            }
            rs.close();
            genericQuery.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        disconnect();
        return info;
    }

    @Override
	public ArrayList<String> getProcessedDocuments() {
        ArrayList<String> docsProcesados = new ArrayList<>();
        try {

            connect();
            genericQuery = conn.prepareStatement("select * from documento");
            ResultSet rs = genericQuery.executeQuery();
            while (rs.next()) {
                docsProcesados.add(rs.getString(1));
            }
            rs.close();
            genericQuery.close();
            disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(DataBaseUtilImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return docsProcesados;
    }
}
