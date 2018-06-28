package cl.motoratrib.rest.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.List;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaIN;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaOUT;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListVariablesPcVariableRS;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.*;
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

    @Mock
    SpListVariablesDAO spListVariablesDAO;
    @Mock
    SpGetReglaDAO spGetReglaDAO;

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
    public void testGetVariablesDAOException() throws Exception {
        when(spListVariablesDAO.execute())
                .thenReturn(EngineFixture.variables());
        try {
            engineService.getVariables();
        }catch (Exception e) {
            assertNotNull(e);
        }
        Mockito.reset(spListVariablesDAO);
    }
    @Test
    public void testGetRuleByNameOK() throws Exception {
        when(spGetReglaDAO.execute(any(SpGetReglaIN.class)))
                .thenReturn(EngineFixture.reglas());
        SpGetReglaOUT rule = engineService.getRuleByName(RULE_NAME);
        assertNotNull(rule);
    }

}
