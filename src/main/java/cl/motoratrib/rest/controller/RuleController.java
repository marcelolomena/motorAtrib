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

            json = URLDecoder.decode(httpEntity.getBody(), "UTF-8");

            InJson in = EngineHandler.readJsonFullFromString(json);
            List<Parameter> listParam = in.getParameterList();

            List<SpListReglaVariablePcVarRS> varsExpected = engineService.getRuleVariable(in.getRulesetName());
            Map<String, Object>  varsReal = EngineHandler.buidParametersTypes(listParam);

            EngineHandler.checkAllVariables(varsExpected, varsReal );

            Map<String, Object> parameters = EngineHandler.createAditionalParameter(listParam,"p5_fechaPep", "p5_fechaVencMac", "p5_diffMacPep");
            Object o = jsrules.executeRuleset(in.getRulesetName(), parameters);
            eval = o.toString();

        }catch(Exception e){
            LOGGER.error("Engine fail!!! " + e.getMessage());
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return eval;
    }

}