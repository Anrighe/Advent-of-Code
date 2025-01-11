import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

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

    private static final String INPUT_FILE_LOCATION = "/home/marrase/Advent-of-Code/2024/day11b/input.txt";

    private static final long MULTIPLICATION_FACTOR = 2024;
    private static final int BLINKING_AMOUNT = 75;

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
     * Inserts a specified quantity of a stone into the stone quantity map.
     * If the stone already exists in the map, the quantity is incremented by the specified amount.
     * If the stone does not exist in the map, it is added with the specified quantity.
     *
     * @param stone the stone to be inserted or updated in the map
     * @param stoneQuantityMap the map that holds the quantities of different stones
     * @param quantityToInsert the quantity of the stone to be inserted or added to the existing quantity
     */
    private static void insertStoneInQuantityMap(Stone stone, Map<Stone, Long> stoneQuantityMap, Long quantityToInsert) {
        if (!stoneQuantityMap.containsKey(stone)) {
            stoneQuantityMap.put(stone, quantityToInsert);
        } else {
            stoneQuantityMap.put(stone, stoneQuantityMap.get(stone) + quantityToInsert);
        }
    }

    /**
     * Simulates a blink by processing a map of stones and their quantities, applying the following rules:
     *   - Rule 1: Stones with a value of 0 transform into stones with a value of 1.
     *   - Rule 2: Stones with an even number of digits split into two stones.
     *   - Rule 3: Stones with an odd number of digits are multiplied by a constant factor.
     *
     * @param stoneQuantityMap a map containing stones and their quantities
     * @return a new map with the transformed stones and their updated quantities
     * @throws RuntimeErrorException if a stone with an even number of digits does not split into exactly two stones
     */
    private static Map<Stone, Long> optimizedBlink(Map<Stone, Long> stoneQuantityMap) throws RuntimeErrorException {
        
        Map<Stone, Long> newStoneQuantityMap = new HashMap<>();

        for (Stone stone : stoneQuantityMap.keySet()) {
            long stoneValue = stone.getValue();

            // Rule 1: Stone with value 0 transforms into value 1
            if (stoneValue == 0) {
                insertStoneInQuantityMap(new Stone(1L), newStoneQuantityMap, stoneQuantityMap.get(stone));
            } else {
                int numberOfDigitsStoneValue = getNumberOfDigits(stoneValue);
                if (numberOfDigitsStoneValue % 2 == 0) {
                    // Rule 2: Stone with an even number of digits splits into two stones
                    Long amountOfStonesToSplit = stoneQuantityMap.get(stone);
                    List<Stone> stoneSplitResult = stone.split();

                    if (stoneSplitResult.size() != 2)
                        throw new RuntimeErrorException(null);

                    Stone firstStone = stoneSplitResult.get(0);
                    Stone secondStone = stoneSplitResult.get(1);

                    insertStoneInQuantityMap(firstStone, newStoneQuantityMap, amountOfStonesToSplit);
                    insertStoneInQuantityMap(secondStone, newStoneQuantityMap, amountOfStonesToSplit);
                } else {
                    // Rule 3: Stone with odd digits is multiplied by the constant
                    insertStoneInQuantityMap(new Stone(stone.getValue() * MULTIPLICATION_FACTOR), newStoneQuantityMap, stoneQuantityMap.get(stone));
                }
            }
        }
        return newStoneQuantityMap;
    }

    public static void main(String args[]) {
        File file = new File(INPUT_FILE_LOCATION);
        long result = 0;
        
        // A map with the stone respective quantity is used to avoid to create every single {@class Stone} object
        //  which would be computationally not sustainable. This can be done because the behaviour of each stone can
        //   be summarized by one single element (the entry of this Map) which memorizes the amount of stones with the
        //    same value.
        Map<Stone, Long> stoneQuantityMap = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String inputLine = br.readLine();
            List<Stone> stoneList = new ArrayList<>();
            String [] splittedInputLine = inputLine.split(" ");

            for (String value : splittedInputLine) {

                Long parsedLongValue = Long.parseLong(value);
                Stone currentStone = new Stone(parsedLongValue);

                insertStoneInQuantityMap(currentStone, stoneQuantityMap, 1L);

                stoneList.add(new Stone(Long.parseLong(value)));
            }

            br.close();

            System.out.println(stoneQuantityMap);

            for (int blinkCounter = 0; blinkCounter < BLINKING_AMOUNT; ++blinkCounter) {
                stoneQuantityMap = optimizedBlink(stoneQuantityMap);
            }

            for (Stone stone : stoneQuantityMap.keySet()) {
                result += stoneQuantityMap.get(stone);
            }

            System.out.println(String.format("The number of stones after blinking %s times is: %s", BLINKING_AMOUNT, result));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Error: could not split the stone due to a uneven number of digits: %s", e.toString()));
            return;
        } catch (RuntimeErrorException e) {
            System.err.println(String.format("Error: the split of a stone did not produce two stones: %s", e.toString()));
            return;  
        }
    }
}