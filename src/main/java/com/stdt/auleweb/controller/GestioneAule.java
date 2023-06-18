package com.stdt.auleweb.controller;

import com.stdt.auleweb.data.dao.AuleWebDataLayer;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.result.TemplateManagerException;
import com.stdt.auleweb.framework.result.TemplateResult;
import com.stdt.auleweb.framework.security.SecurityHelpers;
import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe
 */
public class GestioneAule extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int IDgruppo) throws IOException, ServletException, TemplateManagerException {

        try {
            Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo(IDgruppo);
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Gruppi");

            request.setAttribute(("data"), LocalDate.now());
            request.setAttribute(("aule"), ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAuleByGruppo(gruppo));
            request.setAttribute(("gruppo"), gruppo);
            res.activate("gestione_aule.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int IDgruppo;
        try {
            IDgruppo = SecurityHelpers.checkNumeric(request.getParameter("IDgruppo"));
            action_default(request, response, IDgruppo);

        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

}
