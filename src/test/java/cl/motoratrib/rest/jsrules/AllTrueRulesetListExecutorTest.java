/*
 * The MIT License
 *
 * Copyright 2018 Marcelo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cl.motoratrib.rest.jsrules;

import cl.motoratrib.rest.jsrules.exception.InvalidParameterException;
import cl.motoratrib.rest.jsrules.impl.AllTrueRulesetListExecutorImpl;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Marcelo
 */
@SuppressWarnings("rawtypes")
@RunWith(MockitoJUnitRunner.class)
public class AllTrueRulesetListExecutorTest {
    private RulesetExecutor<String> executor;
    private String responseMock = "mock";
    private final String rulesetName = "mockRuleset";
    private final String rulesetType = "mockRuleset";

    @org.junit.Rule
    public ExpectedException exception = ExpectedException.none();

    private List<RulesetExecutor> rulesetListMock;
    private Map<String, Object> parameters;

    @Mock
    private RulesetExecutor rulesetExecutorMock;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        rulesetListMock = new ArrayList<>();
        parameters = new HashMap<>();

        executor = new AllTrueRulesetListExecutorImpl<>(rulesetName, rulesetType, rulesetListMock, responseMock);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void executeAllTrueRulesetListEmptyTest() throws Exception {
        assertEquals(responseMock, executor.execute(parameters));
    }

    @Test
    public void executeAllTrueRulesetListInvalidParametersTest() throws Exception {
        exception.expect(InvalidParameterException.class);

        when(rulesetExecutorMock.execute(parameters)).thenThrow(new InvalidParameterException());

        rulesetListMock.add(rulesetExecutorMock);

        executor.execute(parameters);
    }

    @Test
    public void executeAllTrueRulesetListValidParametersTrueTest() throws Exception {
        Object mock = new Object();

        parameters.put("left", 21l);
        parameters.put("right", 10l);

        when(rulesetExecutorMock.execute(parameters)).thenReturn(mock);
        rulesetListMock.add(rulesetExecutorMock);

        assertEquals(responseMock, executor.execute(parameters));
    }
    
    @Test
    public void executeAllTrueRulesetListValidParametersFalseTest() throws Exception {
        Object mock = new Object();

        parameters.put("left", 21l);
        parameters.put("right", 10l);

        when(rulesetExecutorMock.execute(parameters)).thenReturn(mock);
        rulesetListMock.add(rulesetExecutorMock);

        Map<String, Object> otherParameters = new HashMap<>();
        parameters.put("left", 10l);
        parameters.put("right", 10l);

        assertNull(executor.execute(otherParameters));
    }

    @Test
    public void executeRulesetListFirstFalse() throws Exception {
        Object mockResponse = new Object();
        when(rulesetExecutorMock.execute(parameters)).thenReturn(mockResponse);

        RulesetExecutor rulesetExecutorMock2 = mock(RulesetExecutor.class);
        when(rulesetExecutorMock.execute(parameters)).thenReturn(null);

        rulesetListMock.add(rulesetExecutorMock);
        rulesetListMock.add(rulesetExecutorMock2);

        assertNull(executor.execute(parameters));
    }
    
    @Test
    public void executeRulesetListSecondFalse() throws Exception {
        Object mockResponse = new Object();
        when(rulesetExecutorMock.execute(parameters)).thenReturn(mockResponse);

        RulesetExecutor rulesetExecutorMock2 = mock(RulesetExecutor.class);
        when(rulesetExecutorMock.execute(parameters)).thenReturn(null);

        rulesetListMock.add(rulesetExecutorMock2);
        rulesetListMock.add(rulesetExecutorMock);

        assertNull(executor.execute(parameters));
    }

    @Test
    public void rulesetNameTest() {
        assertEquals(rulesetName, executor.getName());
    }
}
