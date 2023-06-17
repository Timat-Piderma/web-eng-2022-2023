package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Responsabile;
import com.stdt.auleweb.data.proxy.ResponsabileProxy;
import com.stdt.auleweb.framework.data.DAO;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;
import com.stdt.auleweb.framework.data.OptimisticLockException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ResponsabileDAO_MySQL extends DAO implements ResponsabileDAO {

    private PreparedStatement sResponsabileByID, sResponsabileByEvento;
    private PreparedStatement sResponsabili;
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
            sResponsabileByID = connection.prepareStatement("SELECT * FROM responsabile WHERE ID=?");
            sResponsabileByEvento = connection.prepareStatement("SELECT * FROM responsabile WHERE eventoID=?");
            sResponsabili = connection.prepareStatement("SELECT ID FROM responsabile");
            //notare l'ultimo paametro extra di questa chiamata a
            //prepareStatement: lo usiamo per assicurarci che il JDBC
            //restituisca la chiave generata automaticamente per il
            //record inserito
            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iResponsabile = connection.prepareStatement("INSERT INTO responsabile (nome,email) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            uResponsabile = connection.prepareStatement("UPDATE responsabile SET nome=?,email=?,version=? WHERE ID=? and version=?");
            dResponsabile = connection.prepareStatement("DELETE FROM responsabile WHERE ID=?");
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

            sResponsabili.close();

            iResponsabile.close();
            uResponsabile.close();
            dResponsabile.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Responsabile createResponsabile() throws DataException {
        return new ResponsabileProxy(getDataLayer());
    }

    //helper
    private ResponsabileProxy createResponsabile(ResultSet rs) throws DataException {
        ResponsabileProxy r = (ResponsabileProxy) createResponsabile();
        try {
            r.setKey(rs.getInt("ID"));
            r.setNome(rs.getString("nome"));
            r.setEmail(rs.getString("email"));
            r.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create responsabile object from ResultSet", ex);
        }
        return r;
    }

    @Override
    public Responsabile getResponsabile(int responsabile_key) throws DataException {
        Responsabile r = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Responsabile.class, responsabile_key)) {
            r = dataLayer.getCache().get(Responsabile.class, responsabile_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                sResponsabileByID.setInt(1, responsabile_key);
                try (ResultSet rs = sResponsabileByID.executeQuery()) {
                    if (rs.next()) {
                        r = createResponsabile(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Responsabile.class, r);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load responsabile by ID", ex);
            }
        }
        return r;
    }

    @Override
    public Responsabile getResponsabileByEvento(Evento evento) throws DataException {
        try {
            sResponsabileByEvento.setInt(1, evento.getKey());
            try (ResultSet rs = sResponsabileByEvento.executeQuery()) {
                if (rs.next()) {
                    return getResponsabile(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find responsabile", ex);
        }
        return null;
    }

    @Override
    public List<Responsabile> getResponsabili() throws DataException {
        List<Responsabile> result = new ArrayList();

        try (ResultSet rs = sResponsabili.executeQuery()) {
            while (rs.next()) {
                result.add((Responsabile) getResponsabile(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load responsabili", ex);
        }
        return result;
    }

    @Override
    public void storeResponsabile(Responsabile responsabile) throws DataException {
        try {
            if (responsabile.getKey() != null && responsabile.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                //do not store the object if it is a proxy and does not indicate any modification
                if (responsabile instanceof DataItemProxy && !((DataItemProxy) responsabile).isModified()) {
                    return;
                }
                uResponsabile.setString(1, responsabile.getNome());
                uResponsabile.setString(2, responsabile.getEmail());

                long current_version = responsabile.getVersion();
                long next_version = current_version + 1;

                uResponsabile.setLong(3, next_version);
                uResponsabile.setInt(4, responsabile.getKey());
                uResponsabile.setLong(5, current_version);

                if (uResponsabile.executeUpdate() == 0) {
                    throw new OptimisticLockException(responsabile);
                } else {
                    responsabile.setVersion(next_version);
                }
            } else { //insert
                iResponsabile.setString(1, responsabile.getNome());
                iResponsabile.setString(2, responsabile.getEmail());

                if (iResponsabile.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iResponsabile.getGeneratedKeys()) {
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
                            responsabile.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            //add the new object to the cache
                            dataLayer.getCache().add(Responsabile.class, responsabile);
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
            if (responsabile instanceof DataItemProxy) {
                ((DataItemProxy) responsabile).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store responsabile", ex);
        }
    }
}
