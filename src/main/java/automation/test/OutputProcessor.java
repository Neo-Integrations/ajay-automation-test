package automation.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public enum OutputProcessor {

    INSTANCE;

    private static final String OUTPUT_FILE_NAME_PATTERN = "car_output_[0-9]{1,3}\\.txt";
    private static final String COMMA_DELIMITER = ",";
    private static final String[] headers =
            new String[] {"reg", "make", "model", "year"};

    public Map<String, Map<String, String>> read(String dir) throws IOException {
        Map<String, Map<String, String>> vrns = new HashMap<>();
        for(String eachFile : AutomationUtil.getAllFiles(dir, OUTPUT_FILE_NAME_PATTERN)) {
            try (BufferedReader br = new BufferedReader(new FileReader(eachFile))) {
                String line;
                int lineNumber = 0;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    if (lineNumber == 0) {
                        lineNumber++;
                        continue;
                    }
                    String[] values = line.split(COMMA_DELIMITER);
                    int index = 0;
                    Map<String, String> records = new HashMap<>();
                    for (String value : values) {
                        records.put(headers[index], value);
                        index++;
                    }
                    vrns.put(records.get("reg"), records);
                    lineNumber++;
                }
            }
        }
        return vrns;
    }
}
