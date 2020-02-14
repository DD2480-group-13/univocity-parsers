package com.univocity.parsers.common;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ArgumentUtilsTest {

    /**
     * Cover the first branch of the function where the input array is null ot increase
     * branch coverage. This will throw an NullPointerException "Null Array".
     */
    @Test
    public void testIndexOf() {
        Object[] testArray = null;
        Object testObject = new Object();
        int testFrom = 0;
        try{
            ArgumentUtils.publicIndexOf(testArray, testObject, testFrom);
            fail();
        }catch (NullPointerException e){
            assertEquals(e.getMessage(), "Null array");
        }
    }
}