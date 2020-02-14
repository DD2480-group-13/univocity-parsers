package com.univocity.parsers.common;

import com.univocity.parsers.fixed.FixedWidthFields;
import com.univocity.parsers.fixed.FixedWidthWriter;
import com.univocity.parsers.fixed.FixedWidthWriterSettings;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.testng.Assert.*;
import static org.testng.annotations.AfterSuite.*;
import static org.testng.annotations.BeforeSuite.*;



public class ArgumentUtilsTest {
    @BeforeTest
    public void setup()  {
    }


    @AfterTest
    public void printing() {
        System.out.println("=== DIY branch coverage for trim() ===");
        for(int i = 0; i < ArgumentUtils.trimBranchCoverage.length; i++){
            System.out.println("Trim branch " + i + " = " +ArgumentUtils.trimBranchCoverage[i]);
        }
        System.out.println("======================================");
    }
}