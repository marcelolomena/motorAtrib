package cl.motoratrib.rest.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.*;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.*;
import cl.bancochile.plataformabase.error.BusinessException;
import cl.motoratrib.rest.context.TestContext;
import cl.motoratrib.rest.context.WebAppContext;
import cl.motoratrib.rest.fixture.EngineFixture;
import cl.motoratrib.rest.service.EngineServiceImpl;
import cl.motoratrib.rest.util.EngineHandler;
import org.mockito.InjectMocks;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import cl.motoratrib.rest.context.TestUtil;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;

import java.util.List;
import java.util.Map;

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
    SpListVariablesDAO spListVariablesDAO;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    EngineServiceImpl engineService;

    @InjectMocks
    EngineHandler handler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testPageTest() throws Exception {

        this.mockMvc.perform(get("/test")).andExpect(status().isOk());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetVariablesOK() throws Exception {
        SpListVariablesOUT salidaDaoListVariables = mock(SpListVariablesOUT.class);
        when(spListVariablesDAO.execute()).thenReturn(salidaDaoListVariables);

        mockMvc.perform(get("/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .header("OAM_REMOTE_KEY", TestUtil.OAM_REMOTE_KEY)).andExpect(status().isOk()).andDo(print());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTestingNOK() throws Exception {
        when(engineService.getRuleVariable("POC_1_RulesetList"))
                .thenReturn(null);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/testing")
                        .header("OAM_REMOTE_KEY", TestUtil.OAM_REMOTE_KEY).contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(EngineFixture.JSON_TEST_1);

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .is5xxServerError())
                .andDo(MockMvcResultHandlers.print());

    }

}
