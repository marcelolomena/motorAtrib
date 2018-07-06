package cl.motoratrib.rest.jsrules.integration;

import cl.motoratrib.rest.jsrules.JsRulesImpl;
import cl.motoratrib.rest.jsrules.Rule;
import cl.motoratrib.rest.jsrules.RuleExecutor;
import cl.motoratrib.rest.jsrules.impl.RuleExecutorImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Marcelo Lomeña 5/20/2018
 */
@SuppressWarnings("rawtypes")
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
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = formatDate.parse("2018-05-20");
        Timestamp timestamp = new Timestamp(dateTime.getTime());
        assertEquals(true, ruleExecutor.execute(timestamp));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void betweenDatesFalseTest() throws Exception {
        Rule rule = jsRules.loadRuleByNameFromFile("BetweenDatesRule");

        RuleExecutor ruleExecutor = new RuleExecutorImpl(rule);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = formatDate.parse("2014-05-20");
        Timestamp timestamp = new Timestamp(dateTime.getTime());
        assertNull(ruleExecutor.execute(timestamp));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void beforeDateTest() throws Exception {
        Rule rule = jsRules.loadRuleByNameFromFile("BeforeDateRule");

        RuleExecutor ruleExecutor = new RuleExecutorImpl(rule);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = formatDate.parse("2014-05-20");
        Timestamp timestamp = new Timestamp(dateTime.getTime());
        assertEquals(true, ruleExecutor.execute(timestamp));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void beforeDateFalseTest() throws Exception {
        Rule rule = jsRules.loadRuleByNameFromFile("BeforeDateRule");

        RuleExecutor ruleExecutor = new RuleExecutorImpl(rule);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTime = formatDate.parse("2015-05-20");
        Timestamp timestamp = new Timestamp(dateTime.getTime());
        assertEquals(true,ruleExecutor.execute(timestamp));
    }
}
