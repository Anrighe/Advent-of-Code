import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;



public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";


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

    public static void main(String args[]) {
        int result = 0;

        File file = new File(INPUT_FILE_LOCATION);

        Map<Integer, List<Integer>> pageOrderMap = new HashMap<>();

        try {
            int lineLength = getFileLineLengthIfAllAreEqual();
            int lineNumber = getFileLineNumber();         
            BufferedReader br = new BufferedReader(new FileReader(file));

            
            String inputLine;
            int currentLine = 0;
            Pattern pattern = Pattern.compile("([0-9]+)\\|([0-9]+)");
            Matcher matcher;


            int firstNumber;
            int secondNumber;

            while ((inputLine = br.readLine()) != null) {
                System.out.println("input line: " + inputLine);
                matcher = pattern.matcher(inputLine);


                if (matcher.matches()) {

                    firstNumber = Integer.parseInt(matcher.group(1));
                    secondNumber = Integer.parseInt(matcher.group(2));

                    if (!pageOrderMap.containsKey(firstNumber))
                        pageOrderMap.put(firstNumber, new ArrayList<Integer>());

                    pageOrderMap.get(firstNumber).add(secondNumber);
                }


                currentLine++;
            }
            br.close();

            System.out.println("pageOrderMap: " + pageOrderMap);

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