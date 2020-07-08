package com.salesmanager.test.utils;

import com.salesmanager.core.business.utils.CreditCardUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardUtilsTest extends com.salesmanager.test.common.AbstractSalesManagerCoreTestCase{

    @Test
    void maskCardNumber() throws Exception {

        String expected = "0139XXXXXXXXXX3928";
        String answer = CreditCardUtils.maskCardNumber("0139483928");

        //test 1
        assertEquals(expected, answer);

        //test 2
        Exception exception = assertThrows(Exception.class, () -> {
            CreditCardUtils.maskCardNumber("1351");
        });

        String expectedMessage = "Invalid number of digits";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}