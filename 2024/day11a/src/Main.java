import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulates the transformation of stones based on specific rules over a series of blinks:
 * 
 * Transformation Rules:
 * 1. If the stone's value is 0, it becomes 1.
 * 2. If the stone's value has an even number of digits, it splits into two stones:
 *    - Left digits form the first stone.
 *    - Right digits form the second stone.
 * 3. Otherwise, the stone's value is multiplied by 2024.
 * 
 * Input: List of initial stone values.
 * Output: The number of stones after a specified number of blinks.
 */
public class Main {

    private static final String INPUT_FILE_LOCATION = "/home/marrase/Advent-of-Code/2024/day11a/input.txt";

    private static final long MULTIPLICATION_FACTOR = 2024;
    private static final int BLINKING_AMOUNT = 25;

    /**
     * Calculates the number of digits in a value.
     *
     * @param value The value to analyze.
     * @return The count of digits.
     */
    private static int getNumberOfDigits(long value) {
        return String.valueOf(value).length();
    }

    /**
     * Processes a stone according to the defined transformation rules:
     * 
     * 1. If the stone's value is 0, it transforms into 1.
     * 2. If the stone's value has an even number of digits, it splits into two stones:
     *    - Left half of the digits form the first stone.
     *    - Right half of the digits form the second stone.
     * 3. If none of the above apply, the stone's value is multiplied by a constant (2024).
     * 
     * @param stone The {@link Stone} to process.
     * @return A list of {@link Stone} objects if the stone splits; otherwise, an empty list.
     */
    private static List<Stone> blink(Stone stone) {
        long stoneValue = stone.getValue();

        // Rule 1: Stone with value 0 transforms into value 1
        if (stoneValue == 0) {
            stone.setValue(1);
            return new ArrayList<>();
        } else { 
            int numberOfDigitsStoneValue = getNumberOfDigits(stoneValue);
            if (numberOfDigitsStoneValue % 2 == 0) {
                // Rule 2: Stone with an even number of digits splits into two stones
                return stone.split();
            } else {
                // Rule 3: Stone with odd digits is multiplied by the constant
                stone.setValue(stone.getValue() * MULTIPLICATION_FACTOR);
                return new ArrayList<>();
            }
        }
    }

    public static void main(String args[]) {
        File file = new File(INPUT_FILE_LOCATION);
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String inputLine = br.readLine();
            List<Stone> stoneList = new ArrayList<>();
            String [] splittedInputLine = inputLine.split(" ");

            for (String value : splittedInputLine) {
                stoneList.add(new Stone(Long.parseLong(value)));
            }

            br.close();

            for (int blinkCounter = 0; blinkCounter < BLINKING_AMOUNT; ++blinkCounter) {
                
                for (int index = 0; index < stoneList.size();) {
                    Stone stone = stoneList.get(index);

                    List<Stone> blinkResult = blink(stone);

                    if (blinkResult.isEmpty())
                        index++;
                    else {
                        stoneList.remove(index);
                        stoneList.addAll(index, blinkResult);

                        index += blinkResult.size();
                    }
                }
            }
            System.out.println(String.format("The number of stones after blinking %s times is: %s", BLINKING_AMOUNT, stoneList.size()));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Error: could not split the stone due to a uneven number of digits: %s", e.toString()));
            return;
        }
    }
}