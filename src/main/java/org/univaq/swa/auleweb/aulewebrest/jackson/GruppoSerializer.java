package org.univaq.swa.auleweb.aulewebrest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.univaq.swa.auleweb.aulewebrest.model.Gruppo;

public class GruppoSerializer extends JsonSerializer<Gruppo> {

    @Override
    public void serialize(Gruppo item, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject(); // {
        jgen.writeNumberField("id", item.getId());
        jgen.writeStringField("nome", item.getNome());
        jgen.writeStringField("descrizione", item.getDescrizione());
        jgen.writeObjectField("aule", item.getAule());

        jgen.writeEndObject(); // }
    }
}
