package automation.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum InputProcessor {
    INSTANCE;

    private final String INPUT_FILE_NAME_PATTERN = "car_input_[0-9]{1,3}\\.txt";
    private final Pattern VRN_PATTERN = Pattern.compile("[A-Za-z]{2}[0-9]{2}[ ]?[A-Za-z]{3}");

    public Set<String> extractVehicleRegistrations(String dir) throws IOException {
        Set<String> vrns = new HashSet<>();
        for(String eachFile : AutomationUtil.getAllFiles(dir, INPUT_FILE_NAME_PATTERN)) {
            try (BufferedReader br = new BufferedReader(new FileReader(eachFile))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    Matcher matched = VRN_PATTERN.matcher(line);
                    while (matched.find()) {
                        vrns.add(matched.group());
                    }
                }
            }
        }
        return vrns;
    }
}
