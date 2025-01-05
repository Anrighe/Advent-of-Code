import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Reads a topographic map from a file, identifies trailheads, and computes
 * their ratings based on the number of distinct hiking trails that originate
 * from each trailhead.
 * 
 * The hiking trails follow specific rules:
 * - Start at a position with a height of 0 (a trailhead) and may end at one or 
 *   more positions with a height of 9 (endpoints).
 * - Heights along the trail must increase by exactly 1 at each step.
 * - Movement is restricted to up, down, left, or right.
 * 
 * The program calculates the sum of all trailhead ratings across the topographic
 * map, where the rating of a trailhead is defined as the count of distinct hiking
 * trails beginning at that trailhead.
 * 
 * Example of distinct trails:
 * For a matrix:
 * 
 * .....0.
 * ..4321.
 * ..5..2.
 * ..6543.
 * ..7..4.
 * ..8765.
 * ..9....
 * 
 * The trailhead at height 0 has a rating of 3 due to three distinct hiking trails
 * ending at height 9.
 * 
 * The implementation handles:
 * - Reading a topographic map from a file
 * - Extracting starting coordinates (trailheads with height 0)
 * - Identifying valid adjacent coordinates and paths
 * - Calculating the number of valid trails for each trailhead
 * - Summing up the ratings for all trailheads
 */
public class Main {

    private static final String INPUT_FILE_LOCATION = "/home/marrase/Advent-of-Code/2024/day10b/input.txt";

    private static final int MAXIMUM_MAP_HEIGHT = 9;

    /**
     * Returns the length of each line in the file if all lines have equal length.
     * @return Length of lines in the file
     * @throws FileNotFoundException If the file does not exist
     * @throws IOException If an I/O error occurs
     * @throws AssertionError If lines have different lengths
     */
    public static int getFileLineLengthIfAllAreEqual() throws FileNotFoundException, IOException, AssertionError {
        File file = new File(INPUT_FILE_LOCATION);
        BufferedReader br;
        br = new BufferedReader(new FileReader(file));

        String inputLine;
        inputLine = br.readLine();
        int firstLineLength = inputLine.length();

        while ((inputLine = br.readLine()) != null)
            assert firstLineLength == inputLine.length();

        br.close();
        return firstLineLength;
    }

    /**
     * Counts the number of lines in the file.
     * @return Number of lines in the file
     * @throws FileNotFoundException If the file does not exist
     * @throws IOException If an I/O error occurs
     */
    public static int getFileLineNumber() throws FileNotFoundException, IOException, AssertionError {
        File file = new File(INPUT_FILE_LOCATION);
        BufferedReader br;
        br = new BufferedReader(new FileReader(file));

        int lineNumber = 0;

        while ((br.readLine()) != null)
            lineNumber += 1;

        br.close();
        return lineNumber;
    }

    /**
     * Prints a 2D character matrix to the console.
     * @param mat The 2D character matrix
     */
    public static void print2DMatrix(int mat[][])
    {
        // Loop through all rows
        for (int i = 0; i < mat.length; i++) {

            // Loop through all elements of current row
            for (int j = 0; j < mat[i].length; j++)
                System.out.print(mat[i][j] + " ");
                
            System.out.println();
        }
    }

    /**
     * Retrieves the value from a specific coordinate in the matrix.
     *
     * @param matrix The matrix.
     * @param coordinate The coordinate to access.
     * @return The value at the specified coordinate.
     * @throws ArrayIndexOutOfBoundsException If the coordinate is out of bounds.
     */
    public static int getValuefromCoordinate(int[][] matrix, Coordinate coordinate) throws ArrayIndexOutOfBoundsException{
        return matrix[coordinate.getRow()][coordinate.getCol()];
    }

    /**
     * Identifies all valid adjacent coordinates to a given coordinate in the matrix.
     *
     * @param matrix The matrix.
     * @param coordinate The coordinate for which adjacent positions are needed.
     * @return A set of adjacent coordinates.
     */
    public static Set<Coordinate> getAdjacentCoordinates(int[][] matrix, Coordinate coordinate) {
        Set<Coordinate> adjacentCoordinates = new HashSet<>();
        int row = coordinate.getRow();
        int col = coordinate.getCol();

        // Still assuming all matrix row equals all matrix columns in length
        if (row > 0) adjacentCoordinates.add(new Coordinate(row - 1, col));
        if (row < matrix.length - 1) adjacentCoordinates.add(new Coordinate(row + 1, col));
        if (col > 0) adjacentCoordinates.add(new Coordinate(row, col - 1));
        if (col < matrix[0].length - 1) adjacentCoordinates.add(new Coordinate(row, col + 1));

        return adjacentCoordinates;
    }

    public static void main(String args[]) {
        File file = new File(INPUT_FILE_LOCATION);

        int result = 0;
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            int col = getFileLineLengthIfAllAreEqual();
            int row = getFileLineNumber();

            assert col == row;
            
            int matrix[][] = new int[row][col];

            String inputLine;

            int currentRow = 0;
            int currentCol = 0;

            List<Coordinate> startingCoordinates = new ArrayList<>();
            
            
            while ((inputLine = br.readLine()) != null) {
                currentCol = 0;

                for (char currentChar : inputLine.toCharArray()) {
                    int integerToAdd = Integer.parseInt(String.valueOf(currentChar));

                    matrix[currentRow][currentCol] = integerToAdd;

                    if (integerToAdd == 0) {
                        startingCoordinates.add(new Coordinate(currentRow, currentCol));
                    }
                    currentCol++;
                }
                currentRow++; 
            }
            br.close();
             
            Queue<Coordinate> coordinatesToVisit = new LinkedList<>();

            Map<Coordinate, List<Coordinate>> startArrivalTrackingMap = new HashMap<>();

            for (Coordinate startingCoordinate : startingCoordinates) {
                startArrivalTrackingMap.put(startingCoordinate, new ArrayList<>());
                coordinatesToVisit.add(startingCoordinate);

                while(!coordinatesToVisit.isEmpty()) {
                    Coordinate currentCoordinate = coordinatesToVisit.remove();
                    int currentValue = getValuefromCoordinate(matrix, currentCoordinate);
                    
                    if (currentValue == MAXIMUM_MAP_HEIGHT && !startArrivalTrackingMap.get(startingCoordinate).contains(currentCoordinate)) {
                        result++;
                    }
    
                    coordinatesToVisit.addAll(
                        getAdjacentCoordinates(matrix, currentCoordinate)
                            .stream()
                            .filter(value -> getValuefromCoordinate(matrix, value) == (currentValue + 1))
                            .collect(Collectors.toCollection(HashSet::new))
                    );
                }
            }
            
            System.out.println(String.format("The result is: %s", result));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Error: input file rows length not equal to input file column length: %s", e.toString()));
            return;
        }
    }
}