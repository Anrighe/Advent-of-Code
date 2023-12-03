import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Solution 
{
    /**
     * A game is considered possible if the number of cubes of each color is at maximum:
     * - 12 Red Cubes
     * - 13 Green Cubes
     * - 14 Blue Cubes
     * The result is the sum of game numbers where the following conditions
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) 
    {
        final int MAX_RED = 12;
        final int MAX_GREEN = 13;
        final int MAX_BLUE = 14;

        int gameNumber = 1;
        boolean gamePossible;
        int result = 0;

        try 
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) 
            {
                gamePossible = true;

                String data = myReader.nextLine();

                String gameRegex = "^Game [0-9]+:";
                data = data.replaceAll(gameRegex, "");

                String[] splitData = data.split(";");

                Pattern redPattern = Pattern.compile("[ ]*[0-9]+ red");
                Pattern greenPattern = Pattern.compile("[ ]*[0-9]+ green");
                Pattern bluePattern = Pattern.compile("[ ]*[0-9]+ blue");

                Matcher redMatcher;
                Matcher greenMatcher;
                Matcher blueMatcher;
                
                for (String t : splitData) // For each set of revealed cubes
                {
                    redMatcher = redPattern.matcher(t);
                    greenMatcher = greenPattern.matcher(t);
                    blueMatcher = bluePattern.matcher(t);

                    if (redMatcher.find()) 
                    {
                        String[] splitRed = redMatcher.group(0).stripLeading().split(" ");
                        
                        int redNumber = Integer.parseInt(splitRed[0]);
                        if (redNumber > MAX_RED)
                            gamePossible = false;
                    }
                    if (gamePossible && greenMatcher.find()) 
                    {
                        String[] splitGreen = greenMatcher.group(0).stripLeading().split(" ");
                        int greenNumber = Integer.parseInt(splitGreen[0]);
                        if (greenNumber > MAX_GREEN)
                            gamePossible = false;
                    }
                    if (gamePossible && blueMatcher.find()) 
                    {
                        String[] splitBlue = blueMatcher.group(0).stripLeading().split(" ");
                        int blueNumber = Integer.parseInt(splitBlue[0]);
                        if (blueNumber > MAX_BLUE)
                            gamePossible = false;
                    }
                }

                if (gamePossible)
                    result += gameNumber;

                gameNumber++;
            }
            myReader.close();

            System.out.println("Result: " + result);
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}