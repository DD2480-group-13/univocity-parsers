package com.univocity.parsers.xcoverage;

import com.univocity.parsers.fixed.FixedWidthParser;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
public class readValueUntilNewLineCoverage {

    @AfterSuite
    public void printCoverageValidate() {

        System.out.println("=== DIY branch coverage for FixedWidthParser::readValueUntilNewLine() ===");
        for(int i = 0; i < FixedWidthParser.BranchCoverage.length; i++){
            System.out.println("Validate mappings branch " + i + " = " +FixedWidthParser.BranchCoverage[i]);
        }
        System.out.println("Total branch coverage: " + getRatio(FixedWidthParser.BranchCoverage));
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