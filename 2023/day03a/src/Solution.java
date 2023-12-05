import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Solution
{
    public int findFileRows()
    {
        int rows = 0;
        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
    
            while(myReader.hasNextLine())
            {

                rows++;
            }

        }
        catch(FileNotFoundException e)
        {
            System.out.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
        return rows;
    }

    public static void main(String[] args)
    {
        try
        {
            int rowCount = findFileRows();

            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);



            while(myReader.hasNextLine())
            {
                String data = myReader.nextLine();

                System.out.println(data);
            
                int length = data.length();

                char[][] data = new char[length][rowCount];


            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}