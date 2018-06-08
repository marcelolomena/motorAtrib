package cl.motoratrib.rest.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglaVariablePcVarRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpUpdateReglaOUT;
import cl.motoratrib.rest.domain.GridRule;
import cl.motoratrib.rest.domain.RecordRule;


import java.util.List;

public interface Engine {
   String evaluatorRule(String json) throws Exception;
   List<RecordRule> getRule(int id) throws Exception;
   List<SpListVariablesPcVariableRS> getVariables() throws Exception;
   SpUpdateReglaOUT updateRule(GridRule grule) throws Exception;
   List<SpListReglaVariablePcVarRS> getRuleVariable(String nombre) throws Exception;
}
