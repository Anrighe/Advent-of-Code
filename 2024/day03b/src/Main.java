import java.io.*;
import java.util.regex.*;


/**
 * Parses a corrupted memory log to identify and process valid multiplication instructions,
 * while honoring conditional enable/disable commands.
 *
 * The program scans the input for:
 * - `mul(X,Y)` instructions: Multiplies X and Y if multiplication is enabled.
 * - `do()`: Enables future `mul` instructions.
 * - `don't()`: Disables future `mul` instructions.
 *
 * Conditional Behavior:
 * - Multiplication instructions (`mul(X,Y)`) are enabled by default.
 * - Only the most recent `do()` or `don't()` instruction applies.
 *
 * Example Input:
 * xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
 *
 * Instructions and Results:
 * - mul(2,4): Enabled -> 2 * 4 = 8
 * - don't(): Disables `mul`
 * - mul(5,5): Disabled -> Skipped
 * - mul(32,64): Disabled -> Skipped
 * - mul(11,8): Disabled -> Skipped
 * - do(): Re-enables `mul`
 * - mul(8,5): Enabled -> 8 * 5 = 40
 *
 * Final Output:
 * The sum of the result of all enabled multiplications is: 48
 *
 * Error Handling:
 * - If the input file is missing, a `FileNotFoundException` is logged.
 * - If the input contains no valid instructions, the result will be 0.
 *
 * The input is read from a file specified in `INPUT_FILE_LOCATION`.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";

    // Constant instructions
    public static final String doInstruction = "do()";
    public static final String dontInstruction = "don't()";

    public static void main(String args[]) {
        
        File file = new File(INPUT_FILE_LOCATION);
        boolean mulInstructionEnabled = true;
        int result = 0;

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));
            String inputLine;
            Pattern pattern = Pattern.compile("(mul\\([0-9]{1,3},[0-9]{1,3}\\))|(do\\(\\))|(don\\'t\\(\\))");


            while ((inputLine = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(inputLine);
                
                while(matcher.find()) {
                    String match = matcher.group(0);
                    System.out.println(match);

                    switch (match) {
                        case doInstruction:
                            mulInstructionEnabled = true;
                            break;
                        case dontInstruction:
                            mulInstructionEnabled = false;
                            break;
                        default:
                            if (mulInstructionEnabled) {
                                Pattern numberPattern = Pattern.compile("([0-9]{1,3})");
                                Matcher numberMatcher = numberPattern.matcher(match);

                                numberMatcher.find();
                                int firstNumber = Integer.parseInt(numberMatcher.group(0));

                                numberMatcher.find();
                                int secondNumber = Integer.parseInt(numberMatcher.group(0));

                                result += (firstNumber * secondNumber);
                            }
                            break;
                    }
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