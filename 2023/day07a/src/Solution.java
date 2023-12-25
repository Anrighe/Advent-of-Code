import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
        int charOccurrencesCount = 0;
        Set <Character> viewedChars = new HashSet<Character>();

        int pairCount = 0;
        int threeOfAKindCount = 0;
        //System.out.println("hand: " + hand);
        for (int i = 0; i < hand.length(); ++i)
        {
            //System.out.println("i: " + i);
            if (viewedChars.contains(hand.charAt(i)) == false)
            {                
                //System.out.println("char " + hand.charAt(i) + " is not in the list for the hand " + hand);
                viewedChars.add(hand.charAt(i));
                charOccurrencesCount = getCharOccurrences(hand, i);
                //System.out.println("charOccurrencesCount of " + hand.charAt(i) + " is " + charOccurrencesCount);
                
                if (charOccurrencesCount == 2)
                ++pairCount;
                else if (charOccurrencesCount == 3)
                    ++threeOfAKindCount;
            }
            //else
                //System.out.println("char " + hand.charAt(i) + " is already in the list for the hand " + hand);

            //System.out.println(viewedChars);
                

            if (charOccurrencesCount == fiveOfAKind)
                return fiveOfAKind;
            
            if (charOccurrencesCount == fourOfAKind)
                return fourOfAKind;
    
        }

        if (pairCount == 1 && threeOfAKindCount == 1)
            return fullHouse;
        else if (threeOfAKindCount == 1)
            return threeOfAKind;
        else if (pairCount == 2)
            return twoPair;
        else if (pairCount == 1)
            return onePair;
        else
            return highCard;
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

                //System.out.println("Hand: " + splitData[0] + " - " + splitData[1] + " - " + solution.findHandValue(splitData[0]));
            }

            for (Map.Entry<String, Pair<Integer, Integer>> entry : handValues.entrySet())
                System.out.println("Hand: " + entry.getKey() + " - " + entry.getValue().getKey() + " - " + entry.getValue().getValue());


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