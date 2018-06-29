package cl.motoratrib.rest.fixture;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.*;

import java.util.ArrayList;
import java.util.List;

public class EngineFixture {

    public static final String JSON_TEST_1 = "{\n" +
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

    public static SpListVariablesOUT variables(){
        List<SpListVariablesPcVariableRS> datosVariables = new ArrayList<>();
        SpListVariablesPcVariableRS objVariables = new SpListVariablesPcVariableRS();
        objVariables.setId(1);
        objVariables.setNombre("sf1_pyme");
        objVariables.setDescripcion("sf1_pyme");
        objVariables.setTipo("String");

        datosVariables.add(objVariables);

        objVariables.setId(2);
        objVariables.setNombre("sf1_rating");
        objVariables.setDescripcion("sf1_rating");
        objVariables.setTipo("String");

        SpListVariablesOUT variables = new SpListVariablesOUT();

        variables.setPcVariable(datosVariables);
        variables.setPEstado(1);
        variables.setPGlosa("Glosa");

        return variables;

    }

    public static SpListReglaVariableOUT getruleset(){

        List<SpListReglaVariablePcVarRS> datosVariables = new ArrayList<>();
        SpListReglaVariablePcVarRS objVariables = new SpListReglaVariablePcVarRS();
        objVariables.setParametername("sf1_pyme");
        objVariables.setParameterclass("String");
        datosVariables.add(objVariables);

        SpListReglaVariableOUT out = new SpListReglaVariableOUT();
        out.setPEstado(1);
        out.setPGlosa("Glosa");
        out.setPcVar(datosVariables);
        return out;
    }

    public static SpGetReglaOUT getrule(){
        SpGetReglaOUT out = new SpGetReglaOUT();
        out.setPJson(null);
        out.setPEstado(1);
        out.setPGlosa("Glosa");

        return out;
    }
}
