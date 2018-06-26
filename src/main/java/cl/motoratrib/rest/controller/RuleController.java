package cl.motoratrib.rest.controller;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglaVariablePcVarRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpUpdateConjuntoReglaOUT;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpUpdateReglaOUT;
import cl.motoratrib.rest.domain.*;
import cl.motoratrib.rest.jsrules.JsRules;
import cl.motoratrib.rest.jsrules.exception.JsRulesException;
import cl.motoratrib.rest.service.EngineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import cl.bancochile.plataformabase.error.PlataformaBaseException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class RuleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleController.class);
    @Autowired
    EngineService engineService;

    @Autowired
    JsRules jsrules;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test() throws PlataformaBaseException {
        return new ModelAndView("test.jsp");
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView admin() throws PlataformaBaseException {
        return new ModelAndView("admin.jsp");
    }

    @RequestMapping(value = "/rules/{id}", method = RequestMethod.GET,
            produces = { "application/json;**charset=UTF-8**" })
    public ResponseEntity<List<RecordRule>> getRules(@PathVariable int id)
            throws PlataformaBaseException {
        return new ResponseEntity<>(this.engineService.getRule(id),HttpStatus.OK);

    }

    @RequestMapping(value = "/variables", method = RequestMethod.GET,
            produces = { "application/json;**charset=UTF-8**" })
    public ResponseEntity<List<SpListVariablesPcVariableRS>> getVariables()
            throws PlataformaBaseException {
        return new ResponseEntity<>(this.engineService.getVariables(),HttpStatus.OK);

    }

    @RequestMapping(value = "/uRule", method = RequestMethod.POST,
            produces = { "application/json;**charset=UTF-8**" })
    public ResponseEntity<SpUpdateReglaOUT> updateRule(@RequestBody GridRule grule)
            throws PlataformaBaseException {
        return new ResponseEntity<>(this.engineService.updateRule(grule),HttpStatus.OK);

    }

    @RequestMapping(value = "/uRuleSet", method = RequestMethod.POST,
            produces = { "application/json;**charset=UTF-8**" })
    public ResponseEntity<SpUpdateConjuntoReglaOUT> updateRuleSet(@RequestBody GridRule grule)
            throws PlataformaBaseException {
        return new ResponseEntity<>(this.engineService.updateRuleSet(grule),HttpStatus.OK);

    }

    @RequestMapping(value = "/testing", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String testing(HttpEntity<String> httpEntity) throws PlataformaBaseException {
        String json,eval;
        try {
            json = httpEntity.getBody();
            long startTime = System.currentTimeMillis();

            json = URLDecoder.decode(json, "UTF-8");

            InJson in = readJsonFullFromString(json);
            List<Parameter> listParam = in.getParameterList();

            Map<String, Object> tmplMap = buildTemplateParameter(in.getRulesetName());
            Map<String, Object> parameters  = buidParametersValues(listParam);

            if( !checkVariables(tmplMap, buidParametersTypes(listParam) ) ) {
                eval = "{\"ref\":\"SF00\", \"alerta\":\"Las variables no corresponden al flujo que se esta invocando\"}";
                LOGGER.debug(eval);
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

                eval =  createResponse(o);

            }

            long endTime = System.currentTimeMillis();
            LOGGER.debug("Total Time " + (endTime - startTime) + " milliseconds");
        }catch(PlataformaBaseException e){
            LOGGER.error("FAIL!!!" + e.getMessage());
            throw e;
        }catch(UnsupportedEncodingException e){
            LOGGER.error("FAIL!!!" + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }catch(IOException e){
            LOGGER.error("FAIL!!!" + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }catch(JsRulesException e){
            LOGGER.error("FAIL!!!" + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return eval;
    }

    private InJson readJsonFullFromString(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, InJson.class);
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

    private static boolean checkVariables(Map<String, Object> tmplMap, Map<String, Object> reqMap){
        return tmplMap.equals(reqMap);
    }

    private Map<String, Object> buildTemplateParameter(String nombre) throws PlataformaBaseException{
        Map<String, Object> tmplMap = new HashMap<>();
        List<SpListReglaVariablePcVarRS> vars = engineService.getRuleVariable(nombre);
        for (SpListReglaVariablePcVarRS o : vars) {
            tmplMap.put( o.getParametername(), o.getParameterclass());
        }
        return tmplMap;
    }

    private static Map<String, Object> createAditionalParameter(Map<String, Object> parameters, Parameter pOne,Parameter pTwo, String name){
        DateTime end = DateTime.parse(pTwo.getParameterValue());
        DateTime start = DateTime.parse(pOne.getParameterValue());
        int days = Days.daysBetween(start, end).getDays();
        parameters.put(name, Long.valueOf(days));
        return parameters;
    }

    private static Map<String, Object> buidParametersValues(List<Parameter> listParam){

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

    private static Map<String, Object> buidParametersTypes(List<Parameter> listParam){
        Map<String, Object> parameters = new HashMap<>();
        for (Parameter p : listParam) {
            parameters.put(p.getParameterName(),p.getParameterClass());
        }
        return parameters;
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

    private static Parameter containsParameter(Collection<Parameter> c, String name) {
        for(Parameter o : c) {
            if(o != null && o.getParameterName().equals(name)) {
                return o;
            }
        }
        return null;
    }

}