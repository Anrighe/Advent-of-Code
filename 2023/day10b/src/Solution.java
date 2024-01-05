import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;


public class Solution {

    /**
     * Returns the next position to move to based on the current position in the field.
     * 
     * @param field The 2D character array representing the field.
     * @param currentPosition The current position in the field.
     * @param visited The list of visited positions.
     * @return The next position to move to as a Pair of integers (row, col).
     */
    public static Pair<Integer, Integer> getNextPosition(char[][] field, Pair<Integer, Integer> currentPosition, List<Pair<Integer, Integer>> visited) {
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

        return new Pair<Integer, Integer>(-1, -1);
    }

    /**
     * Returns the number of rows in the input file.
     *
     * @param inputFile the path to the input file
     * @return the number of rows in the input file
     * @throws FileNotFoundException if the input file is not found
     */
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

    /**
     * Reads the input file and returns the number of columns in the first line of the file.
     *
     * @param inputFile the path of the input file
     * @return the number of columns in the first line of the file
     * @throws FileNotFoundException if the input file is not found
     */
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
     * From a starting point "S", finds the loop path and returns the furthest point inside the loop from 
     * the starting point.
     * 
     * The loops are arranged in a two-dimensional grid of tiles:
     *      | is a vertical pipe connecting north and south.
     *      - is a horizontal pipe connecting east and west.
     *      L is a 90-degree bend connecting north and east.
     *      J is a 90-degree bend connecting north and west.
     *      7 is a 90-degree bend connecting south and west.
     *      F is a 90-degree bend connecting south and east.
     *      . is ground; there is no pipe in this tile.
     *      S is the starting position;
     * 
     * Example of a loop:
     *      .....
     *      .F-7.
     *      .|.|.
     *      .L-J.
     *      .....
     * 
     * Example of counting distance:
     *      .....
     *      .012.
     *      .1.3.
     *      .234.
     *      .....
     * In this case, the furthest distance is 4.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int res = 0;

        String inputFile = "input.txt";
        int currentLine = 0;

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

            List<Pair<Integer, Integer>> visited = new ArrayList<Pair<Integer, Integer>>();
            Pair <Integer, Integer> currentPos = new Pair<Integer, Integer>(startRow, startCol);
            visited.add(new Pair<Integer, Integer>(startRow, startCol));
            Pair <Integer, Integer> nextPos;

            while (true) {
                res++;
                nextPos = getNextPosition(field, currentPos, visited);
                System.out.println("Next pos: (" + nextPos.getKey() + ", " + nextPos.getValue() + ")");

                if (nextPos.getKey() == -1 && nextPos.getValue() == -1) {
                    break;
                }   

                if (!visited.contains(nextPos)) {
                    visited.add(nextPos);
                    currentPos = nextPos;
                }
            }

            // !!! Assumption !!!
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