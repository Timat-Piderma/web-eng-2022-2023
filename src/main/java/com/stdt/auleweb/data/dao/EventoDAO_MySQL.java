package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Corso;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.data.model.Responsabile;
import com.stdt.auleweb.data.model.Tipologia;
import com.stdt.auleweb.data.proxy.EventoProxy;
import com.stdt.auleweb.framework.data.DAO;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;
import com.stdt.auleweb.framework.data.OptimisticLockException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO_MySQL extends DAO implements EventoDAO {

    private PreparedStatement sEventoByID;
    private PreparedStatement sEventi, sEventiByAula, sEventiByCorso, sEventiByResponsabile;
    private PreparedStatement sEventiBySettimana, sEventiByGiorno, sEventiNextThreeHours, sEventiBySettimanaAndCorso;
    private PreparedStatement iEvento, uEvento, dEvento;

    public EventoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            //precompile all the queries uses in this class
            sEventoByID = connection.prepareStatement("SELECT * FROM evento WHERE ID=?");
            sEventi = connection.prepareStatement("SELECT ID AS eventoID FROM evento");
            sEventiByAula = connection.prepareStatement("SELECT ID AS eventoID FROM evento WHERE aulaID=?");
            sEventiByCorso = connection.prepareStatement("SELECT ID AS eventoID FROM evento WHERE corsoID=?");
            sEventiByResponsabile = connection.prepareStatement("SELECT ID AS eventoID FROM evento WHERE responsabileID=?");

            sEventiBySettimana = connection.prepareStatement("SELECT ID AS eventoID FROM evento WHERE WEEK(data)=WEEK(?) AND aulaID=?");
            sEventiByGiorno = connection.prepareStatement("SELECT ID AS eventoID FROM evento WHERE data=? AND gruppoID=?");
            sEventiNextThreeHours = connection.prepareStatement("SELECT ID AS eventoID FROM evento WHERE TIMEDIFF(oraInizio,?) <= 3 AND TIMEDIFF(oraInizio, ?) >= 0 AND data=? AND gruppoID=?");
            sEventiBySettimanaAndCorso = connection.prepareStatement("SELECT ID AS eventoID FROM evento WHERE WEEK(data)=WEEK(?) AND corsoID=?");

            iEvento = connection.prepareStatement("INSERT INTO evento (data,oraInizio,oraFine,descrizione,nome,tipologia,aulaID,responsabileID,corsoID) VALUES(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uEvento = connection.prepareStatement("UPDATE evento SET data=?,oraInizio=?,oraFine=?,descrizione=?, nome=?, tipologia=?, aulaID=?, responsabileID=?, corsoID=?, version=? WHERE ID=? and version=?");
            dEvento = connection.prepareStatement("DELETE FROM evento WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing auleweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent � una buona pratica...
        //also closing PreparedStamenents is a good practice...
        try {

            sEventoByID.close();

            sEventi.close();
            sEventiByAula.close();
            sEventiByCorso.close();
            sEventiByResponsabile.close();

            sEventiBySettimana.close();
            sEventiByGiorno.close();
            sEventiNextThreeHours.close();
            sEventiBySettimanaAndCorso.close();

            iEvento.close();
            uEvento.close();
            dEvento.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Evento createEvento() throws DataException {
        return new EventoProxy(getDataLayer());
    }

    //helper
    private EventoProxy createEvento(ResultSet rs) throws DataException {
        EventoProxy e = (EventoProxy) createEvento();
        try {
            e.setKey(rs.getInt("ID"));

            e.setData((LocalDate) rs.getObject("data"));
            e.setOraInizio((LocalTime) rs.getObject("oraInizio"));
            e.setOraFine((LocalTime) rs.getObject("oraFine"));
            e.setDescrizione(rs.getString("descrizione"));
            e.setNome(rs.getString("nome"));
            e.setTipologia((Tipologia) rs.getObject("tipologia"));
            e.setAulaKey(rs.getInt("aulaID"));
            e.setResponsabileKey(rs.getInt("responsabileID"));
            e.setCorsoKey(rs.getInt("corsoID"));
            e.setVersion(rs.getLong("version"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create evento object from ResultSet", ex);
        }
        return e;
    }

    @Override
    public List<Evento> getEventi() throws DataException {
        List<Evento> result = new ArrayList();

        try (ResultSet rs = sEventi.executeQuery()) {
            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("eventoID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi", ex);
        }
        return result;
    }

    @Override
    public List<Evento> getEventi(Aula aula) throws DataException {
        List<Evento> result = new ArrayList();

        try {
            sEventiByAula.setInt(1, aula.getKey());
            try (ResultSet rs = sEventiByAula.executeQuery()) {
                while (rs.next()) {
                    //la query  estrae solo gli ID degli articoli selezionati
                    //poi sarà getArticle che, con le relative query, popolerà
                    //gli oggetti corrispondenti. Meno efficiente, ma così la
                    //logica di creazione degli articoli è meglio incapsulata
                    //the query extracts only the IDs of the selected articles 
                    //then getArticle, with its queries, will populate the 
                    //corresponding objects. Less efficient, but in this way
                    //article creation logic is better encapsulated
                    result.add((Evento) getEvento(rs.getInt("eventoID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi by aula", ex);
        }
        return result;
    }

    @Override
    public List<Evento> getEventiBySettimana(Aula aula, LocalDate giorno) throws DataException {
        List<Evento> result = null;

        try {
            sEventiBySettimana.setObject(1, giorno);
            sEventiBySettimana.setInt(2, aula.getKey());
            ResultSet rs = sEventiBySettimana.executeQuery();

            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("eventoID")));
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi", ex);
        }
    }

    @Override
    public List<Evento> getEventiByGiorno(Gruppo gruppo, LocalDate giorno) throws DataException {
        List<Evento> result = null;

        try {
            sEventiBySettimana.setObject(1, giorno);
            sEventiBySettimana.setInt(2, gruppo.getKey());
            ResultSet rs = sEventiBySettimana.executeQuery();

            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("eventoID")));
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi", ex);
        }
    }

    @Override
    public List<Evento> getEventiNextThreeHours(Gruppo gruppo) throws DataException {
        List<Evento> result = null;

        try {

            sEventiBySettimana.setObject(1, LocalTime.now());
            sEventiBySettimana.setObject(2, LocalTime.now());
            sEventiBySettimana.setObject(3, gruppo);
            ResultSet rs = sEventiBySettimana.executeQuery();

            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("eventoID")));
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi", ex);
        }
    }

    @Override
    public List<Evento> getEventiBySettimanaAndCorso(Corso corso, LocalDate giorno) throws DataException {
        List<Evento> result = null;

        try {

            sEventiBySettimana.setObject(1, giorno);
            sEventiBySettimana.setObject(2, corso);
            ResultSet rs = sEventiBySettimana.executeQuery();

            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("eventoID")));
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi", ex);
        }
    }

    @Override
    public void storeEvento(Evento evento) throws DataException {
        try {
            if (evento.getKey() != null && evento.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                //do not store the object if it is a proxy and does not indicate any modification
                if (evento instanceof DataItemProxy && !((DataItemProxy) evento).isModified()) {
                    return;
                }
                uEvento.setObject(1, evento.getData());
                uEvento.setObject(2, evento.getOraInizio());
                uEvento.setObject(3, evento.getOraFine());
                uEvento.setString(4, evento.getDescrizione());
                uEvento.setString(5, evento.getNome());
                uEvento.setObject(6, evento.getTipologia());

                if (evento.getAula() != null) {
                    uEvento.setInt(7, evento.getAula().getKey());
                } else {
                    uEvento.setNull(7, java.sql.Types.INTEGER);
                }
                if (evento.getResponsabile() != null) {
                    uEvento.setInt(8, evento.getResponsabile().getKey());
                } else {
                    uEvento.setNull(8, java.sql.Types.INTEGER);
                }
                if (evento.getCorso() != null) {
                    uEvento.setInt(9, evento.getCorso().getKey());
                } else {
                    uEvento.setNull(9, java.sql.Types.INTEGER);
                }

                long current_version = evento.getVersion();
                long next_version = current_version + 1;

                uEvento.setLong(10, next_version);
                uEvento.setInt(11, evento.getKey());
                uEvento.setLong(12, current_version);

                if (uEvento.executeUpdate() == 0) {
                    throw new OptimisticLockException(evento);
                } else {
                    evento.setVersion(next_version);
                }
            } else { //insert
                iEvento.setObject(1, evento.getData());
                iEvento.setObject(2, evento.getOraInizio());
                iEvento.setObject(3, evento.getOraFine());
                iEvento.setString(4, evento.getDescrizione());
                iEvento.setString(5, evento.getNome());
                iEvento.setObject(6, evento.getTipologia());

                if (evento.getAula() != null) {
                    iEvento.setInt(7, evento.getAula().getKey());
                } else {
                    iEvento.setNull(7, java.sql.Types.INTEGER);
                }
                if (evento.getResponsabile() != null) {
                    iEvento.setInt(8, evento.getResponsabile().getKey());
                } else {
                    iEvento.setNull(8, java.sql.Types.INTEGER);
                }
                if (evento.getCorso() != null) {
                    iEvento.setInt(9, evento.getCorso().getKey());
                } else {
                    iEvento.setNull(9, java.sql.Types.INTEGER);
                }
                if (iEvento.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try (ResultSet keys = iEvento.getGeneratedKeys()) {
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
                            evento.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            //add the new object to the cache
                            dataLayer
                                    .getCache().add(Evento.class,
                                            evento);
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
            if (evento instanceof DataItemProxy) {
                ((DataItemProxy) evento).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store article", ex);
        }
    }

    @Override
    public Evento getEvento(int evento_key) throws DataException {
        Evento e = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache

        if (dataLayer.getCache().has(Evento.class,
                evento_key)) {
            e = dataLayer.getCache().get(Evento.class, evento_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                sEventoByID.setInt(1, evento_key);
                try (ResultSet rs = sEventoByID.executeQuery()) {
                    if (rs.next()) {
                        e = createEvento(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer
                                .getCache().add(Evento.class,
                                        e);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load evento by ID", ex);
            }
        }
        return e;
    }

    @Override
    public List<Evento> getEventiByResponsabile(Responsabile responsabile) throws DataException {
        List<Evento> result = new ArrayList();

        try {
            sEventiByResponsabile.setInt(1, responsabile.getKey());
            try (ResultSet rs = sEventiByResponsabile.executeQuery()) {
                while (rs.next()) {
                    //la query  estrae solo gli ID degli articoli selezionati
                    //poi sarà getArticle che, con le relative query, popolerà
                    //gli oggetti corrispondenti. Meno efficiente, ma così la
                    //logica di creazione degli articoli è meglio incapsulata
                    //the query extracts only the IDs of the selected articles 
                    //then getArticle, with its queries, will populate the 
                    //corresponding objects. Less efficient, but in this way
                    //article creation logic is better encapsulated
                    result.add((Evento) getEvento(rs.getInt("eventoID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi by responsabile", ex);
        }
        return result;
    }

    @Override
    public List<Evento> getEventiByCorso(Corso corso) throws DataException {
        List<Evento> result = new ArrayList();

        try {
            sEventiByCorso.setInt(1, corso.getKey());
            try (ResultSet rs = sEventiByCorso.executeQuery()) {
                while (rs.next()) {
                    //la query  estrae solo gli ID degli articoli selezionati
                    //poi sarà getArticle che, con le relative query, popolerà
                    //gli oggetti corrispondenti. Meno efficiente, ma così la
                    //logica di creazione degli articoli è meglio incapsulata
                    //the query extracts only the IDs of the selected articles 
                    //then getArticle, with its queries, will populate the 
                    //corresponding objects. Less efficient, but in this way
                    //article creation logic is better encapsulated
                    result.add((Evento) getEvento(rs.getInt("eventoID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi by corso", ex);
        }
        return result;
    }

}
