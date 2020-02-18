package com.univocity.parsers.common;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ArgumentUtilsTest {

    public class testClass{
        int x;
    }

    /**
     * Cover the 5th branch where the input element and the input array don't have the same
     * class type. This should throw an IllegalStateException with message "a". This did not
     * have any coverage before this test.
     */
    @Test
    public void testIndexOf1() {
        testClass[] testArray = new testClass[5];
        String testObject = "hello";
        int testFrom = 0;

        try {
            ArgumentUtils.publicIndexOf(testArray, testObject, testFrom);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "a");
        }
    }

    /**
     * Cover the first branch of the function where the input array is null ot increase
     * branch coverage. This will throw an NullPointerException "Null Array". Did not have
     * coverage before this test.
     */
    @Test
    public void testIndexOf2() {
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