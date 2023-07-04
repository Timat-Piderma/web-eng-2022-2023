package com.stdt.auleweb.framework.data;

import com.stdt.auleweb.data.model.Evento;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

public class iCal4j_Util {

    //Prende in input una lista di eventi e genera una stringa da restituire con file iCalendar
    public static String CalendarUtil(List<Evento> eventi) throws IOException {
        Calendar calendar = new Calendar();

        for (Evento e : eventi) {

            LocalDateTime dtstart = LocalDateTime.of(LocalDate.parse(e.getGiorno().toString()), LocalTime.parse(e.getOraInizio().toString()));
            LocalDateTime dtend = LocalDateTime.of(LocalDate.parse(e.getGiorno().toString()), LocalTime.parse(e.getOraFine().toString()));

            VEvent event = new VEvent(dtstart, dtend, e.getNome() + e.getDescrizione());

            calendar.add(event);
        }

        CalendarOutputter outputter = new CalendarOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(calendar, writer);
        String iCalendarContent = writer.toString();

        return iCalendarContent;
    }
}
