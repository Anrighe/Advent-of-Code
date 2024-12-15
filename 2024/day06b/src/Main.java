import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Simulates and predicts the patrol route of a guard on a grid-based map.
 * Processes an input map, tracks the guard's movement based on predefined rules,
 * and counts distinct positions visited before the guard exits the map. 
 * The guard's behaviour:
 * - Turns 90 degrees clockwise if facing an obstacle.
 * - Moves forward otherwise.
 * 
 * The objective is to detect the total amount of possible paths in which the guard can loop by placing each time a new obstacle
 * in a tile of the map. A new obstacle can be placed if a tile isn't already occupied by the guard or by another obstacle.
 * 
 * To detect if the guard is in a loop, memorizes all the previous position and orientation of the guard and after each moment
 * checks if the guard repeated the same positiona and orientation.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";
    public static final char OBSTACLE = '#';
    public static final char VISITED_POSITION = 'X';

    public static final Set<Character> GUARD_CHARACTERS = Set.of('<', '>', '^', 'V');

    public static final Map<Character, Coordinate> MOVE_TO_MAKE_PER_GUARD_ORIENTATION = Map.of(
        '<', new Coordinate(0, -1), 
        '>', new Coordinate(0, +1), 
        '^', new Coordinate(-1, 0), 
        'V', new Coordinate(+1, 0)
    );

    public static final Map<Character, Character> GUARD_CHARACTER_ROTATION = Map.of(
        '<', '^', 
        '>', 'V', 
        '^', '>', 
        'V', '<'
    );

    public static final Map<String, Character> GUARD_MOVEMENT_TRAIL = Map.of(
        "HORIZONTAL", '-',
        "VERTICAL", '|',
        "ROTATION", '+'
    );

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
    public static int getFileLineNumber() throws FileNotFoundException, IOException {
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
    public static void print2DMatrix(char mat[][])
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
     * Checks if the guard is exiting the map border.
     * @param matrix The current map.
     * @param guardPosition The guard's position.
     * @param rows Number of rows in the map.
     * @param cols Number of columns in the map.
     * @return True if the guard is exiting, false otherwise.
     * @throws RuntimeException if the guard's position is invalid.
     */
    public static boolean isGuardExitingBorder(char[][] matrix, Coordinate guardPosition, int rows, int cols) throws RuntimeException{
        char guardCharacter = matrix[guardPosition.getRow()][guardPosition.getCol()];

        if (!GUARD_CHARACTERS.contains(guardCharacter)) {
            throw new RuntimeException();
        }

        return switch (guardCharacter) {
            case '^' -> guardPosition.getRow() == 0;
            case '<' -> guardPosition.getCol() == 0;
            case '>' -> guardPosition.getCol() == cols - 1;
            case 'V' -> guardPosition.getRow() == rows - 1;
            default -> false;
        };
    }

    /**
     * Checks if the guard can move to the specified position.
     * @param matrix The current map.
     * @param position The target position.
     * @return True if the position is valid, false otherwise.
     */
    public static boolean canMoveToPosition(char[][] matrix, Coordinate position) {
        if (matrix[position.getRow()][position.getCol()] == OBSTACLE)
            return false;

        return true;
    }

    /**
     * Moves the guard on the map based on the patrol rules.
     * @param matrix The current map.
     * @param guardPosition The guard's current position.
     * @return An array containing the updated map and guard position.
     * @throws RuntimeException if the guard's character is invalid.
     */
    public static Object[] moveGuard(char[][] matrix, Coordinate guardPosition) throws RuntimeException {
        char guardCharacter = matrix[guardPosition.getRow()][guardPosition.getCol()];

        if (!MOVE_TO_MAKE_PER_GUARD_ORIENTATION.containsKey(guardCharacter)) {
            throw new RuntimeException();
        }

        Coordinate newGuardPosition = guardPosition.add(MOVE_TO_MAKE_PER_GUARD_ORIENTATION.get(guardCharacter));

        if (canMoveToPosition(matrix, newGuardPosition)) {
            matrix[guardPosition.getRow()][guardPosition.getCol()] = VISITED_POSITION;
            matrix[newGuardPosition.getRow()][newGuardPosition.getCol()] = guardCharacter;
            return new Object[] { matrix, newGuardPosition };
        } else {
            matrix[guardPosition.getRow()][guardPosition.getCol()] = GUARD_CHARACTER_ROTATION.get(guardCharacter);
            return new Object[] { matrix, guardPosition };
        }
        
    }

    /**
     * Counts all visited positions on the map.
     * @param matrix The current map.
     * @param cols Number of columns.
     * @param rows Number of rows.
     * @return The count of visited positions.
     */    
    public static int countAllVisitedPlaces(char[][] matrix, int cols, int rows) {
        int result = 0;
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                if (matrix[row][col] == VISITED_POSITION)
                    result += 1;
            }
        }
        return result;
    }

    /**
     * Instances, copies the matrix and returns it
     * @param matrixToCopy matrix that needs to be copied.
     * @param rows total rows of the matrix.
     * @param cols total columns of the matrix.
     * @return a new instance of the copied matrix.
     */
    public static char[][] copyMatrix (char[][] matrixToCopy, int rows, int cols) {
        char copy[][] = new char[rows][cols]; 
        
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                copy[i][j] = matrixToCopy[i][j];
            }
        }
        return copy;
    }

    /**
     * Determines if a new obstacle can be placed at the given position on the map.
     * An obstacle cannot be placed if:
     * - It overlaps the guard's current position.
     * - It overlaps an already placed obstacle.
     *
     * @param matrix The current map.
     * @param row Row in which to place the obstacle.
     * @param col Column in which to place the obstacle.
     * @return True if the obstacle can be placed, false otherwise.
     */
    public static boolean canPlaceNewObstacle(char[][] matrix, int row, int col) {
        if (GUARD_CHARACTERS.contains(matrix[row][col]) || matrix[row][col] == OBSTACLE) {
            return false;
        }
        return true;
    }

    /**
     * Determines if the guard is caught in a loop.
     * A loop is detected if the guard revisits the same position with the same orientation (same character).
     * 
     * @param matrix The current map.
     * @param visitedCoordinatesWithGuardCharacters A Map of Coordinates and Set of Characters: 
     *  represents the orientation the guard had for each visited position
     * @param previousGuardPosition previous Coordinates of the guard before the movement.
     * @param newGuardPosition new Coordinates of the guard after the movement.
     * @return true if the guard is on a looped path, false otherwise.
     */
    public static boolean isGuardOnALoop(
        char[][] matrix, 
        Map<Coordinate, Set<Character>>  visitedCoordinatesWithGuardCharacters, 
        Coordinate previousGuardPosition, 
        Coordinate newGuardPosition
    ) {
        char newGuardPositionMatrixCharacter = matrix[newGuardPosition.getRow()][newGuardPosition.getCol()];

        if (visitedCoordinatesWithGuardCharacters.containsKey(newGuardPosition)) {
            if (visitedCoordinatesWithGuardCharacters.get(newGuardPosition).contains(newGuardPositionMatrixCharacter)) {
                return true;
            } else {
                visitedCoordinatesWithGuardCharacters.get(newGuardPosition).add(newGuardPositionMatrixCharacter);
            }

        } else {
            visitedCoordinatesWithGuardCharacters.put(newGuardPosition, new HashSet<Character>());
            visitedCoordinatesWithGuardCharacters.get(newGuardPosition).add(newGuardPositionMatrixCharacter);
        }

        return false;
    }

    public static void main(String args[]) {
        int result = 0;

        File file = new File(INPUT_FILE_LOCATION);

        try {     
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            String inputLine;
            
            int rows = getFileLineNumber();
            int cols = getFileLineLengthIfAllAreEqual();

            char matrix[][] = new char[rows][cols];
            
            Coordinate guardPosition = null;
            Coordinate originalGuardPosition = null;
            
            int row = 0;
            
            char currentChar;
            while ((inputLine = br.readLine()) != null) {
                for (int col = 0; col < inputLine.length(); ++col) {
                    currentChar = inputLine.charAt(col);   
                    matrix[row][col] = currentChar;
                    if (GUARD_CHARACTERS.contains(currentChar)) {
                        guardPosition = new Coordinate(row, col);
                        originalGuardPosition = new Coordinate(row, col);
                    }
                }
                row++;
            }
            br.close();
            char originalMatrix[][] = copyMatrix(matrix, rows, cols);

            if (guardPosition == null)
                throw new IllegalArgumentException();

            Map<Coordinate, Set<Character>> visitedCoordinatesWithGuardCharacters;

            for (row = 0; row < rows; ++row) {
                for (int col = 0; col < cols; ++col) {
                    if (canPlaceNewObstacle(matrix, row, col)) {
                        matrix[row][col] = OBSTACLE;
                        visitedCoordinatesWithGuardCharacters = new HashMap<>();
                        visitedCoordinatesWithGuardCharacters.put(originalGuardPosition, new HashSet<Character>());
                        visitedCoordinatesWithGuardCharacters.get(originalGuardPosition).add(matrix[originalGuardPosition.getRow()][originalGuardPosition.getCol()]);

                        while (!isGuardExitingBorder(matrix, guardPosition, rows, cols)) {
                            
                            Object[] moveResult =  moveGuard(matrix, guardPosition);
                            Coordinate newGuardPosition = (Coordinate) moveResult[1];
                            if (isGuardOnALoop(matrix, visitedCoordinatesWithGuardCharacters, guardPosition, newGuardPosition)) {
                                result += 1;
                                break;
                            } else {
                                matrix = (char[][]) moveResult[0];
                                guardPosition = (Coordinate) moveResult[1];
                                visitedCoordinatesWithGuardCharacters.get(guardPosition).add(matrix[guardPosition.getRow()][guardPosition.getCol()]);
                            }
                        }
                        // Resetting matrix and guard position as original
                        matrix = copyMatrix(originalMatrix, rows, cols);
                        guardPosition = originalGuardPosition;
                    }
                }
            }

            System.out.println(String.format("The sum of the distinct position visited by the guard before leaving the map is: %s", result));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Error: file lines have different lengths: %s", e.toString()));
            return;
        } catch (IllegalArgumentException e) {
            System.err.println(String.format("Error: guard position has not been intialized %s", e.toString()));
            return;
        } catch (RuntimeException e) {
            System.err.println(String.format("Error: misalignment of guard position. Character in guard position is not a valid character for guard %s", e.toString()));
            return;
        }
    }
}