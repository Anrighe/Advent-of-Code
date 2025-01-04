import java.io.*;

public class Main {

    public static final String INPUT_FILE_LOCATION = "/home/marrase/Advent-of-Code/2024/day09b/input.txt";

    public static void main(String args[]) {
        File file = new File(INPUT_FILE_LOCATION);

        int result = 0;
        
        try {     
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                
            }
            br.close();
                
            System.out.println(String.format("The result is: %s", result));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        }
    }
}