/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stdt.auleweb.data.model;

import com.stdt.auleweb.framework.data.DataItem;

/**
 *
 * @author andre
 */
public interface Amministratore extends DataItem<Integer>{
    String getUsername();

    void setUsername(String username);
    
    String getPassword();
    
    void setPassword(String password);
    
}
