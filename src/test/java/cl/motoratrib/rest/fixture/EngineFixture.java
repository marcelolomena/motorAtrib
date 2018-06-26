package cl.motoratrib.rest.fixture;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesOUT;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;

import java.util.ArrayList;
import java.util.List;

public class EngineFixture {
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
}
