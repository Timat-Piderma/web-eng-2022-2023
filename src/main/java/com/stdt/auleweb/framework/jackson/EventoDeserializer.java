package com.stdt.auleweb.framework.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import com.stdt.auleweb.template.model.Aula;
import com.stdt.auleweb.template.model.Evento;
import com.stdt.auleweb.template.model.Responsabile;
import com.stdt.auleweb.template.model.Tipologia;

public class EventoDeserializer extends JsonDeserializer<Evento> {
    
    @Override
    public Evento deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Evento e = new Evento();
        JsonNode node = jp.getCodec().readTree(jp);
        
        if (node.has("data")) {
            e.setData(LocalDate.parse(node.get("data").asText()));
        }
        
        if (node.has("oraInizio")) {
            e.setOraInizio(LocalTime.parse(node.get("oraInizio").asText()));
        }
        
        if (node.has("oraFine")) {
            e.setOraFine(LocalTime.parse(node.get("oraInizio").asText()));
        }

        if (node.has("descrizione")) {
            e.setDescrizione(node.get("descrizione").asText());
        }

        if (node.has("nome")) {
            e.setNome(node.get("nome").asText());
        }

        if (node.has("id")) {
            e.setId(node.get("id").asInt());
        }

        if (node.has("aule")) {
            JsonNode ne = node.get("aule");
            List<Aula> aule = new ArrayList<>();
            e.setAule(aule);
            for (int i = 0; i < ne.size(); ++i) {
                aule.add(jp.getCodec().treeToValue(ne.get(i), Aula.class));
            } 
        }
        
        if (node.has("tipo")) {
            e.setTipologia(jp.getCodec().treeToValue(node.get("tipo"), Tipologia.class));
        }

        if (node.has("responsabile")) {
            e.setResponsabile(jp.getCodec().treeToValue(node.get("responsabile"), Responsabile.class));
        }

        return e;   
    }
}
