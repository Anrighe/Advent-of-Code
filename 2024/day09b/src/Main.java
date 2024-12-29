import java.io.*;
import java.util.List;

/**
 * Simulates the process of compacting a fragmented disk by moving file blocks to fill gaps in free space, 
 * then calculates a checksum based on the final arrangement of file blocks. 
 * The input is a dense format disk map, where alternating digits represent the lengths of file blocks and
 * free space blocks.
 *
 * The program operates in the following steps:
 * 1. Parses the input disk map to initialize the memory layout.
 * 2. Iteratively moves file blocks to the leftmost free space to eliminate gaps.
 * 3. Computes a checksum by summing the product of each file block's position and its ID.
 *
 * The checksum provides a numeric representation of the compacted disk's state.
 */
public class Main {

    //public static final String INPUT_FILE_LOCATION = "../input.txt";
    public static final String INPUT_FILE_LOCATION = "/home/marrase/Advent-of-Code/2024/day09b/input.txt";

    public static void main(String args[]) {
        File file = new File(INPUT_FILE_LOCATION);

        Disk disk = new Disk();

        try {     
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            String inputLine;

            int currentFileId = 0;
            boolean readingFile = true; // If not reading a file, it means it's reading a free space block
            
            while ((inputLine = br.readLine()) != null) {

                for (char currentChar : inputLine.toCharArray()) {
                    int memoryBlockSize = Integer.parseInt(Character.toString(currentChar));

                    //System.out.println(disk.getMemory());

                    if (readingFile ) {
                        if (memoryBlockSize != 0)
                            disk.getMemory().add(new FileMemoryBlock(currentFileId, memoryBlockSize));
                        
                        currentFileId++;
                        readingFile = false;

                    } else {
                        if (memoryBlockSize != 0)
                            disk.getMemory().add(new EmptyMemoryBlock(memoryBlockSize));

                        readingFile = true;
                    }


                }
            }
            br.close();

            disk.updateFirstEmptyMemoryBlockIndexCache();

            //System.out.println("Memory before: " + disk.getMemory());
            int memoryAnalyzerIndex = disk.getMemory().size() - 1;
            while (memoryAnalyzerIndex >= 0) {

                for (int i = 0; i < disk.getMemory().size(); ++i) {
                    //System.out.print(".");
                    MemoryBlock memoryBlock = disk.getMemory().get(i);
                    if (memoryBlock instanceof EmptyMemoryBlock && disk.getMemory().get(memoryAnalyzerIndex) instanceof FileMemoryBlock &&
                        memoryAnalyzerIndex > i && memoryBlock.getSize() >= disk.getMemory().get(memoryAnalyzerIndex).getSize()) {

                        //System.out.println("swapping " + memoryBlock + " with " + disk.getMemory().get(memoryAnalyzerIndex));
                        disk.swapElements(i, memoryAnalyzerIndex);
                        //System.out.println(disk.getMemory());
                        memoryAnalyzerIndex--;
                        System.out.println(memoryAnalyzerIndex);
                        i = disk.getFirstEmptyMemoryBlockIndexCache() - 1; // -1 because at the start of the for it increments by 1
                        System.out.println("Starting inner for from " + i+1 + " instead of 0");
                        //continue;

                        // TODO : if everything fails: compact memory
                    }
                }

                memoryAnalyzerIndex--;
                System.out.println(memoryAnalyzerIndex);
                //System.out.print(".");
            }
            //System.out.println("Memory after: " + disk.toString());

            System.out.println(String.format("The result is: %s", disk.calculateChecksumFromStringMemoryConversionToString()));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (IndexOutOfBoundsException e) {
            System.err.println(String.format("Error: Swap index exceeds memory size limit: %s", e.toString()));
            return;
        }
    }
}