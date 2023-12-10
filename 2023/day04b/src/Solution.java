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
     * Returns the total number of cards in the input file.
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
     * Reads the input file and for each line (game), it calculates the number of points.
     * For the first winning number it gives 1 point, for the second 2 points, for the third 4 points, etc.
     * Each game is displayed as follows: "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
     * where the first part separated by the pipe (|) is the list of winning numbers 
     * and the second part is the list of numbers on the card.
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
        {
            cardMap.put(i, 1);
        }

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


                for (int i = currentCard+1; i <= currentCard+winningNumberSize && i <= totalCards; ++i)
                {
                    cardMap.put(i, (cardMap.getOrDefault(i, 1) + 1) * cardMap.get(currentCard));
                }



                /*

                    System.err.println("The number of winning numbers (" + winningNumberSize + ") plus the current card (" + currentCard + ") is greater than the total number of cards (" + totalCards + ").");
                    cardMap.put(currentCard, cardMap.get())
                    //res += totalCards - currentCard; // +1 ?????
                }
                else
                {
                    //res += winningNumberSize;
                    //System.err.println("Card " + currentCard + ": " + splitData[0] + " | " + splitData[1] + " | " + winningNumberSize);
                }*/

                winningNumbers.clear();
                myNumbers.clear();

                currentCard++;
            }

            for (var entry : cardMap.entrySet())
            {
                System.err.println("Card " + entry.getKey() + ": " + entry.getValue());
            }

            myReader.close();


            System.out.println("Result: " + res);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}