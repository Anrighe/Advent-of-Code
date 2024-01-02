import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.util.*;


public class Solution {
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
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int res = 0;
        final String destination = "ZZZ";
        String currentPosition = "AAA";

        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";
            String currentKey;
            String currentValue1 = "";
            String currentValue2 = "";

            List<String> instructions = new ArrayList<String>();

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
                } 
            }

            for (int i = 0; i < instructions.size(); ++i) {
                res++;

                if (instructions.get(i).equals("L"))
                    currentPosition = networkStructure.get(currentPosition).getKey();

                else if (instructions.get(i).equals("R"))
                    currentPosition = networkStructure.get(currentPosition).getValue();                

                if (currentPosition.equals(destination))
                    break;

                if (i == instructions.size() - 1)
                    i = -1;
            }

            myReader.close();
            System.out.println("Result: " + res);
        }
        catch (FileNotFoundException e) {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}