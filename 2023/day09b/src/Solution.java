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
        boolean negative = true;

        while (!allElementsAreZero(currentSequence)) {

            List<Integer> newSequence = new ArrayList<Integer>();
            for (int i = 1; i < currentSequence.size(); i++) {
                newSequence.add(currentSequence.get(i) - currentSequence.get(i - 1));
            }
            allSequences.add(newSequence);
            currentSequence = newSequence;
        }

        // After all the sequences have been calculated, we need to iterate backwards and find from the sequence with only zeroes
        // the first element of the first sequence by subtracting each time

        System.out.print("Seqence: ");
        for (var a : sequence) {
            System.out.print(a + " ");
        }
        System.out.println();



        for (int i = allSequences.size() - 1; i >= 0; i--) {
            
            List<Integer> seq = allSequences.get(i);

            System.out.print("Seqence: ");
            for (var a : seq) {
                System.out.print(a + " ");
            }
            System.out.println();
            
            
            System.out.println(seq.get(0) + " - " + sum + " = " + (seq.get(0) - sum));
            sum = seq.get(0) - sum;
        }

        // print allSequences
        /*
        System.out.println("All sequences: ");
        for (List<Integer> seq : allSequences) {
            for (int s : seq) {
                System.out.print(s + " ");
            }
            System.out.println();
        } */
        System.out.println("-------------");



        System.out.println("Sum: " + sum);
        
        return sum;


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
    }
}