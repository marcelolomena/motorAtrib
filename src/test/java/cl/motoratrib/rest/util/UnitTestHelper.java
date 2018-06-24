package cl.motoratrib.rest.util;

import cl.motoratrib.rest.domain.GridRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitTestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitTestHelper.class);
    public UnitTestHelper() {
        super();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static GridRule createGridRuleUpdate(){
        GridRule testGridRule = new GridRule();
        testGridRule.setId("1");
        testGridRule.setOper("edit");
        testGridRule.setJson("{\n" +
                "  \"rulesetName\": \"POC_1_RulesetList\",\n" +
                "  \"rulesetType\": \"ChainTrueList\",\n" +
                "  \"responseConfig\": {\n" +
                "    \"response\": \"Funca\",\n" +
                "    \"responseClass\": \"String\"\n" +
                "  },\n" +
                "  \"components\": [\n" +
                "    \"POC_0_Ruleset\",\n" +
                "\t\"POC_1_Ruleset\"\n" +
                "  ]\n" +
                "}");

        return testGridRule;

    }
}
