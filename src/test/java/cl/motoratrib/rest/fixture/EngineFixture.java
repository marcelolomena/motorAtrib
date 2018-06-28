package cl.motoratrib.rest.fixture;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaOUT;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesOUT;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;
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
        SpListVariablesPcVariableRS objVaribles = new SpListVariablesPcVariableRS();
        objVaribles.setId(1);
        objVaribles.setNombre("sf1_pyme");
        objVaribles.setDescripcion("sf1_pyme");
        objVaribles.setTipo("String");

        datosVariables.add(objVaribles);

        objVaribles.setId(2);
        objVaribles.setNombre("sf1_rating");
        objVaribles.setDescripcion("sf1_rating");
        objVaribles.setTipo("String");

        SpListVariablesOUT variables = new SpListVariablesOUT();

        variables.setPcVariable(datosVariables);
        variables.setPEstado(1);
        variables.setPGlosa("Glosa");

        return variables;

    }

    public static SpGetReglaOUT reglas(){
        SpGetReglaOUT reglas = new SpGetReglaOUT();
        reglas.setPJson(null);
        reglas.setPEstado(1);
        reglas.setPGlosa("Glosa");

        return reglas;
    }
}
