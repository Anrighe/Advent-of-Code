import java.io.*;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Calculates the total cost of fencing all regions in a garden map based on the area and perimeter of each region. 
 * A region is defined as a group of adjacent garden plots (horizontally or vertically) containing the same plant type.
 * 
 * Steps performed:
 * 1. Reads and parses the garden map from a file into a 2D character array.
 * 2. Groups adjacent plots of the same plant type into regions.
 * 3. Merges adjacent regions of the same plant type to form larger regions.
 * 4. Computes the area and perimeter for each region.
 * 5. Calculates the total cost of fencing all regions, which is the sum of the product of area and perimeter for each region.
 */
public class Main {

    private static final String INPUT_FILE_LOCATION = "/home/marrase/Advent-of-Code/2024/day12a/input.txt";

    /**
     * Returns the length of each line in the file if all lines have equal length.
     * @return Length of lines in the file
     * @throws FileNotFoundException If the file does not exist
     * @throws IOException If an I/O error occurs
     * @throws AssertionError If lines have different lengths
     */
    private static int getFileLineLengthIfAllAreEqual() throws FileNotFoundException, IOException, AssertionError {
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
    private static int getFileLineNumber() throws FileNotFoundException, IOException {
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
    private static void print2DMatrix(char mat[][])
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
     * Merges adjacent plots of the same type within the provided plot map.
     * 
     * Iterates through each entry in the plot map, which maps a character to a list of plots. 
     * For each plot in the list, it checks if there are any adjacent plots of the same type. 
     * If two adjacent plots are found, the regions of the second plot are merged into the first plot, 
     * and the second plot is removed from the list.
     * The iteration is then restarted to ensure all possible adjacent plots are merged.
     * 
     * @param plotMap a map where the key is a character representing the plot type, 
     *                and the value is a list of plots of that type
     */
    public static void mergeAdjacentPlotOfTheSameType(Map<Character, List<Plot>> plotMap) {

        for (Entry<Character, List<Plot>> entry : plotMap.entrySet()) {
            List<Plot> plotList = entry.getValue();

            for (int i = 0; i < plotList.size(); ++i) {
                Plot firstPlot = plotList.get(i);
                
                for (int j = 0; j < plotList.size() && j != i; ++j) {
                    Plot secondPlot = plotList.get(j);

                    if (firstPlot.isAdjacentTo(secondPlot)) {

                        firstPlot.getPlotRegion().addAll(secondPlot.getPlotRegion());
                        plotList.remove(j);

                        // Definitely not optimized but avoids many problems
                        i = 0;
                        j = 0;
                        break;
                    }
                }
            }
        }
    }

    public static void main(String args[]) {
        File file = new File(INPUT_FILE_LOCATION);
        long result = 0;

        char [][] garden;

        Map<Character, List<Plot>> plotMap = new HashMap<>();

        try {
            int rows = getFileLineNumber();
            int cols = getFileLineLengthIfAllAreEqual();

            int currentRow = 0;

            garden = new char[rows][cols];

            BufferedReader br = new BufferedReader(new FileReader(file));

            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                for (int columnIndex = 0; columnIndex < inputLine.length(); ++columnIndex ) {
                    garden[currentRow][columnIndex] = inputLine.charAt(columnIndex);
                }

                currentRow++;
            }

            br.close();

            print2DMatrix(garden);

            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < cols; ++col) {
                    char plantType = garden[row][col];
                    Coordinate currentCoordinate = new Coordinate(row, col);

                    if (!plotMap.containsKey(plantType)) {
                        plotMap.put(plantType, new ArrayList<>());
                        Plot plotToAdd = new Plot(currentCoordinate);

                        plotMap.get(plantType).add(plotToAdd);
                    } else {
                        boolean addedToPlotRegion = false;
                        for (Plot plot : plotMap.get(plantType)) {
                            if (plot.isCoordinateAdjacentToPlotRegion(currentCoordinate)) {
                                plot.addCoordinateToPlotRegion(currentCoordinate);
                                addedToPlotRegion = true;
                                break;
                            }
                        }
                        if (!addedToPlotRegion) {
                            Plot plotToAdd = new Plot(currentCoordinate);
                            plotMap.get(plantType).add(plotToAdd);

                        }
                    }
                }
            }

            mergeAdjacentPlotOfTheSameType(plotMap);

            for (Entry<Character, List<Plot>> entry : plotMap.entrySet()) {
                List<Plot> plots = entry.getValue();
                for (Plot plot : plots) {
                    
                    int area = plot.getArea();
                    int perimeter = plot.getPerimeter(garden);

                    result += (area * perimeter);
                }
            }

            System.out.println(String.format("The total price of fencing all regions is: %s", result));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Error: %s", e.toString()));
            return;
        }
    }
}