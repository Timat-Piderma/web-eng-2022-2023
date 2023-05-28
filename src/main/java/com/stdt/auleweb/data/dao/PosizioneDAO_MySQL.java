package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.framework.data.DAO;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataLayer;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.data.proxy.PosizioneProxy;
import com.stdt.auleweb.framework.data.DataItemProxy;
import jakarta.persistence.OptimisticLockException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PosizioneDAO_MySQL extends DAO implements PosizioneDAO {

    private PreparedStatement sPosizioneByID;
    private PreparedStatement sPosizioneByAula;
    private PreparedStatement iPosizione, uPosizione, dPosizione;

    public PosizioneDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sPosizioneByID = connection.prepareStatement("SELECT * FROM posizione WHERE ID=?");
            sPosizioneByAula = connection.prepareStatement("SELECT * FROM posizione WHERE aulaID=?");

            //notare l'ultimo paametro extra di questa chiamata a
            //prepareStatement: lo usiamo per assicurarci che il JDBC
            //restituisca la chiave generata automaticamente per il
            //record inserito
            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iPosizione = connection.prepareStatement("INSERT INTO posizione (nome,edificio,piano) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uPosizione = connection.prepareStatement("UPDATE posizione SET nome=?,edificio=?,piano=?,version=? WHERE ID=? and version=?");
            dPosizione = connection.prepareStatement("DELETE FROM posizione WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing auleweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            sPosizioneByID.close();

            sPosizioneByAula.close();

            iPosizione.close();
            uPosizione.close();
            dPosizione.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Posizione createPosizione() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //helper
    private PosizioneProxy createPosizione(ResultSet rs) throws DataException {
        PosizioneProxy p = (PosizioneProxy) createPosizione();
        try {
            p.setKey(rs.getInt("ID"));
            p.setNome(rs.getString("nome"));
            p.setEdificio(rs.getString("edificio"));
            p.setPiano(rs.getString("piano"));
            p.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create posizione object from ResultSet", ex);
        }
        return p;
    }

    @Override
    public Posizione getPosizione(int posizione_key) throws DataException {
        Posizione p = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Posizione.class, posizione_key)) {
            p = dataLayer.getCache().get(Posizione.class, posizione_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                sPosizioneByID.setInt(1, posizione_key);
                try (ResultSet rs = sPosizioneByID.executeQuery()) {
                    if (rs.next()) {
                        p = createPosizione(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Posizione.class, p);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load posizione by ID", ex);
            }
        }
        return p;
    }

    @Override
    public Posizione getPosizioneByAula(Aula aula) throws DataException {
        try {
            sPosizioneByAula.setInt(1, aula.getKey());
            try (ResultSet rs = sPosizioneByAula.executeQuery()) {
                if (rs.next()) {
                    return getPosizione(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find posizione", ex);
        }
        return null;
    }

    @Override
    public void storePosizione(Posizione posizione) throws DataException {
        try {
            if (posizione.getKey() != null && posizione.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                //do not store the object if it is a proxy and does not indicate any modification
                if (posizione instanceof DataItemProxy && !((DataItemProxy) posizione).isModified()) {
                    return;
                }
                uPosizione.setString(1, posizione.getNome());
                uPosizione.setString(2, posizione.getEdificio());
                uPosizione.setString(3, posizione.getPiano());

                long current_version = posizione.getVersion();
                long next_version = current_version + 1;

                uPosizione.setLong(6, next_version);
                uPosizione.setInt(7, posizione.getKey());
                uPosizione.setLong(8, current_version);

                if (uPosizione.executeUpdate() == 0) {
                    throw new OptimisticLockException(posizione);
                } else {
                    posizione.setVersion(next_version);
                }
            } else { //insert
                iPosizione.setString(1, posizione.getNome());
                iPosizione.setString(2, posizione.getEdificio());
                iPosizione.setString(3, posizione.getPiano());
            }
            if (iPosizione.executeUpdate() == 1) {
                //per leggere la chiave generata dal database
                //per il record appena inserito, usiamo il metodo
                //getGeneratedKeys sullo statement.
                //to read the generated record key from the database
                //we use the getGeneratedKeys method on the same statement
                try (ResultSet keys = iPosizione.getGeneratedKeys()) {
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
                        posizione.setKey(key);
                        //inseriamo il nuovo oggetto nella cache
                        //add the new object to the cache
                        dataLayer.getCache().add(Posizione.class, posizione);
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
            if (posizione instanceof DataItemProxy) {
                ((DataItemProxy) posizione).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store article", ex);
        }
    }

}
