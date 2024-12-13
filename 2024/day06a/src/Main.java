import java.io.*;
import java.util.Map;
import java.util.Set;


/**
 * Simulates and predicts the patrol route of a guard on a grid-based map.
 * 
 * Processes an input map, tracks the guard's movement based on predefined rules, 
 * and counts distinct positions visited before the guard exits the map. The guard:
 * - Turns 90 degrees clockwise if facing an obstacle.
 * - Moves forward otherwise.
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

            int row = 0;

            char currentChar;
            while ((inputLine = br.readLine()) != null) {
                for (int col = 0; col < inputLine.length(); ++col) {
                    currentChar = inputLine.charAt(col);   
                    matrix[row][col] = currentChar;
                    if (GUARD_CHARACTERS.contains(currentChar)) {
                        guardPosition = new Coordinate(row, col);
                    }
                }
                row++;
            }
            br.close();

            if (guardPosition == null)
                throw new IllegalArgumentException();


            while (!isGuardExitingBorder(matrix, guardPosition, rows, cols)) {
                Object[] moveResult =  moveGuard(matrix, guardPosition);
                matrix = (char[][]) moveResult[0];
                guardPosition = (Coordinate) moveResult[1];
            }

            matrix[guardPosition.getRow()][guardPosition.getCol()] = VISITED_POSITION;
            result = countAllVisitedPlaces(matrix, rows, cols);

            System.out.println(String.format("The sum of the distinc position visited by the guard before leaving the map is: %s", result));

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