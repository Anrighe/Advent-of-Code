import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages the memory layout and operations on the memory blocks.
 * The elements contained in the memory of a disk simply contain a value (if empty they do not contain anything).
 * All addressing is handled via the Disk class.
 */
public class Disk {

    private LinkedList<MemoryBlock> memory;
    private int firstEmptyMemoryBlockIndexCache;

    /** Initializes the memory on the Disk */
    public Disk() {
        memory = new LinkedList<>();
        firstEmptyMemoryBlockIndexCache = 0; 
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
     * @param firstElementIndex  Index of the first element (empty memory allocation).
     * @param secondElementIndex Index of the second element (file to place in the first element index).
     * @throws IndexOutOfBoundsException If indices are invalid.
     */
    public void swapElements(int firstElementIndex, int secondElementIndex) throws IndexOutOfBoundsException {
        int memorySize = memory.size();
        
        EmptyMemoryBlock emptyMemoryBlockToSwap = null;
        if (memory.get(firstElementIndex) instanceof EmptyMemoryBlock)
            emptyMemoryBlockToSwap = (EmptyMemoryBlock) memory.get(firstElementIndex);
        int emptyMemoryBlockToSwapSize = emptyMemoryBlockToSwap.getSize();

        FileMemoryBlock fileMemoryBlockToSwap = null;
        if (memory.get(secondElementIndex) instanceof FileMemoryBlock)
            fileMemoryBlockToSwap = (FileMemoryBlock) memory.get(secondElementIndex);
        int fileToSwapSize = memory.get(secondElementIndex).getSize();

        if (firstElementIndex < 0 || secondElementIndex < 0 || firstElementIndex >= memorySize || secondElementIndex >= memorySize) {
            throw new IndexOutOfBoundsException("Invalid indices for swapping.");
        }

        if (emptyMemoryBlockToSwap != null && fileMemoryBlockToSwap != null /*&& emptyMemoryBlockToSwapSize >= fileToSwapSize*/) {
            
            MemoryBlock tmp = memory.get(firstElementIndex);
            memory.set(firstElementIndex, memory.get(secondElementIndex));
            memory.set(secondElementIndex, tmp);
            if (emptyMemoryBlockToSwapSize != fileToSwapSize) {
                memory.add(firstElementIndex + 1, new EmptyMemoryBlock(emptyMemoryBlockToSwapSize - fileToSwapSize));
                emptyMemoryBlockToSwap.setSize(fileToSwapSize);
            }

            if (firstElementIndex == firstEmptyMemoryBlockIndexCache)
                firstEmptyMemoryBlockIndexCache = getNextEmptyMemoryStartingFromIndex(firstElementIndex + 1);
        }
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

    public long calculateChecksumFromStringMemoryConversionToString() {
        long result = 0;
        char memorySlotChar;
        for (int index = 0; index < toString().length(); ++index) {
            memorySlotChar = toString().charAt(index);
            if (memorySlotChar != '.') 
                result += Character.getNumericValue(memorySlotChar) * index;

            System.out.println(result);
        }
        return result;
    }

    public void updateFirstEmptyMemoryBlockIndexCache() {
        firstEmptyMemoryBlockIndexCache = getFirstEmptyMemoryBlockPosition();
    }

    public int getNextEmptyMemoryStartingFromIndex(int index) {
        for (int i = index; i < memory.size(); ++i) {
            if (memory.get(i) instanceof EmptyMemoryBlock)
                return i;
        }
        return firstEmptyMemoryBlockIndexCache;
    }

    public int getFirstEmptyMemoryBlockIndexCache() {
        return firstEmptyMemoryBlockIndexCache;
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
