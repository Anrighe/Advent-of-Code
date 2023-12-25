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

        for (int i = 0; i < hand.length(); ++i)
        {
            if (viewedChars.contains(hand.charAt(i)) == false)
            {                
                viewedChars.add(hand.charAt(i));
                charOccurrencesCount = getCharOccurrences(hand, i);
                
                if (charOccurrencesCount == 2)
                    ++pairCount;
                else if (charOccurrencesCount == 3)
                    ++threeOfAKindCount;
            }
                
            if (charOccurrencesCount == 5)
                return fiveOfAKind;
            
            if (charOccurrencesCount == 4)
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

    /*
     * Calculates the total winnings of the set of hands based on the following rules:
     * 
     * 1. Each hand can be one of the following types:
     *  - Five of a kind, where all five cards have the same label: AAAAA
     *  - Four of a kind, where four cards have the same label and one card has a different label: AA8AA
     *  - Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
     *  - Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98    
     *  - Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
     *  - One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
     *  - High card, where all cards' labels are distinct: 23456
     * 
     * 2. Hands are primarily ordered based on type; for example, every full house is stronger than any three of a kind.
     * 
     * 3. If two hands have the same type, a second ordering rule takes effect. Start by comparing the first card in each hand. 
     *  If these cards are different, the hand with the stronger first card is considered stronger. 
     *  If the first card in each hand have the same label, however, then move on to considering the second card in each hand. 
     *  If they differ, the hand with the higher second card wins; otherwise, continue with the third card in each hand, 
     *  then the fourth, then the fifth.
     * 
     * The total winnings are calculated by adding up the result of multiplying each hand's bid with its rank, 
     *  where the weakest hand gets rank 1.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        Solution solution = new Solution();
        long res = 0;

        // Key(hand): Value(bettedAmount, handValue)
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
            }

            List<String> orderedHandValues = new ArrayList<String>(handValues.keySet());

            Comparator<String> handValuesComparator = new HandValuesComparator(handValues);
            Collections.sort(orderedHandValues, handValuesComparator);

            int rank = 1;

            for (String hand : orderedHandValues)
            {
                res += rank * handValues.get(hand).getKey();
                rank++;
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