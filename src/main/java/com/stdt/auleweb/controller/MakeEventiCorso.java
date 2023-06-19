package com.stdt.auleweb.controller;

import com.stdt.auleweb.data.dao.AuleWebDataLayer;
import com.stdt.auleweb.data.model.Corso;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.result.SplitSlashesFmkExt;
import com.stdt.auleweb.framework.result.TemplateManagerException;
import com.stdt.auleweb.framework.result.TemplateResult;
import com.stdt.auleweb.framework.security.SecurityHelpers;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MakeEventiCorso extends AuleWebBaseController {

    private void action_evento(HttpServletRequest request, HttpServletResponse response, int IDgruppo, String data) throws IOException, ServletException, TemplateManagerException {
        try {
            Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo(IDgruppo);
            List<Corso> corsi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorsi();
            request.setAttribute("corsi", corsi);

            List<Evento> eventi = null;
            Corso corso;

            if (request.getParameter("corsoSelect") != null) {
                corso = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorso(SecurityHelpers.checkNumeric(request.getParameter("corsoSelect")));
                eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiBySettimanaAndCorso(corso, data, gruppo);
                request.setAttribute("corso", corso);
            }

            if (request.getParameter("IDcorso") != null) {
                corso = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorso(SecurityHelpers.checkNumeric(request.getParameter("IDcorso")));
                eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiBySettimanaAndCorso(corso, data, gruppo);
                request.setAttribute("corso", corso);
            }

            request.setAttribute("gruppo", gruppo);
            request.setAttribute("data", data);

            if (eventi != null) {
                request.setAttribute("eventi", eventi);

                request.setAttribute("settiamanaprecedente", LocalDate.parse(data).plusDays(-7));
                request.setAttribute("settimanasuccessiva", LocalDate.parse(data).plusDays(7));

                request.setAttribute("strip_slashes", new SplitSlashesFmkExt());

            }
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("eventi_corso.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int IDgruppo;
        String data;
        try {
            IDgruppo = SecurityHelpers.checkNumeric(request.getParameter("IDgruppo"));
            data = SecurityHelpers.sanitizeFilename(request.getParameter("data"));

            action_evento(request, response, IDgruppo, data);
        } catch (NumberFormatException ex) {
            handleError("Invalid number specified", request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        } catch (IOException ex) {
            Logger.getLogger(MakeEventiGiorno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
