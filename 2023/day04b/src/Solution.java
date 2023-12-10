import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Solution
{
    /**
     * Returns the total number of cards (euqal to the number of lines) in the input file.
     *
     * @return the total number of cards
     */
    public static int getTotalCards()
    {
        int totalCards = 0;
        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            
            while(myReader.hasNextLine())
            {
                myReader.nextLine();
                totalCards++;
            }
            myReader.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
        return totalCards;
    }
    
    /**
     * Reads the input file and for each line (game), it calculates the number total cards.
     * Each game is displayed as follows: "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
     * where the first part separated by the pipe (|) is the list of winning numbers 
     * and the second part is the list of numbers on the card.
     * Each time a card has a winning number on it, other copies of the next scratchcards are won equal to the number of matches.
     * 
     * In the previous example: "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
     * Since there are 4 matching numbers, 4 copies of the next card (2, 3, 4, 5) are won.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {

        int res = 0;
        int winningNumberSize;
        int currentCard = 1;
        int totalCards = getTotalCards();

        Set<Integer> winningNumbers = new HashSet<Integer>();
        Set<Integer> myNumbers = new HashSet<Integer>();

        Map<Integer, Integer> cardMap = new HashMap<Integer, Integer>();
        for (int i = 1; i <= totalCards; ++i)
            cardMap.put(i, 1);

        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            
            while(myReader.hasNextLine())
            {
                String data = myReader.nextLine();

                String[] splitData = data.split("\\|");
                String[] winningNumbersString = splitData[0].split(":")[1].stripLeading().split(" ");

                for (String number : winningNumbersString)
                {
                    if ((!number.equals(" ")) && (!number.equals("")))
                        winningNumbers.add(Integer.parseInt(number.replace(" ", ""))); 
                }

                String [] myNumbersString = splitData[1].stripLeading().split(" ");
                for (String number : myNumbersString)
                {
                    if ((!number.equals(" ")) && (!number.equals("")))
                        myNumbers.add(Integer.parseInt(number.replace(" ", "")));
                }

                winningNumbers.retainAll(myNumbers);
                winningNumberSize = winningNumbers.size();

                for (int k = 0; k < cardMap.get(currentCard); ++k)
                {
                    for (int i = currentCard+1; i <= currentCard+winningNumberSize && i <= totalCards; ++i)
                        cardMap.put(i, (cardMap.get(i) + 1));
                }

                winningNumbers.clear();
                myNumbers.clear();

                currentCard++;
            }
            
            for (var entry : cardMap.entrySet())
                res += entry.getValue();
            
            myReader.close();


            System.out.println("Total number of scratchcards: " + res);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}