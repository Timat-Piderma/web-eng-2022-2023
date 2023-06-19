package com.stdt.auleweb.controller;

import com.stdt.auleweb.data.dao.AuleWebDataLayer;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.data.model.Posizione;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.result.SplitSlashesFmkExt;
import com.stdt.auleweb.framework.result.TemplateManagerException;
import com.stdt.auleweb.framework.result.TemplateResult;
import com.stdt.auleweb.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestioneAule extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int IDgruppo) throws IOException, ServletException, TemplateManagerException {

        try {
            Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo(IDgruppo);
            TemplateResult res = new TemplateResult(getServletContext());

            request.setAttribute(("aule"), ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAuleByGruppo(gruppo));
            request.setAttribute(("gruppo"), gruppo);
            res.activate("gestione_aule.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_write(HttpServletRequest request, HttpServletResponse response, int IDaula) throws IOException, ServletException, TemplateManagerException {
        try {

            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());

            List<Posizione> posizioni = ((AuleWebDataLayer) request.getAttribute("datalayer")).getPosizioneDAO().getPosizioni();
            request.setAttribute("posizioni", posizioni);

            if (IDaula > 0) {
                //Se si tratta di una modifica, prende dal datalayer l'evento da modificare in base all'id
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(IDaula);
                if (aula != null) {
                    request.setAttribute("aula", aula);
                    res.activate("modifica_aula.ftl.html", request, response);
                } else {
                    handleError("Undefined aula", request, response);
                }
            } else {
                //IDaula==0 indica un nuovo evento 
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().createAula();
                request.setAttribute("aula", aula);
                res.activate("modifica_aula.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_update(HttpServletRequest request, HttpServletResponse response, int IDaula, int IDgruppo) throws IOException, ServletException, TemplateManagerException {
        try {

            Aula aula;
            if (IDaula > 0) {
                aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(IDaula);
            } else {
                aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().createAula();
            }

            Gruppo gruppo;
            gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo(IDgruppo);

            if (aula != null && !request.getParameter("nome").isEmpty() && !request.getParameter("capienza").isEmpty()
                    && !request.getParameter("emailresponsabile").isEmpty() && !request.getParameter("numeropreseelettriche").isEmpty()
                    && !request.getParameter("numeropreserete").isEmpty() && !request.getParameter("posizione").isEmpty()) {

                Posizione posizione;
                posizione = ((AuleWebDataLayer) request.getAttribute("datalayer")).getPosizioneDAO().getPosizione(SecurityHelpers.checkNumeric(request.getParameter("posizione")));

                aula.setNome(SecurityHelpers.sanitizeFilename(request.getParameter("nome")));
                aula.setCapienza(SecurityHelpers.checkNumeric(request.getParameter("capienza")));
                aula.setEmailResponsabile(SecurityHelpers.addSlashes(request.getParameter("emailresponsabile")));

                if (!request.getParameter("note").isEmpty()) {
                    aula.setNote(SecurityHelpers.sanitizeFilename(request.getParameter("note")));
                } else {
                    aula.setNote("");
                }

                aula.setNumeroPreseElettriche(SecurityHelpers.checkNumeric(request.getParameter("numeropreseelettriche")));
                aula.setNumeroPreseRete(SecurityHelpers.checkNumeric(request.getParameter("numeropreserete")));
                aula.setGruppo(gruppo);
                aula.setPosizione(posizione);

                ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().storeAula(aula);
                //delega il resto del processo all'azione write
                //delegates the rest of the process to the write action
                action_write(request, response, aula.getKey());

            } else {
                handleError("Cannot update aula: insufficient parameters", request, response);

            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response, int IDaula) throws IOException, ServletException, TemplateManagerException {
        try {
            Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(IDaula);

            ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().deleteAula(aula);

        } catch (DataException ex) {
            Logger.getLogger(GestioneEventi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int IDgruppo;
        int IDaula;

        IDgruppo = SecurityHelpers.checkNumeric(request.getParameter("IDgruppo"));
        request.setAttribute("IDgruppo", IDgruppo);
        try {

            if (request.getParameter("IDaula") != null) {

                IDaula = SecurityHelpers.checkNumeric(request.getParameter("IDaula"));

                if (request.getParameter("delete") != null) {
                    IDaula = SecurityHelpers.checkNumeric(request.getParameter("IDaula"));
                    action_delete(request, response, IDaula);
                }
                if (request.getParameter("update") != null) {

                    //aggiornamento
                    action_update(request, response, IDaula, IDgruppo);
                } else {
                    //inserimento
                    action_write(request, response, IDaula);
                }
            } else {
                //Altrimenti effettua l'azione di default
                action_default(request, response, IDgruppo);
            }

        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

}
