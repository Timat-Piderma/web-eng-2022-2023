package com.stdt.auleweb.controller;

import com.stdt.auleweb.data.dao.AuleWebDataLayer;
import com.stdt.auleweb.data.model.Aula;
import com.stdt.auleweb.data.model.Corso;
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
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            List<Corso> corsi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorsi();

            List<Tipologia> tipologie = new ArrayList<>();
            tipologie.addAll(Arrays.asList(Tipologia.values()));

            request.setAttribute("aula", aula);
            request.setAttribute("responsabili", responsabili);
            request.setAttribute("corsi", corsi);
            request.setAttribute("tipologie", tipologie);
            if (IDevento > 0) {
                //Se si tratta di una modifica, prende dal datalayer l'evento da modificare in base all'id
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(IDevento);
                if (evento != null) {
                    request.setAttribute("evento", evento);

                    //Questo attributo serve solo per passare la data nel formato corretto all'html
                    request.setAttribute("giornoevento", LocalDate.parse(evento.getGiorno().toString()));

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

            if (request.getParameter("giorno") != null && request.getParameter("oraInizio") != null
                    && !request.getParameter("nome").isEmpty() && request.getParameter("oraFine") != null
                    && !request.getParameter("descrizione").isEmpty() && request.getParameter("tipologia") != null
                    && (request.getParameter("responsabile") != null || request.getParameter("nomeNuovoResponsabile") != null)) {

                Responsabile responsabile;

                //Se l'utente ha aggiunto i dati di un nuovo Responsabile, lo crea e lo aggiunge alla tabella dei responsabili
                if (!request.getParameter("nomeNuovoResponsabile").isEmpty() && !request.getParameter("emailNuovoResponsabile").isEmpty()) {

                    responsabile = ((AuleWebDataLayer) request.getAttribute("datalayer")).getResponsabileDAO().createResponsabile();
                    responsabile.setNome(SecurityHelpers.addSlashes(request.getParameter("nomeNuovoResponsabile")));
                    responsabile.setEmail(SecurityHelpers.addSlashes(request.getParameter("emailNuovoResponsabile")));
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getResponsabileDAO().storeResponsabile(responsabile);

                } //Altrimenti prende quello che è stato scelto dalla select
                else {
                    responsabile = ((AuleWebDataLayer) request.getAttribute("datalayer")).getResponsabileDAO().getResponsabile(SecurityHelpers.checkNumeric(request.getParameter("responsabile")));

                }

                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(IDaula);

                Corso corso;

                if (SecurityHelpers.addSlashes(request.getParameter("tipologia")).equals("lezione")
                        || SecurityHelpers.addSlashes(request.getParameter("tipologia")).equals("esame")
                        || SecurityHelpers.addSlashes(request.getParameter("tipologia")).equals("parziale")) {

                    if (!request.getParameter("nomeNuovoCorso").isEmpty()) {
                        corso = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().createCorso();
                        corso.setNome(SecurityHelpers.addSlashes(request.getParameter("nomeNuovoCorso")));
                        ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().storeCorso(corso);

                    } else {
                        corso = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorso(SecurityHelpers.checkNumeric(request.getParameter("corso")));
                    }
                } else {
                    corso = null;
                }

                if (request.getParameter("ricorrente") != null) {
                    //L'evento è ricorrente
                    Date giornoInizio = Date.valueOf(SecurityHelpers.addSlashes(request.getParameter("giorno")));
                    if (IDevento > 0) {
                        //Modificare più eventi ricorrenti
                        Evento e = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(IDevento);

                        List<Evento> eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiRicorrenti(e.getNome(), e.getResponsabile().getKey());

                        Calendar cal = Calendar.getInstance();

                        cal.setTime(giornoInizio);

                        long da1 = e.getGiorno().getTime();
                        long da2 = cal.getTimeInMillis();

                        long timeDiff = da2 - da1;

                        int daysDiff = (int) TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

                        for (Evento evento : eventi) {

                            cal.setTime(evento.getGiorno());

                            cal.add(Calendar.DATE, daysDiff);
                            giornoInizio.setTime(cal.getTimeInMillis());

                            evento.setGiorno(giornoInizio);
                            /////////////////////////////////////////////////////////////////////////////////////////////// +00
                            evento.setOraInizio(Time.valueOf(SecurityHelpers.addSlashes(request.getParameter("oraInizio").substring(0, 5)) + ":00"));
                            evento.setOraFine(Time.valueOf(SecurityHelpers.addSlashes(request.getParameter("oraFine").substring(0, 5)) + ":00"));
                            evento.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                            evento.setDescrizione(SecurityHelpers.addSlashes(request.getParameter("descrizione")));
                            evento.setTipologia(Tipologia.valueOf(SecurityHelpers.addSlashes(request.getParameter("tipologia"))));
                            evento.setResponsabile(responsabile);
                            evento.setAula(aula);

                            if (corso != null) {
                                evento.setCorso(corso);
                            } else {
                                evento.setCorso(null);
                            }

                            ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);

                        }

                    } else {
                        //Inserire più eventi ricorrenti

                        if (!request.getParameter("finericorrenza").isEmpty()) {
                            Date giornoFine = Date.valueOf(SecurityHelpers.addSlashes(request.getParameter("finericorrenza")));

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(giornoInizio);
                            while (giornoInizio.compareTo(giornoFine) < 0) {
                                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();

                                evento.setGiorno(giornoInizio);
                                /////////////////////////////////////////////////////////////////////////////////////////////// +00
                                evento.setOraInizio(Time.valueOf(SecurityHelpers.addSlashes(request.getParameter("oraInizio").substring(0, 5)) + ":00"));
                                evento.setOraFine(Time.valueOf(SecurityHelpers.addSlashes(request.getParameter("oraFine").substring(0, 5)) + ":00"));
                                evento.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                                evento.setDescrizione(SecurityHelpers.addSlashes(request.getParameter("descrizione")));
                                evento.setTipologia(Tipologia.valueOf(SecurityHelpers.addSlashes(request.getParameter("tipologia"))));
                                evento.setResponsabile(responsabile);
                                evento.setAula(aula);

                                if (corso != null) {
                                    evento.setCorso(corso);
                                } else {
                                    evento.setCorso(null);
                                }

                                ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);

                                cal.add(Calendar.DATE, 7);

                                giornoInizio.setTime(cal.getTimeInMillis());
                            }
                        }
                    }
                    TemplateResult res = new TemplateResult(getServletContext());
                    request.setAttribute("aula", aula);
                    request.setAttribute("settiamanaprecedente", LocalDate.parse(LocalDate.now().toString()).plusDays(-7));
                    request.setAttribute("settimanasuccessiva", LocalDate.parse(LocalDate.now().toString()).plusDays(7));
                    request.setAttribute(("eventi"), ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiBySettimana(aula, Date.valueOf(LocalDate.now().toString())));

                    res.activate("gestione_eventi.ftl.html", request, response);
                } else {
                    //É un'evento singolo

                    Evento evento;
                    if (IDevento > 0) {
                        evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(IDevento);
                    } else {
                        evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();
                    }

                    evento.setGiorno(Date.valueOf(SecurityHelpers.addSlashes(request.getParameter("giorno"))));
                    /////////////////////////////////////////////////////////////////////////////////////////////// +00
                    evento.setOraInizio(Time.valueOf(SecurityHelpers.addSlashes(request.getParameter("oraInizio").substring(0, 5)) + ":00"));
                    evento.setOraFine(Time.valueOf(SecurityHelpers.addSlashes(request.getParameter("oraFine").substring(0, 5)) + ":00"));
                    evento.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                    evento.setDescrizione(SecurityHelpers.addSlashes(request.getParameter("descrizione")));
                    evento.setTipologia(Tipologia.valueOf(SecurityHelpers.addSlashes(request.getParameter("tipologia"))));
                    evento.setResponsabile(responsabile);
                    evento.setAula(aula);

                    if (corso != null) {
                        evento.setCorso(corso);
                    } else {
                        evento.setCorso(null);
                        evento.removeCorso();
                    }
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);
                    //delega il resto del processo all'azione write
                    //delegates the rest of the process to the write action
                    action_write(request, response, evento.getKey(), aula.getKey());
                }
            } else {
                handleError("Cannot update evento: insufficient parameters", request, response);

            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response, int IDevento) throws IOException, ServletException, TemplateManagerException {
        try {
            Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(IDevento);

            ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().deleteEvento(evento);

        } catch (DataException ex) {
            Logger.getLogger(GestioneEventi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        int IDaula;
        int IDevento;
        String data;

        try {

            if (request.getParameter("data") == null) {
                data = LocalDate.now().toString();
            } else {
                data = SecurityHelpers.sanitizeFilename(request.getParameter("data"));
            }

            IDaula = SecurityHelpers.checkNumeric(request.getParameter("IDaula"));
            request.setAttribute("IDaula", IDaula);

            request.setAttribute("data", data);

            //Se gli viene passato l'id di un evento da cancellare chiama action_delete
            if (request.getParameter("delete") != null) {
                IDevento = SecurityHelpers.checkNumeric(request.getParameter("IDevento"));
                action_delete(request, response, IDevento);
                action_default(request, response, IDaula, data);
            } else {

                //Se gli viene passato l'id di un evento, controlla se si tratta di un aggiornamento o di un inserimento
                if (request.getParameter("IDevento") != null) {

                    IDevento = SecurityHelpers.checkNumeric(request.getParameter("IDevento"));

                    if (request.getParameter("update") != null) {

                        //aggiornamento
                        action_update(request, response, IDevento, IDaula);
                    } else {
                        //inserimento
                        action_write(request, response, IDevento, IDaula);
                    }
                } else {
                    //Altrimenti effettua l'azione di default
                    action_default(request, response, IDaula, data);

                }
            }

        } catch (NumberFormatException ex) {
            handleError("Invalid number submitted", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

}
