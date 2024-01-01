import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.util.*;


public class Solution {

    /**
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Long res = 0L;

        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";

            if (myReader.hasNextLine()) {
                data = myReader.nextLine();

            }

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();

                System.out.println(data);
            }

            myReader.close();
            System.out.println(String.format("Result: %d", res));
        }
        catch (FileNotFoundException e) {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
        catch (AssertionError ae) {
            System.err.println("Assertion error: something went wrong while collecting input data!");
            ae.printStackTrace();
        }
    }
}