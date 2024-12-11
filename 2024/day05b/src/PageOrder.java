import java.util.ArrayList;
import java.util.List;

/**
 * Represents the order constraints for a specific page in the update rules.
 *
 * Stores:
 * - A list of pages that must precede this page in an update sequence.
 * - A list of pages that must follow this page in an update sequence.
 */
public class PageOrder {

    private int pageNumber;
    private List<Integer> previous;
    private List<Integer> next;
    
    public PageOrder(int pageNumber) {
        this.pageNumber = pageNumber;
        previous = new ArrayList<>();
        next = new ArrayList<>();
    }

    /**
     * Adds a page number to the list of pages that must precede this page in an update sequence.
     * @param page the page number to add to the preceding list.
     */
    public void addPrevious(int page) {
        previous.add(page);
    }

    /**
     * Adds a page number to the list of pages that must follow this page in an update sequence.
     * @param page the page number to add to the succeeding list.
     */
    public void addNext(int page) {
        next.add(page);
    }    
    /**
     * Retrieves the page number represented by this object.
     */
    public int getPageNumber() {
        return pageNumber;
    }

    public List<Integer> getPrevious() {
        return previous;
    }

    public List<Integer> getNext() {
        return next;
    }

    @Override
    public String toString() {
        return String.format("%s <- Previous {%s} Next -> %s\n", previous, pageNumber, next);
    }
}
