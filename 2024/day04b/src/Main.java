import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

/**
 * The goal is to find occurrences of the patterns * "MAS" and "SAM" forming X-shapes in a 2D character matrix. 
 * Each X-shape consists of two diagonally arranged patterns, which can be forward or reversed.
 *
 * The program reads a matrix from an input file, identifies diagonal patterns in two 
 * directions (top-left to bottom-right and top-right to bottom-left), calculates the 
 * center points of the matches, and counts the total number of unique X-shaped patterns.
 *
 * Key steps include:
 * - Parsing the input file into a matrix.
 * - Extracting diagonal strings.
 * - Matching patterns using regular expressions.
 * - Calculating and counting unique X-shaped patterns.
 *
 * Output: Total number of X-shaped "MAS" patterns.
 */

public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";
    public static final String oldRegexPatternString = "(?=(MAS|SAM))";
    public static final String regexPatternMASString = "MAS";
    public static final String regexPatternSAMString = "SAM";

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
     * Computes the center coordinates of matches based on the starting and ending positions
     * of a detected pattern in the matrix.
     *
     * The method calculates the middle point between the starting and ending coordinates and
     * returns it as a list containing a single {@code Coordinate}.
     *
     * @param startingRow The row index of the starting position of the match.
     * @param startingCol The column index of the starting position of the match.
     * @param endingRow The row index of the ending position of the match.
     * @param endingCol The column index of the ending position of the match.
     * @return A list containing one {@code Coordinate}, which is the middle point of the match.
     */
    public static List<Coordinate> getMatchesCenterCoordinates(
        int startingRow, 
        int startingCol,
        int endingRow, 
        int endingCol
    ) {
        List<Coordinate> coordinateList = new ArrayList<>();

        coordinateList.add(Coordinate.calculateMiddlePoint(
            new Coordinate(startingRow, startingCol), 
            new Coordinate(endingRow, endingCol)
        ));

        return coordinateList;
    }

    /**
     * Checks if a position is on the north or west border of the matrix.
     * @param row Row index
     * @param col Column index
     * @param lineNumber Total number of rows
     * @param lineLength Total number of columns
     * @return True if on the north or west border, false otherwise
     */
    public static boolean onNorthOrWestBorder(int row, int col, int lineNumber, int lineLength) {
        return row == 0 || col == 0;
    }

    /**
     * Checks if a position is on the north or east border of the matrix.
     * @param row Row index
     * @param col Column index
     * @param lineNumber Total number of rows
     * @param lineLength Total number of columns
     * @return True if on the north or east border, false otherwise
     */
    public static boolean onNorthOrEastBorder(int row, int col, int lineNumber, int lineLength) {
        return row == 0 || col == lineLength-1;
    }

    /**
     * Extracts a diagonal string from top-left to bottom-right.
     * @param matrix The 2D character matrix
     * @param row Starting row
     * @param col Starting column
     * @param lineNumber Total number of rows
     * @param lineLength Total number of columns
     * @return The diagonal string
     */
    public static String getObliqueStringTopLeftBottomRight(char[][] matrix, int row, int col, int lineNumber, int lineLength) {
        String result = "";
        while (row <= lineNumber-1 && col <= lineLength-1) {
            result += matrix[row][col];
            row++;
            col++;
        }

        return result;
    }

    /**
     * Extracts a diagonal string from top-right to bottom-left.
     * @param matrix The 2D character matrix
     * @param row Starting row
     * @param col Starting column
     * @param lineNumber Total number of rows
     * @param lineLength Total number of columns
     * @return The diagonal string
     */
    public static String getObliqueStringTopRightBottomLeft(char[][] matrix, int row, int col, int lineLength, int lineNumber) {
        String result = "";
        
        while (row <= lineNumber-1 && col >= 0) {

            result += matrix[row][col];
            row++;
            col--;
        }
        return result;
    }

    /**
     * Processes matching patterns in the specified matrix for oblique strings starting from a given 
     * row and column and returns the list of middle coordinates of the matches.
     *
     * The function handles two diagonal directions:
     * - Top-left to bottom-right (if the starting position is on the north or west border).
     * - Top-right to bottom-left (if the starting position is on the north or east border).
     *
     * @param matrix The 2D character matrix to search within.
     * @param row The starting row index.
     * @param col The starting column index.
     * @param lineNumber The total number of rows in the matrix.
     * @param lineLength The total number of columns in the matrix.
     * @param obliqueString A placeholder for the diagonal string (will be generated within the function).
     * @param patternMAS The {@code Pattern} object representing the "MAS" pattern.
     * @param patternSAM The {@code Pattern} object representing the "SAM" pattern.
     * @return A list of {@code Coordinate} objects representing the middle points of the matches found.
     */
    public static List<Coordinate> handleMatchingAndGetMiddlePoints(
        char[][] matrix, 
        int row, 
        int col, 
        int lineNumber, 
        int lineLength,
        String obliqueString,
        Pattern patternMAS,
        Pattern patternSAM
    ) {
        List<Coordinate> listOfMiddleCoordinates = new ArrayList<>();
        Matcher matcherMAS;
        Matcher matcherSAM;

        if (onNorthOrWestBorder(row, col, lineNumber, lineLength)) {
            obliqueString = getObliqueStringTopLeftBottomRight(matrix, row, col, lineNumber, lineLength);
            matcherMAS = patternMAS.matcher(obliqueString);
            matcherSAM = patternSAM.matcher(obliqueString);

            while (matcherMAS.find()) {
                int offset = matcherMAS.start();
                List<Coordinate> listOfMiddleCoordinatesMAS = getMatchesCenterCoordinates(
                    row+offset, 
                    col+offset, 
                    row+offset+2, 
                    col+offset+2);

                listOfMiddleCoordinates.addAll(listOfMiddleCoordinatesMAS);
            }

            while (matcherSAM.find()) {
                int offset = matcherSAM.start();
                List<Coordinate> listOfMiddleCoordinatesSAM = getMatchesCenterCoordinates(
                    row+offset, 
                    col+offset, 
                    row+offset+2, 
                    col+offset+2);
                        
                listOfMiddleCoordinates.addAll(listOfMiddleCoordinatesSAM);
            }
        }

        // Oblique string Top Right Bottom Left
        if (onNorthOrEastBorder(row, col, lineNumber, lineLength)) {
            obliqueString = getObliqueStringTopRightBottomLeft(matrix, row, col, lineNumber, lineLength);
            matcherMAS = patternMAS.matcher(obliqueString);
            matcherSAM = patternSAM.matcher(obliqueString);

            while (matcherMAS.find()) {
                int offset = matcherMAS.start();
                List<Coordinate> listOfMiddleCoordinatesMAS = getMatchesCenterCoordinates( 
                    row+offset, 
                    col-offset, 
                    row+offset+2, 
                    col-offset-2);

                listOfMiddleCoordinates.addAll(listOfMiddleCoordinatesMAS);
            }

            while (matcherSAM.find()) {
                int offset = matcherSAM.start();
                List<Coordinate> listOfMiddleCoordinatesSAM = getMatchesCenterCoordinates(
                    row+offset, 
                    col-offset, 
                    row+offset+2, 
                    col-offset-2);
                listOfMiddleCoordinates.addAll(listOfMiddleCoordinatesSAM);
            }
        }

        return listOfMiddleCoordinates;
    }

    public static void main(String args[]) {
        int result = 0;

        File file = new File(INPUT_FILE_LOCATION);

        Map<Coordinate, Integer> middleMatchCounterMap = new HashMap<>();

        BufferedReader br;
        List<Coordinate> listOfMiddleCoordinates;
        try {
            int lineLength = getFileLineLengthIfAllAreEqual();
            int lineNumber = getFileLineNumber();         

            br = new BufferedReader(new FileReader(file));
            char matrix[][] = new char[lineNumber][lineLength];
            
            String inputLine;
            int currentLine = 0;

            // Populate the matrix
            while ((inputLine = br.readLine()) != null) {
                for (int charPosition = 0; charPosition < inputLine.length(); ++charPosition)
                    matrix[currentLine][charPosition] = inputLine.charAt(charPosition);

                currentLine++;
            }
            br.close();

            // Count matches for oblique strings (diagonal patterns)
            String obliqueString;

            Pattern patternMAS = Pattern.compile(regexPatternMASString);
            Pattern patternSAM = Pattern.compile(regexPatternSAMString);


            listOfMiddleCoordinates = new ArrayList<>(); 
            for (int row = 0; row < lineLength; ++row) {
                obliqueString = "";

                // Oblique string Top Left Bottom Right
                for (int col = 0; col < lineNumber; ++col) {
                    listOfMiddleCoordinates.addAll(
                        handleMatchingAndGetMiddlePoints(
                            matrix, 
                            row, 
                            col, 
                            lineNumber, 
                            lineLength,
                            obliqueString,
                            patternMAS,
                            patternSAM
                        )
                    );
                    
                }
            }

            for (Coordinate middleCoordinate : listOfMiddleCoordinates) {
                if (middleMatchCounterMap.containsKey(middleCoordinate)) {
                    middleMatchCounterMap.put(middleCoordinate, middleMatchCounterMap.get(middleCoordinate) + 1);
                } else {
                    middleMatchCounterMap.put(middleCoordinate, 1);
                }
            }

            for (Coordinate coordinate : middleMatchCounterMap.keySet())
                result += middleMatchCounterMap.get(coordinate) / 2;
            
            System.out.println(String.format("The number of X-shaped 'MAS' is : %s", result));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("The input is not a square matrix: %s", e.toString()));
            return;
        }
    }
}