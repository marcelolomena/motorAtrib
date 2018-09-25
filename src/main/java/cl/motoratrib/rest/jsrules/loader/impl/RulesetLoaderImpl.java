package cl.motoratrib.rest.jsrules.loader.impl;

import cl.motoratrib.rest.jsrules.*;
import cl.motoratrib.rest.jsrules.JsRulesImpl;
import cl.motoratrib.rest.jsrules.config.ResponseConfig;
import cl.motoratrib.rest.jsrules.config.RulesetConfig;
import cl.motoratrib.rest.jsrules.exception.ClassHandlerException;
import cl.motoratrib.rest.jsrules.exception.InvalidConfigException;
import cl.motoratrib.rest.jsrules.impl.RuleExecutorImpl;
import cl.motoratrib.rest.jsrules.loader.RulesetLoader;
import cl.motoratrib.rest.jsrules.util.ClassHandler;
import cl.motoratrib.rest.jsrules.util.RulesetTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcelo Lomeña 2018/04/06
 */
@SuppressWarnings("rawtypes")
public class RulesetLoaderImpl implements RulesetLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(RulesetLoaderImpl.class);
    private JsRulesImpl jsRulesImpl;

    public RulesetLoaderImpl(JsRulesImpl jsRulesImpl) {
        this.jsRulesImpl = jsRulesImpl;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RulesetExecutor load(RulesetConfig config) throws InvalidConfigException {
        String type;
        if (config.getRulesetType() != null) {
            type = config.getRulesetType().toUpperCase();
        } else {
            throw new InvalidConfigException("Ruleset Type must be provided");
        }

        RulesetTypeHandler rulesetTypeHandler = RulesetTypeHandler.valueOf(type);

        ResponseConfig responseConfig = config.getResponseConfig();
        ClassHandler classHandler;
        try {
            classHandler = ClassHandler.valueOf(responseConfig.getResponseClass().toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new InvalidConfigException("Unable to parse response " + e.getMessage(), e);
        }
        Object response;
        String responseString = responseConfig.getResponse();
        try {
            response = classHandler.convertString(responseString);
        } catch (ClassHandlerException ex) {
            LOGGER.error(ex.getMessage());
            throw new InvalidConfigException("Unable to parse response " + responseString, ex);
        }

        List<Executor> ruleSet = new ArrayList<>();
        List<String> components = config.getComponents();
        for (String component : components) {
            if (rulesetTypeHandler.isRulesetListExecutor()) {
                RulesetExecutor rulesetExecutor = jsRulesImpl.loadRulesetByName(component);
                ruleSet.add(rulesetExecutor);
            } else {
                Rule rule = jsRulesImpl.loadRuleByName(component);
                RuleExecutor ruleExecutor = new RuleExecutorImpl(rule,jsRulesImpl.getServiceEngine());
                ruleSet.add(ruleExecutor);
            }
        }

        String name = config.getRulesetName();

        return rulesetTypeHandler.getRulesetExecutor(name, type, ruleSet, response);
    }
}
