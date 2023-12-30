import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javafx.util.*;

public class Solution {
    
    /*
    public static String getNextInstruction(List<String> instructions, int currentInstructionIndex) {
        if ()
    } */

    /*
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int res = 0;

        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";
            String[] splitData;
            String currentKey;
            String currentValue1 = "";
            String currentValue2 = "";

            List<String> instructions = new ArrayList();
            int currentStep = 0;

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
                    
                    currentValue1 = data.split("\\(")[1].replace("\\)", "").replace(" ", "").split(",")[0]; 
                    currentValue2 = data.split("\\(")[1].replace("\\)", "").replace(" ", "").split(",")[0]; 
    
                    if (!networkStructure.containsKey(currentKey)) {
                        networkStructure.put(currentKey, new Pair<String, String>(currentValue1, currentValue2));
                        System.out.println("Added " + currentKey + " with values " + currentValue1 + " and " + currentValue2);
                    }
                }

                
            }



            myReader.close();
            System.out.println("Result: " + res);
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