import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution
{
    public static boolean isNumberPartOfSchematic(int current_row, int numberStartIndex, int numberEndIndex, char[][] dataMatrix, int rowCount, int rowLength)
    {
        if (current_row > 0) // Top
        {
            for (int i = numberStartIndex; i <= numberEndIndex; i++)
            {
                if (dataMatrix[current_row - 1][i] != '.')
                    return true;

            }
        }

        if (current_row < rowCount - 1) // Bottom
        {
            for (int i = numberStartIndex; i <= numberEndIndex; i++)
            {
                if (dataMatrix[current_row + 1][i] != '.')
                    return true;

            }
        }

        if (numberStartIndex > 0)
        {
            if (dataMatrix[current_row][numberStartIndex - 1] != '.') // Left
                return true;
            
            if (current_row > 0 && dataMatrix[current_row - 1][numberStartIndex - 1] != '.') // Bottom-right
                return true;

            if (current_row < rowCount - 1 && dataMatrix[current_row + 1][numberStartIndex - 1] != '.') // Bottom-left
                return true;
        }
        
        if (numberEndIndex < rowLength - 1)
        {
            if (dataMatrix[current_row][numberEndIndex + 1] != '.') // Right
                return true;
            
            if (current_row > 0 && dataMatrix[current_row - 1][numberEndIndex + 1] != '.') // Top-right
                return true;

            if (current_row < rowCount - 1 && dataMatrix[current_row + 1][numberEndIndex + 1] != '.') // Bottom-right
                return true;
        }

        return false;
    }

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
            
            System.out.println("SSSSSSSSS");

            myReader.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Was not able to locate the input file.");
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
            System.out.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
        return length;
    }
    
    public static void main(String[] args)
    {
        int current_row = 0;
        int res = 0;
        
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
                {
                    dataMatrix[current_row][i] = data.charAt(i);
                }
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
                    //System.out.print(dataMatrix[i][j]);


                    if (!foundNumber && Character.isDigit(dataMatrix[i][j]))
                    {
                        foundNumber = true;
                        numberStartIndex = j;
                        numberEndIndex = j;
                    }
                    else if (foundNumber && Character.isDigit(dataMatrix[i][j]))
                    {
                        numberEndIndex = j;
                    }
                    else if (foundNumber && !Character.isDigit(dataMatrix[i][j]))
                    {
                        foundNumber = false;
                        String numberString = "";

                        for (int k = numberStartIndex; k <= numberEndIndex; k++)
                            numberString += dataMatrix[i][k];

                        number = Integer.parseInt(numberString);

                        if (isNumberPartOfSchematic(i, numberStartIndex, numberEndIndex, dataMatrix, rowCount, rowLength))
                        {
                            System.out.println("Schematic number found: " + numberString);
                            res += number;
                        }
                        
                        //System.out.println("Number found: " + numberString);
                    }

                        
                }
                System.out.println("-------------");

            }

            System.out.println("Result: " + res);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}