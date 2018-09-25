package cl.motoratrib.rest.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.*;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.*;
import cl.bancochile.plataformabase.error.PlataformaBaseException;
import cl.motoratrib.rest.context.TestContext;
import cl.motoratrib.rest.context.WebAppContext;
import cl.motoratrib.rest.fixture.EngineFixture;
import cl.motoratrib.rest.service.EngineService;
import cl.motoratrib.rest.service.EngineServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;

import java.util.Arrays;
import java.util.List;

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

    //@Mock
    //EngineServiceImpl engineService;

    @Autowired
    EngineService engineService;


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
    public void testAttributionsNOK() throws Exception {

        when(engineService.getRuleVariable(isA(String.class))).thenThrow(PlataformaBaseException.class);

        MockHttpServletRequestBuilder builder =
                post("/getAttributions")
                        .header("OAM_REMOTE_KEY", TestUtil.OAM_REMOTE_KEY)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(EngineFixture.JSON_TEST_1);

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                .is5xxServerError())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAttributionsOK() throws Exception {

        SpListReglaVariablePcVarRS obj1 = new SpListReglaVariablePcVarRS();
        obj1.setParametername("sf1_pyme");
        obj1.setParameterclass("String");

        SpListReglaVariablePcVarRS obj2 = new SpListReglaVariablePcVarRS();
        obj2.setParametername("sf1_rating");
        obj2.setParameterclass("String");

        SpListReglaVariablePcVarRS obj3 = new SpListReglaVariablePcVarRS();
        obj3.setParametername("sf1_tipoRating");
        obj3.setParameterclass("String");

        SpListReglaVariablePcVarRS obj4 = new SpListReglaVariablePcVarRS();
        obj4.setParametername("sf1_privada");
        obj4.setParameterclass("String");
        List<SpListReglaVariablePcVarRS> lstExpected = Arrays.asList(obj1,obj2,obj3,obj4);

        when(engineService.getRuleVariable(isA(String.class)))
                .thenReturn(lstExpected);

        MockHttpServletRequestBuilder builder =
                post("/getAttributions")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(EngineFixture.JSON_TEST_1);

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                .isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testVarAvailableOK() throws Exception {
        String ruleSetName = "m_fl08_POC_1_RulesetList";
        mockMvc.perform(get("/getVarAvailable/{ruleSetName}", ruleSetName).contentType(TestUtil.APPLICATION_JSON_UTF8)
                .header("OAM_REMOTE_KEY", TestUtil.OAM_REMOTE_KEY)).andExpect(status().isOk()).andDo(print());
    }

}
