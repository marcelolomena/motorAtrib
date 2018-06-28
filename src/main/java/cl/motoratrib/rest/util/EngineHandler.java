package cl.motoratrib.rest.util;

import cl.bancochile.plataformabase.error.PlataformaBaseException;
import cl.motoratrib.rest.domain.InJson;
import cl.motoratrib.rest.domain.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.util.*;

public class EngineHandler {
    private static final String CODIGO_ERROR_GENERICO = "100000";
    private static final String GLOSA_ERROR_GENERICO = "Error al invocar motor de reglas ";

    private EngineHandler() {
    }

    public static InJson readJsonFullFromString(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, InJson.class);
    }

    public static boolean checkVariables(Map<String, Object> tmplMap, Map<String, Object> reqMap){
        return tmplMap.equals(reqMap);
    }

    public static Map<String, Object> createAditionalParameter(Map<String, Object> parameters, Parameter pOne, Parameter pTwo, String name){
        DateTime end = DateTime.parse(pTwo.getParameterValue());
        DateTime start = DateTime.parse(pOne.getParameterValue());
        int days = Days.daysBetween(start, end).getDays();
        parameters.put(name, Long.valueOf(days));
        return parameters;
    }

    public static Map<String, Object> buidParametersValues(List<Parameter> listParam){

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

    public static Map<String, Object> buidParametersTypes(List<Parameter> listParam){
        Map<String, Object> parameters = new HashMap<>();
        for (Parameter p : listParam) {
            parameters.put(p.getParameterName(),p.getParameterClass());
        }
        return parameters;
    }

    public static List<Parameter> containsParameters(Collection<Parameter> c, String leftOne, String leftTwo) {
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

    public static boolean isEqual(Parameter p, String leftOne, String leftTwo){
        return p.getParameterName().equals(leftOne) || p.getParameterName().equals(leftTwo);
    }

    public static Parameter containsParameter(Collection<Parameter> c, String name) {
        for(Parameter o : c) {
            if(o != null && o.getParameterName().equals(name)) {
                return o;
            }
        }
        return null;
    }

    public static String getStringSromClob(Clob cl) throws PlataformaBaseException
    {
        StringWriter write = new StringWriter();

        try {
            Reader read = cl.getCharacterStream();
            int c = -1;
            while ((c = read.read()) != -1) {
                write.write(c);
            }
            write.flush();
        } catch(Exception e){
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, e, CODIGO_ERROR_GENERICO);
        }
        return write.toString();

    }

}
