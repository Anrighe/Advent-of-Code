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

    public int getCharOccurrences(String hand, int charPosition)
    {
        int res = 0;

        for (int i = 0; i < hand.length(); ++i)
        {
            if (hand.charAt(i) == hand.charAt(charPosition))
                ++res;
        }

        return res;
    }

    public int findHandValue(String hand)
    {
        int charOccurrencesCount;

        for (int i = 0; i < hand.length(); ++i)
        {
            charOccurrencesCount = getCharOccurrences(hand, i);
            System.out.println("charOccurrencesCount of " + hand.charAt(i) + " is " + charOccurrencesCount);
            switch (charOccurrencesCount) 
            {
                case fiveOfAKind:
                    return fiveOfAKind;
                
                case fourOfAKind:
                    return fourOfAKind;



            
                default:
                    return 22222;
            }
            
        }
        return -1;

    }
    
    /**
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        Solution solution = new Solution();
        long res = 0;

        // hand: (bettedAmount, handValue)
        Map <String, Pair<Integer, Integer>> handValues = new HashMap<String, Pair<Integer, Integer>>(); 

        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";
            String[] splitData;

            while (myReader.hasNextLine())
            {
                data = myReader.nextLine();

                splitData = data.split(" ");
                handValues.put(splitData[0], new Pair <Integer, Integer> (Integer.parseInt(splitData[1]), solution.findHandValue(splitData[0])));

                System.out.println("Hand: " + splitData[0] + " - " + splitData[1] + " - " + solution.findHandValue(splitData[0]));
            }


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