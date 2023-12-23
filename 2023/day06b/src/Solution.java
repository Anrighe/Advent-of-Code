import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution
{
    /**
     * Based on the puzzle input, calculates how many different ways there are to win a boat race.
     * For each millisecond a button is hold, the boat will move 1 millimeter faster per millisecond.
     * 
     * The puzzle input describes the time available for the race and the current record it needs to be beaten.
     * Example:
     * -----------------------
     *  Time:      7  15   30
     *  Distance:  9  40  200
     * -----------------------
     * This example describes three races, where in the first race the time available is 7 milliseconds 
     * and the record to beat is 9 millimeters.
     * 
     * If the button is hold for 1 millisecond, the boat will move 1 millimeter per millisecond for 6 milliseconds,
     * (the boat will travel for 6 millimeters) so it will not beat the record.
     * If the button is hold for 2 milliseconds, the boat will move 2 millimeters per millisecond for 5 milliseconds,
     * (the boat will travel for 10 millimeters) so it will not beat the record.
     * 
     * In the second part of the problem there is only one race and the numbers must be read as a 
     * single number. 
     *  Example: "7  15   30" need to be considered as the number 71530.
     * 
     * 
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        long res = 0;

        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";
            String[] splitData;

            long times = 0L;
            long distances = 0L;

            if (myReader.hasNextLine()) // Time
            {
                data = myReader.nextLine();
                splitData = data.split(":");
                splitData[1] = splitData[1].stripLeading();
                splitData[1] = splitData[1].replaceAll("\\s+", "");

                times = Long.parseLong(splitData[1]);
            }
            
            if (myReader.hasNextLine()) // Distance
            {
                data = myReader.nextLine();
                splitData = data.split(":");
                splitData[1] = splitData[1].stripLeading();
                splitData[1] = splitData[1].replaceAll("\\s+", "");

                distances = Long.parseLong(splitData[1]);
            }

            long speed_mm_ms;
            long remainingTime;
            long traveledDistance;

            long numberOfWaysToWin;

            numberOfWaysToWin = 0;
            for (long k = 0; k < times; ++k)
            {
                
                speed_mm_ms = k;
                remainingTime = times - k;
                traveledDistance = speed_mm_ms * remainingTime;

                if (traveledDistance > distances)
                    numberOfWaysToWin++;
            }
            
            if (res != 0)
                res *= numberOfWaysToWin;
            else
                res = numberOfWaysToWin;
            

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