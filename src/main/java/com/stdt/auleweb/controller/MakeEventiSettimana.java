package com.stdt.auleweb.controller;

import com.stdt.auleweb.data.dao.AuleWebDataLayer;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.result.SplitSlashesFmkExt;
import com.stdt.auleweb.framework.result.TemplateManagerException;
import com.stdt.auleweb.framework.result.TemplateResult;
import com.stdt.auleweb.framework.security.SecurityHelpers;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe
 */
public class MakeEventiSettimana extends AuleWebBaseController {

    private void action_evento(HttpServletRequest request, HttpServletResponse response, int IDaula, String data) throws IOException, ServletException, TemplateManagerException {
        try {
            Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(IDaula);
            List<Evento> eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiBySettimana(aula, Date.valueOf(data));

            if (eventi != null) {
                request.setAttribute("eventi", eventi);
                request.setAttribute("aula", aula);
                request.setAttribute("giorno", data);
                request.setAttribute("settiamanaprecedente", LocalDate.parse(data).plusDays(-7));
                request.setAttribute("settimanasuccessiva", LocalDate.parse(data).plusDays(7));
                //verr� usato automaticamente il template di outline spcificato tra i context parameters
                //the outlne template specified through the context parameters will be added by the TemplateResult to the specified template
                TemplateResult res = new TemplateResult(getServletContext());
                //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
                //add to the template a wrapper object that allows to call the stripslashes function
                request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                res.activate("eventi_settimana.ftl.html", request, response);
            } else {
                handleError("Unable to load eventi", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int IDaula;
        String data;
        try {
            IDaula = SecurityHelpers.checkNumeric(request.getParameter("IDaula"));
            data = SecurityHelpers.sanitizeFilename(request.getParameter("data"));

            action_evento(request, response, IDaula, data);
        } catch (NumberFormatException ex) {
            handleError("Invalid number specified", request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        } catch (IOException ex) {
            Logger.getLogger(MakeEventiGiorno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
