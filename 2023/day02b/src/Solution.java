import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Solution 
{
    /**
     * The result is the sum of the powers calculated for each game.
     * The power of a game is calculated by finding the minimum value of each color (red, green, blue)
     * and multiplying them together.
     */
    public static void main(String[] args) 
    {
        int result = 0;
        int power = 0;

        int minRed = 1;
        int minGreen = 1;
        int minBlue = 1;

        try 
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) 
            {
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
                
                power = 0;
                minRed = 1;
                minGreen = 1;
                minBlue = 1;

                for (String t : splitData) // For each set of revealed cubes
                {
                    redMatcher = redPattern.matcher(t);
                    greenMatcher = greenPattern.matcher(t);
                    blueMatcher = bluePattern.matcher(t);

                    if (redMatcher.find()) 
                    {
                        String[] splitRed = redMatcher.group(0).stripLeading().split(" ");
                        int redNumber = Integer.parseInt(splitRed[0]);
                        
                        if (redNumber > minRed)
                            minRed = redNumber;
                    }
                    if (greenMatcher.find()) 
                    {
                        String[] splitGreen = greenMatcher.group(0).stripLeading().split(" ");
                        int greenNumber = Integer.parseInt(splitGreen[0]);
                        if (greenNumber > minGreen)
                            minGreen = greenNumber;
                    }
                    if (blueMatcher.find()) 
                    {
                        String[] splitBlue = blueMatcher.group(0).stripLeading().split(" ");
                        int blueNumber = Integer.parseInt(splitBlue[0]);
                        if (blueNumber > minBlue)
                            minBlue = blueNumber;
                    }
                }
                power = minRed * minGreen * minBlue;
                result += power;
            }
            myReader.close();

            System.out.println("The sum of the power of the sets with the minimum set of cubes used is: " + result);
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}