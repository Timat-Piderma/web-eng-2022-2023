package com.stdt.auleweb.controller;

import com.stdt.auleweb.data.dao.AuleWebDataLayer;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.result.SplitSlashesFmkExt;
import com.stdt.auleweb.framework.result.TemplateManagerException;
import com.stdt.auleweb.framework.result.TemplateResult;
import com.stdt.auleweb.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestioneGruppi extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Gruppi");
            request.setAttribute(("gruppi"), ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppi());
            res.activate("gestione_gruppi.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_write(HttpServletRequest request, HttpServletResponse response, int IDgruppo) throws IOException, ServletException, TemplateManagerException {
        try {

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());

            if (IDgruppo > 0) {
                //Se si tratta di una modifica, prende dal datalayer l'evento da modificare in base all'id
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo(IDgruppo);
                if (gruppo != null) {
                    request.setAttribute("gruppo", gruppo);
                    res.activate("modifica_gruppo.ftl.html", request, response);
                } else {
                    handleError("Undefined gruppo", request, response);
                }
            } else {
                //IDevento==0 indica un nuovo evento 
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().createGruppo();
                request.setAttribute("gruppo", gruppo);
                res.activate("modifica_gruppo.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_update(HttpServletRequest request, HttpServletResponse response, int IDgruppo) throws IOException, ServletException, TemplateManagerException {
        try {

            Gruppo gruppo;
            if (IDgruppo > 0) {
                gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo(IDgruppo);
            } else {
                gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().createGruppo();
            }

            if (gruppo != null && !request.getParameter("nome").isEmpty() && !request.getParameter("descrizione").isEmpty()) {

                gruppo.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                gruppo.setDescrizione(SecurityHelpers.addSlashes(request.getParameter("descrizione")));

                ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().storeGruppo(gruppo);
                //delega il resto del processo all'azione write
                //delegates the rest of the process to the write action
                action_write(request, response, gruppo.getKey());

            } else {
                handleError("Cannot update evento: insufficient parameters", request, response);

            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response, int IDgruppo) throws IOException, ServletException, TemplateManagerException {
        try {
            Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo(IDgruppo);

            ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().deleteGruppo(gruppo);

        } catch (DataException ex) {
            Logger.getLogger(GestioneEventi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int IDgruppo;
        try {

            if (request.getParameter("IDgruppo") != null) {

                IDgruppo = SecurityHelpers.checkNumeric(request.getParameter("IDgruppo"));

                if (request.getParameter("delete") != null) {
                    IDgruppo = SecurityHelpers.checkNumeric(request.getParameter("IDgruppo"));
                    action_delete(request, response, IDgruppo);
                }
                if (request.getParameter("update") != null) {

                    //aggiornamento
                    action_update(request, response, IDgruppo);
                } else {
                    //inserimento
                    action_write(request, response, IDgruppo);
                }
            } else {
                //Altrimenti effettua l'azione di default
                action_default(request, response);
            }

        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

}
