package com.stdt.auleweb.data.proxy;

import com.stdt.auleweb.data.dao.AulaDAO;
import com.stdt.auleweb.data.impl.AttrezzaturaImpl;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.data.DataItemProxy;
import com.stdt.auleweb.framework.data.DataLayer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AttrezzaturaProxy extends AttrezzaturaImpl implements DataItemProxy {

    protected boolean modified;
    protected int posizione_key = 0;
    protected int gruppo_key = 0;

    protected DataLayer dataLayer;

    public AttrezzaturaProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public List<Aula> getAule() {
        if (super.getAule() == null) {
            try {
                super.setAule(((AulaDAO) dataLayer.getDAO(Aula.class)).getAuleByAttrezzatura(this));
            } catch (DataException ex) {
                Logger.getLogger(AttrezzaturaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getAule();
    }

    @Override
    public void addAula(Aula aula) {
        super.addAula(aula);
        this.modified = true;
    }

    //METODI DEL PROXY
    //PROXY-ONLY METHODS
    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

}
