package cl.motoratrib.rest.controller;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpUpdateConjuntoReglaOUT;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpUpdateReglaOUT;
import cl.motoratrib.rest.domain.GridRule;
import cl.motoratrib.rest.domain.RecordRule;
import cl.motoratrib.rest.service.EngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import cl.bancochile.plataformabase.error.PlataformaBaseException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class RuleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleController.class);
    @Autowired
    EngineService engineService;

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
            eval = engineService.evaluatorRule(URLDecoder.decode(json, "UTF-8"));
            long endTime = System.currentTimeMillis();
            LOGGER.debug("Total Time " + (endTime - startTime) + " milliseconds");
        }catch(PlataformaBaseException e){
            LOGGER.error("FAIL!!!" + e.getMessage());
            //eval=e.getMessage();
            throw e;
        }catch(UnsupportedEncodingException e){
            LOGGER.error("FAIL!!!" + e.getMessage());
            //eval=e.getMessage();
            throw new PlataformaBaseException(e.getMessage(),e,HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        return eval;
    }

}