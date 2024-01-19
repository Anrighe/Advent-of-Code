import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javafx.util.Pair;

public class Solution {

    /**
     * Transposes a list of strings by swapping rows with columns.
     * 
     * @param stringList the list of strings to be transposed
     * @return the transposed list of strings
     */
    public static List<String> transposeStringList(List<String> stringList) throws AssertionError{

        List<String> transposedStringList = new ArrayList<String>();
        String tmp;

        for (int i = 0; i < stringList.get(0).length(); ++i) {
            // all strings must have the same length
            assert (stringList.get(0).length() == stringList.get(i).length()); 
            tmp = "";

            for (int j = 0; j < stringList.size(); ++j) {
                tmp += stringList.get(j).charAt(i);
            }
            transposedStringList.add(tmp);
        }

        return transposedStringList;
    }

    /**
     * Calculates the factorial of a given number.
     *
     * @param n the number for which factorial is to be calculated
     * @return the factorial of the given number
     */
    public static long factorial(long n) {
        for (long i = n - 1; i > 0; --i) {
            n *= i;
        }
        return n;
    }


    /**
     * Adds a set of galaxies to the existing collection.
     *
     * @param galaxies the set of galaxies to be added
     */
    public static void addGalaxies(Set<Pair<Integer, Integer>> galaxies, String line, int currentLine) {
        for (int i = 0; i < line.length(); ++i) {
            if (line.charAt(i) == '#') {
                galaxies.add(new Pair<Integer, Integer>(currentLine, i));
                //System.out.println("Added galaxy at " + currentLine + ", " + i);
            }
        }
    }

    /**
     * Checks if a given string only contains dots.
     *
     * @param line the string to be checked
     * @return true if the string only contains dots, false otherwise
     */
    public static boolean onlyContainDots(String line) {
        for (int i = 0; i < line.length(); ++i) {
            if (line.charAt(i) != '.')
                return false;
        }

        return true;
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
     * The problem asks us to find the sum of the distance between all pairs of galaxies.
     * The same pair of galaxies should not be used twice. E.g. (1, 2) and (2, 1) are the same pair.
     * 
     * Before calcolating the shortest distances between all pairs, the input text must be treated:
     * 1. The rows that have no galaxies (only contain dots) must be expanded by duplicating them
     * 2. The columns that have no galaxies must be expanded by transposing the actual space
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        long res = 0L;

        String inputFile = "input.txt";
        int currentLine = 0;

        try {
            File myObj = new File(inputFile);
            Scanner myReader = new Scanner(myObj);

            List<String> actualSpace = new ArrayList<String>();
            Set<Pair<Integer, Integer>> galaxies = new HashSet<Pair<Integer, Integer>>();
            Set<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> pairs = new HashSet<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>();
            
            List<Integer> emptyRows = new ArrayList<Integer>();
            List<Integer> emptyCols = new ArrayList<Integer>();

            // Reading the input file and adding expanding the rows that have no galaxies (only contain dots)
            while (myReader.hasNextLine()) {
                
                String line = myReader.nextLine();
                
                if (onlyContainDots(line)) {
                    //actualSpace.add(line); // not adding it in part 2
                    emptyRows.add(currentLine);
                    //currentLine++; // not adding it in part 2
                }
                actualSpace.add(line);
                currentLine++;
            }

            System.out.println("Empty rows: " + emptyRows.toString());

            // Expanding the columns that have no galaxies by transposing the actual space
            // then duplicating the rows that have no galaxies (only contain dots)
            // and then transposing again to get the original space
            List<String> actualSpaceTransposed = transposeStringList(actualSpace);
            //List<String> actualSpaceTransposedTmp = new ArrayList<>();

            for (int i = 0; i < actualSpaceTransposed.size(); ++i) {
                String line = actualSpaceTransposed.get(i);
                //actualSpaceTransposedTmp.add(line);

                if (onlyContainDots(line)) {
                    emptyCols.add(i);
                    //actualSpaceTransposedTmp.add(line); // not adding it in part 2
                }
            }

            System.out.println("Empty cols: " + emptyCols.toString());

            //actualSpaceTransposed = actualSpaceTransposedTmp;
            //actualSpace = transposeStringList(actualSpaceTransposed);

            System.out.println("actual space:");
            for (String line : actualSpace) {
                System.out.println(line);
            }

            // Adding the galaxies to the set
            currentLine = 0;
            for (String line : actualSpace) {
                addGalaxies(galaxies, line, currentLine);
                currentLine++;
            }

            // Adding all the possible pairs of galaxies to the set without using the same pair twice
            // e.g. (1, 2) and (2, 1) are the same pair
            for (Pair<Integer, Integer> element : galaxies) {
                
                for (Pair<Integer, Integer> element2 : galaxies) {
                    if (element != element2 && !pairs.contains(new Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>(element, element2))
                        && !pairs.contains(new Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>(element2, element))) {
                        pairs.add(new Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>(element, element2));
                    }
                }
            } 

            // Total number of permutation -> P(n,r) = (n!)/((n-r)!)
            // n: objects (in this case 9)
            // r: objects taken at a time (in this case 2)          

            // Only asserting for a small galaxy value because the factorial function would result in an overflow
            // which would mean that the denominator would be 0 and it would cause a division by 0 error
            if (galaxies.size() <= 10)
                assert ((long)pairs.size() == factorial((long)galaxies.size()) / factorial((long)galaxies.size() - 2));

            int rowDistance = 0;
            int colDistance = 0;
            for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> element : pairs) {

                rowDistance = Math.abs(element.getKey().getKey() - element.getValue().getKey());

                for (int emptyRow : emptyRows) {
                    if (element.getKey().getKey() < emptyRow && element.getValue().getKey() > emptyRow ||
                        element.getKey().getKey() > emptyRow && element.getValue().getKey() < emptyRow) {
                        rowDistance += 999999;
                        //System.out.println("The pair " + element.getKey().toString() + " and " + element.getValue().toString() + " have the empty row " + emptyRow + " between them");
                        //System.out.println("because " + element.getKey().getKey() + " < " + emptyRow + " < " + element.getValue().getKey());
                    }
                }
                
                colDistance = Math.abs(element.getKey().getValue() - element.getValue().getValue());
                
                for (int emptyColumn : emptyCols) {
                    if (element.getKey().getValue() < emptyColumn && element.getValue().getValue() > emptyColumn || 
                        element.getKey().getValue() > emptyColumn && element.getValue().getValue() < emptyColumn) {
                        colDistance += 999999;
                        //System.out.println("The pair " + element.getKey().toString() + " and " + element.getValue().toString() + " have the empty column " + emptyColumn + " between them");
                    }
                }

                //System.out.println("total distance for pair " + element.getKey().toString() + " and " + element.getValue().toString() + ": " + (rowDistance + colDistance));
                res += rowDistance + colDistance;
                System.out.println("res: " + res);
            }

            myReader.close();
            System.out.println(String.format("Result: %d", res));
        }
        catch (FileNotFoundException e) {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}