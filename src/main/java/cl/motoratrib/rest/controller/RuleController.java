package cl.motoratrib.rest.controller;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpUpdateReglaOUT;
import cl.motoratrib.rest.domain.GridRule;
import cl.motoratrib.rest.domain.RecordRule;
import cl.motoratrib.rest.service.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.net.URLDecoder;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class RuleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleController.class);
    @Autowired
    Engine engine;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test()  {
        ModelAndView modelAndview=new ModelAndView("test.jsp");;
        return modelAndview;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView admin()  {
        ModelAndView modelAndview=new ModelAndView("admin.jsp");;
        return modelAndview;
    }

    @RequestMapping(value = "/rules/{id}", method = RequestMethod.GET,
            produces = { "application/json;**charset=UTF-8**" })//application/json;charset=UTF-8
    public ResponseEntity<List<RecordRule>> getRules(@PathVariable int id)
            throws Exception {

        return new ResponseEntity<>(this.engine.getRule(id),HttpStatus.OK);

    }

    @RequestMapping(value = "/variables", method = RequestMethod.GET,
            produces = { "application/json;**charset=UTF-8**" })//application/json;charset=UTF-8
    public ResponseEntity<List<SpListVariablesPcVariableRS>> getVariables()
            throws Exception {

        return new ResponseEntity<>(this.engine.getVariables(),HttpStatus.OK);

    }

    @RequestMapping(value = "/uRule", method = RequestMethod.POST,
            produces = { "application/json;**charset=UTF-8**" })
    public ResponseEntity<SpUpdateReglaOUT> upadaterule(@RequestBody GridRule grule)
            throws Exception {

        System.out.println("ID -----------> " + grule.getId());
        System.out.println("OPER -----------> " + grule.getOper());
        System.out.println("JSON -----------> " +grule.getJson());

        return new ResponseEntity<>(this.engine.updateRule(grule),HttpStatus.OK);

    }

    @RequestMapping(value = "/testing", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String testing(HttpEntity<String> httpEntity) {
        String _json = "";
        String _eval = "";
        try {
            _json = httpEntity.getBody();
            long startTime = System.currentTimeMillis();
            _eval = engine.evaluatorRule(URLDecoder.decode(_json, "UTF-8"));
            long endTime = System.currentTimeMillis();
            System.out.println("Total Time " + (endTime - startTime) + " milliseconds");
        }catch(Exception e){
            System.out.println("FAIL!!!");
        }
        return _eval;
    }

}