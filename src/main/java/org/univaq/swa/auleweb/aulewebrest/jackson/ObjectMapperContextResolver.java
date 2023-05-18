package org.univaq.swa.auleweb.aulewebrest.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import org.univaq.swa.auleweb.aulewebrest.model.Attrezzatura;
import org.univaq.swa.auleweb.aulewebrest.model.Aula;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper>{
       private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        this.mapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //abilitiamo una feature nuova...
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule customSerializer = new SimpleModule("CustomSerializersModule");

        //configuriamo i nostri serializzatori custom

        customSerializer.addSerializer(Aula.class, new AulaSerializer());
        customSerializer.addDeserializer(Aula.class, new AulaDeserializer());
        //
        customSerializer.addSerializer(Attrezzatura.class, new AttrezzaturaSerializer());
        customSerializer.addDeserializer(Attrezzatura.class, new AttrezzaturaDeserializer());
        
        mapper.registerModule(customSerializer);

        return mapper;
    }
}
