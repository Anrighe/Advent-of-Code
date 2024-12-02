import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Analyzes reactor safety reports to determine how many are considered "safe" based on specified criteria.
 *
 * Each report consists of a list of integers (levels), and a report is deemed safe if it satisfies the following:
 * 1. The levels are either strictly increasing or strictly decreasing.
 * 2. The absolute difference between any two adjacent levels is within a specified range (1 to 3, inclusive).
 *
 * The program reads reports from a file, where each line represents a report with space-separated integers.
 * It processes each report, checks the safety conditions, and counts the total number of safe reports.
 *
 * Example Input:
 * 7 6 4 2 1
 * 1 2 7 8 9
 * 9 7 6 2 1
 *
 * Example Output:
 * The total number of safe reports is: 2
 *
 * Error Handling:
 * - If the input file is missing, a FileNotFoundException is logged.
 * - If the input format is invalid, unmatched lines are ignored, and processing continues.
 * - If a report contains fewer than one level, an AssertionError is thrown.
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
    
    public static void main(String args[]) {
        
        File file = new File(INPUT_FILE_LOCATION);
        int result = 0;

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));
            String inputLine;

            
            List<String> reportStringList;
            List<Integer> reportIntegerList;
            while ((inputLine = br.readLine()) != null) {
                reportStringList =  Arrays.asList(inputLine.split("\\s+"));
                reportIntegerList = new ArrayList<>();

                for(String level : reportStringList)
                    reportIntegerList.add(Integer.parseInt(level.trim()));

                if (isReportDecreasing(reportIntegerList) || isReportIncreasing(reportIntegerList)) {
                    if (isDifferenceForEachCoupleInRange(reportIntegerList, 1, 3))
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