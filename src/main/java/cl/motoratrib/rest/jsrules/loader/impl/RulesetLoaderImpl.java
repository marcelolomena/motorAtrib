package cl.motoratrib.rest.jsrules.loader.impl;


import cl.motoratrib.rest.jsrules.Rule;
import cl.motoratrib.rest.jsrules.Executor;
import cl.motoratrib.rest.jsrules.JsRules;
import cl.motoratrib.rest.jsrules.RuleExecutor;
import cl.motoratrib.rest.jsrules.RulesetExecutor;
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
public class RulesetLoaderImpl implements RulesetLoader {
    private final static Logger LOGGER = LoggerFactory.getLogger(RulesetLoaderImpl.class);
    private JsRules jsRules;

    public RulesetLoaderImpl(JsRules jsRules) {
        this.jsRules = jsRules;
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
        //LOGGER.debug("tipo -------->" + type);
        RulesetTypeHandler rulesetTypeHandler = RulesetTypeHandler.valueOf(type);

        ResponseConfig responseConfig = config.getResponseConfig();
        ClassHandler classHandler;
        try {
            classHandler = ClassHandler.valueOf(responseConfig.getResponseClass().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidConfigException("Invalid response class: " + responseConfig.getResponseClass());
        }
        Object response;
        String responseString = responseConfig.getResponse();
        try {
            response = classHandler.convertString(responseString);
        } catch (ClassHandlerException ex) {
            throw new InvalidConfigException("Unable to parse response " + responseString, ex);
        }

        List<Executor> ruleSet = new ArrayList<>();
        List<String> components = config.getComponents();
        for (String component : components) {
            if (rulesetTypeHandler.isRulesetListExecutor()) {
                RulesetExecutor rulesetExecutor = jsRules.loadRulesetByName(component);
                ruleSet.add(rulesetExecutor);
            } else {
                Rule rule = jsRules.loadRuleByName(component);
                RuleExecutor ruleExecutor = new RuleExecutorImpl(rule);
                ruleSet.add(ruleExecutor);
            }
        }

        String name = config.getRulesetName();

        return rulesetTypeHandler.getRulesetExecutor(name, type, ruleSet, response);
    }
}
