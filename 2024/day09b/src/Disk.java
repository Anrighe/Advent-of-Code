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
    public int swapElements(int firstElementIndex, int secondElementIndex) throws IndexOutOfBoundsException {
        int memorySize = memory.size();
        int compactedIndex = secondElementIndex;
        
        EmptyMemoryBlock emptyMemoryBlockToSwap = null;
        if (memory.get(firstElementIndex) instanceof EmptyMemoryBlock)
            emptyMemoryBlockToSwap = (EmptyMemoryBlock) memory.get(firstElementIndex);
        long emptyMemoryBlockToSwapSize = emptyMemoryBlockToSwap.getSize();

        FileMemoryBlock fileMemoryBlockToSwap = null;
        if (memory.get(secondElementIndex) instanceof FileMemoryBlock)
            fileMemoryBlockToSwap = (FileMemoryBlock) memory.get(secondElementIndex);
        long fileToSwapSize = memory.get(secondElementIndex).getSize();

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

                compactedIndex = compactEmptyMemorySpaces(secondElementIndex + 1); // A new element has been added so the second index is shifted by 1
            } else 
                compactedIndex = compactEmptyMemorySpaces(secondElementIndex);
            //System.out.println("After compacting: " + memory.toString());

            if (firstElementIndex == firstEmptyMemoryBlockIndexCache)
                firstEmptyMemoryBlockIndexCache = getNextEmptyMemoryStartingFromIndex(firstElementIndex);
        }
        return compactedIndex;
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
            if (memorySlotChar != '.') {
                result += (long) Character.getNumericValue(memorySlotChar) * (long) index;
                System.out.println(result + " | " + index);
            }
        }
        return result;
    }

    public long calculateChecksum() {
        long result = 0;
        long index = 0;
        int memorySize = memory.size();

        for (MemoryBlock memoryBlock : memory) {
            if (memoryBlock instanceof FileMemoryBlock && memoryBlock.getSize() != 0) {

                for (int i = 0; i < memoryBlock.getSize(); ++i) {
                    result += ((FileMemoryBlock) memoryBlock).getFileBlockId() * index;
                    //System.out.println(String.format("%s * %s", ((FileMemoryBlock) memoryBlock).getFileBlockId(), index));
                    System.out.println(result + " | " + i + "/" + memorySize);
                    index++;
                }
            } else {
                if (memoryBlock.getSize() != 0)
                    index += memoryBlock.getSize();
                else
                    index++;
            }

        }


        return result;
    }

    public void updateFirstEmptyMemoryBlockIndexCache() {
        firstEmptyMemoryBlockIndexCache = getFirstEmptyMemoryBlockPosition();
    }

    public int getNextEmptyMemoryStartingFromIndex(int index) {
        for (int i = index + 1; i < memory.size(); ++i) {
            if (memory.get(i) instanceof EmptyMemoryBlock)
                return i;
        }
        return firstEmptyMemoryBlockIndexCache;
    }

    public int getFirstEmptyMemoryBlockIndexCache() {
        return firstEmptyMemoryBlockIndexCache;
    }

    public int compactEmptyMemorySpaces(int index) {
        if (memory.get(index) instanceof EmptyMemoryBlock) {
            int adjacentEmptySpaceSize = findAdjacentEmptySpaceSizeFromEmptySpaceIndex(index);
            int leftMostEmptyAdjacentBlockIndex = findLeftMostAdjacentBlockIndex(index);

            if (memory.get(index).getSize() == adjacentEmptySpaceSize)
                return index;

            while (leftMostEmptyAdjacentBlockIndex >= 0 && leftMostEmptyAdjacentBlockIndex < memory.size() 
                && memory.get(leftMostEmptyAdjacentBlockIndex) instanceof EmptyMemoryBlock) {
                
                memory.remove(leftMostEmptyAdjacentBlockIndex);
            }

            memory.add(leftMostEmptyAdjacentBlockIndex, new EmptyMemoryBlock(adjacentEmptySpaceSize));
            return leftMostEmptyAdjacentBlockIndex;
        } else 
            return index;
    }

    public int findAdjacentEmptySpaceSizeFromEmptySpaceIndex(int index) {
        int result = 0;
        if (memory.get(index) instanceof EmptyMemoryBlock) {

            int offset = 1;
    
            result += memory.get(index).getSize();
    
            while (index - offset >= 0 && memory.get(index - offset) instanceof EmptyMemoryBlock) {
                result += memory.get(index - offset).getSize();
                offset++;
            }
    
            offset = 1;
    
            while (index + offset < memory.size() && memory.get(index + offset) instanceof EmptyMemoryBlock) {
                result += memory.get(index + offset).getSize();
                offset++;
            }
        }

        return result;
    }

    public int findLeftMostAdjacentBlockIndex(int index) {
        int result = index;
        int offset = 0;
        while (index - offset >= 0 && memory.get(index - offset) instanceof EmptyMemoryBlock) {
            result = index - offset;
            offset++;
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
