package cl.motoratrib.rest.controller;

import cl.motoratrib.rest.service.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class RuleController {
    @Autowired
    Engine engine;
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