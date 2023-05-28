package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.framework.data.DAO;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataLayer;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.template.model.Responsabile;
import com.stdt.auleweb.data.proxy.ResponsabileProxy;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ResponsabileDAO_MySQL extends DAO implements ResponsabileDAO{

    private PreparedStatement sResponsabileByID;
    private PreparedStatement sResponsabileByEvento;
    private PreparedStatement iResponsabile, uResponsabile, dResponsabile;
    
    public ResponsabileDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sResponsabileByID = connection.prepareStatement("SELECT * FROM Responsabile WHERE ID=?");
            //++++++sAulaByPosizione = connection.prepareStatement("SELECT ID AS aulaID FROM aula WHERE posizioneID=?");
            //++++++sAule = connection.prepareStatement("SELECT ID AS aulaID FROM aula");
            sResponsabileByEvento=connection.prepareStatement("SELECT r.* FROM Responsabile as r,Evento WHERE Evento.ID=?");
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
            iResponsabile = connection.prepareStatement("INSERT INTO Responsabile (nome,email) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            uResponsabile = connection.prepareStatement("UPDATE Responsabile SET nome=?, email=?, version=? WHERE ID=? and version=?");
            dResponsabile = connection.prepareStatement("DELETE FROM Responsabile WHERE ID=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing auleweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {
            sResponsabileByID.close();
            sResponsabileByEvento.close();
            iResponsabile.close();
            uResponsabile.close();
            dResponsabile.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Responsabile createResponsabile() {
        return new ResponsabileProxy(getDataLayer());
    }
    

    @Override
    public Responsabile getResponsabileByEvento(Evento evento) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void storeResponsabile(Responsabile responsabile) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Responsabile getResponsabile(int responsabile_key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
