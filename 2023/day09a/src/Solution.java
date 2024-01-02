import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.util.*;


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

    public static int getNextValue(List<Integer> sequence) {
        List<Integer> currentSequence = sequence;

        List<List<Integer>> allSequences = new ArrayList<List<Integer>>();
        allElementsAreZero(currentSequence);

        int sum = 0;

        while (!allElementsAreZero(currentSequence)) {

            List<Integer> newSequance = new ArrayList<Integer>();
            for (int i = 1; i < currentSequence.size(); i++) {
                newSequance.add(currentSequence.get(i) - currentSequence.get(i - 1));

            }
            allSequences.add(newSequance);

            currentSequence = newSequance;

            sum += currentSequence.get(currentSequence.size() - 1);
        }

        // print allSequences
        System.out.println("All sequences: ");
        for (List<Integer> seq : allSequences) {
            for (int s : seq) {
                System.out.print(s + " ");
            }
            System.out.println();
        }

        return sequence.get(sequence.size() - 1) + sum;
    }


    /**
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
        catch (AssertionError ae) {
            System.err.println("Assertion error: something went wrong while collecting input data!");
            ae.printStackTrace();
        }
    }
}