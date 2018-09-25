package cl.motoratrib.rest.jsrules.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class AllExceptionTest {

    @Test(expected=JsRulesException.class)
    public void JsRulesExceptionOneTest() throws JsRulesException {
        if (true) {
            throw new JsRulesException("JsRulesException");
        }
    }

    @Test(expected=JsRulesException.class)
    public void JsRulesExceptionTwoTest() throws JsRulesException {
        if (true) {
            throw new JsRulesException("JsRulesException",new Throwable("JsRulesException"));
        }
    }

    @Test(expected=JsRulesException.class)
    public void JsRulesExceptionThreeTest() throws JsRulesException {
        if (true) {
            throw new JsRulesException(new Throwable("JsRulesException"));
        }
    }
}
