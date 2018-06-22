package cl.motoratrib.rest.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.*;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.*;
import cl.motoratrib.rest.domain.*;
import cl.motoratrib.rest.jsrules.JsRules;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Clob;
import java.util.*;

import cl.bancochile.plataformabase.error.PlataformaBaseException;

@Service
public class EngineServiceImpl implements EngineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineServiceImpl.class);

    private static final String CODIGO_ERROR_GENERICO = "100000";
    private static final String GLOSA_ERROR_GENERICO = "Error al invocar motor de reglas ";

    @Autowired
    JsRules jsrules;
    @Autowired
    SpListReglasDAO spListReglasDAO;
    @Autowired
    SpListVariablesDAO spListVariablesDAO;
    @Autowired
    SpUpdateReglaDAO spUpdateReglaDAO;
    @Autowired
    SpListReglaVariableDAO spListReglaVariableDAO;
    @Autowired
    SpUpdateConjuntoReglaDAO spUpdateConjuntoReglaDAO;

    @Override
    public String evaluatorRule(String json) throws PlataformaBaseException {

        String responseRule;

        try{
            InJson in = readJsonFullFromString(json);
            List<Parameter> listParam = in.getParameterList();

            Map<String, Object> tmplMap = buildTemplateParameter(in.getRulesetName());
            Map<String, Object> parameters  = buidParametersValues(listParam);

            if( !checkVariables(tmplMap, buidParametersTypes(listParam) ) ) {
                responseRule = "{\"ref\":\"SF00\", \"alerta\":\"Las variables no corresponden al flujo que se esta invocando\"}";
                LOGGER.debug(responseRule);
            } else {

                List<Parameter> lParam = containsParameters(listParam, "p5_fechaPep", "p5_fechaVencMac");
                Parameter p5fechaPep = containsParameter(lParam, "p5_fechaPep");
                Parameter p5fechaVencMac = containsParameter(lParam, "p5_fechaVencMac");

                if (p5fechaPep != null && p5fechaVencMac != null) {
                    parameters = createAditionalParameter(parameters, p5fechaPep, p5fechaVencMac, "p5_diffMacPep");
                    parameters.remove(p5fechaPep);
                    parameters.remove(p5fechaVencMac);
                }

                Object o = jsrules.executeRuleset(in.getRulesetName(), parameters);

                responseRule =  createResponse(o);

            }

        } catch (Exception ex) {
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, ex, CODIGO_ERROR_GENERICO);
        }

        return responseRule;
    }

    private String createResponse(Object o){
        ClaseGenerica response = null;
        String responseRule;

        if (o != null)
            response = new ClaseGenerica(o);

        if ("java.lang.String".equals(response.classType()))
            responseRule = response.getObj().toString();
        else
            responseRule = "{\"error\": 1}";

        return responseRule;
    }

    private boolean checkVariables(Map<String, Object> tmplMap, Map<String, Object> reqMap){
        return tmplMap.equals(reqMap);
    }

    private Map<String, Object> buildTemplateParameter(String nombre) throws PlataformaBaseException{
        Map<String, Object> tmplMap = new HashMap<>();
        List<SpListReglaVariablePcVarRS> vars = getRuleVariable(nombre);
        for (SpListReglaVariablePcVarRS o : vars) {
            tmplMap.put( o.getParametername(), o.getParameterclass());
        }
        return tmplMap;
    }

    private Map<String, Object> createAditionalParameter(Map<String, Object> parameters, Parameter pOne,Parameter pTwo, String name){
        DateTime end = DateTime.parse(pTwo.getParameterValue());
        DateTime start = DateTime.parse(pOne.getParameterValue());
        int days = Days.daysBetween(start, end).getDays();
        parameters.put(name, Long.valueOf(days));
        return parameters;
    }

    private Map<String, Object> buidParametersValues(List<Parameter> listParam){

        Map<String, Object> parameters = new HashMap<>();

        for (Parameter p : listParam) {
            if ("Long".equals(p.getParameterClass())) {
                parameters.put(p.getParameterName(), Long.valueOf(p.getParameterValue()));
            } else if ("String".equals(p.getParameterClass())) {
                parameters.put(p.getParameterName(), p.getParameterValue());
            } else if ("DateTime".equals(p.getParameterClass())) {
                parameters.put(p.getParameterName(), DateTime.parse(p.getParameterValue()));
            }
        }

        return parameters;
    }

    private Map<String, Object> buidParametersTypes(List<Parameter> listParam){
        Map<String, Object> parameters = new HashMap<>();
        for (Parameter p : listParam) {
            parameters.put(p.getParameterName(),p.getParameterClass());
        }
        return parameters;
    }

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
                String sClob = getStringSromClob(rule.getJson());

                recRule.setJson(sClob.replaceAll("[\\s\u0000]+",""));
                lstRecRule.add(recRule);
            }

        } catch (Exception ex) {
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
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, ex, CODIGO_ERROR_GENERICO);
        }
        return spListVariablesOUT.getPcVariable();
    }

    @Override
    public SpUpdateReglaOUT updateRule(GridRule grule) throws PlataformaBaseException {
        SpUpdateReglaOUT out;
        try {
            SpUpdateReglaIN params = new SpUpdateReglaIN();
            if(grule.getId() != null)
                params.setPId(Integer.parseInt(grule.getId()));
            else
                params.setPId(0);
            params.setPOper(grule.getOper());
            SqlLobValue slv=new SqlLobValue(grule.getJson());
            params.setPJson(slv);
            out =  this.spUpdateReglaDAO.execute(params);

        } catch (Exception ex) {
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, ex, CODIGO_ERROR_GENERICO);
        }

        return out;

    }

    @Override
    public SpUpdateConjuntoReglaOUT updateRuleSet(GridRule grule) throws PlataformaBaseException {
        SpUpdateConjuntoReglaOUT out;
        try {
            SpUpdateConjuntoReglaIN params = new SpUpdateConjuntoReglaIN();
            if(grule.getId() != null)
                params.setPId(Integer.parseInt(grule.getId()));
            else
                params.setPId(0);
            params.setPOper(grule.getOper());
            SqlLobValue slv=new SqlLobValue(grule.getJson());
            params.setPJson(slv);
            out =  this.spUpdateConjuntoReglaDAO.execute(params);

        } catch (Exception ex) {
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, ex, CODIGO_ERROR_GENERICO);
        }

        return out;
    }

    @Override
    public List<SpListReglaVariablePcVarRS> getRuleVariable(String nombre) throws PlataformaBaseException {
        SpListReglaVariableOUT out;
        try{
            SpListReglaVariableIN param = new SpListReglaVariableIN();
            param.setPNombre(nombre);

            out = this.spListReglaVariableDAO.execute(param);

        } catch (Exception ex) {
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, ex, CODIGO_ERROR_GENERICO);
        }
        return out.getPcVar();
    }

    private static Parameter containsParameter(Collection<Parameter> c, String name) {
        for(Parameter o : c) {
            if(o != null && o.getParameterName().equals(name)) {
                return o;
            }
        }
        return null;
    }

    private static List<Parameter> containsParameters(Collection<Parameter> c, String leftOne, String leftTwo) {
        int indexTrue = 0;
        List<Parameter> params = new ArrayList<Parameter>();
        for(Parameter o : c) {

            if(o != null && isEqual(o, leftOne, leftTwo)) {
                indexTrue++;
                params.add(o);
            }

            if(indexTrue>2){
                break;
            }
        }
        return params;
    }

    private static boolean isEqual(Parameter p, String leftOne, String leftTwo){
        return p.getParameterName().equals(leftOne) || p.getParameterName().equals(leftTwo);
    }

    private InJson readJsonFullFromString(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, InJson.class);
    }

    private static String getStringSromClob(Clob cl) throws PlataformaBaseException
    {
        StringWriter write = new StringWriter();
        try{
            Reader read  = cl.getCharacterStream();
            int c = -1;
            while ((c = read.read()) != -1)
            {
                write.write(c);
            }
            write.flush();
        }catch(Exception e)
        {
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, e, CODIGO_ERROR_GENERICO);
        }
        return write.toString();

    }

}
