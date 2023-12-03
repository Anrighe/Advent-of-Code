import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Solution 
{

    // 12 Red Cubes
    // 13 Green Cubes
    // 14 Blue Cubes


    public static void main(String[] args) 
    {

        System.out.println("Hello, World!");

        int gameNumber = 1;

        try 
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) 
            {
                System.out.println("Game " + gameNumber + ":");
                String data = myReader.nextLine();

                String gameRegex = "^Game [0-9]+:";
                data = data.replaceAll(gameRegex, "");

                String[] splitData = data.split(";");
                for (String t : splitData)
                {
                    System.out.println(t);
                }




                gameNumber++;
            }
            
            
            
            
            
            
            
            
            
            
            
            myReader.close();
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("Was not able to locate the input file.");
            e.printStackTrace();
        } 

 
    }
 }