package cl.motoratrib.rest.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.*;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.SpListReglasDAO;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.SpListVariablesDAO;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.SpUpdateReglaDAO;
import cl.bancochile.plataformabase.error.BusinessException;
import cl.motoratrib.rest.domain.*;
import cl.motoratrib.rest.jsrules.JsRules;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;

@Service
public class EngineImpl implements Engine {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineImpl.class);

    @Autowired
    JsRules jsrules;
    @Autowired
    SpListReglasDAO spListReglasDAO;
    @Autowired
    SpListVariablesDAO spListVariablesDAO;
    @Autowired
    SpUpdateReglaDAO spUpdateReglaDAO;

    @Override
    public String evaluatorRule(String json) throws Exception {

        String responseRule = null;

        ClaseGenerica response = null;
        Parameter p5_fechaPep, p5_fechaVencMac = null;

        InJson in = readJsonFullFromString(json);

        Map<String, Object> parameters = new HashMap<>();

        List<Parameter> listParam = in.getParameterList();

        List<Parameter> lParam = containsParameters(listParam, "p5_fechaPep", "p5_fechaVencMac");
        p5_fechaPep = containsParameter(lParam, "p5_fechaPep");
        p5_fechaVencMac = containsParameter(listParam, "p5_fechaVencMac");

        if (p5_fechaPep != null && p5_fechaVencMac != null) {
            DateTime end = DateTime.parse(p5_fechaVencMac.getParameterValue());
            DateTime start = DateTime.parse(p5_fechaPep.getParameterValue());
            int days = Days.daysBetween(start, end).getDays();
            parameters.put("p5_diffMacPep", Long.valueOf(days));
            //LOGGER.debug("agregando la variable p5_diffMacPep con valor : " + days);
            listParam.remove(p5_fechaPep);
            listParam.remove(p5_fechaVencMac);
        }


        for (Parameter p : listParam) {
            if (p.getParameterClass().equals("Long")) {
                parameters.put(p.getParameterName(), Long.valueOf(p.getParameterValue()));
            } else if (p.getParameterClass().equals("String")) {
                parameters.put(p.getParameterName(), p.getParameterValue());
            } else if (p.getParameterClass().equals("DateTime")) {
                parameters.put(p.getParameterName(), DateTime.parse(p.getParameterValue()));
            }
        }

        //System.out.println(parameters);

        Object o = jsrules.executeRuleset(in.getRulesetName(), parameters);

        if (o != null)
            response = new ClaseGenerica(o);

        if (response != null) {
            if (response.classType().equals("java.lang.String")) {
                responseRule = response.obj.toString();
            } else {
                System.out.println("ERROR");
                responseRule = "ERROR";
            }
        }

        return responseRule;
    }

    @Override
    public List<RecordRule> getRule(int id) throws Exception {
        SpListReglasOUT spListReglasOUT;
        List<RecordRule> lstRecRule = new ArrayList<>();
        try {
            //System.out.println("el id : " + id);
            SpListReglasIN params  = new SpListReglasIN();
            params.setPIdPadre(id);
            spListReglasOUT = this.spListReglasDAO.execute(params);

            for (SpListReglasPcReglaRS rule : spListReglasOUT.getPcRegla()){
                RecordRule recRule = new RecordRule();
                recRule.setId(rule.getId().intValue());
                recRule.setIdParent(rule.getIdPadre().intValue());
                recRule.setName(rule.getNombre());
                //String sClob = clobToString(rule.getJson());
                String sClob = getstringfromclob(rule.getJson());

                //System.out.println("the clean sClob : " + sClob.replaceAll("[\\s\u0000]+","") );
                //String myString = sClob.replaceAll("\\s+", "");
                //recRule.setJson(sClob.replaceAll("\\s+", ""));
                recRule.setJson(sClob.replaceAll("[\\s\u0000]+",""));
                lstRecRule.add(recRule);
            }
            //System.out.println("el resultado : " + spListReglasOUT);
            //System.out.println("el super resultado : " + spListReglasOUT.getPcRegla());

        }catch(BusinessException e){
            throw new Exception(e.getMessage());
        }
        return lstRecRule;
    }

    @Override
    public List<SpListVariablesPcVariableRS> getVariables() throws Exception {
        SpListVariablesOUT spListVariablesOUT;

        try {

            spListVariablesOUT = this.spListVariablesDAO.execute();

        }catch(BusinessException e){
            throw new Exception(e.getMessage());
        }
        return spListVariablesOUT.getPcVariable();
    }

    @Override
    public SpUpdateReglaOUT updateRule(GridRule grule) throws Exception {
        SpUpdateReglaOUT out;
        try {
            SpUpdateReglaIN params = new SpUpdateReglaIN();
            params.setPId(Integer.parseInt(grule.getId()));
            params.setPOper(grule.getOper());
            params.setPOper(grule.getOper());
            out =  this.spUpdateReglaDAO.execute(params);
            System.out.println("trullul");
            System.out.println(out);
        }catch (BusinessException e){
            throw new Exception(e.getMessage());
        }

        return out;

    }

    private Parameter containsParameter(Collection<Parameter> c, String name) {
        for(Parameter o : c) {
            if(o != null && o.getParameterName().equals(name)) {
                return o;
            }
        }
        return null;
    }

    private List<Parameter> containsParameters(Collection<Parameter> c, String leftOne, String leftTwo) {
        int indexTrue = 0;
        List<Parameter> params = new ArrayList<Parameter>();
        for(Parameter o : c) {
            if(o != null){
                if(o.getParameterName().equals(leftOne) || o.getParameterName().equals(leftTwo)) {
                    indexTrue++;
                    params.add(o);
                }
            }
            if(indexTrue>2) break;
        }
        return params;
    }


    private InJson readJsonFullFromString(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InJson in = mapper.readValue(json, InJson.class);
        return in;

    }

    private String convertToJSON(Object obj)  throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (obj == null) {
                return null;
            } else if (obj instanceof String) {
                return (String) obj;
            }
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new Exception("Cannot serialize to JSON " + obj, e);
        }
    }

    private String getstringfromclob(Clob cl)
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
        }catch(Exception ec)
        {
            ec.printStackTrace();
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
