package com.stdt.auleweb.controller;

import com.stdt.auleweb.data.dao.AuleWebDataLayer;
import com.stdt.auleweb.data.model.Evento;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.result.SplitSlashesFmkExt;
import com.stdt.auleweb.framework.result.TemplateManagerException;
import com.stdt.auleweb.framework.result.TemplateResult;
import com.stdt.auleweb.framework.security.SecurityHelpers;
import java.io.IOException;
import java.sql.Date;
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
public class MakeEventiGiorno extends AuleWebBaseController {

    private void action_evento(HttpServletRequest request, HttpServletResponse response, int IDgruppo, String data) throws IOException, ServletException, TemplateManagerException {
        try {
            Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo(IDgruppo);
            List<Evento> eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiByGiorno(gruppo, Date.valueOf(data));

            if (eventi != null) {
                request.setAttribute("eventi", eventi);
                request.setAttribute("giorno", data);
                //verr� usato automaticamente il template di outline spcificato tra i context parameters
                //the outlne template specified through the context parameters will be added by the TemplateResult to the specified template
                TemplateResult res = new TemplateResult(getServletContext());
                //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
                //add to the template a wrapper object that allows to call the stripslashes function
                request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                res.activate("eventi_giorno.ftl.html", request, response);
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
