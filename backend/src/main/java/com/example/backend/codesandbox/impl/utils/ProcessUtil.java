package com.example.backend.codesandbox.impl.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessUtil {
    public static String getOutputString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder output = new StringBuilder();
        String outputLine;
        while((outputLine=bufferedReader.readLine())!=null){
            output.append(outputLine);
        }
        return output.toString();
    }
}
