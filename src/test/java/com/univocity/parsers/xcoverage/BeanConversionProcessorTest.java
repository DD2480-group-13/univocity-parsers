package com.univocity.parsers.xcoverage;


import com.univocity.parsers.common.processor.core.BeanConversionProcessor;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BeanConversionProcessorTest {
    @BeforeTest
    public void setup()  {
    }


    @AfterSuite
    public void printingOfCoverage() {

        System.out.println("=== DIY branch coverage for validateMappings() ===");
        for(int i = 0; i < BeanConversionProcessor.validateMappingsBranchCoverage.length; i++){
            System.out.println("Validate mappings branch " + i + " = " +BeanConversionProcessor.validateMappingsBranchCoverage[i]);
        }
        System.out.println("Total branch coverage: " + getRatio(BeanConversionProcessor.validateMappingsBranchCoverage));
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
