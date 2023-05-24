package com.stdt.auleweb.rest.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import com.stdt.auleweb.rest.model.Attrezzatura;
import com.stdt.auleweb.rest.model.Aula;
import com.stdt.auleweb.rest.model.Corso;
import com.stdt.auleweb.rest.model.Gruppo;
import com.stdt.auleweb.rest.model.Posizione;
import com.stdt.auleweb.rest.model.Responsabile;

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
        //
        customSerializer.addSerializer(Corso.class, new CorsoSerializer());
        customSerializer.addDeserializer(Corso.class, new CorsoDeserializer());
        //
        customSerializer.addSerializer(Gruppo.class, new GruppoSerializer());
        customSerializer.addDeserializer(Gruppo.class, new GruppoDeserializer());
        //
        customSerializer.addSerializer(Posizione.class, new PosizioneSerializer());
        customSerializer.addDeserializer(Posizione.class, new PosizioneDeserializer());
        //
        customSerializer.addSerializer(Responsabile.class, new ResponsabileSerializer());
        customSerializer.addDeserializer(Responsabile.class, new ResponsabileDeserializer());
        
        mapper.registerModule(customSerializer);

        return mapper;
    }
}
