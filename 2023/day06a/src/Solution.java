import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Solution
{
    
    /**
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        int res = 0;

        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";
            String[] splitData;

            int times[] = null;
            int distances[] = null;

            if (myReader.hasNextLine()) // Time
            {
                data = myReader.nextLine();
                splitData = data.split(":");
                splitData[1] = splitData[1].stripLeading();
                splitData[1] = splitData[1].replaceAll("\\s+", " ");
                System.out.println(splitData[1]);
                
                times = Arrays.stream(splitData[1].split(" ")).mapToInt(Integer::parseInt).toArray();

            }
            
            if (myReader.hasNextLine()) // Distance
            {
                data = myReader.nextLine();
                splitData = data.split(":");
                splitData[1] = splitData[1].stripLeading();
                splitData[1] = splitData[1].replaceAll("\\s+", " ");
                System.out.println(splitData[1]);

                distances = Arrays.stream(splitData[1].split(" ")).mapToInt(Integer::parseInt).toArray();

            }

            assert times.length == distances.length;
            assert times != null;
            assert distances != null;

            for (int i = 0; i < times.length; i++)
            {
                //sa
                System.out.println();
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