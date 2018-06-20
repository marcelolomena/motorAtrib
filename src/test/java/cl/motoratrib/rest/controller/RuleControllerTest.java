package cl.motoratrib.rest.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglasIN;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpListReglasOUT;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.*;
import cl.motoratrib.rest.context.TestContext;
import cl.motoratrib.rest.context.WebAppContext;
import cl.motoratrib.rest.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import cl.motoratrib.rest.context.TestUtil;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class, WebAppContext.class })
@TestExecutionListeners(listeners = { ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, WithSecurityContextTestExecutionListener.class })
@WebAppConfiguration
public class RuleControllerTest {
    MockMvc mockMvc;

    @Mock
    SpListReglasDAO spListReglasDAO;
    @Mock
    SpListVariablesDAO spListVariablesDAO;
    @Mock
    SpUpdateReglaDAO spUpdateReglaDAO;
    @Mock
    SpListReglaVariableDAO spListReglaVariableDAO;
    @Mock
    SpUpdateConjuntoReglaDAO spUpdateConjuntoReglaDAO;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    @InjectMocks
    private EngineService engineService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @SuppressWarnings("unchecked")

    public void testGetRuleOK() throws Exception {
        String id = "1";
        SpListReglasOUT salidaDaoListReglas = mock(SpListReglasOUT.class);
        when(spListReglasDAO.execute(any(SpListReglasIN.class))).thenReturn(salidaDaoListReglas);

        mockMvc.perform(get("/rules/{id}", id).contentType(TestUtil.APPLICATION_JSON_UTF8)
                .header("OAM_REMOTE_KEY", TestUtil.OAM_REMOTE_KEY)).andExpect(status().isOk()).andDo(print());

    }
}
