import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.util.*;


public class Solution {

    /**
     * Calculates the greatest common divisor (GCD) of two numbers using Euclid's algorithm.
     *
     * @param n1 the first number
     * @param n2 the second number
     * @return the GCD of n1 and n2
     */
    public static long gcdByEuclidsAlgorithm(long n1, long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, n1 % n2);
    }

    /*
     * By following the input instructions, find the number of instruction needed in order to reach "ZZZ" from "AAA".
     * 
     * Example: 
     *  RL
     * 
     *  AAA = (BBB, CCC)
     *  BBB = (DDD, EEE)
     *  CCC = (ZZZ, GGG)
     *  DDD = (DDD, DDD)
     *  EEE = (EEE, EEE)
     *  GGG = (GGG, GGG)
     *  ZZZ = (ZZZ, ZZZ)
     * 
     * Based on the current position (e.g. AAA), the next position is determined by the instructions (e.g. RL)
     * and the available paths (e.g. (BBB, CCC) for "AAA").
     * Each time a "L" instruction is encountered, the first path is taken (e.g. "BBB" in the "AAA" position).
     * Each time a "R" instruction is encountered, the second path is taken (e.g. "CCC" in the "AAA" position).
     * If the instructions are terminated and the destination has not yet been reached, the instructions are repeated.
     * 
     * ---------------------------------------------------------------------------------------------------------------
     * 
     * Second part of the problem:
     * 
     * Each position that ends with "A" is now a starting position.
     * From each starting position, a ghost must be moved simultaneously to reach any destination that ends in "Z".
     * It will find the amount of instructions needed to reach the destination for each ghost in each starting position.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Long res = 0L;
        String currentPosition = "AAA";

        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";
            String currentKey;
            String currentValue1 = "";
            String currentValue2 = "";

            List<String> instructions = new ArrayList<String>();

            List<String> startingPositions = new ArrayList<String>();

            List<Long> results = new ArrayList<Long>();

            if (myReader.hasNextLine()) {
                data = myReader.nextLine();
                for (String s : data.split("(?!^)"))
                    instructions.add(s);
            }

            Map<String, Pair<String, String>> networkStructure = new HashMap<String, Pair<String, String>>();

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();

                if (!data.equals("")) {
                    
                    currentKey = data.split(" ")[0];                    
                    
                    currentValue1 = data.split("\\(")[1].replaceAll("\\)", "").replaceAll(" ", "").split(",")[0]; 
                    currentValue2 = data.split("\\(")[1].replaceAll("\\)", "").replaceAll(" ", "").split(",")[1];
    
                    if (!networkStructure.containsKey(currentKey))
                        networkStructure.put(currentKey, new Pair<String, String>(currentValue1, currentValue2));

                    if (currentKey.charAt(2) == 'A')
                        startingPositions.add(currentKey);
                } 
            }

            for (int j = 0; j < startingPositions.size(); ++j) {
                System.out.println("j: " + j);

                currentPosition = startingPositions.get(j);

                for (int i = 0; i < instructions.size(); ++i) {
                    res++;
    
                    if (instructions.get(i).equals("L"))
                        currentPosition = networkStructure.get(currentPosition).getKey();
    
                    else if (instructions.get(i).equals("R"))
                        currentPosition = networkStructure.get(currentPosition).getValue();                
    
                    if (currentPosition.charAt(2) == 'Z')
                        break;
    
                    if (i == instructions.size() - 1)
                        i = -1;
                    
                }
                results.add(res);
                res = 0L;
            }

            long lcm = results.get(0);

            for (int i = 1; i < results.size(); ++i) {
                lcm = (lcm * results.get(i)) / gcdByEuclidsAlgorithm(lcm, results.get(i));
            }

            myReader.close();
            System.out.println("Result: " + lcm);
        }
        catch (FileNotFoundException e) {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}