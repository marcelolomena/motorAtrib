package cl.motoratrib.rest.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglaVariablePcVarRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpUpdateReglaOUT;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpUpdateConjuntoReglaOUT;
import cl.motoratrib.rest.domain.GridRule;
import cl.motoratrib.rest.domain.RecordRule;
import cl.bancochile.plataformabase.error.PlataformaBaseException;

import java.util.List;

public interface EngineService {
   String evaluatorRule(String json) throws PlataformaBaseException;
   List<RecordRule> getRule(int id) throws PlataformaBaseException;
   List<SpListVariablesPcVariableRS> getVariables() throws PlataformaBaseException;
   SpUpdateReglaOUT updateRule(GridRule grule) throws PlataformaBaseException;
   SpUpdateConjuntoReglaOUT updateRuleSet(GridRule grule) throws PlataformaBaseException;
   List<SpListReglaVariablePcVarRS> getRuleVariable(String nombre) throws PlataformaBaseException;
}
