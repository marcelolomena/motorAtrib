package cl.motoratrib.rest.jsrules;

import cl.motoratrib.rest.jsrules.exception.InvalidConfigException;
import cl.motoratrib.rest.jsrules.exception.JsRulesException;

import java.util.Map;
@SuppressWarnings("rawtypes")
public interface JsRules {
    <T> T executeRuleset(String rulesetName, Map<String, Object> parameters) throws JsRulesException;
    Rule loadRuleByJson(String json) throws InvalidConfigException;
    Rule loadRuleByName(String ruleName) throws InvalidConfigException;
    RulesetExecutor loadRulesetByJson(String json) throws InvalidConfigException;
    RulesetExecutor loadRulesetByName(String rulesetName) throws InvalidConfigException;
    Rule loadRuleByNameFromFile(String ruleName) throws InvalidConfigException;
 }
