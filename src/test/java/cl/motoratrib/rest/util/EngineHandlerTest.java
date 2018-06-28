package cl.motoratrib.rest.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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

    Parameter parameter;

    List<Parameter> parameterList;

    Map<String, Object> parametersO;

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
        parameterList = new ArrayList<>();
        parameterList.add(parameter);


        jsonReponse = new InJson();
        jsonReponse.setRulesetName("POC_1_RulesetList");
        jsonReponse.setParameterList(parameterList);

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
    public void shouldEqualsContainsParameters() {
        assertEquals( parameterList, handler.containsParameters(parameterList,"sf1_pyme",""));
    }

    @Test
    public void shouldEqualsClobToString() throws Exception {
        CLOB clob = mock(CLOB.class);
        clob.setString(1,"lala");
        exception.expect(PlataformaBaseException.class);
        assertNull( handler.getStringSromClob(clob));
    }
}
