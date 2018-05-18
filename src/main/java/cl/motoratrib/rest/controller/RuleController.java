package cl.motoratrib.rest.controller;

import cl.motoratrib.rest.service.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.net.URLDecoder;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class RuleController {
    @Autowired
    Engine engine;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test()  {
        ModelAndView modelAndview=new ModelAndView("test.jsp");;
        return modelAndview;
    }

    @RequestMapping(value = "/testing", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String testing(HttpEntity<String> httpEntity) {
        String _json = "";
        String _eval = "";
        try {
            _json = httpEntity.getBody();
            _eval = engine.evaluatorRule(URLDecoder.decode(_json, "UTF-8"));
        }catch(Exception e){
            System.out.println("FAIL!!!");
        }
        return _eval;
    }

}