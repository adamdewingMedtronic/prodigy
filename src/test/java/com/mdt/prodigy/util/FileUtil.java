package com.mdt.prodigy.util;

import java.awt.image.ImagingOpException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {
    public static String load(String filename) throws ImagingOpException {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(FileUtil.class.getClassLoader().getResourceAsStream(filename)))) {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            // Can be caused when creating the FileReader if it can't find the file.
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return sb.toString();
        }
    }

    public static void save(String filename, String output){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
