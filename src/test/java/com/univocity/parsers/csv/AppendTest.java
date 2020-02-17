package com.univocity.parsers.csv;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class AppendTest {
    @BeforeTest
    public void setup()  {
    }


    @AfterTest
    public void printingOfCoverage() {

        System.out.println("=== DIY branch coverage for append() ===");
        for(int i = 0; i < CsvWriter.appendBranchCoverage.length; i++){
            System.out.println("Append branch " + i + " = " +CsvWriter.appendBranchCoverage[i]);
        }
        System.out.println("Total branch coverage: " + getRatio(CsvWriter.appendBranchCoverage));
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
