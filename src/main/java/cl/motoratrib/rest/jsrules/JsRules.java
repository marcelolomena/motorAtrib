package cl.motoratrib.rest.jsrules;

import cl.motoratrib.rest.jsrules.exception.JsRulesException;

import java.util.Map;

public interface JsRules {
    <T> T executeRuleset(String rulesetName, Map<String, Object> parameters) throws JsRulesException;
}
