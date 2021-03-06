package cl.motoratrib.rest.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglaVariablePcVarRS;
import cl.bancochile.plataformabase.error.BusinessException;
import cl.bancochile.plataformabase.error.PlataformaBaseException;
import cl.motoratrib.rest.domain.InJson;
import cl.motoratrib.rest.domain.Parameter;
import java.sql.Clob;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class EngineHandlerTest {

    private static final String json ="{\"rulesetName\": \"POC_1_RulesetList\",\"parameterList\": [{\"parameterName\": \"sf1_pyme\",\"parameterValue\": \"SI\",\"parameterClass\": \"String\"}]}";

    @org.junit.Rule
    public ExpectedException exception = ExpectedException.none();

    InJson jsonReponse;

    Parameter parameter,parameterD1,parameterD2,parameterExtOne,parameterExtTwo,parameterExtThree;

    List<Parameter> parameterList,parameterAditionalList,parameterListExt;

    List<SpListReglaVariablePcVarRS> lstExpected;
    Map<String, Object> lstReal,lstRealExt;

    Map<String, Object> parametersO,parametersMap;

    @Before
    public void setup(){
        parametersO = new HashMap<>();
        parametersO.put("sf1_pyme","SI");

        parameter = new Parameter();
        parameter.setParameterClass("String");
        parameter.setParameterName("sf1_pyme");
        parameter.setParameterValue("SI");

        parameterD1 = new Parameter();
        parameterD1.setParameterName("p5_fechaPep");
        parameterD1.setParameterValue("2018-06-07");
        parameterD1.setParameterClass("Datetime");

        parameterD2 = new Parameter();
        parameterD2.setParameterName("p5_fechaVencMac");
        parameterD2.setParameterValue("2019-06-07");
        parameterD2.setParameterClass("Datetime");

        parameterList = new ArrayList<>();
        parameterList.add(parameter);

        parameterAditionalList = new ArrayList<>();
        parameterAditionalList.add(parameter);
        parameterAditionalList.add(parameterD1);
        parameterAditionalList.add(parameterD2);

        jsonReponse = new InJson();
        jsonReponse.setRulesetName("POC_1_RulesetList");
        jsonReponse.setParameterList(parameterList);

        lstExpected = new ArrayList<>();
        SpListReglaVariablePcVarRS objVariables = new SpListReglaVariablePcVarRS();
        objVariables.setParametername("sf1_pyme");
        objVariables.setParameterclass("String");
        lstExpected.add(objVariables);

        objVariables = new SpListReglaVariablePcVarRS();
        objVariables.setParametername("sf1_rating");
        objVariables.setParameterclass("String");
        lstExpected.add(objVariables);

        lstReal = new HashMap<>();
        lstReal.put("sf1_pyme","String");

        lstRealExt = new HashMap<>();
        lstRealExt.put("sf1_pyme","String");
        lstRealExt.put("sf1_rating","String");

        parameterListExt = new ArrayList<>();

        parameterExtOne = new Parameter();
        parameterExtOne.setParameterClass("String");
        parameterExtOne.setParameterName("sf1_pyme");
        parameterExtOne.setParameterValue("SI");
        parameterListExt.add(parameterExtOne);

        parameterExtTwo = new Parameter();
        parameterExtTwo.setParameterClass("Long");
        parameterExtTwo.setParameterName("age");
        parameterExtTwo.setParameterValue("15");
        parameterListExt.add(parameterExtTwo);

        parameterExtThree = new Parameter();
        parameterExtThree.setParameterClass("DateTime");
        parameterExtThree.setParameterName("born");
        parameterExtThree.setParameterValue("1979-04-04");
        parameterListExt.add(parameterExtThree);

        parametersMap = new HashMap<>();
        parametersMap.put("sf1_pyme","SI");
        parametersMap.put("age","15");
        parametersMap.put("born","1979-04-04");

    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSize() throws IOException {
        assertEquals(jsonReponse.getParameterList().size(), EngineHandler.readJsonFullFromString(json).getParameterList().size());
    }

    @Test
    public void shouldEqualsOne() {
        assertTrue( EngineHandler.isEqual(parameter,"sf1_pyme",""));
    }

    @Test
    public void shouldEqualsTwo() {
        assertTrue( EngineHandler.isEqual(parameter,"","sf1_pyme"));
    }

    @Test
    public void shouldEqualThree() {
        assertTrue( EngineHandler.isEqual(parameter,"sf1_pyme","sf1_pyme"));
    }

    @Test
    public void shouldEqualFour() {
        assertFalse( EngineHandler.isEqual(parameter,"",""));
    }

    @Test
    public void shouldEqualsBuidParametersValues() throws ParseException {
        assertEquals( parametersO, EngineHandler.buidParametersValues(parameterList));
    }

    @Test
    public void shouldEqualsBuidCompleteParametersValues() throws ParseException {
        assertNotEquals( parametersMap, EngineHandler.buidParametersValues(parameterListExt));
    }

    @Test
    public void shouldEqualsContainsParameters() {
        assertEquals( parameterList, EngineHandler.containsParameters(parameterList,"sf1_pyme",""));
    }

    @Test
    public void shouldEqualsContainsParameter() {
        assertEquals( parameter, EngineHandler.containsParameter(parameterList,"sf1_pyme"));
    }

    @Test
    public void shouldEqualsContainsCompleteParameter() {
        assertTrue( EngineHandler.containsParameters(parameterListExt,"sf1_pyme","age").size()>1);
    }

    @Test
    public void shouldNoContainsParameter() {
        assertNull( EngineHandler.containsParameter(parameterList,"dummy"));
    }

    @Test
    public void addAditionalParameter() throws ParseException {
        assertTrue(parameterAditionalList.size()>EngineHandler.createAditionalParameter(parameterAditionalList,"p5_fechaPep","p5_fechaVencMac","p5_diffMacPep").size());
    }

    @Test
    public void checkAllVariablesException() throws BusinessException {

        try {
            EngineHandler.checkAllVariables(lstExpected,lstReal);
        } catch (BusinessException e){
            assertNotNull(e);
        }
    }

    @Test
    public void checkAllVariablesOK() throws BusinessException {

        try {
            EngineHandler.checkAllVariables(lstExpected,lstRealExt);
        } catch (BusinessException e){
            assertNull(e);
        }
    }

    @Test
    public void shouldEqualsClobToString() throws Exception {
        Clob clob = mock(Clob.class);
        clob.setString(1,"lala");
        exception.expect(PlataformaBaseException.class);
        assertNull( EngineHandler.getStringSromClob(clob));

    }

    @Test
    public void shouldEqualsLogic() throws PlataformaBaseException {
        String in ="[{\"ref\":\"CE01\",\"descripcion\":\"comite\",\"atribucionLarga\":[{\"nombre\":\"nivel_pot\",\"min\":100,\"max\":200,\"opcional\":0}],\"atribucionCorta\":[]}]";
        assertNotNull(EngineHandler.businessLogicAtribution(in));
    }
}
