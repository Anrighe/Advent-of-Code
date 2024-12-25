import java.util.Iterator;
import java.util.LinkedList;

/**
 * Manages the memory layout and operations on the memory blocks.
 * The elements contained in the memory of a disk simply contain a value (if empty they do not contain anything).
 * All addressing is handled via the Disk class.
 */
public class Disk {

    private LinkedList<MemoryBlock> memory;

    /** Initializes the memory on the Disk */
    public Disk() {
        memory = new LinkedList<>();
    }

    /**
     * Checks if the disk contains any empty memory block.
     * @return True if an empty block exists; otherwise, false.
     */    
    public boolean hasEmptyMemoryBlock() {
        if (memory.size() == 0)
            return false;

        for (MemoryBlock memoryBlock : memory) {
            if (memoryBlock instanceof EmptyMemoryBlock) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the position of the first empty memory block.
     * @return Index of the first empty block or a default value of Integer.MIN_VALUE if no empty block is in memory.
     */
    public int getFirstEmptyMemoryBlockPosition() {
        int position = 0;
        for (MemoryBlock memoryBlock : memory) {
            if (memoryBlock instanceof EmptyMemoryBlock)
                return position;
            position++;
        }
        return Integer.MIN_VALUE;
    }

    /**
     * Checks if the disk contains any file memory block.
     * @return True if a file block exists; otherwise, false.
     */
    public boolean hasFileMemoryBlock() {
        if (memory.size() == 0)
            return false;

        for (MemoryBlock memoryBlock : memory) {
            if (memoryBlock instanceof FileMemoryBlock)
                return true;
        }
        return false;
    }

    /**
     * Gets the position of the last file memory block.
     * @return Index of the last file block or 1 if none exist.
     */
    public int getLastFileMemoryBlockPosition() {
        int position = memory.size()-1;
        Iterator<MemoryBlock> descendingIterator = memory.descendingIterator();
        
        while (descendingIterator.hasNext()) {
            MemoryBlock block = descendingIterator.next();
            
            if (block instanceof FileMemoryBlock)
                return position;

            position--;
        }
        return 1;
    }

    /**
     * Provides access to the disk memory.
     * @return LinkedList of memory blocks.
     */
    public LinkedList<MemoryBlock> getMemory() {
        return memory;
    }

    /**
     * Swaps two elements in the memory based on their indices.
     * 
     * @param firstElementIndex  Index of the first element.
     * @param secondElementIndex Index of the second element.
     * @throws IndexOutOfBoundsException If indices are invalid.
     */
    public void swapElements(int firstElementIndex, int secondElementIndex) throws IndexOutOfBoundsException {
        int memorySize = memory.size();

        if (firstElementIndex < 0 || secondElementIndex < 0 || firstElementIndex >= memorySize || secondElementIndex >= memorySize) {
            throw new IndexOutOfBoundsException("Invalid indices for swapping.");
        }

        MemoryBlock tmp = memory.get(firstElementIndex);
        memory.set(firstElementIndex, memory.get(secondElementIndex));
        memory.set(secondElementIndex, tmp);
    }

    /**
     * Checks if the memory is compact: a compact memory has no empty memory blocks in between FileMemoryBlocks.
     * If present, a compact memory has all the empty memory blocks after all the FileMemoryBlocks.
     * @return True if there are no gaps between file blocks; otherwise, false.
     */
    public boolean isMemoryCompact() {
        if (getFirstEmptyMemoryBlockPosition() > getLastFileMemoryBlockPosition())
            return true;
        else
            return false;
    }

    /**
     * Calculates the checksum of the disk by iterating over the memory blocks.
     * For each memory block that is an instance of FileMemoryBlock, it adds the product
     * of the block's index and its file block ID (which has because it's an instance of FileMemoryBlock) to the result.
     *
     * @return the calculated checksum as a long value
     */
    public long calculateChecksum() {
        long result = 0;
        int index = 0;
        for (MemoryBlock memoryBlock : memory) {
            if (memoryBlock instanceof FileMemoryBlock) {
                result += index * ((FileMemoryBlock) memoryBlock).getFileBlockId();
            }
            index++;
        }
        return result;
    }

    @Override
    public String toString() {
        String output = "";
        for (MemoryBlock memoryBlock : memory) {
            output += memoryBlock.toString();
        }
        return output;
    }
}
