import java.util.Iterator;
import java.util.LinkedList;

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
     * Swaps the contents of two memory blocks at the specified indices. The swap is only
     * performed if the block at the first index is an empty memory block, and the block at
     * the second index is a file memory block. If the swap occurs and the sizes of the blocks
     * differ, the function adjusts the memory layout and compacts empty memory spaces.
     * 
     * @param firstElementIndex  The index of the first memory block, which must be an empty memory block.
     * @param secondElementIndex The index of the second memory block, which must be a file memory block.
     * @return The index of the compacted memory after the swap, reflecting any adjustments.
     * @throws IndexOutOfBoundsException If either index is invalid (negative or out of bounds).
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
    public boolean isMemoryFullyCompact() {
        if (getFirstEmptyMemoryBlockPosition() > getLastFileMemoryBlockPosition())
            return true;
        else
            return false;
    }

    /**
     * Calculates a checksum for the memory based on the contents of each memory block. 
     * The checksum is computed by iterating over all memory blocks and performing the following:
     * 
     * If the memory block is a {@code FileMemoryBlock} and has a non-zero size, each file block ID
     *  in the memory block contributes to the checksum. The contribution is calculated as the product 
     *   of the file block ID and a global index that tracks the position across all memory blocks.
     * If the memory block is not a {@code FileMemoryBlock}, or its size is zero, the global index 
     *  is incremented by the size of the block or by one for zero-sized blocks.
     * 
     * @return The computed checksum as a long value, representing the weighted sum of file block IDs.
     */
    public long calculateChecksum() {
        long result = 0;
        long index = 0;

        for (MemoryBlock memoryBlock : memory) {
            if (memoryBlock instanceof FileMemoryBlock && memoryBlock.getSize() != 0) {

                for (int i = 0; i < memoryBlock.getSize(); ++i) {
                    result += ((FileMemoryBlock) memoryBlock).getFileBlockId() * index;
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

    /**
     * Manually updates the cache for the firstEmptyMemoryBlockIndexCache value.
     */
    public void updateFirstEmptyMemoryBlockIndexCache() {
        firstEmptyMemoryBlockIndexCache = getFirstEmptyMemoryBlockPosition();
    }

    /**
     * Starting from the specified index, finds the next index in memory that is empty.
     * If no index is found returns the index contained in firstEmptyMemoryBlockIndexCache.
     * @param index
     * @return
     */
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

    /**
     * Compacts adjacent empty memory spaces starting from the specified index. 
     * If the memory block at the given index is an empty memory block, the function 
     *  consolidates all adjacent empty blocks into a single block, removes the individual 
     *   empty blocks, and inserts a new empty block with the total size of the consolidated blocks.
     * If the block at the index is not an empty memory block, the function returns the index unchanged.
     *
     * @param index The index of the memory block to start the compaction from.
     * @return The index of the consolidated empty memory block, or the original index if no compaction occurred.
     */
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

    /**
     * Finds the total size of adjacent empty memory blocks starting from a given index.
     *
     * Calculates the size of the empty memory block at the specified index and then adds the sizes 
     * of any contiguous empty memory blocks to the left and right of the specified index.
     *
     * @param index the index of the empty memory block to start from
     * @return the total size of adjacent empty memory blocks
     */
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

    /**
     * Finds the index of the leftmost adjacent empty memory block starting from the given index.
     *
     * This method searches to the left of the given index to find the first instance of an
     * {@class EmptyMemoryBlock}: it returns the index of this block. 
     * If no such block is found, it returns the original index.
     *
     * @param index the starting index to search from
     * @return the index of the leftmost adjacent empty memory block, or the original index if no
     *          empty block is found
     */
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
