package com.stdt.auleweb.controller;

import com.stdt.auleweb.data.dao.AuleWebDataLayer;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Gruppo;
import com.stdt.auleweb.framework.data.DataException;
import com.stdt.auleweb.framework.result.SplitSlashesFmkExt;
import com.stdt.auleweb.framework.result.TemplateManagerException;
import com.stdt.auleweb.framework.result.TemplateResult;
import com.stdt.auleweb.framework.security.SecurityHelpers;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Giuseppe
 */
public class MakeAula extends AuleWebBaseController {

    private void action_aula(HttpServletRequest request, HttpServletResponse response, int id) throws IOException, ServletException, TemplateManagerException {
        try {
            Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(id);
            if (aula != null) {
                request.setAttribute("aula", aula);
                request.setAttribute("page_title", aula.getGruppo().getNome());
                //verr� usato automaticamente il template di outline spcificato tra i context parameters
                //the outlne template specified through the context parameters will be added by the TemplateResult to the specified template
                TemplateResult res = new TemplateResult(getServletContext());
                //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
                //add to the template a wrapper object that allows to call the stripslashes function
                request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                res.activate("aula.ftl.html", request, response);
            } else {
                handleError("Unable to load aula", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        int id;
        try {
            id = SecurityHelpers.checkNumeric(request.getParameter("id"));
            action_aula(request, response, id);
        } catch (NumberFormatException ex) {
            handleError("Invalid number specified", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Make Gruppo servlet";
    }// </editor-fold>
}
