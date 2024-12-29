/**
 * The MemoryBlock interface represents a block of memory.
 * It provides methods to check if the block is empty and to get a string representation of the block.
 */
interface MemoryBlock {
    public boolean isEmptyBlock();
    public int getSize();
    public String toString();
}