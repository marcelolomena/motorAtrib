package cl.motoratrib.rest.controller;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglaVariablePcVarRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;
import cl.motoratrib.rest.domain.*;
import cl.motoratrib.rest.jsrules.JsRules;
import cl.motoratrib.rest.service.EngineService;
import cl.motoratrib.rest.util.EngineHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.net.URLDecoder;
import java.util.*;

import cl.bancochile.plataformabase.error.PlataformaBaseException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class RuleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleController.class);
    private static final String pre_positive="{rta:true,desc=\"Puede ingresar MAC\"}";
    private static final String pre_negative="{rta:false,desc=\"No puede ingresar MAC\"}";
    @Autowired
    EngineService engineService;

    @Autowired
    JsRules jsrules;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test() throws PlataformaBaseException {
        return new ModelAndView("test.jsp");
    }

    @RequestMapping(value = "/variables", method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SpListVariablesPcVariableRS>> getVariables()
            throws PlataformaBaseException {
        return new ResponseEntity<>(this.engineService.getVariables(),HttpStatus.OK);
    }

    @RequestMapping(value = "/getVarAvailable/{ruleSetName}", method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public List<SpListReglaVariablePcVarRS> getVarAvailable(@PathVariable String ruleSetName)
            throws PlataformaBaseException {
        return engineService.getRuleVariable(ruleSetName);
    }

    @RequestMapping(value = "/getFlow", method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public String getFlow(HttpEntity<String> httpEntity) throws PlataformaBaseException {
        String requestJson,responseJson;
        try {
            requestJson = URLDecoder.decode(httpEntity.getBody(), "UTF-8");
            InJson in = EngineHandler.readJsonFullFromString(requestJson);
            List<Parameter> listParam = in.getParameterList();
            List<SpListReglaVariablePcVarRS> varsExpected = engineService.getRuleVariable(in.getRulesetName());

            Map<String, Object>  varsReal = EngineHandler.buidParametersTypes(listParam);

            EngineHandler.checkAllVariables(varsExpected, varsReal );
            Object o = jsrules.executeRuleset(in.getRulesetName(), EngineHandler.buidParametersValues(listParam));
            responseJson = o.toString().
                    replace("\"{", "{").
                    replace("}\"", "}").
                    replace("\\", "");

        }catch(Exception e){
            LOGGER.error("Engine fail!!! " + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return responseJson;
    }

    @RequestMapping(value = "/getPreconditions", method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public String getPreconditions(HttpEntity<String> httpEntity) throws PlataformaBaseException {
        String requestJson,responseJson;
        try {
            requestJson = URLDecoder.decode(httpEntity.getBody(), "UTF-8");
            InJson in = EngineHandler.readJsonFullFromString(requestJson);
            List<Parameter> listParam = in.getParameterList();
            List<SpListReglaVariablePcVarRS> varsExpected = engineService.getRuleVariable(in.getRulesetName());
            List<Parameter> parameters = EngineHandler.createCompleteAditionalParameter(listParam,
                    "m_pre05_v_fechaPep",
                    "m_pre05_v_fechaVencMac",
                    "m_pre05_v_diffMacPep");
            Map<String, Object>  varsReal = EngineHandler.buidParametersTypes(parameters);
            EngineHandler.checkAllVariables(varsExpected, varsReal );
            Object o = jsrules.executeRuleset(in.getRulesetName(), EngineHandler.buidParametersValues(parameters));
            responseJson = o.toString().
                    replace("\"{", "{").
                    replace("}\"", "}").
                    replace("\\", "");

            System.out.println(responseJson);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseJson.replaceAll("[\\\\]",""));

            int count = 0;
            int total = 0;
            for (JsonNode node : root) {
                total++;
                count = count + Integer.parseInt(node.path("result").asText());
            }
            System.out.println("total : " + total);
            System.out.println("result : " + count);

            if(count != total)
                responseJson = pre_negative;
            else
                responseJson = pre_positive;

        }catch(Exception e){
            LOGGER.error("Engine fail!!! " + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return responseJson;
    }

    @RequestMapping(value = "/engine", method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public String engine(HttpEntity<String> httpEntity) throws PlataformaBaseException {
        String requestJson,responseJson;
        try {
            requestJson = URLDecoder.decode(httpEntity.getBody(), "UTF-8");
            InJson in = EngineHandler.readJsonFullFromString(requestJson);
            List<Parameter> listParam = in.getParameterList();
            Object o = jsrules.executeRuleset(in.getRulesetName(), EngineHandler.buidParametersValues(listParam));
            if(o!=null)
                responseJson="{\"value\":\"" + o.toString() + "\"}";
            else
                responseJson="{\"value\":\"F\"}";

        }catch(Exception e){
            LOGGER.error("Engine fail!!! " + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return responseJson;
    }

    @RequestMapping(value = "/getAttributions", method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public String getAttributions(HttpEntity<String> httpEntity) throws PlataformaBaseException {
        String requestJson,responseJson;
        try {
            requestJson = URLDecoder.decode(httpEntity.getBody(), "UTF-8");
            InJson in = EngineHandler.readJsonFullFromString(requestJson);
            List<Parameter> listParam = in.getParameterList();
            List<SpListReglaVariablePcVarRS> varsExpected = engineService.getRuleVariable(in.getRulesetName());

            Map<String, Object>  varsReal = EngineHandler.buidParametersTypes(listParam);

            EngineHandler.checkAllVariables(varsExpected, varsReal );
            Object o = jsrules.executeRuleset(in.getRulesetName(), EngineHandler.buidParametersValues(listParam));
            responseJson = o.toString().
                    replace("\"{", "{").
                    replace("}\"", "}").
                    replace("\\", "");

            responseJson = EngineHandler.businessLogicAtribution(responseJson);

        }catch(Exception e){
            LOGGER.error("Engine fail!!! " + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return responseJson;
    }


    @RequestMapping(value = "/getEnough", method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public String getEnough(HttpEntity<String> httpEntity) throws PlataformaBaseException {
        String requestJson,responseJson;

        try {
            responseJson = "F";
            requestJson = URLDecoder.decode(httpEntity.getBody(), "UTF-8");
            InJson in = EngineHandler.readJsonFullFromString(requestJson);
            List<Parameter> listParam = in.getParameterList();
            List<SpListReglaVariablePcVarRS> varsExpected = engineService.getRuleVariable(in.getRulesetName());

            Map<String, Object>  varsReal = EngineHandler.buidParametersTypes(listParam);

            //EngineHandler.checkAllVariables(varsExpected, varsReal );
            Object o = jsrules.executeRuleset(in.getRulesetName(), EngineHandler.buidParametersValues(listParam));
            if(o!=null)
                responseJson=o.toString();


        }catch(Exception e){
            LOGGER.error("Engine fail!!! " + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return responseJson;
    }

    @RequestMapping(value = "/testing", method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public String testing(HttpEntity<String> httpEntity) throws PlataformaBaseException {
        String requestJson,responseJson;
        try {
            requestJson = URLDecoder.decode(httpEntity.getBody(), "UTF-8");
            InJson in = EngineHandler.readJsonFullFromString(requestJson);
            List<Parameter> listParam = in.getParameterList();
            Object o = jsrules.executeRuleset(in.getRulesetName(), EngineHandler.buidParametersValues(listParam));
            responseJson = o.toString().
                    replace("\"{", "{").
                    replace("}\"", "}").
                    replace("\\", "");

        }catch(Exception e){
            LOGGER.error("Engine fail!!! " + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return responseJson;
    }

}