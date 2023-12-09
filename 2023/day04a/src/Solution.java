import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class Solution
{
    
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

        Set<Integer> winningNumbers = new HashSet<Integer>();
        Set<Integer> myNumbers = new HashSet<Integer>();

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
                    {
                        myNumbers.add(Integer.parseInt(number.replace(" ", ""))); 
                    }
                }

                winningNumbers.retainAll(myNumbers);
                winningNumberSize = winningNumbers.size();

                if (winningNumberSize >= 1)
                {
                    if (winningNumberSize == 1)
                        res += 1;
                    else
                        res += (2 << (winningNumberSize - 2));
                }
                winningNumbers.clear();
                myNumbers.clear();
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