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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO_MySQL extends DAO implements EventoDAO {

    private PreparedStatement sEventoByID;
    private PreparedStatement sEventi, sEventiByAula, sEventiByCorso, sEventiByResponsabile;
    private PreparedStatement sEventiBySettimana, sEventiByGiorno, sEventiNextThreeHours, sEventiBySettimanaAndCorso, sEventiRicorrenti;
    private PreparedStatement iEvento, uEvento, dEvento;

    private PreparedStatement iTiene, dTiene;
    private PreparedStatement iRichiede, uRichiede, dRichiede;

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

            sEventiBySettimana = connection.prepareStatement("SELECT ID AS eventoID FROM evento WHERE WEEK(evento.giorno)=WEEK(?) AND aulaID=?");
            sEventiByGiorno = connection.prepareStatement("SELECT evento.ID as eventoID from evento inner join tiene on tiene.eventoID = evento.ID inner join aula on tiene.aulaID = aula.ID where evento.giorno = ? and aula.gruppoID = ?");
            sEventiNextThreeHours = connection.prepareStatement("SELECT evento.ID AS eventoID FROM evento JOIN tiene on evento.ID = tiene.eventoID JOIN aula on aula.ID = tiene.aulaID WHERE evento.oraInizio >= CURRENT_TIMESTAMP AND evento.oraInizio <= CURRENT_TIMESTAMP + INTERVAL 3 HOUR AND aula.gruppoID=? AND evento.giorno= curdate()");
            sEventiBySettimanaAndCorso = connection.prepareStatement("SELECT evento.ID AS eventoID FROM evento JOIN aula on evento.aulaID = aula.ID WHERE WEEK(giorno)=WEEK(?) AND corsoID=? AND aula.gruppoID=?");
            sEventiRicorrenti = connection.prepareStatement("SELECT ID AS eventoID from evento where nome=? AND responsabileID=? order by giorno ");

            iEvento = connection.prepareStatement("INSERT INTO evento (giorno,oraInizio,oraFine,descrizione,nome,tipologia,aulaID,responsabileID,corsoID) VALUES(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uEvento = connection.prepareStatement("UPDATE evento SET giorno=?,oraInizio=?,oraFine=?,descrizione=?, nome=?, tipologia=?, aulaID=?, responsabileID=?, corsoID=?, version=? WHERE ID=? and version=?");
            dEvento = connection.prepareStatement("DELETE FROM evento WHERE ID=?");

            iTiene = connection.prepareStatement("INSERT INTO tiene (aulaID, eventoID) VALUES(?,?)");
            dTiene = connection.prepareStatement("DELETE From tiene WHERE aulaID=? AND eventoID=?");

            iRichiede = connection.prepareStatement("INSERT INTO richiede (eventoID, corsoID) VALUES(?,?)");
            uRichiede = connection.prepareStatement("UPDATE richiede SET corsoID=? WHERE eventoID=?");
            dRichiede = connection.prepareStatement("DELETE FROM richiede WHERE eventoID=?");
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

            iTiene.close();
            dTiene.close();

            iRichiede.close();
            uRichiede.close();
            dRichiede.close();

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

            e.setGiorno((Date) rs.getObject("giorno"));
            e.setOraInizio(Time.valueOf(rs.getString("oraInizio")));
            e.setOraFine(Time.valueOf(rs.getString("oraFine")));
            e.setDescrizione(rs.getString("descrizione"));
            e.setNome(rs.getString("nome"));
            e.setTipologia(Tipologia.valueOf(rs.getObject("tipologia").toString()));
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

        try ( ResultSet rs = sEventi.executeQuery()) {
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
            try ( ResultSet rs = sEventiByAula.executeQuery()) {
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
    public List<Evento> getEventiBySettimana(Aula aula, Date giorno) throws DataException {
        List<Evento> result = new ArrayList();

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
    public List<Evento> getEventiRicorrenti(String nome, int IDresponsabile) throws DataException {
        List<Evento> result = new ArrayList();

        try {
            sEventiRicorrenti.setString(1, nome);
            sEventiRicorrenti.setInt(2, IDresponsabile);
            ResultSet rs = sEventiRicorrenti.executeQuery();

            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("eventoID")));
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi", ex);
        }
    }

    @Override
    public List<Evento> getEventiByGiorno(Gruppo gruppo, Date giorno) throws DataException {
        List<Evento> result = new ArrayList();

        try {
            sEventiByGiorno.setString(1, giorno.toString());
            sEventiByGiorno.setInt(2, gruppo.getKey());
            ResultSet rs = sEventiByGiorno.executeQuery();

            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("eventoID")));
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi giorno.toString()", ex);
        }
    }

    @Override
    public List<Evento> getEventiNextThreeHours(Gruppo gruppo) throws DataException {
        List<Evento> result = new ArrayList();

        try {
            sEventiNextThreeHours.setInt(1, gruppo.getKey());

            ResultSet rs = sEventiNextThreeHours.executeQuery();

            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("eventoID")));
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Unable to load eventi", ex);
        }
    }

    @Override
    public List<Evento> getEventiBySettimanaAndCorso(Corso corso, String giorno, Gruppo gruppo) throws DataException {
        List<Evento> result = new ArrayList();

        try {

            sEventiBySettimanaAndCorso.setString(1, giorno);
            sEventiBySettimanaAndCorso.setInt(2, corso.getKey());
            sEventiBySettimanaAndCorso.setInt(3, gruppo.getKey());
            ResultSet rs = sEventiBySettimanaAndCorso.executeQuery();

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
                uEvento.setObject(1, evento.getGiorno());
                uEvento.setObject(2, evento.getOraInizio());
                uEvento.setObject(3, evento.getOraFine());
                uEvento.setString(4, evento.getDescrizione());
                uEvento.setString(5, evento.getNome());
                uEvento.setString(6, evento.getTipologia().toString());

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
                    uRichiede.setInt(2, evento.getKey());
                    uRichiede.setInt(1, evento.getCorso().getKey());
                } else {
                    uEvento.setNull(9, java.sql.Types.INTEGER);
                    dRichiede.setInt(1, evento.getKey());
                    dRichiede.executeUpdate();
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

                if (evento.getCorso() != null) {

                    if (uRichiede.executeUpdate() == 0) {
                        iRichiede.setInt(1, evento.getKey());
                        iRichiede.setInt(2, evento.getCorso().getKey());
                        iRichiede.executeUpdate();
                    }
                } else {
                    dRichiede.executeUpdate();
                }
            } else { //insert
                iEvento.setDate(1, new Date(evento.getGiorno().getTime()));
                iEvento.setTime(2, new Time(evento.getOraInizio().getTime()));
                iEvento.setTime(3, new Time(evento.getOraFine().getTime()));
                iEvento.setString(4, evento.getDescrizione());
                iEvento.setString(5, evento.getNome());
                iEvento.setString(6, evento.getTipologia().toString());

                if (evento.getAula() != null) {
                    iEvento.setInt(7, evento.getAula().getKey());
                    iTiene.setInt(1, evento.getAula().getKey());
                } else {
                    iEvento.setNull(7, java.sql.Types.INTEGER);
                    iTiene.setNull(1, java.sql.Types.INTEGER);
                }
                if (evento.getResponsabile() != null) {
                    iEvento.setInt(8, evento.getResponsabile().getKey());
                } else {
                    iEvento.setNull(8, java.sql.Types.INTEGER);
                }
                if (evento.getCorso() != null) {
                    iEvento.setInt(9, evento.getCorso().getKey());
                    iRichiede.setInt(2, evento.getCorso().getKey());
                } else {
                    iEvento.setNull(9, java.sql.Types.INTEGER);
                    iRichiede.setNull(2, java.sql.Types.INTEGER);
                }

                if (iEvento.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    //to read the generated record key from the database
                    //we use the getGeneratedKeys method on the same statement
                    try ( ResultSet keys = iEvento.getGeneratedKeys()) {
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
                            dataLayer.getCache().add(Evento.class, evento);

                            iTiene.setInt(2, evento.getKey());
                            iTiene.executeUpdate();

                            if (evento.getCorso() != null) {
                                iRichiede.setInt(1, evento.getKey());
                                iRichiede.executeUpdate();
                            }
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
            throw new DataException("Unable to store evento", ex);
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
                try ( ResultSet rs = sEventoByID.executeQuery()) {
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
            try ( ResultSet rs = sEventiByResponsabile.executeQuery()) {
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
            try ( ResultSet rs = sEventiByCorso.executeQuery()) {
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

    @Override
    public void deleteEvento(Evento evento) throws DataException {
        try {
            dEvento.setInt(1, evento.getKey());
            int affectedRows = dEvento.executeUpdate();

            if (affectedRows == 0) {
                throw new DataException("Failed to delete evento");
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to delete evento", ex);
        }
    }

}
