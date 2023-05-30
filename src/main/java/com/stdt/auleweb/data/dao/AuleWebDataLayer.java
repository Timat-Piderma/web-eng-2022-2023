package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.model.Amministratore;
import com.stdt.auleweb.data.model.Attrezzatura;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataLayer;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Corso;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.data.model.Responsabile;
import java.sql.SQLException;
import javax.sql.DataSource;

public class AuleWebDataLayer extends DataLayer {

    public AuleWebDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }

    @Override
    public void init() throws DataException {
        //registriamo i nostri dao
        //register our daos
        //registerDAO(Article.class, new ArticleDAO_MySQL(this));
        registerDAO(Amministratore.class, new AmministratoreDAO_MySQL(this));
        registerDAO(Attrezzatura.class, new AttrezzaturaDAO_MySQL(this));
        registerDAO(Aula.class, new AulaDAO_MySQL(this));
        registerDAO(Corso.class, new CorsoDAO_MySQL(this));
        registerDAO(Evento.class, new EventoDAO_MySQL(this));
        registerDAO(Gruppo.class, new GruppoDAO_MySQL(this));
        registerDAO(Posizione.class, new PosizioneDAO_MySQL(this));
        registerDAO(Responsabile.class, new ResponsabileDAO_MySQL(this));
    }

    //helpers    
    public AulaDAO getAulaDAO() {
        return (AulaDAO) getDAO(Aula.class);
    }

    public AttrezzaturaDAO getAttrezzaturaDAO() {
        return (AttrezzaturaDAO) getDAO(Attrezzatura.class);
    }

    public CorsoDAO getCorsoDAO() {
        return (CorsoDAO) getDAO(Corso.class);
    }

    public EventoDAO getEventoDAO() {
        return (EventoDAO) getDAO(Evento.class);
    }

    public GruppoDAO getGruppoDAO() {
        return (GruppoDAO) getDAO(Gruppo.class);
    }

    public PosizioneDAO getPosizioneDAO() {
        return (PosizioneDAO) getDAO(Posizione.class);
    }

    public ResponsabileDAO getResponsabileDAO() {
        return (ResponsabileDAO) getDAO(Responsabile.class);
    }

    public AmministratoreDAO getAmministratoreDAO() {
        return (AmministratoreDAO) getDAO(Amministratore.class);
    }

}
