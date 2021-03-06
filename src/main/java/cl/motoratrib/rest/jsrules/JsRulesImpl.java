package cl.motoratrib.rest.jsrules;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaOUT;
import cl.motoratrib.rest.service.EngineService;
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
@SuppressWarnings({"rawtypes","unchecked"})
@Component
public class JsRulesImpl implements JsRules {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsRulesImpl.class);
    private static final String JSONEXT = ".json";
    @Autowired
    EngineService engineService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RuleLoader ruleLoader = new RuleLoaderImpl();
    private final RulesetLoader rulesetLoader = new RulesetLoaderImpl(this);

    // default cache values
    private static final int CACHE_SIZE = 25;
    private static final long TIME_TO_LIVE = (long)15 * 60 * 1000;

    // these maps provide rudimentary caching
    private final Map<String, Rule> ruleMap = new CacheMap<>(CACHE_SIZE, TIME_TO_LIVE);
    private final Map<String, RulesetExecutor> rulesetExecutorMap = new CacheMap<>(CACHE_SIZE, TIME_TO_LIVE);

    @Override
    public Rule loadRuleByJson(String json) throws InvalidConfigException {
        try {
            RuleConfig ruleConfig = objectMapper.readValue(json, RuleConfig.class);
            return getRule(ruleConfig);
        } catch (IOException ex) {
            throw new InvalidConfigException("Unable to parse json: " + json, ex);
        }
    }
    @Override
    public Rule loadRuleByNameFromFile(String ruleName) throws InvalidConfigException {
        Rule rule = ruleMap.get(ruleName);

        if (rule == null) {
            String fileName = ruleName + JSONEXT;

            InputStream stream = getFileFromClasspath(fileName);

            if (stream == null) {
                throw new InvalidConfigException("Unable to find rule file: " + fileName);
            }

            try {
                RuleConfig ruleConfig = objectMapper.readValue(stream, RuleConfig.class);
                rule = getRule(ruleConfig);
            } catch (IOException ex) {
                throw new InvalidConfigException("Unable to parse rule file: " + ruleName, ex);
            }
        }

        return rule;
    }
    @Override
    public Rule loadRuleByName(String ruleName) throws InvalidConfigException {
        Rule rule = ruleMap.get(ruleName);
        System.out.println("LOAD RULE : " + ruleName);
            if (rule == null) {

                try {

                    InputStream stream = getRecordFromDatabase(ruleName);
                    if (stream == null) {
                        String fileName = ruleName + JSONEXT;
                        stream = getFileFromClasspath(fileName);
                    }
                    if (stream == null) {
                        throw new InvalidConfigException("Unable to find ruleset record : " + ruleName);
                    }
                    //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                    //System.out.println( convertStreamToString(stream) );
                    //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                    RuleConfig ruleConfig = objectMapper.readValue(stream, RuleConfig.class);
                    rule = getRule(ruleConfig);
                } catch (IOException ex) {
                    throw new InvalidConfigException("Unable to parse rule record rule : " + ruleName, ex);
                } catch (Exception ex) {
                    throw new InvalidConfigException("Unable to parse rule record rule : " + ruleName, ex);
                }
            }


        return rule;
    }
    @Override
    public RulesetExecutor loadRulesetByJson(String json) throws InvalidConfigException {
        try {
            RulesetConfig rulesetConfig = objectMapper.readValue(json, RulesetConfig.class);
            return getRulesetExecutor(rulesetConfig);
        } catch (IOException ex) {
            throw new InvalidConfigException("Unable to parse json: " + json, ex);
        }
    }
    @Override
    public RulesetExecutor loadRulesetByName(String rulesetName) throws InvalidConfigException {
        RulesetExecutor ruleset = rulesetExecutorMap.get(rulesetName);
        System.out.println("LOAD RULESET : " + rulesetName);
            if (ruleset == null) {

                try {
                    InputStream stream = getRecordFromDatabase(rulesetName);
                    if (stream == null) {
                        String fileName = rulesetName + JSONEXT;
                        stream = getFileFromClasspath(fileName);
                    }
                    if (stream == null) {
                        throw new InvalidConfigException("Unable to find ruleset record : " + rulesetName);
                    }
                    //System.out.println("################################");
                    //System.out.println( convertStreamToString(stream) );
                    //System.out.println("################################");
                    RulesetConfig rulesetConfig = objectMapper.readValue(stream, RulesetConfig.class);
                    ruleset = getRulesetExecutor(rulesetConfig);
                } catch (IOException ex) {
                    throw new InvalidConfigException("Unable to parse ruleset record: " + rulesetName, ex);
                }  catch (Exception ex) {
                    throw new InvalidConfigException("Unable to parse ruleset record: " + rulesetName, ex);
                }
            }

        return ruleset;
    }

    @Override
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

    private InputStream getRecordFromDatabase(String name)  {
        InputStream is;
        try {
            SpGetReglaOUT spOut = this.engineService.getRuleByName(name);
            OracleClob oc = spOut.getPJson();
            is = oc.getAsciiStream();
        } catch (Exception e){
            LOGGER.error("Mensaje de error descriptivo.", e);
            return null;
        }

        return is;
    }
    private InputStream getFileFromClasspath(String fileName) {
        return this.getClass().getResourceAsStream("/" + fileName);
    }

    public EngineService getServiceEngine(){
        return this.engineService;
    }

    public RulesetLoader getRulesetLoader(){
        return this.rulesetLoader;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
