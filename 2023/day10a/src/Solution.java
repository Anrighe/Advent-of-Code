import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;


public class Solution {

    public static Pair<Integer, Integer> getNextPosition(char[][] field, Pair<Integer, Integer> currentPosition, List<Pair<Integer, Integer>> visited) {
        
        //System.out.println("ENTRO");
        int row = currentPosition.getKey();
        int col = currentPosition.getValue();

        // Move up case
        if (field[row][col] == '|' || 
            field[row][col] == 'L' ||
            field[row][col] == 'J' ||
            field[row][col] == 'S') {
            if (row <= field.length -1 && row >= 1) {
                if (field[row - 1][col] == '|' ||
                    field[row - 1][col] == '7' ||
                    field[row - 1][col] == 'F') {
                        if (!visited.contains(new Pair<Integer, Integer> (row - 1, col))) {
                            return new Pair<Integer, Integer> (row - 1, col);
                        }
                }
            }
        }
        // Move down case
        if (field[row][col] == '|' ||
            field[row][col] == '7' || 
            field[row][col] == 'F' ||
            field[row][col] == 'S') {
            if (row >= 0 && row <= field.length - 2) {
                if (field[row + 1][col] == '|' ||
                    field[row + 1][col] == 'L' ||
                    field[row + 1][col] == 'J') {
                        if (!visited.contains(new Pair<Integer, Integer> (row + 1, col))) {
                            return new Pair<Integer, Integer> (row + 1, col);
                        }
                }
            }
        }
        // Move left case
        if (field[row][col] == '-' ||
            field[row][col] == 'J' ||
            field[row][col] == '7' ||
            field[row][col] == 'S') {
            if (col >= 1 && col <= field[0].length - 1) {
                if (field[row][col - 1] == '-' ||
                    field[row][col - 1] == 'L' ||
                    field[row][col - 1] == 'F') {
                        if (!visited.contains(new Pair<Integer, Integer> (row, col - 1))) {
                            return new Pair<Integer, Integer> (row, col - 1);
                        }
                }
            }
        }
        // Move right case
        if (field[row][col] == '-' ||
            field[row][col] == 'L' ||
            field[row][col] == 'F' ||
            field[row][col] == 'S') {
            if (col >= 0 && col <= field[0].length - 2) {
                if (field[row][col + 1] == '-' ||
                    field[row][col + 1] == 'J' ||
                    field[row][col + 1] == '7') {
                        if (!visited.contains(new Pair<Integer, Integer> (row, col + 1))) {
                            return new Pair<Integer, Integer> (row, col + 1);
                        }
                }
            }
        }
        
        /*
        // S case- move up
        if (field[row][col] == 'S') {
            if (row <= field.length -1 && row >= 1) {
                if (field[row - 1][col] == '|' ||
                    field[row - 1][col] == '7' ||
                    field[row - 1][col] == 'F') {
                        
                    return new Pair<Integer, Integer> (row - 1, col);
                }
            }
        }
        // S case- move down
        if (field[row][col] == 'S') {
            if (row >= 0 && row <= field.length - 2) {
                if (field[row + 1][col] == '|' ||
                    field[row + 1][col] == 'L' ||
                    field[row + 1][col] == 'J') {
                    return new Pair<Integer, Integer> (row + 1, col);
                }
            }
        }
        // S case- move left
        if (field[row][col] == 'S') {
            if (col >= 1 && col <= field[0].length - 1) {
                if (field[row][col - 1] == '-' ||
                    field[row][col - 1] == 'L' ||
                    field[row][col - 1] == 'F') {
                    return new Pair<Integer, Integer> (row, col - 1);
                }
            }
        }
        // S case- move right
        if (field[row][col] == 'S') {
            if (col >= 0 && col < field[0].length - 2) {
                if (field[row][col + 1] == '-' ||
                    field[row][col + 1] == 'J' ||
                    field[row][col + 1] == '7') {
                    return new Pair<Integer, Integer> (row, col + 1);
                }
            }
        }*/



        return new Pair<Integer, Integer>(-1, -1);
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

            
            

            Pair <Integer, Integer> nextPos;

            while (field[currentRow][currentCol] != 'S') {
                res++;
                nextPos = getNextPosition(field, currentPos, visited);
                //System.out.println("Current pos: (" + currentPos.getKey() + ", " + currentPos.getValue() + ")");
                //System.out.println("Next pos: (" + nextPos.getKey() + ", " + nextPos.getValue() + ")");

                if (nextPos.getKey() == -1 && nextPos.getValue() == -1) {
                    break;
                }   

                if (!visited.contains(nextPos)) {
                    visited.add(nextPos);
                    currentPos = nextPos;
                }
            }


            // Assumption:
            // The loop total length is an even number

            myReader.close();
            System.out.println(String.format("Result: %d", res/2));
        }
        catch (FileNotFoundException e) {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}