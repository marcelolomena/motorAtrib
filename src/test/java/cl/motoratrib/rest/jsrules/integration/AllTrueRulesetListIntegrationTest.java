package cl.motoratrib.rest.jsrules.integration;

import cl.motoratrib.rest.jsrules.JsRules;
import cl.motoratrib.rest.jsrules.JsRulesImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * This integration test executes a simple ruleset list that evaluates whether a customer can afford something, and if
 * they can, evaluates that that inventory item is in stock at a particular store.
 * <p/>
 * There are two rulesets in the list:
 * <p/>
 * 1. Evaluate that the customer can afford something in that store
 * 2. Evaluate that the current inventory at that store is greater than zero.
 * <p/>
 * If both rulesets evaluate to true, the ruleset will return the text "Customer can purchase something"
 * <p/>
 * Stores with the item in stock: 1, 2, 3, 5, 7
 * Trouser cost: $200.00
 * <p/>
 * Files:
 * CanPurchaseRulesetList.json
 * InBudgetRuleset.json
 * InventoryRuleset.json
 * <p/>
 * Created by Marcelo Lomeña 5/16/2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class AllTrueRulesetListIntegrationTest {

    Map<String, Object> parameters;

    @InjectMocks
    private JsRulesImpl jsRules;

    @Before
    public void beforeEach() {

        parameters = new HashMap<>();
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    /**
     * In this test, the customer has $250.00 and is shopping in store 3
     * <p/>
     * With $250.00, the customer can afford trousers, and store 3 carries that item and has 8 in inventory, so it
     * should return the success message
     */
    @Test
    public void AllRulesTrueTest() throws Exception {
        String success = "Customer can purchase something";

        parameters.put("budget", 250.00);
        parameters.put("store", 3l);
        parameters.put("inventory", 8l);

        assertEquals(success, jsRules.executeRuleset("CanPurchaseRulesetList", parameters));
    }

    /**
     * In this test, the customer is still shopping at store 3 but only has $50.00
     * <p/>
     * Even though the item is in stock, the customer can not affort it so it should return null (false)
     */
    @Test
    public void FirstRulesetFalseTest() throws Exception {
        parameters.put("budget", 50.00);
        parameters.put("store", 3l);
        parameters.put("inventory", 8l);

        assertNull(jsRules.executeRuleset("CanPurchaseRulesetList", parameters));
    }

    /**
     * In this test, the customer has $150.00 but is shopping at store 4.
     * <p/>
     * The customer can afford a shirt, but Store 4 does not stock the item, so it should return null (false)
     */
    @Test
    public void SecondRulesetFalseTest() throws Exception {
        parameters.put("budget", 150.00);
        parameters.put("store", 4l);
        parameters.put("inventory", 8l);

        assertNull(jsRules.executeRuleset("CanPurchaseRulesetList", parameters));
    }
}
