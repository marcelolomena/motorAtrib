package cl.motoratrib.rest.controller;

import cl.motoratrib.rest.service.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
        try {
            _json = httpEntity.getBody();
            System.out.println("<---------------------------------------------------------------------->");
            System.out.println(_json);
            System.out.println("<---------------------------------------------------------------------->");
        }catch(Exception e){
            System.out.println("CAGO!!!");
        }
        // json contains the plain json string
        return String.format("HOLA %s!", _json);
        //return _json;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET,headers="Accept=application/json")
    public List list() throws Exception {

        String json= "{\n" +
                "\t\"rulesetName\": \"POC_1_RulesetList\",\n" +
                "\t\"parameterList\": [{\n" +
                "\t\t\t\"parameterName\": \"sf1_pyme\",\n" +
                "\t\t\t\"parameterValue\": \"SI\",\n" +
                "\t\t\t\"parameterClass\": \"String\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"parameterName\": \"sf1_rating\",\n" +
                "\t\t\t\"parameterValue\": \"SI\",\n" +
                "\t\t\t\"parameterClass\": \"String\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"parameterName\": \"sf1_tipoRating\",\n" +
                "\t\t\t\"parameterValue\": \"AGRICOLA\",\n" +
                "\t\t\t\"parameterClass\": \"String\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"parameterName\": \"sf1_privada\",\n" +
                "\t\t\t\"parameterValue\": \"SI\",\n" +
                "\t\t\t\"parameterClass\": \"String\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
        //System.out.println(json);
        //Object o = engine.evaluator(json);

        //System.out.println(o);
        return engine.evaluator(json);


    }


}