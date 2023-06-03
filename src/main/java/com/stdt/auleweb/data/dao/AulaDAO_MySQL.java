package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.framework.data.DAO;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataLayer;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.data.proxy.AulaProxy;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.OptimisticLockException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AulaDAO_MySQL extends DAO implements AulaDAO {

    private PreparedStatement sAulaByID;
    private PreparedStatement sAule, sAuleByPosizione, sAulaByGruppo;
    private PreparedStatement iAula, uAula, dAula;

    public AulaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sAulaByID = connection.prepareStatement("SELECT * FROM aula WHERE ID=?");
            sAule = connection.prepareStatement("SELECT ID AS aulaID FROM aula");
            sAuleByPosizione = connection.prepareStatement("SELECT ID AS aulaID FROM aula WHERE posizioneID=?");
            sAulaByGruppo = connection.prepareStatement("SELECT ID AS aulaID FROM aula WHERE gruppoID=?");

            //notare l'ultimo paametro extra di questa chiamata a
            //prepareStatement: lo usiamo per assicurarci che il JDBC
            //restituisca la chiave generata automaticamente per il
            //record inserito
            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iAula = connection.prepareStatement("INSERT INTO aula (nome,capienza,numeroPreseElettriche,numeroPreseRete,emailResponsabile,note,gruppoID,posizioneID) VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uAula = connection.prepareStatement("UPDATE aula SET nome=?,capienza=?,numeroPreseElettriche=?,numeroPreseRete=?, emailResponsabile=?, note=?, gruppoID=?, posizioneID=?, version=? WHERE ID=? and version=?");
            dAula = connection.prepareStatement("DELETE FROM aula WHERE ID=?");
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

            sAule.close();
            sAuleByPosizione.close();
            sAulaByGruppo.close();

            iAula.close();
            uAula.close();
            dAula.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Aula createAula() {
        return new AulaProxy(getDataLayer());
    }

    public Aula createAula(ResultSet rs) throws DataException {
        AulaProxy a = (AulaProxy) createAula();
        try {
            a.setKey(rs.getInt("ID"));
            a.setGruppoKey(rs.getInt("gruppoID"));
            a.setPosizioneKey(rs.getInt("posizioneID"));
            a.setNome(rs.getString("nome"));
            a.setCapienza(rs.getInt("capienza"));
            a.setNumeroPreseElettriche(rs.getInt("numeroPreseElettriche"));
            a.setNumeroPreseRete(rs.getInt("numeroPreseRete"));
            a.setEmailResponsabile(rs.getString("emailResponsabile"));
            a.setNote(rs.getString("note"));
            a.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create aula object form ResultSet", ex);

        }
        return a;
    }

    @Override
    public Aula getAula(int aula_key) throws DataException {
        Aula a = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Aula.class, aula_key)) {
            a = dataLayer.getCache().get(Aula.class, aula_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                sAulaByID.setInt(1, aula_key);
                try (ResultSet rs = sAulaByID.executeQuery()) {
                    if (rs.next()) {
                        a = createAula(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Aula.class, a);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load aula by ID", ex);
            }
        }
        return a;
    }

    @Override
    public List<Aula> getAule() throws DataException {
        List<Aula> result = new ArrayList();

        try (ResultSet rs = sAule.executeQuery()) {
            while (rs.next()) {
                result.add((Aula) getAula(rs.getInt("aulaID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load aule", ex);
        }
        return result;
    }

    @Override
    public List<Aula> getAuleByPosizione(Posizione posizione) throws DataException {
        List<Aula> result = new ArrayList();

        try {
            sAuleByPosizione.setInt(1, posizione.getKey());
            try (ResultSet rs = sAuleByPosizione.executeQuery()) {
                while (rs.next()) {
                    //la query  estrae solo gli ID degli articoli selezionati
                    //poi sarà getArticle che, con le relative query, popolerà
                    //gli oggetti corrispondenti. Meno efficiente, ma così la
                    //logica di creazione degli articoli è meglio incapsulata
                    //the query extracts only the IDs of the selected articles 
                    //then getArticle, with its queries, will populate the 
                    //corresponding objects. Less efficient, but in this way
                    //article creation logic is better encapsulated
                    result.add((Aula) getAula(rs.getInt("posizioneID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load aule by posizione", ex);
        }
        return result;
    }

    @Override
    public List<Aula> getAuleByGruppo(Gruppo gruppo) throws DataException {
        List<Aula> result = new ArrayList();

        try {
            sAuleByPosizione.setInt(1, gruppo.getKey());
            try (ResultSet rs = sAuleByPosizione.executeQuery()) {
                while (rs.next()) {
                    //la query  estrae solo gli ID degli articoli selezionati
                    //poi sarà getArticle che, con le relative query, popolerà
                    //gli oggetti corrispondenti. Meno efficiente, ma così la
                    //logica di creazione degli articoli è meglio incapsulata
                    //the query extracts only the IDs of the selected articles 
                    //then getArticle, with its queries, will populate the 
                    //corresponding objects. Less efficient, but in this way
                    //article creation logic is better encapsulated
                    result.add((Aula) getAula(rs.getInt("posizioneID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load aule by posizione", ex);
        }
        return result;
    }

    @Override
    public List<Aula> getAuleByAttrezzatura(Attrezzatura attrezzatura) throws DataException {
        List<Aula> result = new ArrayList();

        try {
            sAuleByPosizione.setInt(1, attrezzatura.getKey());
            try (ResultSet rs = sAuleByPosizione.executeQuery()) {
                while (rs.next()) {
                    //la query  estrae solo gli ID degli articoli selezionati
                    //poi sarà getArticle che, con le relative query, popolerà
                    //gli oggetti corrispondenti. Meno efficiente, ma così la
                    //logica di creazione degli articoli è meglio incapsulata
                    //the query extracts only the IDs of the selected articles 
                    //then getArticle, with its queries, will populate the 
                    //corresponding objects. Less efficient, but in this way
                    //article creation logic is better encapsulated
                    result.add((Aula) getAula(rs.getInt("attrezzaturaID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load aule by attrezzatura", ex);
        }
        return result;
    }

    @Override
    public List<Aula> getAuleByEvento(Evento evento) throws DataException {
        List<Aula> result = new ArrayList();

        try {
            sAuleByPosizione.setInt(1, evento.getKey());
            try (ResultSet rs = sAuleByPosizione.executeQuery()) {
                while (rs.next()) {
                    //la query  estrae solo gli ID degli articoli selezionati
                    //poi sarà getArticle che, con le relative query, popolerà
                    //gli oggetti corrispondenti. Meno efficiente, ma così la
                    //logica di creazione degli articoli è meglio incapsulata
                    //the query extracts only the IDs of the selected articles 
                    //then getArticle, with its queries, will populate the 
                    //corresponding objects. Less efficient, but in this way
                    //article creation logic is better encapsulated
                    result.add((Aula) getAula(rs.getInt("eventoID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load aule by evento", ex);
        }
        return result;
    }

    @Override
    public void storeAula(Aula aula) throws DataException {
        try {
            if (aula.getKey() != null && aula.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                //do not store the object if it is a proxy and does not indicate any modification
                if (aula instanceof DataItemProxy && !((DataItemProxy) aula).isModified()) {
                    return;
                }
                uAula.setString(1, aula.getNome());
                uAula.setInt(2, aula.getCapienza());
                uAula.setInt(3, aula.getNumeroPreseElettriche());
                uAula.setInt(4, aula.getNumeroPreseRete());
                uAula.setString(5, aula.getEmailResponsabile());
                uAula.setString(6, aula.getNote());

                if (aula.getGruppo() != null) {
                    uAula.setInt(7, aula.getGruppo().getKey());
                } else {
                    uAula.setNull(7, java.sql.Types.INTEGER);
                }
                if (aula.getPosizione() != null) {
                    uAula.setInt(8, aula.getPosizione().getKey());
                } else {
                    uAula.setNull(8, java.sql.Types.INTEGER);
                }

                long current_version = aula.getVersion();
                long next_version = current_version + 1;

                uAula.setLong(6, next_version);
                uAula.setInt(7, aula.getKey());
                uAula.setLong(8, current_version);

                if (uAula.executeUpdate() == 0) {
                    throw new OptimisticLockException(aula);
                } else {
                    aula.setVersion(next_version);
                }
            } else { //insert
                iAula.setString(1, aula.getNome());
                iAula.setInt(2, aula.getCapienza());
                iAula.setInt(3, aula.getNumeroPreseElettriche());
                iAula.setInt(4, aula.getNumeroPreseRete());
                iAula.setString(5, aula.getEmailResponsabile());
                iAula.setString(6, aula.getNome());

                if (aula.getGruppo() != null) {
                    iAula.setInt(7, aula.getGruppo().getKey());
                } else {
                    iAula.setNull(7, java.sql.Types.INTEGER);
                }

                if (aula.getPosizione() != null) {
                    iAula.setInt(8, aula.getPosizione().getKey());
                } else {
                    iAula.setNull(8, java.sql.Types.INTEGER);
                }

                if (iAula.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iAula.getGeneratedKeys()) {
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
                            aula.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            //add the new object to the cache
                            dataLayer.getCache().add(Aula.class, aula);
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
            if (aula instanceof DataItemProxy) {
                ((DataItemProxy) aula).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store aula", ex);
        }
    }

}
