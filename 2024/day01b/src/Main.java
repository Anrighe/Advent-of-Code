import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

/**
 * This program calculates the "total similarity" between two lists of integers extracted
 * from a file. The input file should contain lines with two space-separated integers.
 * The first number on each line is added to the "left list," and the second to the "right list."
 * The program calculates how often each number from the left list appears in the right list,
 * multiplies the number by its occurrences, and sums these products to compute the similarity score.
 * 
 * Features:
 * - Reads input from a file specified by the constant INPUT_FILE_LOCATION.
 * - Validates input using a regular expression to ensure each line contains
 *   exactly two space-separated integers.
 * - Outputs warnings for lines that don't match the expected format.
 * - Ensures the two lists are the same size before proceeding, using an assertion.
 * - Computes the similarity score using a helper function to count occurrences of numbers in a list.
 * 
 * Example Input:
 * 3   4
 * 4   3
 * 2   5
 * 1   3
 * 3   9
 * 3   3
 * 
 * Example Output:
 * The total similarity between the left list and the right list is: 31
 * 
 * Error Handling:
 * - If the input file is not found, a FileNotFoundException is caught and an error message is displayed.
 * - If the input format is invalid, unmatched lines are logged with a warning.
 * - If the two lists are not equal in size, an AssertionError is thrown.
 * 
 * Assumptions:
 * - Each line in the input file must either match the pattern or be ignored.
 * - The input lists must be non-empty and equal in size for the program to work correctly.
 * - The similarity score is computed only for numbers that appear in both lists.
 * 
 * Helper Functions:
 * - `getNumberOccurrencesInList(List<Integer> list)`: Creates a map of numbers and their frequencies in a list.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";

    /**
     * Counts the occurrences of each number in the given list and returns a map
     * where the keys are the numbers and the values are their respective frequencies.
     * 
     * Example:
     * If the input list is [3, 4, 3, 2, 3, 4], the returned map will be:
     * {3=3, 4=2, 2=1}
     * 
     * @param list the list of integers for which to count occurrences.
     * @return a map containing each unique number in the list as a key,
     *         and the number of times it appears in the list as the value.
     */
    public static Map<Integer, Integer> getNumberOccurrencesInList(List<Integer> list) {
        Map<Integer, Integer> occurencesMap = new HashMap<>();   
        
        for (int number : list) {
            if (occurencesMap.containsKey(number)) {
                occurencesMap.put(number, occurencesMap.get(number) + 1);
            } else {
                occurencesMap.put(number, 1);
            }
        }

        return occurencesMap;
    }
    
    public static void main(String args[]) {
        
        File file = new File(INPUT_FILE_LOCATION);
        int result = 0;

        BufferedReader br;
        List<Integer> firstList = new ArrayList<>();
        List<Integer> secondList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(file));
            String inputLine;

            Pattern pattern = Pattern.compile("^([0-9]+)( )+([0-9]+)$");
            
            while ((inputLine = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(inputLine);
                boolean matcherResult = matcher.matches();
                
                if (matcherResult && inputLine != null) {
                    firstList.add(Integer.parseInt(matcher.group(1)));
                    secondList.add(Integer.parseInt(matcher.group(3)));
                } else {
                    System.out.println("Warning: no match for line: " + inputLine);
                }
            }
            br.close();

            assert firstList.size() == secondList.size();
        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Error: lists of numbers are not equal in size: %s", e.toString()));
            return;
        }
        
        Map<Integer, Integer> secondListOccurrencesMap = getNumberOccurrencesInList(secondList);

        for (int number : firstList) {
            int occurrencesInSecondList;
            if (secondListOccurrencesMap.containsKey(number)) {
                occurrencesInSecondList = secondListOccurrencesMap.get(number);
            } else {
                occurrencesInSecondList = 0;
            }

            System.out.println(number * occurrencesInSecondList);
            result +=  number * occurrencesInSecondList;
        }

        System.out.println(String.format("The total similarity between the left list and the right list is: %s", result));
    }
}