package cl.motoratrib.rest.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglasPcReglaRS;


import java.util.List;

public interface Engine {
   String evaluatorRule(String json) throws Exception;
   List<SpListReglasPcReglaRS> getRule(int id) throws Exception;
}
