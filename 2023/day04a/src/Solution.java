import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class Solution
{
    
    
    /**
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        int gameCounter = 1;
        int res = 0;

        Set<Integer> winningNumbers = new HashSet<Integer>();

        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            
            while(myReader.hasNextLine())
            {
                System.out.println("Game: " + gameCounter);
                String data = myReader.nextLine();

                String[] splitData = data.split("\\|");
                
                String[] winningNumbersString = splitData[0].split(":")[1].stripLeading().split(" ");

                for (String number : winningNumbersString)
                {
                    //TODO: NOT WORKING SOMETHING IS WRONG (WHITESPACES ARE NOT REMOVED?)
                    if ((!number.equals(" ")) || (!number.equals("\n")))
                        winningNumbers.add(Integer.parseInt(number.replace(" ", ""))); 
                    
                        System.out.println(number.replace(" ", "").replace("\n", ""));

                }
                gameCounter++;
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