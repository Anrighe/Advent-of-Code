import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution
{
    /**
     * Updates the gears coordinates in a provided map with the given row, column.
     * If the coordinates are not present in the map, they are added with the given number as value,
     * otherwise the value is multiplied with the given number.
     * 
     * Also updates the gears occurrences map with the given coordinates.
     * 
     * @param row The row of the gear.
     * @param col The column of the gear.
     * @param dataMatrix The data matrix containing the gears.
     * @param number The number to update the gear with.
     * @param gears The map of gear coordinates to their values.
     * @param gearsOccurrences The map of gear coordinates to their occurrences.
     */
    public static void updateGears(int row, int col, char[][] dataMatrix, int number, Map<List<Integer>, Integer> gears, Map<List<Integer>, Integer> gearsOccurrences)
    {
        List<Integer> gearCoordinates = new ArrayList<Integer>();
        gearCoordinates.add(row);
        gearCoordinates.add(col);

        if (gearsOccurrences.containsKey(gearCoordinates))
            gearsOccurrences.put(gearCoordinates, gearsOccurrences.get(gearCoordinates) + 1);
        else
            gearsOccurrences.put(gearCoordinates, 1);

        if (gears.containsKey(gearCoordinates))
            gears.put(gearCoordinates, gears.get(gearCoordinates) * number);
        else
            gears.put(gearCoordinates, number);
    }

    /**
     * Checks if a number in the data matrix is part of a gear.
     * To be part of a gear a number must be adjacent to a star "*" symbol, and the star symbol must be adjacent to another number.
     * If the number is part of a gear, the coordinates of the gear are returned, otherwise null is returned.
     * 
     * @param current_row The current row index
     * @param numberStartIndex The starting index of the number
     * @param numberEndIndex The ending index of the number
     * @param dataMatrix The 2D character array representing the data matrix
     * @param rowCount The total number of rows in the data matrix
     * @param rowLength The length of each row in the data matrix
     * @return a List of integers representing the coordinates of the gear adjacent to the number, null if no gear is found
     */
    public static List<Integer> getGearAdjacentToNumberCoordinates(int current_row, int numberStartIndex, int numberEndIndex, char[][] dataMatrix, int rowCount, int rowLength, Map<List<Integer>, Integer> gears)
    {
        if (current_row > 0) // Top
        {
            for (int i = numberStartIndex; i <= numberEndIndex; i++)
            {
                if (dataMatrix[current_row - 1][i] == '*')
                    return List.of(current_row - 1, i);
            }
        }

        if (current_row < rowCount - 1) // Bottom
        {
            for (int i = numberStartIndex; i <= numberEndIndex; i++)
            {
                if (dataMatrix[current_row + 1][i] == '*')
                    return List.of(current_row + 1, i);
            }
        }

        if (numberStartIndex > 0)
        {
            if (dataMatrix[current_row][numberStartIndex - 1] == '*') // Left
                return List.of(current_row, numberStartIndex - 1);
        
            if (current_row > 0 && dataMatrix[current_row - 1][numberStartIndex - 1] == '*') // Bottom-right
                return List.of(current_row - 1, numberStartIndex - 1);

            if (current_row < rowCount - 1 && dataMatrix[current_row + 1][numberStartIndex - 1] == '*') // Bottom-left
                return List.of(current_row + 1, numberStartIndex - 1);
        }
        
        if (numberEndIndex < rowLength - 1)
        {
            if (dataMatrix[current_row][numberEndIndex + 1] == '*') // Right
                return List.of(current_row, numberEndIndex + 1);
            
            if (current_row > 0 && dataMatrix[current_row - 1][numberEndIndex + 1] == '*') // Top-right
                return List.of(current_row - 1, numberEndIndex + 1);

            if (current_row < rowCount - 1 && dataMatrix[current_row + 1][numberEndIndex + 1] == '*') // Bottom-right
                return List.of(current_row + 1, numberEndIndex + 1);
        }

        return null;
    }

    /**
     * Returns the number of rows in the input file.
     *
     * @return The number of rows in the input file.
     */
    public static int getFileRowCount()
    {
        int rows = 0;
        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            
            while(myReader.hasNextLine())
            {
                rows++;
                myReader.nextLine();
            }

            myReader.close();
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }

        return rows;
    }

    public static int getFileRowLength()
    {
        int length = 0;
        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
    
            if (myReader.hasNextLine())
            {
                length = myReader.nextLine().length();
                myReader.nextLine();
            }

            myReader.close();
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
        return length;
    }
    
    /**
     * Sums the product of the number and the adjacent gear value to the result.
     * In this example there are two gears (" * "):
     *      
     *      467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
        
        The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. 
        The second gear is in the lower right; its gear ratio is 451490. 
        (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) 
        Adding up all of the gear ratios produces 467835.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        int current_row = 0;
        int res = 0;
        
        Map<List<Integer>, Integer> gears = new HashMap<List<Integer>, Integer>(); // key: list of x,y coordinates, value: gear value
        Map<List<Integer>, Integer> gearsOccurrences = new HashMap<List<Integer>, Integer>(); // key: list of x,y coordinates, value: number of times the gear appears

        try
        {
            int rowCount = getFileRowCount();
            int rowLength = getFileRowLength(); // implying all rows have the same length
            
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            
            char[][] dataMatrix = new char[rowCount][rowLength];
            
            while(myReader.hasNextLine())
            {
                String data = myReader.nextLine();
                
                for(int i = 0; i < data.length(); i++)
                    dataMatrix[current_row][i] = data.charAt(i);

                current_row++;
            }

            myReader.close();

            boolean foundNumber = false;
            int numberStartIndex = 0;
            int numberEndIndex = 0;
            int number = 0;
            for (int i = 0; i < dataMatrix.length; i++)
            {
                foundNumber = false;
                for (int j = 0; j < dataMatrix[i].length; j++)
                {
                    if (!foundNumber && Character.isDigit(dataMatrix[i][j]))
                    {
                        foundNumber = true;
                        numberStartIndex = j;
                        numberEndIndex = j;
                    }
                    else if (foundNumber && Character.isDigit(dataMatrix[i][j]))
                    {
                        numberEndIndex = j;

                        if (j == dataMatrix[i].length - 1)
                        {
                            foundNumber = false;
                            String numberString = "";
                            
                            for (int k = numberStartIndex; k <= numberEndIndex; k++)
                                numberString += dataMatrix[i][k];
                            
                            number = Integer.parseInt(numberString);
                            
                            List<Integer> coords = getGearAdjacentToNumberCoordinates(i, numberStartIndex, numberEndIndex, dataMatrix, rowCount, rowLength, gears);
                            if (coords != null)
                                updateGears(coords.get(0), coords.get(1), dataMatrix, number, gears, gearsOccurrences);
                        }
                    }
                    else if (foundNumber && !Character.isDigit(dataMatrix[i][j]))
                    {
                        foundNumber = false;
                        String numberString = "";
                        
                        for (int k = numberStartIndex; k <= numberEndIndex; k++)
                        numberString += dataMatrix[i][k];
                        
                        number = Integer.parseInt(numberString);
                        
                        List<Integer> coords = getGearAdjacentToNumberCoordinates(i, numberStartIndex, numberEndIndex, dataMatrix, rowCount, rowLength, gears);
                        if (coords != null)
                            updateGears(coords.get(0), coords.get(1), dataMatrix, number, gears, gearsOccurrences);
                    }    
                }
            }

            for (var el : gears.entrySet())
            {
                if (gearsOccurrences.get(el.getKey()) > 1)
                    res += el.getValue();
            }

            System.out.println("Result: " + res);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}