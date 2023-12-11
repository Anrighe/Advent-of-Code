import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Solution
{

    
    /**
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {

        int res = 0;

        List<Long> seeds = new ArrayList<Long>();

        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";

            if (myReader.hasNextLine())
            {
                data = myReader.nextLine();
                String[] splitData = data.split(":");
                for (String element : splitData[1].stripLeading().split(" "))
                    seeds.add(Long.parseLong(element));    
            }
            
            while(myReader.hasNextLine())
            {
                data = myReader.nextLine();


                // TODO: doesn't really stop when it should - Fix this
                if (data.contains("seed-to-soil map:"))
                {
                    System.out.println("Seed to soil map:");
                    while (myReader.hasNextLine() && ((data != "\n") || (data != "") || (data != " ")))
                    {
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                }


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