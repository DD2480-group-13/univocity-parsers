package com.univocity.parsers.issues.github;


import com.univocity.parsers.common.*;
import com.univocity.parsers.common.processor.*;
import com.univocity.parsers.conversions.*;
import com.univocity.parsers.examples.Example;
import com.univocity.parsers.fixed.*;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class Github_364 extends Example {
    @Test
    public void github364_test1() throws Exception {
        //##CODE_START

        // creates the sequence of field lengths in the file to be parsed
        FixedWidthFields lengths = new FixedWidthFields(4);
        lengths.addField(5,FieldAlignment.RIGHT);

        // creates the default settings for a fixed width parser
        FixedWidthParserSettings settings = new FixedWidthParserSettings(lengths);

        //sets the character used for padding unwritten spaces in the file
        settings.getFormat().setPadding('_');
        settings.getFormat().setLineSeparator("\n");
        settings.setIgnoreTrailingWhitespaces(true);
        settings.setKeepPadding(true);
        settings.setRecordEndsOnNewline(true);
        // creates a fixed-width parser with the given settings
        FixedWidthParser parser = new FixedWidthParser(settings);

        // parses all rows in one go.
        List<String[]> allRows = parser.parseAll(getReader("/examples/github_364.txt"));
        ArrayList<String> output = new ArrayList<String>();
        int i =0;
        for(String[] a : allRows){
            for(String b : a){
                output.add(b);
            }
            i++;
        }
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("abc_");
        expected.add("abcde");
        expected.add("par_");
        expected.add("parse");
        assertEquals(output.toString(),expected.toString());

        //##CODE_END

        //printAndValidate(null, allRows);
    }

    @Test
    public void github364_test2() throws Exception {
        //##CODE_STARTe

        // creates the sequence of field lengths in the file to be parsed
        FixedWidthFields lengths = new FixedWidthFields(4);
        lengths.addField(5,FieldAlignment.RIGHT);

        // creates the default settings for a fixed width parser
        FixedWidthParserSettings settings = new FixedWidthParserSettings(lengths);

        //sets the character used for padding unwritten spaces in the file
        settings.getFormat().setPadding('_');
        settings.getFormat().setLineSeparator("\n");
        settings.setIgnoreTrailingWhitespaces(false);
        settings.setKeepPadding(true);
        settings.setRecordEndsOnNewline(true);
        // creates a fixed-width parser with the given settings
        FixedWidthParser parser = new FixedWidthParser(settings);

        // parses all rows in one go.
        List<String[]> allRows = parser.parseAll(getReader("/examples/github_364.txt"));
        ArrayList<String> output = new ArrayList<String>();
        int i =0;
        for(String[] a : allRows){
            for(String b : a){
                output.add(b);
            }
            i++;
        }
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("abc_");
        expected.add("abcde");
        expected.add("par_");
        expected.add("parse");
        assertEquals(output.toString(),expected.toString());
    }
}
