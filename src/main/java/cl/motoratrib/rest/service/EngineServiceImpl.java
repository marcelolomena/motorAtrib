package cl.motoratrib.rest.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.*;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.*;

import cl.bancochile.plataformabase.error.PlataformaBaseException;

@Service
public class EngineServiceImpl implements EngineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineServiceImpl.class);

    private static final String CODIGO_ERROR_GENERICO = "100000";
    private static final String GLOSA_ERROR_GENERICO = "Error al invocar motor de reglas ";

    @Autowired
    SpListVariablesDAO spListVariablesDAO;
    @Autowired
    SpListReglaVariableDAO spListReglaVariableDAO;
    @Autowired
    SpGetReglaDAO spGetReglaDAO;
    @Autowired
    SpGetValorAtribDAO spGetValorAtribDAO;

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
            LOGGER.error(e.getMessage());
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, e, CODIGO_ERROR_GENERICO);
        }
        return ruleValue;
    }

    @Override
    public SpGetValorAtribOUT getValorAtrib(String ruleName, String json) throws PlataformaBaseException {
        SpGetValorAtribOUT valorAtrib;
        try {
            byte[] content  = json.getBytes();
            int length =(content != null ? content.length : 0);
            SpGetValorAtribIN params = new SpGetValorAtribIN();
            params.setPJson(new SqlLobValue(
                    new ByteArrayInputStream(content),
                    length, new DefaultLobHandler()));
            params.setPNombreRegla(ruleName);
            valorAtrib=this.spGetValorAtribDAO.execute(params);

        }catch(Exception  e){
            LOGGER.error(e.getMessage());
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, e, CODIGO_ERROR_GENERICO);
        }
        return valorAtrib;
    }


}
