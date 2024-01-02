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
            if (s != 0) {
                return false;
            }
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
        int sum = 0;

        while (!allElementsAreZero(currentSequence)) {

            List<Integer> newSequance = new ArrayList<Integer>();
            for (int i = 1; i < currentSequence.size(); i++)
                newSequance.add(currentSequence.get(i) - currentSequence.get(i - 1));

            currentSequence = newSequance;
            sum += currentSequence.get(currentSequence.size() - 1);
        }

        return sequence.get(sequence.size() - 1) + sum;
    }


    /**
     * In the above dataset, the first history is 0 3 6 9 12 15. Because the values increase by 3 each step, the first sequence of differences that you generate will be 3 3 3 3 3. Note that this sequence has one fewer value than the input sequence because at each step it considers two numbers from the input. Since these values aren't all zero, repeat the process: the values differ by 0 at each step, so the next sequence is 0 0 0 0. This means you have enough information to extrapolate the history! Visually, these sequences can be arranged like this:
     * 
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