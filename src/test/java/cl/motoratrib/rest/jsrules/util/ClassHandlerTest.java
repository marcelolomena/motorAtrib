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
package cl.motoratrib.rest.jsrules.util;

import cl.motoratrib.rest.jsrules.exception.ClassHandlerException;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Marcelo
 */
public class ClassHandlerTest {
    @Rule
    public ExpectedException exception= ExpectedException.none();
        
    public ClassHandlerTest() {
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
    public void LongClassTest() {
        ClassHandler handler = ClassHandler.LONG;
        
        assertEquals(Long.class, handler.getMyClass());
    }
    
    @Test
    public void LongConversionTest() throws Exception {
        ClassHandler handler = ClassHandler.LONG;
        
        assertEquals(10l, handler.convertString("10"));
    }
    
    @Test
    public void DoubleClassTest() {
        ClassHandler handler = ClassHandler.DOUBLE;
        
        assertEquals(Double.class, handler.getMyClass());
    }
    
    @Test
    public void DoubleConversionTest() throws Exception {
        ClassHandler handler = ClassHandler.DOUBLE;
        
        assertEquals(10.2, handler.convertString("10.2"));
    }
    
    @Test
    public void BooleanClassTest() {
        ClassHandler handler = ClassHandler.BOOLEAN;
        
        assertEquals(Boolean.class, handler.getMyClass());
    }
    
    @Test
    public void BooleanConversionTest() throws Exception {
        ClassHandler handler = ClassHandler.BOOLEAN;
        
        assertEquals(true, handler.convertString("true"));
    }
    
    @Test
    public void StringClassTest() {
        ClassHandler handler = ClassHandler.STRING;
        
        assertEquals(String.class, handler.getMyClass());
    }
    
    @Test
    public void StringConversionTest() throws Exception {
        String string = "string";
        ClassHandler handler = ClassHandler.STRING;
        
        assertEquals(string, handler.convertString(string));
    }
    
    @Test
    public void LongSetClassTest() {
        ClassHandler handler = ClassHandler.NUMBERSET;
        
        assertEquals(Set.class, handler.getMyClass());
    }
    
    @Test
    public void LongSetConversionTest() throws Exception {
        String string = "[1,2,3,4,5]";
        ClassHandler handler = ClassHandler.NUMBERSET;
        
        Set<Long> set = handler.convertString(string);
        
        assertEquals(5, set.size());
    }
    
    @Test
    public void LongSetExceptionTest() throws Exception {
        exception.expect(ClassHandlerException.class);
        
        String string = "not an array";
        ClassHandler handler = ClassHandler.NUMBERSET;
        
        handler.convertString(string);
    }

    @Test
    public void DateTimeClassTest() {
        ClassHandler handler = ClassHandler.DATETIME;

        assertEquals(Date.class, handler.getMyClass());
    }
/*
    @Test
    public void DateTimeConversionTest() throws Exception {
        String string = "2018-05-20";
        ClassHandler handler = ClassHandler.DATETIME;

        DateTime dateTime = handler.convertString(string);

        dateTime = dateTime.toDateTime(DateTimeZone.forOffsetHours(-6)); // allows the test to be run in any time zone

        assertEquals("2018-05-20T00:00:00.000-06:00", dateTime.toString());
    }
*/
    @Test
    public void DateTimeExceptionTest() throws Exception {
        exception.expect(ClassHandlerException.class);

        String string = "not a date";
        ClassHandler handler = ClassHandler.DATETIME;

        handler.convertString(string);
    }

    @Test
    public void DateSetClassTest() {
        ClassHandler handler = ClassHandler.DATESET;

        assertEquals(Set.class, handler.getMyClass());
    }

    @Test
    public void DateSetConversionTest() throws Exception {
        String string = "[\"2018-01-01\",\"2018-12-31\"]";
        ClassHandler handler = ClassHandler.DATESET;

        Set<Long> set = handler.convertString(string);

        assertEquals(2, set.size());
    }

    @Test
    public void DateSetExceptionTest() throws Exception {
        exception.expect(ClassHandlerException.class);

        String string = "not an array";
        ClassHandler handler = ClassHandler.DATESET;

        handler.convertString(string);
    }
}
