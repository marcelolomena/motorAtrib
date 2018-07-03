package cl.motoratrib.rest.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglaVariablePcVarRS;
import cl.bancochile.plataformabase.error.BusinessException;
import cl.bancochile.plataformabase.error.PlataformaBaseException;
import cl.motoratrib.rest.domain.InJson;
import cl.motoratrib.rest.domain.Parameter;
import oracle.sql.CLOB;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @InjectMocks
    EngineHandler handler;

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
        assertEquals(jsonReponse.getParameterList().size(), handler.readJsonFullFromString(json).getParameterList().size());
    }

    @Test
    public void shouldEqualsOne() {
        assertTrue( handler.isEqual(parameter,"sf1_pyme",""));
    }

    @Test
    public void shouldEqualsTwo() {
        assertTrue( handler.isEqual(parameter,"","sf1_pyme"));
    }

    @Test
    public void shouldEqualThree() {
        assertTrue( handler.isEqual(parameter,"sf1_pyme","sf1_pyme"));
    }

    @Test
    public void shouldEqualFour() {
        assertFalse( handler.isEqual(parameter,"",""));
    }

    @Test
    public void shouldEqualsBuidParametersValues() {
        assertEquals( parametersO, handler.buidParametersValues(parameterList));
    }

    @Test
    public void shouldEqualsBuidCompleteParametersValues() {
        assertNotEquals( parametersMap, handler.buidParametersValues(parameterListExt));
    }

    @Test
    public void shouldEqualsContainsParameters() {
        assertEquals( parameterList, handler.containsParameters(parameterList,"sf1_pyme",""));
    }

    @Test
    public void shouldEqualsContainsParameter() {
        assertEquals( parameter, handler.containsParameter(parameterList,"sf1_pyme"));
    }

    @Test
    public void shouldEqualsContainsCompleteParameter() {
        assertTrue( handler.containsParameters(parameterListExt,"sf1_pyme","age").size()>1);
    }

    @Test
    public void shouldNoContainsParameter() {
        assertNull( handler.containsParameter(parameterList,"dummy"));
    }

    @Test
    public void addAditionalParameter() {
        assertTrue(parameterAditionalList.size()>handler.createAditionalParameter(parameterAditionalList,"p5_fechaPep","p5_fechaVencMac","p5_diffMacPep").size());
    }

    @Test
    public void checkAllVariablesException() throws BusinessException {

        try {
            handler.checkAllVariables(lstExpected,lstReal);
        } catch (BusinessException e){
            assertNotNull(e);
        }
    }

    @Test
    public void checkAllVariablesOK() throws BusinessException {

        try {
            handler.checkAllVariables(lstExpected,lstRealExt);
        } catch (BusinessException e){
            assertNull(e);
        }
    }

    @Test
    public void shouldEqualsClobToString() throws Exception {
        CLOB clob = mock(CLOB.class);
        clob.setString(1,"lala");
        exception.expect(PlataformaBaseException.class);
        assertNull( handler.getStringSromClob(clob));
    }
}
