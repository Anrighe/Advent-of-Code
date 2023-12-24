import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import javafx.util.*;


public class Solution
{
    final int fiveOfAKind = 7;
    final int fourOfAKind = 6;
    final int fullHouse = 5;
    final int threeOfAKind = 4;
    final int twoPair = 3;
    final int onePair = 2;
    final int highCard = 1;
    
    /**
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        long res = 0;

        Map <String, Pair<Integer, Integer>> handValues = new HashMap<String, Pair<Integer, Integer>>(); 

        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";
            String[] splitData;


            myReader.close();
            System.out.println("Result: " + res);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
        catch (AssertionError ae)
        {
            System.err.println("Assertion error: something went wrong while collecting input data!");
            ae.printStackTrace();
        }
    }
}