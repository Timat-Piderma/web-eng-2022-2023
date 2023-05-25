package com.stdt.auleweb.framework.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import com.stdt.auleweb.template.model.Aula;

public class AulaSerializer extends JsonSerializer<Aula> {

    @Override
    public void serialize(Aula item, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject(); // {
        jgen.writeStringField("nome", item.getNome()); // A1.2, C1.10
        jgen.writeNumberField("capienza", item.getCapienza()); // 25, 50
        jgen.writeStringField("emailResponsabile", item.getEmailResponsabile());
        jgen.writeNumberField("numeroPreseElettriche", item.getNumeroPreseElettriche());
        jgen.writeNumberField("numeroPreseRete", item.getNumeroPreseRete());
        jgen.writeStringField("note", item.getNote());
        jgen.writeObjectField("attrezzature", item.getAttrezzature());
        jgen.writeObjectField("gruppo", item.getGruppo());
        jgen.writeObjectField("posizione", item.getPosizione());
        jgen.writeObjectField("eventi", item.getEventi());
        jgen.writeEndObject(); // }
    }
}
