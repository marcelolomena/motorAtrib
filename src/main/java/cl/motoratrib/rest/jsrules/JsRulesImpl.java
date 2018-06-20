package cl.motoratrib.rest.jsrules;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaOUT;
import cl.motoratrib.rest.jsrules.service.RuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import cl.motoratrib.rest.jsrules.config.RuleConfig;
import cl.motoratrib.rest.jsrules.config.RulesetConfig;
import cl.motoratrib.rest.jsrules.exception.InvalidConfigException;
import cl.motoratrib.rest.jsrules.exception.JsRulesException;
import cl.motoratrib.rest.jsrules.loader.RuleLoader;
import cl.motoratrib.rest.jsrules.loader.RulesetLoader;
import cl.motoratrib.rest.jsrules.loader.impl.RuleLoaderImpl;
import cl.motoratrib.rest.jsrules.loader.impl.RulesetLoaderImpl;
import cl.motoratrib.rest.tools.CacheMap;
import oracle.jdbc.OracleClob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * Created by Marcelo Lomeña 5/13/2018
 */
@Component
public class JsRulesImpl implements JsRules {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsRulesImpl.class);

    @Autowired
    RuleService ruleService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RuleLoader ruleLoader = new RuleLoaderImpl();
    private final RulesetLoader rulesetLoader = new RulesetLoaderImpl(this);

    // default cache values
    private static final int CACHE_SIZE = 25;
    private static final long TIME_TO_LIVE = (long)15 * 60 * 1000;

    // these maps provide rudimentary caching
    private final Map<String, Rule> ruleMap = new CacheMap<>(CACHE_SIZE, TIME_TO_LIVE);
    private final Map<String, RulesetExecutor> rulesetExecutorMap = new CacheMap<>(CACHE_SIZE, TIME_TO_LIVE);

    public Rule loadRuleByJson(String json) throws InvalidConfigException {
        try {
            RuleConfig ruleConfig = objectMapper.readValue(json, RuleConfig.class);
            return getRule(ruleConfig);
        } catch (IOException ex) {
            throw new InvalidConfigException("Unable to parse json: " + json, ex);
        }
    }

    public Rule loadRuleByName(String ruleName) throws InvalidConfigException {
        Rule rule = ruleMap.get(ruleName);

            if (rule == null) {

                InputStream stream = getRecordFromDatabase(ruleName);

                if (stream == null) {
                    throw new InvalidConfigException("Unable to find rule in table record : " + ruleName);
                }

                try {
                    RuleConfig ruleConfig = objectMapper.readValue(stream, RuleConfig.class);
                    rule = getRule(ruleConfig);
                } catch (IOException ex) {
                    throw new InvalidConfigException("Unable to parse rule record : " + ruleName, ex);
                }
            }


        return rule;
    }

    public RulesetExecutor loadRulesetByJson(String json) throws InvalidConfigException {
        try {
            RulesetConfig rulesetConfig = objectMapper.readValue(json, RulesetConfig.class);
            return getRulesetExecutor(rulesetConfig);
        } catch (IOException ex) {
            throw new InvalidConfigException("Unable to parse json: " + json, ex);
        }
    }

    public RulesetExecutor loadRulesetByName(String rulesetName) throws InvalidConfigException {
        RulesetExecutor ruleset = rulesetExecutorMap.get(rulesetName);

            if (ruleset == null) {
                InputStream stream = getRecordFromDatabase(rulesetName);

                if (stream == null) {
                    throw new InvalidConfigException("Unable to find ruleset record : " + rulesetName);
                }

                try {
                    RulesetConfig rulesetConfig = objectMapper.readValue(stream, RulesetConfig.class);
                    ruleset = getRulesetExecutor(rulesetConfig);
                } catch (IOException ex) {
                    throw new InvalidConfigException("Unable to parse ruleset record: " + rulesetName, ex);
                }
            }

        return ruleset;
    }

    public <T> T executeRuleset(String rulesetName, Map<String, Object> parameters) throws JsRulesException {
        RulesetExecutor<T> executor = loadRulesetByName(rulesetName);

        return executor.execute(parameters);
    }

    private Rule getRule(RuleConfig ruleConfig) throws InvalidConfigException {
        String ruleName = ruleConfig.getRuleName();
        Rule rule = ruleMap.get(ruleName);
        if (rule == null) {
            rule = ruleLoader.load(ruleConfig);
            ruleMap.put(ruleName, rule);
        }
        return rule;
    }

    private RulesetExecutor getRulesetExecutor(RulesetConfig rulesetConfig) throws InvalidConfigException {
        String rulesetName = rulesetConfig.getRulesetName();
        RulesetExecutor ruleset = rulesetExecutorMap.get(rulesetName);
        if (ruleset == null) {
            ruleset = rulesetLoader.load(rulesetConfig);
            rulesetExecutorMap.put(rulesetName, ruleset);
        }
        return ruleset;
    }

    private InputStream getFileFromClasspath(String fileName) {
        return this.getClass().getResourceAsStream("/" + fileName);
    }

    private InputStream getRecordFromDatabase(String name) {
        InputStream is = null;

        try {
            if(ruleService == null) throw new Exception("service is null");
            LOGGER.debug("OK ruleService");

            SpGetReglaOUT spOut = this.ruleService.getRuleByName(name);

            if(spOut == null) throw new Exception("spOut is null");

            OracleClob oc = spOut.getPJson();

            if(oc == null) throw new Exception("oc is null");
            LOGGER.debug("OK oc");

            is=oc.getAsciiStream();
            if(is == null) throw new Exception("is is null");


        }catch(Exception e){
            LOGGER.error("#=========================================== " + e.getMessage() + " ===========================================#");
        }

        return is;
    }

}
