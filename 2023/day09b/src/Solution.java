import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Solution {

    /**
     * Checks if all elements in the given sequence are zero.
     *
     * @param sequence the list of integers to check
     * @return true if all elements are zero, false otherwise
     */
    private static boolean allElementsAreZero(List<Integer> sequence) {
        for (int s : sequence) {
            if (s != 0)
                return false;
        }
        return true;
    }

    /**
     * Calculates the next value in the sequence based on the given input sequence
     *
     * @param sequence the input sequence
     * @return the next value in the sequence
     */
    public static int getNextValue(List<Integer> sequence) {
        List<Integer> currentSequence = sequence;
        List<List<Integer>> allSequences = new ArrayList<List<Integer>>();
        allElementsAreZero(currentSequence);
        int first = 0;

        while (!allElementsAreZero(currentSequence)) {

            List<Integer> newSequence = new ArrayList<Integer>();
            for (int i = 1; i < currentSequence.size(); i++)
                newSequence.add(currentSequence.get(i) - currentSequence.get(i - 1));

            allSequences.add(newSequence);
            currentSequence = newSequence;
        }

        // After all the sequences have been calculated, we need to iterate backwards and find from 
        // the sequence with only zeroes the first element of the first sequence by subtracting each time
        for (int i = allSequences.size() - 1; i >= 0; i--) {
            List<Integer> seq = allSequences.get(i);
            first = seq.get(0) - first;
        }
        return sequence.get(0) - first;
    }


    /**
     * Based on the given sequences, calculates and sum all the next value in each sequence.
     * Example: 
     * Given the sequence "0 3 6 9 12 15" it must calculate first each differences between each element:
     *                      3 3 3 3  3
     * Then, if the differences are still not all zero, keep going:
     *                       0 0 0 0
     * When they are all zero, the next value in the sequence is the last element of the original sequence
     *  plus the sum of the last differences sequence:
     *   0 + 3 + 15 = 18
     * 
     * So the next value in the sequence is 18.
     * 
     * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     * 
     * In the second part of the problem we need to calculate the first value of the sequence instead of the last,
     *  then sum them all up and return the result.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int res = 0;

        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                var stringSequence = myReader.nextLine().split(" ");
                res += getNextValue(Arrays.stream(stringSequence).map(Integer::parseInt).collect(Collectors.toList()));
            }

            myReader.close();
            System.out.println(String.format("Result: %d", res));
        }
        catch (FileNotFoundException e) {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}