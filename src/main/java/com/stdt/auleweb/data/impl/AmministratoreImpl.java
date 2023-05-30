/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stdt.auleweb.data.impl;

import com.stdt.auleweb.data.model.Amministratore;
import com.stdt.auleweb.framework.data.DataItemImpl;

/**
 *
 * @author andre
 */
public class AmministratoreImpl extends DataItemImpl<Integer> implements Amministratore{
    String nome;
    String password;
    
    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome=nome;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword() {
        this.password=password;
    }
}
