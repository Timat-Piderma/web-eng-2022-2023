package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.proxy.AttrezzaturaProxy;
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

public class AttrezzaturaDAO_MySQL extends DAO implements AttrezzaturaDAO {

    private PreparedStatement sAttrezzaturaByID;
    private PreparedStatement sAttrezzature, sAttrezzatureByAula;
    private PreparedStatement iAttrezzatura, uAttrezzatura, dAttrezzatura;

    public AttrezzaturaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sAttrezzaturaByID = connection.prepareStatement("SELECT * FROM attrezzatura WHERE ID=?");
            sAttrezzature = connection.prepareStatement("SELECT ID AS attrezzaturaID FROM attrezzatura");
            sAttrezzatureByAula = connection.prepareStatement("SELECT ID AS attrezzaturaID FROM aula WHERE aulaID=?");

            //notare l'ultimo paametro extra di questa chiamata a
            //prepareStatement: lo usiamo per assicurarci che il JDBC
            //restituisca la chiave generata automaticamente per il
            //record inserito
            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iAttrezzatura = connection.prepareStatement("INSERT INTO attrezzatura (nome) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            uAttrezzatura = connection.prepareStatement("UPDATE attrezzatura SET nome=?, version=? WHERE ID=? and version=?");
            dAttrezzatura = connection.prepareStatement("DELETE FROM attrezzatura WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing auleweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            sAttrezzaturaByID.close();

            sAttrezzatureByAula.close();
            sAttrezzature.close();

            iAttrezzatura.close();
            uAttrezzatura.close();
            dAttrezzatura.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Attrezzatura createAttrezzatura() {
        return new AttrezzaturaProxy(getDataLayer());
    }

    //helper
    public Attrezzatura createAttrezzatura(ResultSet rs) throws DataException {
        AttrezzaturaProxy a = (AttrezzaturaProxy) createAttrezzatura();
        try {
            a.setKey(rs.getInt("ID"));
            a.setNome(rs.getString("nome"));
            a.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create attrezzatura object from ResultSet", ex);
        }
        return a;
    }

    @Override
    public Attrezzatura getAttrezzatura(int attrezzatura_key) throws DataException {
        Attrezzatura a = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Attrezzatura.class, attrezzatura_key)) {
            a = dataLayer.getCache().get(Attrezzatura.class, attrezzatura_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                sAttrezzaturaByID.setInt(1, attrezzatura_key);
                try (ResultSet rs = sAttrezzaturaByID.executeQuery()) {
                    if (rs.next()) {
                        a = createAttrezzatura(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Attrezzatura.class, a);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load attrezzatura by ID", ex);
            }
        }
        return a;
    }

    @Override
    public List<Attrezzatura> getAttrezzatureByAula(Aula aula) throws DataException {
        List<Attrezzatura> result = new ArrayList();

        try {
            sAttrezzatureByAula.setInt(1, aula.getKey());
            try (ResultSet rs = sAttrezzatureByAula.executeQuery()) {
                while (rs.next()) {
                    //la query  estrae solo gli ID degli articoli selezionati
                    //poi sarà getArticle che, con le relative query, popolerà
                    //gli oggetti corrispondenti. Meno efficiente, ma così la
                    //logica di creazione degli articoli è meglio incapsulata
                    //the query extracts only the IDs of the selected articles 
                    //then getArticle, with its queries, will populate the 
                    //corresponding objects. Less efficient, but in this way
                    //article creation logic is better encapsulated
                    result.add((Attrezzatura) getAttrezzatura(rs.getInt("attrezzaturaID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load attrezzature by aula", ex);
        }
        return result;
    }

    @Override
    public void storeAttrezzatura(Attrezzatura attrezzatura) throws DataException {
        try {
            if (attrezzatura.getKey() != null && attrezzatura.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                //do not store the object if it is a proxy and does not indicate any modification
                if (attrezzatura instanceof DataItemProxy && !((DataItemProxy) attrezzatura).isModified()) {
                    return;
                }
                uAttrezzatura.setString(1, attrezzatura.getNome());

                long current_version = attrezzatura.getVersion();
                long next_version = current_version + 1;

                uAttrezzatura.setLong(6, next_version);
                uAttrezzatura.setInt(7, attrezzatura.getKey());
                uAttrezzatura.setLong(8, current_version);

                if (uAttrezzatura.executeUpdate() == 0) {
                    throw new OptimisticLockException(attrezzatura);
                } else {
                    attrezzatura.setVersion(next_version);
                }
            } else { //insert
                iAttrezzatura.setString(1, attrezzatura.getNome());

                if (iAttrezzatura.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iAttrezzatura.getGeneratedKeys()) {
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
                            attrezzatura.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            //add the new object to the cache
                            dataLayer.getCache().add(Attrezzatura.class, attrezzatura);
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
            if (attrezzatura instanceof DataItemProxy) {
                ((DataItemProxy) attrezzatura).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store article", ex);
        }
    }

    @Override
    public List<Attrezzatura> getAttrezzature() throws DataException {
        List<Attrezzatura> result = new ArrayList();

        try (ResultSet rs = sAttrezzature.executeQuery()) {
            while (rs.next()) {
                result.add((Attrezzatura) getAttrezzatura(rs.getInt("attrezzaturaID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load attrezzature", ex);
        }
        return result;
    }
}
