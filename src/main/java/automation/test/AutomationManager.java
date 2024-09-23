package automation.test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class AutomationManager {
    private static final String dir = "src/main/resources/";
    private final static String INPUT_FILE_NAME_PATTERN = "car_input_[0-9]{1,3}\\.txt";
    private final static String CAR_VALUATION_WEBSITE = "https://www.webuyanycar.com/";

    public static void main(String[] args) throws IOException {
        Set<String> vrns = InputProcessor.INSTANCE.extractVehicleRegistrations(dir);
        Map<String, Map<String, String>> outputData = OutputProcessor.INSTANCE.read(dir);
        int success = 0;
        int failures = 0;
        int ignored = 0;
        for(String vrn : vrns) {
            Map<String, String> currentData =  VRNValidation.INSTANCE.getVRNDetails(CAR_VALUATION_WEBSITE, vrn, 30000);

            if (currentData.isEmpty()) {
                System.out.println(vrn +" - Validation Failed. Not able to locate the vehicle");
                failures++;
                continue;
            }
            Map<String, String> expectedData = outputData.get(vrn);
            if(expectedData == null) {
                System.out.println(vrn +" - Ignored. Missing from the output file");
                ignored++;
                continue;
            }

            if (!currentData.get("make").equalsIgnoreCase(expectedData.get("make"))) {
                System.out.println(vrn +" - Validation Failed. Car 'make' does not match");
                failures++;
                continue;
            }
            if (!currentData.get("model").equalsIgnoreCase(expectedData.get("model"))) {
                System.out.println(vrn +" - Validation Failed. Car 'model' does not match");
                failures++;
                continue;
            }
            if (!currentData.get("year").equalsIgnoreCase(expectedData.get("year"))) {
                System.out.println(vrn +" - Validation Failed. Car 'year' does not match");
                failures++;
                continue;
            }
            System.out.println(vrn +" - Validation Success.");
            success++;
        }
        System.out.println("Total success: "+ ignored);
        System.out.println("Total failures: "+ failures);
        System.out.println("Total success: "+ success);


    }
}
