package cl.motoratrib.rest.jsrules.integration;

import cl.motoratrib.rest.jsrules.JsRulesImpl;
import cl.motoratrib.rest.jsrules.Rule;
import cl.motoratrib.rest.jsrules.RuleExecutor;
import cl.motoratrib.rest.jsrules.impl.RuleExecutorImpl;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Marcelo Lomeña 5/20/2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class DateIntegrationTest {

    @org.junit.Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private JsRulesImpl jsRules;

    @Before
    public void beforeEach() {

    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @SuppressWarnings("unchecked")
    public void betweenDatesTest() throws Exception {
        Rule rule = jsRules.loadRuleByNameFromFile("BetweenDatesRule");

        RuleExecutor ruleExecutor = new RuleExecutorImpl(rule);

        DateTime dateTime = DateTime.parse("2018-05-20");

        assertEquals(true, ruleExecutor.execute(dateTime));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void betweenDatesFalseTest() throws Exception {
        Rule rule = jsRules.loadRuleByNameFromFile("BetweenDatesRule");

        RuleExecutor ruleExecutor = new RuleExecutorImpl(rule);

        DateTime dateTime = DateTime.parse("2014-05-20");

        assertNull(ruleExecutor.execute(dateTime));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void beforeDateTest() throws Exception {
        Rule rule = jsRules.loadRuleByNameFromFile("BeforeDateRule");

        RuleExecutor ruleExecutor = new RuleExecutorImpl(rule);

        DateTime dateTime = DateTime.parse("2014-05-20");

        assertEquals(true, ruleExecutor.execute(dateTime));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void beforeDateFalseTest() throws Exception {
        Rule rule = jsRules.loadRuleByNameFromFile("BeforeDateRule");

        RuleExecutor ruleExecutor = new RuleExecutorImpl(rule);

        DateTime dateTime = DateTime.parse("2015-05-20");

        assertEquals(true,ruleExecutor.execute(dateTime));
    }
}
