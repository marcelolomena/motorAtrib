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
import java.sql.SQLException;
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

        String responseRule = null;

        ClaseGenerica response = null;
        Parameter p5fechaPep, p5fechaVencMac = null;

        try{

            InJson in = readJsonFullFromString(json);

            List<SpListReglaVariablePcVarRS> vars = getRuleVariable(in.getRulesetName());

            Map<String, Object> parameters = new HashMap<>();

            List<Parameter> listParam = in.getParameterList();

            Map<String, String> tmplMap = new HashMap<String, String>();
            Map<String, String> reqMap = new HashMap<String, String>();
            for (SpListReglaVariablePcVarRS o : vars) {
                tmplMap.put( o.getParametername(), o.getParameterclass());
            }
            List<Parameter> lParam = containsParameters(listParam, "p5_fechaPep", "p5_fechaVencMac");
            p5fechaPep = containsParameter(lParam, "p5_fechaPep");
            p5fechaVencMac = containsParameter(listParam, "p5_fechaVencMac");

            if (p5fechaPep != null && p5fechaVencMac != null) {
                DateTime end = DateTime.parse(p5fechaVencMac.getParameterValue());
                DateTime start = DateTime.parse(p5fechaPep.getParameterValue());
                int days = Days.daysBetween(start, end).getDays();
                parameters.put("p5_diffMacPep", Long.valueOf(days));
                listParam.remove(p5fechaPep);
                listParam.remove(p5fechaVencMac);
            }


            for (Parameter p : listParam) {
                if ("Long".equals(p.getParameterClass())) {
                    parameters.put(p.getParameterName(), Long.valueOf(p.getParameterValue()));
                } else if ("String".equals(p.getParameterClass())) {
                    parameters.put(p.getParameterName(), p.getParameterValue());
                } else if ("DateTime".equals(p.getParameterClass())) {
                    parameters.put(p.getParameterName(), DateTime.parse(p.getParameterValue()));
                }
                reqMap.put(p.getParameterName(),p.getParameterClass());
            }

            if(!reqMap.equals(tmplMap)) {
                responseRule = "{\"ref\":\"SF00\", \"alerta\":\"Las variables no corresponden al flujo que se esta invocando\"}";
                throw new PlataformaBaseException(responseRule, new Exception(responseRule), CODIGO_ERROR_GENERICO);
            } else {
                Object o = jsrules.executeRuleset(in.getRulesetName(), parameters);

                if (o != null)
                    response = new ClaseGenerica(o);

                if ("java.lang.String".equals(response.classType()))
                    responseRule = response.getObj().toString();
                else
                    responseRule = "ERROR";

            }
        } catch (Exception ex) {
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, ex, CODIGO_ERROR_GENERICO);
        }

        return responseRule;
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
                String sClob = getstringfromclob(rule.getJson());

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
            if(o != null){
                if(o.getParameterName().equals(leftOne) || o.getParameterName().equals(leftTwo)) {
                    indexTrue++;
                    params.add(o);
                }
            }
            if(indexTrue>2){
                break;
            }
        }
        return params;
    }


    private InJson readJsonFullFromString(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, InJson.class);
    }

    private String getstringfromclob(Clob cl) throws PlataformaBaseException
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

    private String clobToString(Clob data)
    {
        final StringBuilder builder= new StringBuilder();

        try
        {
            if(data == null)  throw new Exception("data is null");
            final Reader reader = data.getCharacterStream();
            final BufferedReader br     = new BufferedReader(reader);
            if(br == null)  throw new Exception("buffer is null");
            int b;
            while(-1 != (b = br.read()))
            {
                builder.append((char)b);
            }

            br.close();
        }
        catch (SQLException e)
        {
            LOGGER.error("Within SQLException, Could not convert CLOB to string",e);
            return e.toString();
        }
        catch (IOException e)
        {
            LOGGER.error("Within IOException, Could not convert CLOB to string",e);
            return e.toString();
        }
        catch (Exception e)
        {
            LOGGER.error("Within Exception, Could not convert CLOB to string",e);
            return e.toString();
        }

        return builder.toString();
    }

}
