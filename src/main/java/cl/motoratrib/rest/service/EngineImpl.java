package cl.motoratrib.rest.service;

import cl.motoratrib.rest.domain.ClaseGenerica;
import cl.motoratrib.rest.domain.InJson;
import cl.motoratrib.rest.domain.Message;
import cl.motoratrib.rest.jsrules.JsRules;
import cl.motoratrib.rest.domain.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;

@Service
public class EngineImpl implements Engine {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineImpl.class);

    @Override
    public List evaluator(String json) throws Exception {
        LOGGER.debug("OK TUTTI");

        ClaseGenerica response = null;
        Parameter p5_fechaPep, p5_fechaVencMac = null;

        JsRules jsRules = JsRules.getInstance();

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

        System.out.println(parameters);

        Object o = jsRules.executeRuleset(in.getRulesetName(), parameters);

        if (o != null)
            response = new ClaseGenerica(o);

        if (response != null) {
            if (response.classType().equals("java.lang.String")) {
                System.out.println("Respuesta : " + response.obj.toString());
            } else {
                System.out.println("ERROR");
            }


        }



        Message indiaCountry=new Message("1","Curauma");
        Message chinaCountry=new Message("2", "Nuevo Pichidangui");
        Message nepalCountry=new Message("3", "Santiago");
        Message bhutanCountry=new Message("4", "Puente Alto");
        Message chileCountry=new Message("5", "La Florida");

        List listOfCountries = new ArrayList();
        listOfCountries.add(indiaCountry);
        listOfCountries.add(chinaCountry);
        listOfCountries.add(nepalCountry);
        listOfCountries.add(bhutanCountry);
        listOfCountries.add(chileCountry);
        return listOfCountries;


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


}
