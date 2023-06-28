package com.stdt.auleweb.framework.data;

import com.stdt.auleweb.data.model.Evento;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.util.GregorianCalendar;
import java.util.List;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;

public class iCal4j_Util {

    //Prende in input una lista di eventi e genera una stringa da restituire con file iCalendar
    public static String CalendarUtil(List<Evento> eventi) throws IOException {
        Calendar calendar = new Calendar();

        for (Evento e : eventi) {

            VEvent event = new VEvent(LocalDate.parse(e.getGiorno().toString()), e.getNome() + e.getDescrizione());

            calendar.add(event);
        }

        CalendarOutputter outputter = new CalendarOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(calendar, writer);
        String iCalendarContent = writer.toString();

        return iCalendarContent;
    }
}
