package com.univocity.parsers.xcoverage;
import com.univocity.parsers.conversions.ValidatedConversion;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
public class validateCoverage {

    @AfterSuite
    public void printCoverageValidate() {

        System.out.println("=== DIY branch coverage for ValidatedConversion::validate() ===");
        for(int i = 0; i < ValidatedConversion.validateBranchCoverage.length; i++){
            System.out.println("Validate mappings branch " + i + " = " +ValidatedConversion.validateBranchCoverage[i]);
        }
        System.out.println("Total branch coverage: " + getRatio(ValidatedConversion.validateBranchCoverage));
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

