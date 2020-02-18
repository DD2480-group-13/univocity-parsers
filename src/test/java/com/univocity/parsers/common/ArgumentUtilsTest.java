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
        try {
            ArgumentUtils.publicIndexOf(testArray, testObject, testFrom);
            fail();
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "Null array");
        }
    }

    /**
     * Test the ArgumentUtils::trim function with an empty string to test that
     * the branch withing in first if statement is covered. It should return
     * the empty string
     */
    @Test
    public void testTrim1() {
        String answer;
        answer = ArgumentUtils.trim("",true,true );
        assertEquals(answer,"");
    }

    /**
     * Test the ArgumentUtils::trim function with both left and right as false.
     * This should just return the input string. This test covers the first branch as well.
     */
    @Test
    public void testTrim2() {
        String answer;
        answer = ArgumentUtils.trim("test",false,false );
        assertEquals(answer,"test");
    }
}