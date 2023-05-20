/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package org.univaq.swa.auleweb.aulewebrest.jackson;

/**
*
* @author mttpe
*/
public class EventoDeserializer {

   @Override
   public Evento deserialize (JsonParser jp, DeserializationContext ctxt)throws IOException, JsonProcessingException{
       Evento e = new Evento();

       JsonNode node = jp.getCodec().readTree(jp);

       if (node.has("data")) {
           e.setData(node.get("data").asText());
       }

       if (node.has("oraInizio")) {
           e.setOraInizio(node.get("oraInizio").asText());
       }
       if (node.has("oraFine")) {
           e.setOraFine(node.get("oraFine").asText());
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

       if (node.has("tipo")){
           e.setTipologia(jp.getCodec().treeToValue(node.get("tipo"), Tipologia.class));
       }
       if (node.has("responsabile")){
           e.setResponsabile(jp.getCodec().treeToValue(node.get("responsabile"),Responsabile.class));
       }

       return e;
   }



}