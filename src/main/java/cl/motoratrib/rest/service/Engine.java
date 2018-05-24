package cl.motoratrib.rest.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglasPcReglaRS;
import cl.motoratrib.rest.domain.RecordRule;


import java.util.List;

public interface Engine {
   String evaluatorRule(String json) throws Exception;
   List<RecordRule> getRule(int id) throws Exception;
   List<SpListReglasPcReglaRS> getRule2(int id) throws Exception;
}
