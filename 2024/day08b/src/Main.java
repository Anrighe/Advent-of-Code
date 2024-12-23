import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Process a map of antennas and calculate unique antinode locations.
 * Antinodes are positions influenced by the resonant frequencies of antennas in the grid.
 * 
 * Part One: Antinodes occur perfectly in line with two antennas of the same frequency,
 * and one of the antennas is twice the distance from the antinode.
 * 
 * Part Two: Resonant harmonics are considered. Antinodes occur at any position aligned
 * with at least two antennas of the same frequency, regardless of distance. 
 * This includes the positions of antennas themselves unless they are the sole antenna 
 * of their frequency.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";

    /**
     * Determines if a character represents an antenna.
     * @param c Character to check
     * @return true if the character is an antenna, false otherwise
     */
    public static boolean isAnAntenna(char c) {
        return c != '.';
    }

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
     * Checks if the given row and column are outside of the map boundaries.
     * @param rowToCheck row to check
     * @param columnToCheck column to check
     * @param totalRows total number of rows
     * @param totalColumns total number of columns
     * @return true if the row and column are outside of the map boundaries, false otherwise
     */
    public static boolean isOutsideOfMapBoundaries(int rowToCheck, int columnToCheck, int totalRows, int totalColumns) {
        return rowToCheck < 0 || rowToCheck > totalRows-1 || columnToCheck < 0 || columnToCheck > totalColumns-1;
    }

    /**
     * Checks if the given antinode is outside of the map boundaries.
     * @param antinode antinode to check
     * @param totalRows total number of rows
     * @param totalColumns total number of columns
     * @return true if the antinode is outside of the map boundaries, false otherwise
     */
    public static boolean isAntinodeOutOfMapBoundaries(Antinode antinode, int totalRows, int totalColumns) {
        return isOutsideOfMapBoundaries(antinode.getRow(), antinode.getColumn(), totalRows, totalColumns);
    }

    /**
     * Calculates the antinode coordinate based on the positions of two antennas.
     * @param coord1 Coordinate of the first antenna (row or column)
     * @param coord2 Coordinate of the second antenna (row or column)
     * @param distance Distance between the antennas in that dimension
     * @return The calculated antinode coordinate
     */
    public static int calculateAntinodeCoordinate(int coord1, int coord2, int distance) {
        if (coord1 < coord2) {
            return coord2 + distance;
        } else if (coord1 > coord2) {
            return coord2 - distance;
        } else {
            return coord2;
        }
    }

    public static void main(String args[]) {

        Map<Character, List<Antenna>> antennas = new HashMap<>();
        Set<Antinode> antinodes = new HashSet<>();

        File file = new File(INPUT_FILE_LOCATION);

        int result = 0;

        try {     
            BufferedReader br = new BufferedReader(new FileReader(file));

            int inputRows = getFileLineLengthIfAllAreEqual();
            int inputColumns = getFileLineNumber();

            assert inputRows == inputColumns;
            
            String inputLine;

            int currentRow = 0;

            while ((inputLine = br.readLine()) != null) {

                for (int currentColumn = 0; currentColumn < inputLine.length(); currentColumn++) {
                    char currentChar = inputLine.charAt(currentColumn);
                    if (isAnAntenna(currentChar)) {

                        if (!antennas.containsKey(currentChar))
                            antennas.put(currentChar, new ArrayList<>());

                        antennas.get(currentChar).add(new Antenna(currentChar, currentRow, currentColumn));
                    }
                }
                currentRow++;
            }

            br.close();
            
            for (char antennaType : antennas.keySet()) {
                for (Antenna firstAntenna : antennas.get(antennaType)) {
                    for (Antenna secondAntenna : antennas.get(antennaType)) {
                        if (!firstAntenna.equals(secondAntenna)) {
                            
                            int rowAntennaDistance = Math.abs(firstAntenna.getRow() - secondAntenna.getRow());
                            int columnAntennaDistance = Math.abs(firstAntenna.getColumn() - secondAntenna.getColumn());

                            int rowAntennaDistanceIncrement = rowAntennaDistance;
                            int columnAntennaDistanceIncrement = columnAntennaDistance;

                            boolean atLeastOneAntinodeInsideMapBoundaries = true;
                            boolean firstAntinodeInsideMapBoundaries = true;
                            boolean secondAntinodeInsideMapBoundaries = true;

                            while (atLeastOneAntinodeInsideMapBoundaries) {

                                int firstAntinodeRow = calculateAntinodeCoordinate(firstAntenna.getRow(), secondAntenna.getRow(), rowAntennaDistanceIncrement);
                                int firstAntinodeColumn = calculateAntinodeCoordinate(firstAntenna.getColumn(), secondAntenna.getColumn(), columnAntennaDistanceIncrement);
    
                                int secondAntinodeRow = calculateAntinodeCoordinate(secondAntenna.getRow(), firstAntenna.getRow(), rowAntennaDistanceIncrement);
                                int secondAntinodeColumn = calculateAntinodeCoordinate(secondAntenna.getColumn(), firstAntenna.getColumn(), columnAntennaDistanceIncrement);

                                Antinode firstAntinode = new Antinode(
                                    firstAntinodeRow, 
                                    firstAntinodeColumn
                                );
                                
                                Antinode secondAntinode = new Antinode(
                                    secondAntinodeRow, 
                                    secondAntinodeColumn
                                );

                                if (!isAntinodeOutOfMapBoundaries(firstAntinode, inputRows, inputColumns)) {
                                    antinodes.add(firstAntinode);
                                } else {
                                    firstAntinodeInsideMapBoundaries = false;
                                }
    
                                if (!isAntinodeOutOfMapBoundaries(secondAntinode, inputRows, inputColumns)) {
                                    antinodes.add(secondAntinode);
                                } else {
                                    secondAntinodeInsideMapBoundaries = false;
                                }

                                atLeastOneAntinodeInsideMapBoundaries = firstAntinodeInsideMapBoundaries || secondAntinodeInsideMapBoundaries;

                                rowAntennaDistanceIncrement += rowAntennaDistance;
                                columnAntennaDistanceIncrement += columnAntennaDistance;
                            }
                        }
                    }
                }
            }
            
            Antinode tmpAntinode;

            for (Character antennaType : antennas.keySet()) {
                int antennasCountOfThisType = antennas.get(antennaType).size();
                System.out.println(antennasCountOfThisType);
                
                if (antennasCountOfThisType > 1) {
                    for (Antenna antenna : antennas.get(antennaType)) {
                        tmpAntinode = new Antinode(antenna.getRow(), antenna.getColumn());

                        antinodes.add(tmpAntinode);
                    }
                }
            }

            result = antinodes.size();
            
            System.out.println(antinodes);
            System.out.println(String.format("The total number of antinodes is: %s", result));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Error: line lengths in input file are not all equal or rows not equal to columns: %s", e.toString()));
            return;
        }
    }
}