/**
 * The MemoryBlock interface represents a block of memory.
 * It provides methods to check if the block is empty, to get its size
 * and to get its string representation.
 */
interface MemoryBlock {
    public boolean isEmptyBlock();
    public long getSize();
    public String toString();
}