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

    /*
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

            while (myReader.hasNextLine())
            {
                data = myReader.nextLine();

                splitData = data.split(" ");

                System.out.println("Data: " + data);
                
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