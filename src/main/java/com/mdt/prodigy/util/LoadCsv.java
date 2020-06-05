package com.mdt.prodigy.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class LoadCsv {

    public static final String SEPARATER = ",";

    /**
     * Loads a CSV file from the classpath.
     * @param fileName Name of the file.
     * @return A list of values.
     */
    public static List<String> load(String fileName){
        List<String> values = Collections.EMPTY_LIST;
        String line = "";
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(LoadCsv.class.getClassLoader().getResourceAsStream(fileName)))){
            while((line = bufferedReader.readLine()) != null){
                String[] array = line.split(SEPARATER);
                values = Arrays.asList(array);
            }
        } catch (FileNotFoundException e) {
            // Can be caused when creating the FileReader if it can't find the file.
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }
}
