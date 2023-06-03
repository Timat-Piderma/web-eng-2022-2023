package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Corso;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.proxy.CorsoProxy;
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

public class CorsoDAO_MySQL extends DAO implements CorsoDAO {

    private PreparedStatement sCorsoByID, sCorsoByEvento, sCorsoByNome;
    private PreparedStatement sCorsi;
    private PreparedStatement iCorso, uCorso, dCorso;

    public CorsoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sCorsoByID = connection.prepareStatement("SELECT * FROM corso WHERE ID=?");
            sCorsoByEvento = connection.prepareStatement("SELECT * FROM corso WHERE EventoID=?");
            sCorsoByNome = connection.prepareStatement("SELECT * FROM corso WHERE nome=?");
            sCorsi = connection.prepareStatement("SELECT ID AS corsoID FROM corso");

            //notare l'ultimo paametro extra di questa chiamata a
            //prepareStatement: lo usiamo per assicurarci che il JDBC
            //restituisca la chiave generata automaticamente per il
            //record inserito
            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iCorso = connection.prepareStatement("INSERT INTO corso (nome) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            uCorso = connection.prepareStatement("UPDATE corso SET nome=?, version=? WHERE ID=? and version=?");
            dCorso = connection.prepareStatement("DELETE FROM corso WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing auleweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            sCorsoByID.close();
            sCorsoByEvento.close();
            sCorsoByNome.close();

            sCorsi.close();

            iCorso.close();
            uCorso.close();
            dCorso.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Corso createCorso() {
        return new CorsoProxy(getDataLayer());
    }

    //helper
    private CorsoProxy createCorso(ResultSet rs) throws DataException {
        CorsoProxy c = (CorsoProxy) createCorso();
        try {
            c.setKey(rs.getInt("ID"));
            c.setNome(rs.getString("nome"));
            c.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create corso object from ResultSet", ex);
        }
        return c;
    }

    @Override
    public Corso getCorso(int corso_key) throws DataException {
        Corso c = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Corso.class, corso_key)) {
            c = dataLayer.getCache().get(Corso.class, corso_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                sCorsoByID.setInt(1, corso_key);
                try (ResultSet rs = sCorsoByID.executeQuery()) {
                    if (rs.next()) {
                        c = createCorso(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Corso.class, c);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load corso by ID", ex);
            }
        }
        return c;
    }

    @Override
    public Corso getCorsoByEvento(Evento evento) throws DataException {
        try {
            sCorsoByEvento.setInt(1, evento.getKey());
            try (ResultSet rs = sCorsoByEvento.executeQuery()) {
                if (rs.next()) {
                    return getCorso(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find corso", ex);
        }
        return null;
    }

    @Override
    public Corso getCorsoByNome(String nome) throws DataException {
        try {
            sCorsoByNome.setString(1, nome);
            try (ResultSet rs = sCorsoByNome.executeQuery()) {
                if (rs.next()) {
                    return getCorso(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find corso", ex);
        }
        return null;
    }

    @Override
    public List<Corso> getCorsi() throws DataException {
        List<Corso> result = new ArrayList();

        try (ResultSet rs = sCorsi.executeQuery()) {
            while (rs.next()) {
                result.add((Corso) getCorso(rs.getInt("corsoID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load corsi", ex);
        }
        return result;
    }

    @Override
    public void storeCorso(Corso corso) throws DataException {
        try {
            if (corso.getKey() != null && corso.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                //do not store the object if it is a proxy and does not indicate any modification
                if (corso instanceof DataItemProxy && !((DataItemProxy) corso).isModified()) {
                    return;
                }
                uCorso.setString(1, corso.getNome());

                long current_version = corso.getVersion();
                long next_version = current_version + 1;

                uCorso.setLong(2, next_version);
                uCorso.setInt(3, corso.getKey());
                uCorso.setLong(4, current_version);

                if (uCorso.executeUpdate() == 0) {
                    throw new OptimisticLockException(corso);
                } else {
                    corso.setVersion(next_version);
                }
            } else { //insert
                iCorso.setString(1, corso.getNome());

                if (iCorso.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iCorso.getGeneratedKeys()) {
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
                            corso.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            //add the new object to the cache
                            dataLayer.getCache().add(Corso.class, corso);
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
            if (corso instanceof DataItemProxy) {
                ((DataItemProxy) corso).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store corso", ex);
        }
    }

}
