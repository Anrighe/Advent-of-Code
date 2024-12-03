import java.io.*;
import java.util.regex.*;


/**
 * Parses a corrupted memory log to identify and process valid multiplication instructions.
 * 
 * Scans the input for valid `mul(X,Y)` instructions, where:
 * - `mul` is a keyword followed by two integers (X and Y) between 1 and 3 digits.
 * - Invalid instructions or malformed sequences are ignored.
 * 
 * The program computes the sum of all products derived from valid instructions.
 * 
 * Example Input:
 * xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
 * 
 * Valid instructions extracted:
 * - mul(2,4) -> 2 * 4 = 8
 * - mul(3,7) -> 3 * 7 = 21
 * - mul(11,8) -> 11 * 8 = 88
 * - mul(8,5) -> 8 * 5 = 40
 * 
 * Output:
 * The sum of the result of all multiplications is: 157
 * 
 * Error Handling:
 * - If the input file is missing, a `FileNotFoundException` is logged.
 * - If the input contains no valid instructions, the result will be 0.
 * 
 * The input is read from a file specified in `INPUT_FILE_LOCATION`.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";
    
    public static void main(String args[]) {
        
        File file = new File(INPUT_FILE_LOCATION);
        int result = 0;

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));
            String inputLine;

            Pattern pattern = Pattern.compile("mul\\([0-9]{1,3},[0-9]{1,3}\\)");
            
            while ((inputLine = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(inputLine);
                
                while(matcher.find()) {
                    String match = matcher.group(0);
                    
                    Pattern numberPattern = Pattern.compile("([0-9]{1,3})");
                    Matcher numberMatcher = numberPattern.matcher(match);

                    numberMatcher.find();
                    int firstNumber = Integer.parseInt(numberMatcher.group(0));

                    numberMatcher.find();
                    int secondNumber = Integer.parseInt(numberMatcher.group(0));

                    result += (firstNumber * secondNumber);
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
            System.err.println(String.format("Error: Number of pairs not equal to 2: %s", e.toString()));
            return;
        }
        
        System.out.println(String.format("The sum of the result of all moltiplication is: %s", result));
    }
}