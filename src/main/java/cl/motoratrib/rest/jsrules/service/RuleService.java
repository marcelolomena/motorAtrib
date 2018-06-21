package cl.motoratrib.rest.jsrules.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaOUT;
import cl.bancochile.plataformabase.error.PlataformaBaseException;


public interface RuleService {
    SpGetReglaOUT getRuleByName(String name) throws PlataformaBaseException;
}
