package cl.motoratrib.rest.jsrules;

import com.fasterxml.jackson.databind.ObjectMapper;
import cl.motoratrib.rest.jsrules.config.ResponseConfig;
import cl.motoratrib.rest.jsrules.config.RulesetConfig;
import cl.motoratrib.rest.jsrules.exception.InvalidConfigException;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Marcelo Lomeña 5/13/2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class JsRulesTest {
    @org.junit.Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private JsRulesImpl jsrules;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadJsonString() throws Exception {
        String ruleName = "mock rule";

        String json = "{" +
                "\"ruleName\": \"" + ruleName + "\"," +
                "\"leftParamConfig\": {" +
                "\"parameterName\": \"left\"," +
                "\"parameterClass\": \"Long\"" +
                "}," +
                "\"operator\": \"GT\"," +
                "\"rightParamConfig\": {" +
                "\"parameterName\": \"right\"," +
                "\"parameterClass\": \"Long\"," +
                "\"parameterStaticValue\": \"10\"" +
                "}," +
                "\"responseConfig\": {" +
                "\"response\": \"true\"," +
                "\"responseClass\": \"Boolean\"" +
                "}" +
                "}";

        Rule rule = jsrules.loadRuleByJson(json);

        assertEquals(ruleName, rule.getRuleName());
    }

    @Test
    public void testLoadJsonInvalid() throws Exception {
        exception.expect(InvalidConfigException.class);

        jsrules.loadRuleByJson("{ bad json");
    }

    @Test
    public void testLoadRuleByName() throws Exception {
        String ruleName = "GreaterThan10";

        Rule rule = jsrules.loadRuleByNameFromFile(ruleName);

        assertEquals(ruleName, rule.getRuleName());
    }

    @Test
    public void testLoadRuleByNameFileMissing() throws Exception {
        exception.expect(InvalidConfigException.class);

        String ruleName = "BogusRuleName";

        jsrules.loadRuleByName(ruleName);
    }

    @Test
    public void testLoadRuleByNameIOError() throws Exception {
        exception.expect(InvalidConfigException.class);

        String ruleName = "EmptyFile";

        jsrules.loadRuleByName(ruleName);
    }

    @Test
    public void testLoadRulesetJson() throws Exception {
        String rulesetName = "mockRuleset";

        RulesetConfig rulesetConfig = new RulesetConfig(rulesetName, "ALLTRUE", new ResponseConfig("true", "Boolean"),
                new ArrayList<String>());

        String json = objectMapper.writeValueAsString(rulesetConfig);

        RulesetExecutor rulesetExecutor = jsrules.loadRulesetByJson(json);

        assertEquals(rulesetName, rulesetExecutor.getName());
    }

    @Test
    public void testLoadRulesetJsonInvalid() throws Exception {
        exception.expect(InvalidConfigException.class);

        jsrules.loadRulesetByJson("{ bad json");
    }

    @Test
    public void testLoadRulesetByName() throws Exception {
        String rulesetName = "MockRuleset";

        RulesetExecutor ruleSet = jsrules.loadRulesetByName(rulesetName);

        assertEquals(rulesetName, ruleSet.getName());
    }

    @Test
    public void testLoadRulesetByNameFileMissing() throws Exception {
        exception.expect(InvalidConfigException.class);

        String rulesetName = "BogusRuleName";

        jsrules.loadRulesetByName(rulesetName);
    }

    @Test
    public void testLoadRulesetByNameIOError() throws Exception {
        exception.expect(InvalidConfigException.class);

        String rulesetName = "EmptyFile";

        jsrules.loadRulesetByName(rulesetName);
    }

    @Test
    public void testExecuteRulesetByName() throws Exception {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("left", 15l);

        assertNotNull(jsrules.executeRuleset("MockRuleset", parameters));
    }


}
