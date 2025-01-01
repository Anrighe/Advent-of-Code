/** Class representing an empty memory block. */
public class EmptyMemoryBlock implements MemoryBlock {

    private long size;

    /** Constructs an empty memory block. */
    public EmptyMemoryBlock(int size) {
        this.size = (long) size;
    }

    public EmptyMemoryBlock(long size) {
        this.size = size;
    }

    /**
     * Checks if this is an empty block.
     * @return true, as a {@class EmptyMemoryBlock} is always an empty memory block.
     */
    public boolean isEmptyBlock() {
        return true;
    }

    /**
     * Gets the size of the empty memory block.
     * @return Empty memory block size.
     */    
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of the empty memory block.
     * @param fileBlockId New empty memory block size.
     */        
    public void setSize(int size) {
        this.size = size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        String ret = "";
        for (int i = 0; i < size; ++i)
            ret += ".";
        return ret;
    }

}