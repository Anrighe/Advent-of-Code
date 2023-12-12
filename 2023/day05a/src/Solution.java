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
                        if (data == "")
                            break;
                            
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                }

                if (data.contains("soil-to-fertilizer map:"))
                {
                    System.out.println("soil-to-fertilizer map:");
                    while (myReader.hasNextLine() && ((data != "\n") || (data != "") || (data != " ")))
                    {
                        if (data == "")
                            break;
                            
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                }

                if (data.contains("fertilizer-to-water map:"))
                {
                    System.out.println("fertilizer-to-water map:");
                    while (myReader.hasNextLine() && ((data != "\n") || (data != "") || (data != " ")))
                    {
                        if (data == "")
                            break;
                            
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                }

                if (data.contains("water-to-light map:"))
                {
                    System.out.println("water-to-light map:");
                    while (myReader.hasNextLine() && ((data != "\n") || (data != "") || (data != " ")))
                    {
                        if (data == "")
                            break;
                            
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                }

                if (data.contains("light-to-temperature map:"))
                {
                    System.out.println("light-to-temperature map:");
                    while (myReader.hasNextLine() && ((data != "\n") || (data != "") || (data != " ")))
                    {
                        if (data == "")
                            break;
                            
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                }

                if (data.contains("temperature-to-humidity map:"))
                {
                    System.out.println("temperature-to-humidity map:");
                    while (myReader.hasNextLine() && ((data != "\n") || (data != "") || (data != " ")))
                    {
                        if (data == "")
                            break;
                            
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                }

                if (data.contains("humidity-to-location map:"))
                {
                    System.out.println("humidity-to-location map:");
                    while (myReader.hasNextLine() && ((data != "\n") || (data != "") || (data != " ")))
                    {
                        if (data == "")
                            break;
                            
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