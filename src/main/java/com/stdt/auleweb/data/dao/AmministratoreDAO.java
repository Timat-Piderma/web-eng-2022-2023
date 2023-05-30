/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stdt.auleweb.data.dao;
import com.stdt.auleweb.data.model.Amministratore;
import com.stdt.auleweb.framework.data.DataException;

/**
 *
 * @author andre
 */
public interface AmministratoreDAO {
    Amministratore createAmministratore() throws DataException;
    
    Amministratore getAmministratore(int amministratore_key) throws DataException;
    
    void storeAmministratore(Amministratore amministratore) throws DataException;
}
