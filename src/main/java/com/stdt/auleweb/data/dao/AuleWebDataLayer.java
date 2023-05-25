package com.stdt.auleweb.data.dao;

import com.stdt.auleweb.data.DataException;
import com.stdt.auleweb.data.DataLayer;
import com.stdt.auleweb.data.model.Aula;
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
        registerDAO(Aula.class, new AulaDAO_MySQL(this));
        registerDAO(Aula.class, new AulaDAO_MySQL(this));
        registerDAO(Aula.class, new AulaDAO_MySQL(this));
        registerDAO(Aula.class, new AulaDAO_MySQL(this));

    }

    //helpers    
    public AulaDAO getAulaDAO() {
        return (AulaDAO) getDAO(Aula.class);
    }


}
