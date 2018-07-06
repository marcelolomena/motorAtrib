package cl.motoratrib.rest.jsrules.integration;

import cl.motoratrib.rest.jsrules.JsRulesImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

/**
 * This integration test executes a simply ruleset that evaluates whether an inventory item is in stock at a particular
 * store.
 * <p/>
 * There are two rules in the set:
 * <p/>
 * 1. Evaluate that the store is one that carries the item
 * 2. Evaluate that the current inventory of the item is greater than zero.
 * <p/>
 * If both rules evaluate to true, the ruleset will return the text "Item is in stock"
 * <p/>
 * Stores with the item in stock: 1, 2, 3, 5, 7
 * <p/>
 * Files:
 * InventoryRuleset.json
 * CarriesItem.json
 * HasInventory.json
 * <p/>
 * Created by Marcelo Lomeña 5/16/2018
 */
@RunWith(MockitoJUnitRunner.class)
public class ChainTrueIntegrationTest {
    private final String success = "[\"{ref:\\\"SF01\\\", alerta:\\\"Invocar FL03 Flujo Rating Agricola\\\"}\"]";
    private final String name = "POC_1_RulesetList";
    private final String type = "CHAINTRUELIST";
    @InjectMocks
    private JsRulesImpl jsRules;

    Map<String, Object> parameters;

    @Before
    public void beforeEach() {

        parameters = new HashMap<>();
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * This test looks to see if the item is in stock at store 3
     * <p/>
     * Store 3 carries the item and has 8 in inventory, so it should return the success message
     */
    @Test
    public void AllRulesTrueTest() throws Exception {
        parameters.put("sf1_pyme", "SI");
        parameters.put("sf1_rating", "SI");
        parameters.put("sf1_tipoRating", "AGRICOLA");
        parameters.put("sf1_privada", "SI");

        assertEquals(success, jsRules.executeRuleset("POC_1_RulesetList", parameters));
        assertEquals(name, jsRules.loadRulesetByName(name).getName());
        assertEquals(type,jsRules.loadRulesetByName(name).getType());
    }
}
