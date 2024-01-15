import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javafx.util.Pair;

public class Solution {

    public static int factorial(int n) {
        for (int i = n - 1; i > 0; --i) {
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
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int res = 0;

        String inputFile = "input.txt";
        int currentLine = 0;

        try {
            
            File myObj = new File(inputFile);
            Scanner myReader = new Scanner(myObj);

            List<String> actualSpace = new ArrayList<String>();

            Set<Pair<Integer, Integer>> galaxies = new HashSet<Pair<Integer, Integer>>();

            Set<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> pairs = new HashSet<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>();

            while (myReader.hasNextLine()) {

                String line = myReader.nextLine();
                addGalaxies(galaxies, line, currentLine);

                if (onlyContainDots(line)) {
                    actualSpace.add(line);
                    currentLine++;
                }

                actualSpace.add(line);

                currentLine++;
            }

            for (Pair<Integer, Integer> element : galaxies) {
                for (Pair<Integer, Integer> element2 : galaxies) {
                    if (element != element2 && !pairs.contains(new Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>(element, element2))) {
                        pairs.add(new Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>(element, element2));
                    }
                }
            } 

            // Total number of permutation -> P(n,r) = (n!)/((n-r)!)
            // n: objects (in this case 9)
            // r: objects taken at a time (in this case 2)          
            assert (pairs.size() == factorial(galaxies.size()) / factorial(galaxies.size() - 2));
            
            /*
            for (var line : actualSpace) {
                System.out.println(line);
            }

            for (var element : pairs) {
                System.out.println(element);
            }
            System.out.println(pairs.size()); */

            int rowDistance = 0;
            int colDistance = 0;
            for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> element : pairs) {


                rowDistance = Math.abs(element.getKey().getKey() - element.getValue().getKey());
                colDistance = Math.abs(element.getKey().getValue() - element.getValue().getValue());

                res += rowDistance + colDistance;
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