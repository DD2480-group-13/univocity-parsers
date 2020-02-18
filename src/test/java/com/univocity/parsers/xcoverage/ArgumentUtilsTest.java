package com.univocity.parsers.xcoverage;

import com.univocity.parsers.annotations.helpers.AnnotationHelper;
import com.univocity.parsers.common.ArgumentUtils;
import com.univocity.parsers.fixed.FixedWidthFields;
import com.univocity.parsers.fixed.FixedWidthWriter;
import com.univocity.parsers.fixed.FixedWidthWriterSettings;
import org.testng.annotations.AfterSuite;
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


    @AfterSuite
    public void printingOfCoverage() {
        System.out.println("=== DIY branch coverage for trim() ===");
        for(int i = 0; i < ArgumentUtils.trimBranchCoverage.length; i++){
            System.out.println("Trim branch " + i + " = " +ArgumentUtils.trimBranchCoverage[i]);
        }
        System.out.println("Coverage: " + getRatio(ArgumentUtils.trimBranchCoverage));
        System.out.println("======================================\n");

        System.out.println("=== DIY branch coverage for indexOf() ===");
        for(int i = 0; i < ArgumentUtils.indexOfBranchCoverage.length; i++){
            System.out.println("Trim branch " + i + " = " +ArgumentUtils.indexOfBranchCoverage[i]);
        }
        System.out.println("Coverage: " + getRatio(ArgumentUtils.indexOfBranchCoverage));

        System.out.println("======================================");
    }

    private double getRatio(boolean[] branchCoverage){
        int tot = 0;
        for (boolean b : branchCoverage){
            if (b){
                tot += 1;
            }
        }
        return (double) tot / (double) branchCoverage.length;
    }
}