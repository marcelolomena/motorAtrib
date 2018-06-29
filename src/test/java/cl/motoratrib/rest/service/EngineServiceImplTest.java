package cl.motoratrib.rest.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.List;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.*;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.*;
import cl.bancochile.plataformabase.error.PlataformaBaseException;
import cl.motoratrib.rest.fixture.EngineFixture;
import cl.motoratrib.rest.context.TestContext;
import cl.motoratrib.rest.context.WebAppContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class, WebAppContext.class })
@TestExecutionListeners(listeners = { ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, WithSecurityContextTestExecutionListener.class })
@WebAppConfiguration
public class EngineServiceImplTest {
    MockMvc mockMvc;

    @InjectMocks
    EngineServiceImpl engineService;

    private static final String RULE_NAME = "sf1_pyme";
    private static final String RULESET_NAME = "POC_1_RulesetList";

    @Mock
    SpListVariablesDAO spListVariablesDAO;
    @Mock
    SpGetReglaDAO spGetReglaDAO;
    @Mock
    SpListReglaVariableDAO spListReglaVariableDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetVariablesOK() throws Exception {
        when(spListVariablesDAO.execute())
                .thenReturn(EngineFixture.variables());
        List<SpListVariablesPcVariableRS> var = engineService.getVariables();
        assertNotNull(var);
    }

    @Test
    public void testGetRuleVariableOK() throws Exception {
        when(spListReglaVariableDAO.execute(any(SpListReglaVariableIN.class))).thenReturn(EngineFixture.getruleset());

        List<SpListReglaVariablePcVarRS> ruleset = engineService.getRuleVariable(RULESET_NAME);
        assertNotNull(ruleset);
    }

    @Test
    public void testGetRuleByNameOK() throws Exception {
        when(spGetReglaDAO.execute(any(SpGetReglaIN.class)))
                .thenReturn(EngineFixture.getrule());
        SpGetReglaOUT rule = engineService.getRuleByName(RULE_NAME);
        assertNotNull(rule);
    }

    @Test
    public void testGetVariablesDAOException() throws Exception {
        when(spListVariablesDAO.execute())
                .thenThrow(PlataformaBaseException.class);
        try {
            engineService.getVariables();
        }catch (Exception e) {
            assertNotNull(e);
        }
        Mockito.reset(spListVariablesDAO);
    }

    @Test
    public void testGetRuleVariableDAOException() throws Exception {
        when(spListReglaVariableDAO.execute(any(SpListReglaVariableIN.class)))
                .thenThrow(PlataformaBaseException.class);
        try {
            engineService.getRuleVariable(RULESET_NAME);
        }catch (Exception e) {
            assertNotNull(e);
        }
        Mockito.reset(spListReglaVariableDAO);
    }

    @Test
    public void testGetRuleByNameDAOException() throws Exception {
        when(spGetReglaDAO.execute(any(SpGetReglaIN.class)))
                .thenThrow(PlataformaBaseException.class);
        try {
            engineService.getRuleByName(RULE_NAME);
        }catch (Exception e) {
            assertNotNull(e);
        }
        Mockito.reset(spGetReglaDAO);
    }

}
