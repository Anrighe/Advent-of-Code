import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.*;

/**
 * Calculates the total distance between two lists of integers extracted
 * from an input file. The file should contain lines with two space-separated integers.
 * The first number on each line is added to the "left list" and the second to the "right list."
 * After reading the file, both lists are sorted, and the absolute differences between
 * corresponding numbers are summed to produce the final result.
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
 * The total distance between the left list and the right list is: 11
 * 
 * Error Handling:
 * - If the input file is not found, a FileNotFoundException is caught and an error message is displayed.
 * - If the input format is invalid, unmatched lines are logged with a warning.
 * - If the two lists are not equal in size, an AssertionError is thrown.
 * 
 * Assumptions:
 * - Each line in the input file must either match the pattern or be ignored.
 * - The input lists must be non-empty and equal in size for the program to work correctly.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";

    
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
                
                if (matcherResult) {
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
        
        // Sorting lists in ascending order
        Collections.sort(firstList);
        Collections.sort(secondList);

        // Calculate the absolute difference of each number in the same position in the list
        for (int numberPosition = 0; numberPosition < firstList.size(); ++numberPosition)
            result += Math.abs(firstList.get(numberPosition) - secondList.get(numberPosition));

        System.out.println(String.format("The total distance between the left list and the right list is: %s", result));
    }
}