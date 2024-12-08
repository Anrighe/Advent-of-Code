import java.io.*;
import java.util.regex.*;

public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";
    public static final String regexPatternString = "(?=(XMAS|SAMX))";

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
     * Counts occurrences of a regex pattern in a string.
     * @param stringToCheck The string to search
     * @param regexPatternString The regex pattern
     * @return Number of matches
     */
    public static int getNumberOfMatches(String stringToCheck, String regexPatternString) {
        int result = 0;
        Pattern pattern = Pattern.compile(regexPatternString);
        Matcher matcher = pattern.matcher(stringToCheck);
            
        while(matcher.find()) {
            //String match = matcher.group(0);
            result++;
        }

        return result;
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
     * Checks if a position is on the south or east border of the matrix.
     * @param row Row index
     * @param col Column index
     * @param lineNumber Total number of rows
     * @param lineLength Total number of columns
     * @return True if on the south or east border, false otherwise
     */
    public static boolean onSouthOrEastBorder(int row, int col, int lineNumber, int lineLength) {
        return row == lineNumber-1 || col == lineLength-1;
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
     * Checks if a position is on any border of the matrix.
     * @param row Row index
     * @param col Column index
     * @param lineNumber Total number of rows
     * @param lineLength Total number of columns
     * @return True if on any border, false otherwise
     */
    public static boolean onBorder(int row, int col, int lineNumber, int lineLength) {
        return row == 0 || col == 0 || row == lineNumber-1 || col == lineLength-1;
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
     * Transposes a 2D character matrix.
     * @param matrix The 2D character matrix
     * @return Transposed matrix
     */
    public static char[][] transposeMatrix(char[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        char[][] transposed = new char[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }

        return transposed;
    }

    public static void main(String args[]) {
        
        File file = new File(INPUT_FILE_LOCATION);

        BufferedReader br;
        try {
            int lineLength = getFileLineLengthIfAllAreEqual();
            int lineNumber = getFileLineNumber();         

            br = new BufferedReader(new FileReader(file));
            String inputLine;
            
            char matrix[][] = new char[lineNumber][lineLength];
            
            int currentLine = 0;
            int horizontalStringResult = 0;

            // Populate the matrix and count matches for horizontal strings
            while ((inputLine = br.readLine()) != null) {
                
                for (int charPosition = 0; charPosition < inputLine.length(); ++charPosition)
                    matrix[currentLine][charPosition] = inputLine.charAt(charPosition);

                currentLine++;
                horizontalStringResult += getNumberOfMatches(inputLine, regexPatternString);
            }
            br.close();

            // Transpose the matrix and count matches for vertical strings
            String transposedMatrixLine;
            char[][] transposedMatrix = transposeMatrix(matrix);
            int transposedStringResult = 0;
            for (int row = 0; row < lineLength; ++row) {
                transposedMatrixLine = "";
                for (int col = 0; col < lineNumber; ++col) {
                    transposedMatrixLine += transposedMatrix[row][col];
                }
                transposedStringResult += getNumberOfMatches(transposedMatrixLine, regexPatternString);
            }

            // Count matches for oblique strings (diagonal patterns)
            String obliqueString;
            int obliqueStringResult = 0;
            for (int row = 0; row < lineLength; ++row) {
                obliqueString = "";
                
                for (int col = 0; col < lineNumber; ++col) {
                    if (onNorthOrWestBorder(row, col, lineNumber, lineLength)) {
                        obliqueString = getObliqueStringTopLeftBottomRight(matrix, row, col, lineNumber, lineLength);
                        obliqueStringResult += getNumberOfMatches(obliqueString, regexPatternString);
                    }
                    if (onNorthOrEastBorder(row, col, lineNumber, lineLength)) {
                        obliqueString = getObliqueStringTopRightBottomLeft(matrix, row, col, lineNumber, lineLength);
                        obliqueStringResult += getNumberOfMatches(obliqueString, regexPatternString);
                    }
                }
            }

            System.out.println(String.format("The word 'XMAS' appears %s times", horizontalStringResult + transposedStringResult + obliqueStringResult));

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