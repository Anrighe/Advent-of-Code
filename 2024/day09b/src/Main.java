import java.io.*;

/**
 * Simulates the process of compacting a fragmented disk by moving file blocks to fill gaps in free space, 
 * then calculates a checksum based on the final arrangement of file blocks. 
 * The input is a dense format disk map, where alternating digits represent the lengths of file blocks and
 * free space blocks.
 *
 * The program operates in the following steps:
 * 1. Parses the input disk map to initialize the memory layout.
 * 2. Iteratively checks for free space and if found, moves the rightmost entire file block to the leftmost 
 *     free space to eliminate gaps. If the file dimension is less than the empty space dimension,
 *      manages both the empty space that remains from the movement and if present, compacts all the empty 
 *       spaces from the previously occupied spaces from the FileMemoryBlock.
 *    If no empty space is found to accomodate the whole file, the process skips to the next file (to the left).
 * 3. Computes a checksum by summing the product of each file block's position and its ID.
 *
 * The checksum provides a numeric representation of the compacted disk's state.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "/home/marrase/Advent-of-Code/2024/day09b/input.txt";

    public static void main(String args[]) {
        File file = new File(INPUT_FILE_LOCATION);
        
        Disk disk = new Disk();
        try {     
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            String inputLine;

            int currentFileId = 0;
            boolean readingFile = true; // If not reading a file, it means it's reading a free space block
            
            // Building memory blocks inside memory of the disk
            while ((inputLine = br.readLine()) != null) {

                for (char currentChar : inputLine.toCharArray()) {
                    int memoryBlockSize = Integer.parseInt(Character.toString(currentChar));

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

            // Finds the first empty memory block available and updates the cache of the disk with its location
            disk.updateFirstEmptyMemoryBlockIndexCache();
                
            // The Memory Analyzer Index is used to point to iterate trough File Memory Block (from the rightmost)
            int fileMemoryIndex = disk.getMemory().size() - 1;

            while (fileMemoryIndex >= 0) {

                for (int emptyMemoryIndex = disk.getFirstEmptyMemoryBlockIndexCache(); emptyMemoryIndex < fileMemoryIndex; ++emptyMemoryIndex) {
                    MemoryBlock memoryBlock = disk.getMemory().get(emptyMemoryIndex);

                    // If the found memory space is Empty, and the other block is a File Memory Block
                    //  if the File Memory Block is to the right of the Empty Memory Block and
                    //   the Empty Memory Block is big enough to accomodate the File Memory Block
                    if (memoryBlock instanceof EmptyMemoryBlock && 
                        disk.getMemory().get(fileMemoryIndex) instanceof FileMemoryBlock &&
                        fileMemoryIndex > emptyMemoryIndex && 
                        memoryBlock.getSize() >= disk.getMemory().get(fileMemoryIndex).getSize()) {

                        
                        // Swapping elements and getting back the index of the compacted empty memory space (could be the same)
                        fileMemoryIndex = disk.swapElements(emptyMemoryIndex, fileMemoryIndex);
                        fileMemoryIndex--;

                        // Decrementing by 1 because at the start of the for it increments by 1
                        emptyMemoryIndex = disk.getFirstEmptyMemoryBlockIndexCache() - 1; 
                    }
                }
                fileMemoryIndex--; // Regardless of swaps, the file index is decremented
            }
            System.out.println(String.format("The result is: %s", disk.calculateChecksum()));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (IndexOutOfBoundsException e) {
            System.err.println(String.format("Error: exceeding memory limit: %s", e.toString()));
            return;
        }
    }
}