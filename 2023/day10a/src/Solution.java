import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;


public class Solution {

    public static Pair<Integer, Integer> getNextPosition(char[][] field, Pair<Integer, Integer> currentPosition) {
        
        // Move up case
        if (field[currentPosition.getKey()][currentPosition.getValue()] == '|' || 
            field[currentPosition.getKey()][currentPosition.getValue()] == 'S' ||
            field[currentPosition.getKey()][currentPosition.getValue()] == 'L' ||
            field[currentPosition.getKey()][currentPosition.getValue()] == 'J') {
            
            if (currentPosition.getKey() < field.length - 1 && currentPosition.getKey() >= 0) {
                if (field[currentPosition.getKey() + 1][currentPosition.getValue()] == '|' ||
                    field[currentPosition.getKey() + 1][currentPosition.getValue()] == '7' ||
                    field[currentPosition.getKey() + 1][currentPosition.getValue()] == 'F') {
                    return new Pair<Integer, Integer>(currentPosition.getKey() + 1, currentPosition.getValue());
                }
            }
        }
        else if () //TODO - add more cases

        
        
        
        
        
        
        return new Pair<Integer, Integer>(0, 0);
    }

    public static int getInputRows(String inputFile) throws FileNotFoundException {
        File myObj = new File(inputFile);
        Scanner myReader = new Scanner(myObj);

        int rows = 0;

        while (myReader.hasNextLine()) {
            rows++;
            myReader.nextLine();
        }
        myReader.close();

        return rows;
    }

    public static int getInputCols(String inputFile) throws FileNotFoundException {
        File myObj = new File(inputFile);
        Scanner myReader = new Scanner(myObj);

        int cols = 0;

        if (myReader.hasNextLine()) {
            cols = myReader.nextLine().length();
        }
        myReader.close();

        return cols;
    }

    /**
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int res = 0;

        String inputFile = "input.txt";
        int currentLine = 0;
        int currentRow = 0;
        int currentCol = 0;

        int startRow = -1;
        int startCol = -1;

        try {

            char field [][] = new char[getInputRows(inputFile)][getInputCols(inputFile)];

            
            File myObj = new File(inputFile);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                field[currentLine] = myReader.nextLine().toCharArray();
                
                for (int i = 0; i < field[currentLine].length; i++) {
                    if (field[currentLine][i] == 'S') {
                        startRow = currentLine;
                        startCol = i;
                    }
                }
                
                currentLine++;
            }

            for (int i = 0; i < field.length; i++) {
                System.out.println(field[i]);
            }

            System.out.println("Start row: " + startRow);
            System.out.println("Start col: " + startCol);

            List<Pair<Integer, Integer>> visited = new ArrayList<Pair<Integer, Integer>>();

            Pair <Integer, Integer> currentPos = new Pair<Integer, Integer>(startRow, startCol);

            visited.add(new Pair<Integer, Integer>(startRow, startCol));

            currentPos = getNextPosition(field, currentPos);

            while (field[currentRow][currentCol] != 'S') {

            }



            myReader.close();
            System.out.println(String.format("Result: %d", res));
        }
        catch (FileNotFoundException e) {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}