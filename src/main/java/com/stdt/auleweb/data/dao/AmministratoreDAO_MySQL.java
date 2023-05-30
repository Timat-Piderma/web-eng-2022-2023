package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Amministratore;
import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.proxy.AmministratoreProxy;
import com.stdt.auleweb.framework.data.DAO;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;
import jakarta.persistence.OptimisticLockException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AmministratoreDAO_MySQL extends DAO implements AmministratoreDAO {

    private PreparedStatement sAmministratoreByID, sAmministratoreByUsername;
    private PreparedStatement iAmministratore, uAmministratore, dAmministratore;

    public AmministratoreDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sAmministratoreByID = connection.prepareStatement("SELECT * FROM amministratore WHERE ID=?");
            sAmministratoreByUsername = connection.prepareStatement("SELECT * FROM amministratore WHERE username=?");
            //notare l'ultimo paametro extra di questa chiamata a
            //prepareStatement: lo usiamo per assicurarci che il JDBC
            //restituisca la chiave generata automaticamente per il
            //record inserito
            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iAmministratore = connection.prepareStatement("INSERT INTO amministratore (username,password) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            uAmministratore = connection.prepareStatement("UPDATE amministratore SET username=?, password=?, version=? WHERE ID=? and version=?");
            dAmministratore = connection.prepareStatement("DELETE FROM amministratore WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing auleweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            sAmministratoreByID.close();
            sAmministratoreByUsername.close();
            iAmministratore.close();
            uAmministratore.close();
            dAmministratore.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Amministratore createAmministratore() {
        return new AmministratoreProxy(getDataLayer());
    }

    //helper
    public Amministratore createAmministratore(ResultSet rs) throws DataException {
        AmministratoreProxy a = (AmministratoreProxy) createAmministratore();
        try {
            a.setKey(rs.getInt("ID"));
            a.setUsername(rs.getString("username"));
            a.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create attrezzatura object from ResultSet", ex);
        }
        return a;
    }

    @Override
    public Amministratore getAmministratore(int amministratore_key) throws DataException {
        Amministratore a = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Amministratore.class, amministratore_key)) {
            a = dataLayer.getCache().get(Amministratore.class, amministratore_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                sAmministratoreByID.setInt(1, amministratore_key);
                try (ResultSet rs = sAmministratoreByID.executeQuery()) {
                    if (rs.next()) {
                        a = createAmministratore(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Amministratore.class, a);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load attrezzatura by ID", ex);
            }
        }
        return a;
    }

    @Override
    public void storeAmministratore(Amministratore amministratore) throws DataException {
        try {
            if (amministratore.getKey() != null && amministratore.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                //do not store the object if it is a proxy and does not indicate any modification
                if (amministratore instanceof DataItemProxy && !((DataItemProxy) amministratore).isModified()) {
                    return;
                }
                uAmministratore.setString(1, amministratore.getUsername());
                uAmministratore.setString(2, amministratore.getPassword());

                long current_version = amministratore.getVersion();
                long next_version = current_version + 1;

                uAmministratore.setLong(6, next_version);
                uAmministratore.setInt(7, amministratore.getKey());
                uAmministratore.setLong(8, current_version);

                if (uAmministratore.executeUpdate() == 0) {
                    throw new OptimisticLockException(amministratore);
                } else {
                    amministratore.setVersion(next_version);
                }
            } else { //insert
                iAmministratore.setString(1, amministratore.getUsername());
                iAmministratore.setString(2, amministratore.getPassword());
                if (iAmministratore.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iAmministratore.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        //the returned value is a ResultSet with a distinct record for
                        //each generated key (only one in our case)
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            //the record fields are the key componenets
                            //(a single integer in our case)
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave in caso di inserimento
                            //after an insert, uopdate the object key
                            amministratore.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            //add the new object to the cache
                            dataLayer.getCache().add(Amministratore.class, amministratore);
                        }
                    }
                }
            }

//            //se possibile, restituiamo l'oggetto appena inserito RICARICATO
//            //dal database tramite le API del modello. In tal
//            //modo terremo conto di ogni modifica apportata
//            //durante la fase di inserimento
//            //if possible, we return the just-inserted object RELOADED from the
//            //database through our API. In this way, the resulting
//            //object will ambed any data correction performed by
//            //the DBMS
//            if (key > 0) {
//                article.copyFrom(getArticle(key));
//            }
            //se abbiamo un proxy, resettiamo il suo attributo dirty
            //if we have a proxy, reset its dirty attribute
            if (amministratore instanceof DataItemProxy) {
                ((DataItemProxy) amministratore).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store amministratore", ex);
        }
    }

    @Override
    public Amministratore getAmministratoreByUsername(String username) throws DataException {
        try {
            sAmministratoreByUsername.setString(1, username);
            try (ResultSet rs = sAmministratoreByUsername.executeQuery()) {
                if (rs.next()) {
                    return getAmministratore(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find amministratore", ex);
        }
        return null;
    }

}
