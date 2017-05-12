package com.buddybuild.rest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

final class JsonStringReader {

    private JsonStringReader() {
        // cannot instantiate static utils class
    }

    /**
     * read in a json string from a file
     *
     * @param fileName filename (excluding .json extension) located in web-response-samples director
     * @return JSON string
     */
    public static String readJsonSampleFromFile(String fileName) {
        FileInputStream input;
        BufferedReader reader;

        try {
            String fullPath = "../rest/sample_json/" + fileName + ".json";
            input = new FileInputStream(fullPath);
            reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            String errorMsg = "failed to read in test JSON: "
                    + e.getMessage();
            System.out.println(errorMsg);

            System.out.println("current dir is: " + System.getProperty("user.dir"));

            throw new RuntimeException(errorMsg);
        }
    }
}
