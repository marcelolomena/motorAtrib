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
package cl.motoratrib.rest.jsrules.loader;

import cl.motoratrib.rest.jsrules.config.ResponseConfig;
import cl.motoratrib.rest.jsrules.exception.InvalidConfigException;
import cl.motoratrib.rest.jsrules.loader.impl.ResponseLoaderImpl;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Marcelo
 */
@SuppressWarnings("rawtypes")
public class ResponseLoaderTest {
    @Rule
    public ExpectedException exception= ExpectedException.none();
    
    private final ResponseLoader responseLoader = new ResponseLoaderImpl();
    
    public ResponseLoaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void loadResponseTest() throws Exception {
        ResponseConfig responseConfig = new ResponseConfig("25", "long");
                
        assertEquals(25l, responseLoader.load(responseConfig));
    }
    
    @Test
    public void loadResponseInvalidClassTest() throws Exception {
        exception.expect(InvalidConfigException.class);
        
        ResponseConfig responseConfig = new ResponseConfig("25", "loong");
        
        responseLoader.load(responseConfig);
    }
    
    @Test
    public void loadResponseInvalidValueTest() throws Exception {
        exception.expect(InvalidConfigException.class);
        
        ResponseConfig responseConfig = new ResponseConfig("NaN", "long");
        
        responseLoader.load(responseConfig);
    }

    @Test
    public void loadNullReponseClassTest() throws Exception {
        exception.expect(InvalidConfigException.class);

        ResponseConfig responseConfig = new ResponseConfig(null, null);

        responseLoader.load(responseConfig);
    }
}
