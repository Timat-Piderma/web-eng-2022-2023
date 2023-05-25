package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.framework.data.DAO;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataLayer;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.template.model.Responsabile;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResponsabileDAO_MySQL extends DAO implements ResponsabileDAO{

    private PreparedStatement sAulaByID;
    private PreparedStatement sAule, sAulaByPosizione;
    private PreparedStatement iAula, uAula, dAula;
    
    public ResponsabileDAO_MySQL(DataLayer d) {
        super(d);
    }
    
        @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sAulaByID = connection.prepareStatement("SELECT * FROM aula WHERE ID=?");
            //++++++sAulaByPosizione = connection.prepareStatement("SELECT ID AS aulaID FROM aula WHERE posizioneID=?");
            //++++++sAule = connection.prepareStatement("SELECT ID AS aulaID FROM aula");

            //notare l'ultimo paametro extra di questa chiamata a
            //prepareStatement: lo usiamo per assicurarci che il JDBC
            //restituisca la chiave generata automaticamente per il
            //record inserito
            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            
           // iAula = connection.prepareStatement("INSERT INTO article (title,text,authorID,issueID,page) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            //uAula = connection.prepareStatement("UPDATE article SET title=?,text=?,authorID=?,issueID=?, page=?, version=? WHERE ID=? and version=?");
            //dAula = connection.prepareStatement("DELETE FROM article WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing auleweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            sAulaByID.close();

            sAulaByPosizione.close();
            sAule.close();

            iAula.close();
            uAula.close();
            dAula.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Responsabile createResponsabile() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Responsabile getResponsabile(Evento evento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
