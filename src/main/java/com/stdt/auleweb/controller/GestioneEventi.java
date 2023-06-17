package com.stdt.auleweb.controller;

import com.stdt.auleweb.data.dao.AuleWebDataLayer;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Responsabile;
import com.stdt.auleweb.data.model.Tipologia;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.result.SplitSlashesFmkExt;
import com.stdt.auleweb.framework.result.TemplateManagerException;
import com.stdt.auleweb.framework.result.TemplateResult;
import com.stdt.auleweb.framework.security.SecurityHelpers;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe
 */
public class GestioneEventi extends AuleWebBaseController {
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, int IDaula, String data) throws IOException, ServletException, TemplateManagerException {
        try {
            Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(IDaula);
            
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute(("aula"), aula);
            request.setAttribute("settiamanaprecedente", LocalDate.parse(data).plusDays(-7));
            request.setAttribute("settimanasuccessiva", LocalDate.parse(data).plusDays(7));
            request.setAttribute(("eventi"), ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiBySettimana(aula, Date.valueOf(data)));
            res.activate("gestione_eventi.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_write(HttpServletRequest request, HttpServletResponse response, int IDevento, int IDaula) throws IOException, ServletException, TemplateManagerException {
        try {
            
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            
            Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(IDaula);
            
            List<Responsabile> responsabili = ((AuleWebDataLayer) request.getAttribute("datalayer")).getResponsabileDAO().getResponsabili();
            
            List<Tipologia> tipologie = new ArrayList<>();
            tipologie.addAll(Arrays.asList(Tipologia.values()));
            
            request.setAttribute(("aula"), aula);
            request.setAttribute("responsabili", responsabili);
            request.setAttribute("tipologie", tipologie);
            if (IDevento > 0) {
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(IDevento);
                if (evento != null) {
                    request.setAttribute("evento", evento);
                    res.activate("modifica_evento.ftl.html", request, response);
                } else {
                    handleError("Undefined evento", request, response);
                }
            } else {
                //IDevento==0 indica un nuovo evento 
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();
                request.setAttribute("evento", evento);
                res.activate("modifica_evento.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_update(HttpServletRequest request, HttpServletResponse response, int IDevento, int IDaula) throws IOException, ServletException, TemplateManagerException {
        try {
            Evento evento;
            if (IDevento > 0) {
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(IDevento);
            } else {
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();
            }
            if (evento != null && request.getParameter("giorno") != null && request.getParameter("oraInizio") != null
                    && !request.getParameter("nome").isEmpty() && request.getParameter("oraFine") != null
                    && request.getParameter("descrizione") != null && request.getParameter("tipologia") != null
                    && request.getParameter("responsabile") != null) {
                Responsabile responsabile = ((AuleWebDataLayer) request.getAttribute("datalayer")).getResponsabileDAO().getResponsabile(SecurityHelpers.checkNumeric(request.getParameter("responsabile")));
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(IDaula);
                
                if (responsabile != null) {
                    
                    evento.setGiorno(Date.valueOf(SecurityHelpers.addSlashes(request.getParameter("giorno"))));
                    
                    /////////////////////////////////////////////////////////////////////////////////////////////// +00
                    evento.setOraInizio(Time.valueOf(SecurityHelpers.addSlashes(request.getParameter("oraInizio") + ":00")));                      
                    evento.setOraFine(Time.valueOf(SecurityHelpers.addSlashes(request.getParameter("oraFine") + ":00")));
                    
                    evento.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                    evento.setDescrizione(SecurityHelpers.addSlashes(request.getParameter("descrizione")));
                    evento.setTipologia(Tipologia.valueOf(SecurityHelpers.addSlashes(request.getParameter("tipologia"))));
                    evento.setResponsabile(responsabile);
                    evento.setAula(aula);
                    
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);
                    //delega il resto del processo all'azione write
                    //delegates the rest of the process to the write action
                    action_write(request, response, evento.getKey(), IDaula);
                } else {
                    handleError("Cannot update evento: undefined responsabile", request, response);
                }
            } else {
                handleError("Cannot update evento: insufficient parameters", request, response);
                
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        
        int IDevento;
        String data;
        int IDaula;
        try {
            IDaula = SecurityHelpers.checkNumeric(request.getParameter("IDaula"));
            
            if (request.getParameter("k") != null) {
                IDevento = SecurityHelpers.checkNumeric(request.getParameter("k"));
                if (request.getParameter("update") != null) {
                    action_update(request, response, IDevento, IDaula);
                } else {
                    action_write(request, response, IDevento, IDaula);
                }
            } else {
                data = SecurityHelpers.sanitizeFilename(request.getParameter("data"));
                action_default(request, response, IDaula, data);
            }
        } catch (NumberFormatException ex) {
            handleError("Invalid number submitted", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
    
}
