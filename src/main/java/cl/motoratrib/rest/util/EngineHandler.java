package cl.motoratrib.rest.util;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglaVariablePcVarRS;
import cl.bancochile.plataformabase.error.BusinessException;
import cl.bancochile.plataformabase.error.PlataformaBaseException;
import cl.motoratrib.rest.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Timestamp;

public class EngineHandler {
    private static final String CODIGO_ERROR_GENERICO = "100000";
    private static final String GLOSA_ERROR_GENERICO = "Error al invocar motor de reglas ";

    private EngineHandler() {
    }

    public static InJson readJsonFullFromString(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, InJson.class);
    }

    public static void checkAllVariables(List<SpListReglaVariablePcVarRS> vars, Map<String, Object> reqMap) throws BusinessException {

        Map<String, Object> tmplMap = new HashMap<>();
        for (SpListReglaVariablePcVarRS o : vars) {
            tmplMap.put( o.getParametername(), o.getParameterclass());
        }

        if (!tmplMap.equals(reqMap)) {
            throw new BusinessException("\"{\\\"ref\\\":\\\"SF00\\\", \\\"alerta\\\":\\\"Las variables no corresponden al flujo que se esta invocando\\\"}\"");
        }

    }


    public static Map<String, Object> createAditionalParameter
            (List<Parameter> listParam, String pOne, String pTwo, String name) throws ParseException {

        Map<String, Object> parameters  = EngineHandler.buidParametersValues(listParam);

        List<Parameter> lParam = containsParameters(listParam, pOne, pTwo);
        Parameter p5fechaPep = containsParameter(lParam, pOne);
        Parameter p5fechaVencMac = containsParameter(lParam, pTwo);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        if (p5fechaPep != null && p5fechaVencMac != null) {

            Date dateTimeEnd = formatDate.parse(p5fechaVencMac.getParameterValue() );
            long end = dateTimeEnd.getTime();
            Date dateTimeStart = formatDate.parse(p5fechaPep.getParameterValue() );
            long start = dateTimeStart.getTime();

            int days = (int)end - (int)start;

            parameters.put(name, Long.valueOf(days));

            parameters.remove(p5fechaPep.getParameterName());
            parameters.remove(p5fechaVencMac.getParameterName());
        }

        return parameters;
    }

    public static List<Parameter> createCompleteAditionalParameter
            (List<Parameter> listParam, String pOne, String pTwo, String name) throws ParseException {

        List<Parameter> lParam = containsParameters(listParam, pOne, pTwo);
        Parameter p5fechaPep = containsParameter(lParam, pOne);
        Parameter p5fechaVencMac = containsParameter(lParam, pTwo);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        if (p5fechaPep != null && p5fechaVencMac != null) {

            Date dateTimeEnd = formatDate.parse(p5fechaVencMac.getParameterValue() );
            long end = dateTimeEnd.getTime();
            Date dateTimeStart = formatDate.parse(p5fechaPep.getParameterValue() );
            long start = dateTimeStart.getTime();

            int days = (int)end - (int)start;

            Parameter pAdd = new Parameter();
            pAdd.setParameterName(name);
            pAdd.setParameterValue(String.valueOf(days));
            pAdd.setParameterClass("Long");

            listParam.add(pAdd);
            listParam.remove(p5fechaPep);
            listParam.remove(p5fechaVencMac);
        }

        return listParam;
    }

    public static Map<String, Object> buidParametersValues(List<Parameter> listParam) throws ParseException{

        Map<String, Object> parameters = new HashMap<>();

        for (Parameter p : listParam) {
            if ("Long".equals(p.getParameterClass())) {
                parameters.put(p.getParameterName(), Long.valueOf(p.getParameterValue()));
                System.out.println(p.getParameterName() + "," +Long.valueOf(p.getParameterValue()));
            } else if ( "String".equals(p.getParameterClass())   ) {
                parameters.put(p.getParameterName(), p.getParameterValue());
                System.out.println(p.getParameterName() + "," +p.getParameterValue());
            } else if ("DateTime".equals(p.getParameterClass())) {
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                Date dateTime = formatDate.parse(p.getParameterValue() );
                Timestamp timestamp = new Timestamp(dateTime.getTime());
                parameters.put(p.getParameterName(), timestamp);
                System.out.println(p.getParameterName() + "," + timestamp);
            } else if ( "JsonNode".equals(p.getParameterClass())   ) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    parameters.put(p.getParameterName(), objectMapper.readTree(p.getParameterValue()));
                    System.out.println(p.getParameterName() + "," + p.getParameterValue());
                }catch(JsonProcessingException e){
                    throw new ParseException(e.getMessage(),e.hashCode());
                }catch(IOException e){
                    throw new ParseException(e.getMessage(),e.hashCode());
                }
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

    public static RespuestaDBAtribucion expandAttribution(RespuestaDBAtribucion[] reqAtr)
            throws PlataformaBaseException {
        RespuestaDBAtribucion [] resAtrList;
        try {

            if (reqAtr == null || reqAtr.length == 0)
                throw new NullPointerException("Empty arrangement");

            List<RespuestaDBAtribucion> respAtrList = new ArrayList<>();

            for(RespuestaDBAtribucion candidateAtribution: reqAtr){

                if( candidateAtribution.getAtribucionLarga().size() > 1 ) {

                    for (int i = 0; i < candidateAtribution.getAtribucionLarga().size(); i++ ){
                        respAtrList.add(candidateAtribution);
                    }

                }

            }

        } catch(Exception e){
            String mensajeError = "Listado de variables vacío.";
            throw new PlataformaBaseException(mensajeError, e, "300007");
        }

        return null;
    }

    public static RespuestaDBAtribucion[] reduceAtribution(RespuestaDBAtribucion[] reqAtr)
            throws PlataformaBaseException {

        RespuestaDBAtribucion [] resAtrList;

        try {

            if (reqAtr == null || reqAtr.length == 0)
                throw new NullPointerException("Empty arrangement");

            List<RespuestaDBAtribucion> respAtrList = new ArrayList<>();

            for(RespuestaDBAtribucion candidateAtribution: reqAtr){

                if(!candidateAtribution.getAtribucionLarga().isEmpty()) {
                    respAtrList.add(candidateAtribution);
                }

            }

            resAtrList = respAtrList.toArray( new RespuestaDBAtribucion[ respAtrList.size() ] );


        } catch(Exception e){
            String mensajeError = "Listado de variables vacío.";
            throw new PlataformaBaseException(mensajeError, e, "300007");
        }

        return resAtrList;
    }

    public static Map replaceAtribution(Map mapAtrIn,RespuestaDBAtribucion candidateAtribution) {

        // analisis de la atribucion que ya existe

        RespuestaDBAtribucion existingAtribution=(RespuestaDBAtribucion)mapAtrIn.
                    get(candidateAtribution.getAtribucionLarga().get(0).getNombre());

        System.out.println("candidateAtribution #-------------> [" + candidateAtribution.getAtribucionLarga().get(0).getNombre() + "]");
        System.out.println("existingAtribution #-------------> [" + existingAtribution.getAtribucionLarga().get(0).getNombre() + "]");
        System.out.println("candidateAtribution ##getMin : " + candidateAtribution.getAtribucionLarga().get(0).getMin());
        System.out.println("existingAtribution ##getMin : " + existingAtribution.getAtribucionLarga().get(0).getMin());

        if (!"nivel".equals(candidateAtribution.getAtribucionLarga().get(0).getNombre()) &&
                    !"nivel_r".equals(candidateAtribution.getAtribucionLarga().get(0).getNombre())) {

            if (existingAtribution.getAtribucionLarga().get(0).getMax() >
                    candidateAtribution.getAtribucionLarga().get(0).getMax()) {
                mapAtrIn.put(candidateAtribution.getAtribucionLarga().get(0).getNombre(), candidateAtribution);

            }

        }else{

            if (existingAtribution.getAtribucionLarga().get(0).getMin() <
                    candidateAtribution.getAtribucionLarga().get(0).getMin()) {
                mapAtrIn.put(candidateAtribution.getAtribucionLarga().get(0).getNombre(), candidateAtribution);
            }
        }

        return mapAtrIn;
    }

    public static int countOptional(List<RespuestaDBAtribucion> listAtribution)
            throws PlataformaBaseException {
        int count = 0;
        try {

            for(RespuestaDBAtribucion candidateAtribution: listAtribution){
                count = count + candidateAtribution.getAtribucionLarga().get(0).getOpcional();
            }

        } catch(Exception e){
            String mensajeError = "Listado de variables vacío.";
            throw new PlataformaBaseException(mensajeError, e, "300007");
        }

        return count;
    }

    public static <X> List<X> removeAttributionOptional(List<X> original, Collection<Integer> indices) {

        indices = new HashSet<>(indices);
        List<X> output = new ArrayList<X>();
        int len = original.size();
        for(int i = 0; i < len; i++) {
            if(!indices.contains(i)) {
                output.add(original.get(i));
            }
        }
        return output;
    }

    public static List<RespuestaDBAtribucion> cloningAttribution(List<RespuestaDBAtribucion> original){

        List<RespuestaDBAtribucion> newListAttribution = new ArrayList<>();
        for(RespuestaDBAtribucion obj : original){
            newListAttribution.add((RespuestaDBAtribucion)SerializationUtils.clone(obj));
        }

        return newListAttribution;

    }

    public static List<ResponseMinimalAttributions> distributeAttribution(List<RespuestaDBAtribucion> listAttribution){

        List<RespuestaDBAtribucion> listAtributionOptional = new ArrayList<>();
        List<Integer> listIndexToRemove = new ArrayList<>();
        List<ResponseMinimalAttributions> listResponseMinimalAttributions = new ArrayList<>();

        for(int i = 0; i < listAttribution.size(); i++){
            if( listAttribution.get(i).getAtribucionLarga().get(0).getOpcional() == 1 ) {
                listAtributionOptional.add(listAttribution.get(i));
                listIndexToRemove.add(i);
            }
        }

        List<RespuestaDBAtribucion> listAttributionWithouOptional =
                EngineHandler.removeAttributionOptional(listAttribution, listIndexToRemove);

        for(int j = 0; j < listAtributionOptional.size(); j++){

            List<RespuestaDBAtribucion> clonListAttr = cloningAttribution(listAttributionWithouOptional);
            clonListAttr.add(listAtributionOptional.get(j));
            listResponseMinimalAttributions.add(new ResponseMinimalAttributions(clonListAttr));
        }


        return listResponseMinimalAttributions;
    }

    public static String businessLogicAtribution(String strIn)
            throws PlataformaBaseException{
        String strOut=null;
        try {

            ObjectMapper mapper = new ObjectMapper();

            RespuestaDBAtribucion[] responseList=mapper.readValue(strIn, RespuestaDBAtribucion[].class);

            System.out.println("LARGO INICIAL DE ATRS : " + responseList.length);

            responseList = EngineHandler.reduceAtribution(responseList);

            System.out.println("LARGO FINAL DE ATRS : " + responseList.length);

            Map<String, Object> mapIn = new HashMap<>();

            for(RespuestaDBAtribucion candidateAtribution: responseList){

                System.out.println(candidateAtribution.getAtribucionLarga().get(0).getNombre() +
                        "--> minimo: " + candidateAtribution.getAtribucionLarga().get(0).getMin() + " maximo : " +
                        candidateAtribution.getAtribucionLarga().get(0).getMax());

                if(!mapIn.containsKey(candidateAtribution.getAtribucionLarga().get(0).getNombre())) {
                    //no existe la atribucion la introduce en mapIn
                    mapIn.put(candidateAtribution.getAtribucionLarga().get(0).getNombre(), candidateAtribution);
                } else{
                    // analisis de la atribucion que ya existe

                    mapIn=EngineHandler.replaceAtribution(mapIn,candidateAtribution);
                }

            }

            List<RespuestaDBAtribucion> listAtribution = new ArrayList<>();

            for( Iterator entries = mapIn.entrySet().iterator(); entries.hasNext();){
                Map.Entry entry = (Map.Entry) entries.next();
                listAtribution.add((RespuestaDBAtribucion)entry.getValue());
            }

            System.out.println("CANTIDAD DE OPCIONALES  : " + countOptional(listAtribution));

            if( countOptional(listAtribution) < 2 ) {

                //una sola respuesta
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                mapper = new ObjectMapper();

                List<ResponseMinimalAttributions> listResponseMinimalAttributions = new ArrayList<>();
                ResponseMinimalAttributions rma = new ResponseMinimalAttributions(listAtribution);
                listResponseMinimalAttributions.add(rma);

                //mapper.writeValue(out, listAtribution);
                mapper.writeValue(out, listResponseMinimalAttributions);
                strOut = new String(out.toByteArray(), Charset.forName("UTF-8"));

            }

        } catch(Exception e){
            String mensajeError = "Listado de variables vacío.";
            throw new PlataformaBaseException(mensajeError, e, "300007");
        }

        return strOut;


    }

    public static String convert(String value, String fromEncoding, String toEncoding) throws java.io.UnsupportedEncodingException {
        return new String(value.getBytes(fromEncoding), toEncoding);
    }

    public static String charset(String value, String charsets[]) throws java.io.UnsupportedEncodingException {
        String probe = StandardCharsets.UTF_8.name();
        for(String c : charsets) {
            Charset charset = Charset.forName(c);
            if(charset != null) {
                if(value.equals(convert(convert(value, charset.name(), probe), probe, charset.name()))) {
                    return c;
                }
            }
        }
        return StandardCharsets.UTF_8.name();
    }

/*
    public static Clob setClobFromstring(String stringData) throws PlataformaBaseException{
        Clob clob;
        try {
            clob = new SerialClob(stringData.toCharArray());

        } catch(SQLException e){
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, e, CODIGO_ERROR_GENERICO);
        }
        return clob;
    }
*/

}
