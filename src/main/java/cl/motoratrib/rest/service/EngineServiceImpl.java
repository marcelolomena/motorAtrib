package cl.motoratrib.rest.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.*;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.*;
import cl.motoratrib.rest.domain.*;
import cl.motoratrib.rest.util.EngineHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import cl.bancochile.plataformabase.error.PlataformaBaseException;

@Service
public class EngineServiceImpl implements EngineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineServiceImpl.class);

    private static final String CODIGO_ERROR_GENERICO = "100000";
    private static final String GLOSA_ERROR_GENERICO = "Error al invocar motor de reglas ";

    @Autowired
    SpListReglasDAO spListReglasDAO;
    @Autowired
    SpListVariablesDAO spListVariablesDAO;
    @Autowired
    SpListReglaVariableDAO spListReglaVariableDAO;
    @Autowired
    SpGetReglaDAO spGetReglaDAO;

    @Override
    public List<RecordRule> getRule(int id) throws PlataformaBaseException {
        SpListReglasOUT spListReglasOUT;
        List<RecordRule> lstRecRule = new ArrayList<>();
        try {
            SpListReglasIN params  = new SpListReglasIN();
            params.setPIdPadre(id);
            spListReglasOUT = this.spListReglasDAO.execute(params);

            for (SpListReglasPcReglaRS rule : spListReglasOUT.getPcRegla()){
                RecordRule recRule = new RecordRule();
                recRule.setId(rule.getId().intValue());
                recRule.setIdParent(rule.getIdPadre().intValue());
                recRule.setName(rule.getNombre());
                String sClob = EngineHandler.getStringSromClob(rule.getJson());

                recRule.setJson(sClob.replaceAll("[\\s\u0000]+",""));
                lstRecRule.add(recRule);
            }

        } catch (Exception  ex) {
            LOGGER.error(ex.getMessage());
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, ex, CODIGO_ERROR_GENERICO);
        }
        return lstRecRule;
    }

    @Override
    public List<SpListVariablesPcVariableRS> getVariables() throws PlataformaBaseException {
        SpListVariablesOUT spListVariablesOUT;

        try {
            spListVariablesOUT = this.spListVariablesDAO.execute();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, ex, CODIGO_ERROR_GENERICO);
        }
        return spListVariablesOUT.getPcVariable();
    }

    @Override
    public List<SpListReglaVariablePcVarRS> getRuleVariable(String nombre) throws PlataformaBaseException {
        SpListReglaVariableOUT out;
        try{
            SpListReglaVariableIN param = new SpListReglaVariableIN();
            param.setPNombre(nombre);

            out = this.spListReglaVariableDAO.execute(param);

        } catch (Exception  ex) {
            LOGGER.error(ex.getMessage());
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, ex, CODIGO_ERROR_GENERICO);
        }
        return out.getPcVar();
    }

    @Override
    public SpGetReglaOUT getRuleByName(String name) throws PlataformaBaseException {
        SpGetReglaOUT ruleValue;
        try {
            SpGetReglaIN params = new SpGetReglaIN();
            params.setPNombre(name);
            ruleValue=this.spGetReglaDAO.execute(params);
            LOGGER.debug(ruleValue.toString());
        }catch(Exception  e){
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, e, CODIGO_ERROR_GENERICO);
        }
        return ruleValue;
    }


}
