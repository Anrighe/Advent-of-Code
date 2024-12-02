import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Analyzes reactor safety reports to determine how many are considered "safe" based on specified criteria.
 * 
 * The program applies two levels of safety checks:
 * 
 * 1. **Standard Safety Rules**: A report is safe if:
 *    - All levels are either strictly increasing or strictly decreasing.
 *    - The absolute difference between any two adjacent levels is between 1 and 3 (inclusive).
 * 
 * 2. **Problem Dampener Rules**: If a report is unsafe by standard rules, it may still be deemed safe if removing 
 *    any single level results in a valid report that satisfies the standard safety rules.
 * 
 * The program processes input from a file where each line represents a report with space-separated integer levels.
 * 
 * Example Input:
 * 7 6 4 2 1
 * 1 2 7 8 9
 * 9 7 6 2 1
 * 
 * Example Output:
 * The total number of safe reports is: 4
 * 
 * Features:
 * - **Standard Safety Check**: Validates reports directly based on strict rules.
 * - **Problem Dampener Check**: Evaluates if a report can be corrected by removing a single level.
 * 
 * Error Handling:
 * - Reports with fewer than one level trigger an AssertionError.
 * - Input file-related issues (e.g., missing or corrupted files) are logged and halt execution.
 * - Lines with invalid formatting are ignored, and processing continues.
 * 
 * The final output includes the count of all reports that are safe, either directly or with the Problem Dampener.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";

    /**
     * Determines if the given report is strictly decreasing. A report is considered decreasing if 
     * each level is smaller than the previous level.
     *
     * @param report the list of integers representing the report levels.
     * @return true if all levels in the report are strictly decreasing; false otherwise.
     * @throws AssertionError if the report contains fewer than one level.
     */
    public static boolean isReportDecreasing(List<Integer> report) throws AssertionError {
        assert report.size() >= 1;
        int value;
        int previous;

        for (int i = 1; i < report.size(); ++i) {
            value = report.get(i);
            previous = report.get(i-1);
            if (previous <= value)
                return false;
        }

        return true;
    }

    /**
     * Determines if the given report is strictly increasing. A report is considered increasing if 
     * each level is larger than the previous level.
     *
     * @param report the list of integers representing the report levels.
     * @return true if all levels in the report are strictly increasing; false otherwise.
     * @throws AssertionError if the report contains fewer than one level.
     */
    public static boolean isReportIncreasing(List<Integer> report) throws AssertionError {
        assert report.size() >= 1;
        int value;
        int previous;

        for (int i = 1; i < report.size(); ++i) {
            value = report.get(i);
            previous = report.get(i-1);
            if (previous >= value)
                return false;
        }

        return true;
    }

    /**
     * Checks if the absolute difference between each pair of adjacent levels in the report
     * falls within a specified range (inclusive).
     *
     * @param report the list of integers representing the report levels.
     * @param minRangeValue the minimum acceptable difference between adjacent levels.
     * @param maxRangeValue the maximum acceptable difference between adjacent levels.
     * @return true if the difference between every pair of adjacent levels is within the specified range; false otherwise.
     * @throws AssertionError if the report contains fewer than one level.
     */
    public static boolean isDifferenceForEachCoupleInRange(List<Integer> report, int minRangeValue, int maxRangeValue) throws AssertionError {
        assert report.size() >= 1;
        int value;
        int previous;
        int difference;

        for (int i = 1; i < report.size(); ++i) {
            value = report.get(i);
            previous = report.get(i-1);

            difference = Math.abs(value-previous);
            if (!(difference >= minRangeValue && difference <= maxRangeValue))
                return false;
        }

        return true;
    }

    /**
     * Generates a list of modified reports, each created by removing one level from the original report.
     * 
     * This method is primarily used in conjunction with the Problem Dampener logic to determine 
     * if removing a single level from an unsafe report can make it safe.
     * 
     * For a given input report, it creates a list of new reports where each report excludes exactly 
     * one level from the original. The number of generated reports equals the size of the input report.
     * 
     * @param report the original list of integers representing the levels in the report.
     * @return a list of reports, each derived by removing one level from the input report.
     * @throws AssertionError if the input report contains fewer than one level.
     */
    public static List<List<Integer>> getListOfReportsForDampener(List<Integer> report) throws AssertionError {
        assert report.size() >= 1;

        List<List<Integer>> listOfReportsForDampener = new ArrayList<>();

        for (int i = 0; i < report.size(); ++i) {
            List<Integer> clone = new ArrayList<>(report);
            clone.remove(i);
            listOfReportsForDampener.add(clone);
        }

        return listOfReportsForDampener;
    }
    
    public static void main(String args[]) {
        
        File file = new File(INPUT_FILE_LOCATION);
        int result = 0;

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));
            String inputLine;

            boolean unsafeReport = false;
            List<String> reportStringList;
            List<Integer> reportIntegerList;
            while ((inputLine = br.readLine()) != null) {
                reportStringList =  Arrays.asList(inputLine.split("\\s+"));
                reportIntegerList = new ArrayList<>();

                for(String level : reportStringList)
                    reportIntegerList.add(Integer.parseInt(level.trim()));

                if ((isReportDecreasing(reportIntegerList) || isReportIncreasing(reportIntegerList)) &&
                    isDifferenceForEachCoupleInRange(reportIntegerList, 1, 3)) {
                    result += 1;
                    unsafeReport = false;
                } else
                    unsafeReport = true;
                
                if (unsafeReport) {
                    List<List<Integer>> listOfReportsForDampener = getListOfReportsForDampener(reportIntegerList);
                    boolean safeWithDampner = listOfReportsForDampener
                                                .stream()
                                                .anyMatch(report -> (isReportDecreasing(report) || isReportIncreasing(report)) && 
                                                        isDifferenceForEachCoupleInRange(report, 1, 3));
                    
                    if (safeWithDampner)
                        result += 1; 
                }
            }
            br.close();

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Error: not enough levels in report: %s", e.toString()));
            return;
        }
        
        System.out.println(String.format("The total number of safe report is: %s", result));
    }
}