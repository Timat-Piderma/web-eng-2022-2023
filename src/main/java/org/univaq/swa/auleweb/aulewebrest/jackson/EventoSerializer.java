/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package org.univaq.swa.auleweb.aulewebrest.jackson;

/**
*
* @author mttpe
*/
public class EventoSerializer extends JsonSerializer<Evento> {

   @Override
   public void serialize(Evento item, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

       jgen.writeStartObject(); // {
       jgen.writeStringField("data", item.getData());
       jgen.writeStringField("oraInizio", item.getOraInizio());
       jgen.writeStringField("oraFine", item.getOraFine());
       jgen.writeStringField("descrizione", item.getDescrizione());
       jgen.writeStringField("nome", item.getNome());
       jgen.writeStringField("note", item.getNote());
       jgen.writeNumberField("id", item.getId());
       jgen.writeObjectField("tipo", item.getTipo());
       jgen.writeObjectField("aule", item.getAule());
       jgen.writeObjectField("responsabile", item.getResponsabile());
       jgen.writeEndObject(); // }
   }
}
