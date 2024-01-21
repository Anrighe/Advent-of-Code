import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javafx.util.Pair;

public class Solution {


    // ?#?#?#?#?#?#?#? 1,3,1,6

    public static int findNumberOfPossibleArrangements(String springStauts, List<Integer> damagedSprings) {
        int res = 0;


        return res;
    }


    /**
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int res = 0;

        String inputFile = "input.txt";


        try {
            File myObj = new File(inputFile);
            Scanner myReader = new Scanner(myObj);

            String line;
            String springStauts;
            List<Integer> damagedSprings;

            String [] tmp;
            

            while (myReader.hasNextLine()) {
                line = myReader.nextLine();
                springStauts = line.split(" ")[0];
                
                tmp = line.split(" ")[1].split(",");
                damagedSprings = new ArrayList<Integer>();

                for (String element : tmp)
                    damagedSprings.add(Integer.parseInt(element));
                System.out.println("Damaged springs: " + damagedSprings.toString());

                res += findNumberOfPossibleArrangements(springStauts, damagedSprings);
            }


            myReader.close();
            System.out.println(String.format("Result: %d", res));
        }
        catch (FileNotFoundException e) {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}