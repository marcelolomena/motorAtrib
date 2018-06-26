package cl.motoratrib.rest.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.*;
import cl.motoratrib.rest.domain.RecordRule;
import cl.bancochile.plataformabase.error.PlataformaBaseException;

import java.util.List;

public interface EngineService {
   List<RecordRule> getRule(int id) throws PlataformaBaseException;
   List<SpListVariablesPcVariableRS> getVariables() throws PlataformaBaseException;
   List<SpListReglaVariablePcVarRS> getRuleVariable(String nombre) throws PlataformaBaseException;
   SpGetReglaOUT getRuleByName(String name) throws PlataformaBaseException;
}
