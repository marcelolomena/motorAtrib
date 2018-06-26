package cl.motoratrib.rest.controller;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglaVariablePcVarRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;
import cl.motoratrib.rest.domain.*;
import cl.motoratrib.rest.jsrules.JsRules;
import cl.motoratrib.rest.service.EngineService;
import cl.motoratrib.rest.util.EngineHandler;
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
    @Autowired
    EngineService engineService;

    @Autowired
    JsRules jsrules;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test() throws PlataformaBaseException {
        return new ModelAndView("test.jsp");
    }

    @RequestMapping(value = "/variables", method = RequestMethod.GET,
            produces = { "application/json;**charset=UTF-8**" })
    public ResponseEntity<List<SpListVariablesPcVariableRS>> getVariables()
            throws PlataformaBaseException {
        return new ResponseEntity<>(this.engineService.getVariables(),HttpStatus.OK);
    }

    @RequestMapping(value = "/testing", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String testing(HttpEntity<String> httpEntity) throws PlataformaBaseException {
        String json,eval;
        try {
            json = httpEntity.getBody();
            long startTime = System.currentTimeMillis();

            json = URLDecoder.decode(json, "UTF-8");

            InJson in = EngineHandler.readJsonFullFromString(json);
            List<Parameter> listParam = in.getParameterList();

            Map<String, Object> tmplMap = buildTemplateParameter(in.getRulesetName());
            Map<String, Object> parameters  = EngineHandler.buidParametersValues(listParam);

            if( !EngineHandler.checkVariables(tmplMap, EngineHandler.buidParametersTypes(listParam) ) ) {
                eval = "{\"ref\":\"SF00\", \"alerta\":\"Las variables no corresponden al flujo que se esta invocando\"}";
                LOGGER.debug(eval);
            } else {

                List<Parameter> lParam = EngineHandler.containsParameters(listParam, "p5_fechaPep", "p5_fechaVencMac");
                Parameter p5fechaPep = EngineHandler.containsParameter(lParam, "p5_fechaPep");
                Parameter p5fechaVencMac = EngineHandler.containsParameter(lParam, "p5_fechaVencMac");

                if (p5fechaPep != null && p5fechaVencMac != null) {
                    parameters = EngineHandler.createAditionalParameter(parameters, p5fechaPep, p5fechaVencMac, "p5_diffMacPep");
                    parameters.remove(p5fechaPep);
                    parameters.remove(p5fechaVencMac);
                }

                Object o = jsrules.executeRuleset(in.getRulesetName(), parameters);

                eval =  EngineHandler.createResponse(o);

            }

            long endTime = System.currentTimeMillis();
            LOGGER.debug("Total Time " + (endTime - startTime) + " milliseconds");
        }catch(Exception e){
            LOGGER.error("FAIL!!!" + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return eval;
    }

    private Map<String, Object> buildTemplateParameter(String nombre) throws PlataformaBaseException {
        Map<String, Object> tmplMap = new HashMap<>();
        List<SpListReglaVariablePcVarRS> vars = engineService.getRuleVariable(nombre);
        for (SpListReglaVariablePcVarRS o : vars) {
            tmplMap.put( o.getParametername(), o.getParameterclass());
        }
        return tmplMap;
    }

}