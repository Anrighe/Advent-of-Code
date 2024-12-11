import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;


/**
 * Processes page order rules and update sequences, ensuring correctness
 * and calculating the sum of middle pages from corrected sequences.
 *
 * Tasks:
 * - Reads and parses page order rules and update sequences from an input file.
 * - Validates and reorders update sequences as needed.
 * - Calculates the sum of middle pages only from corrected update sequences.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";

    /**
     * Retrieves the list of update numbers to the left (before) a specified occurrence
     * in the given update number list.
     *
     * @param updateNumberList the list of update numbers.
     * @param occurrence the specific number whose left neighbors are sought.
     * @return a list of update numbers that are before the specified occurrence.
     */
    public static List<Integer> getLeftUpdateNumbersOfOccurrence(List<Integer> updateNumberList, int occurrence) {
        int occurrencePosition = updateNumberList.indexOf(occurrence);

        if (occurrencePosition == 0) 
            return new ArrayList<Integer>();

        return updateNumberList.subList(0, occurrencePosition);
    }

    /**
     * Retrieves the list of update numbers to the right (after) a specified occurrence
     * in the given update number list.
     *
     * @param updateNumberList the list of update numbers.
     * @param occurrence the specific number whose right neighbors are sought.
     * @return a list of update numbers that are after the specified occurrence.
     */
    public static List<Integer> getRightUpdateNumbersOfOccurrence(List<Integer> updateNumberList, int occurrence) {
        int occurrencePosition = updateNumberList.indexOf(occurrence);

        if (occurrencePosition == updateNumberList.size()-1) 
            return new ArrayList<Integer>();

        return updateNumberList.subList(occurrencePosition+1, updateNumberList.size());
    }

    /**
     * Checks whether the given update number list adheres to the page order rules
     * specified in the page order map.
     *
     * @param pageOrderMap a map of page numbers to their ordering constraints.
     * @param updateNumberList the list of update numbers to validate.
     * @return true if the update sequence is in the correct order, false otherwise.
     */
    public static boolean arePageUpdateInCorrectOrder(
        Map<Integer, PageOrder> pageOrderMap,
        List<Integer> updateNumberList
    ) {
        List<Integer> previousPageNumbersOfOccurence;
        List<Integer> nextPageNumbersOfOccurence;

        for (int pageNumber : updateNumberList) {
            // Get all the update numbers on the left of the pageNumber (exclued)
            previousPageNumbersOfOccurence = getLeftUpdateNumbersOfOccurrence(updateNumberList, pageNumber);

            // Get all the update numbers on the right of the pageNumber (exclued)
            nextPageNumbersOfOccurence = getRightUpdateNumbersOfOccurrence(updateNumberList, pageNumber);

            for (int previousPageNumber : previousPageNumbersOfOccurence) {
                if (pageOrderMap.get(pageNumber).getNext().contains(previousPageNumber))
                    return false;
            }

            for (int nextPageNumber : nextPageNumbersOfOccurence) {
                if (pageOrderMap.get(pageNumber).getPrevious().contains(nextPageNumber))
                    return false;
            }            
        }

        return true;
    }

    /**
     * Finds the middle page number in an odd-sized update number list.
     *
     * @param updatePageList the list of update page numbers (must have an odd size).
     * @return the page number at the middle of the list.
     * @throws AssertionError if the list size is even.
     */
    public static int getMiddlePage(List<Integer> updatePageList) throws AssertionError {
        assert updatePageList.size() %2 != 0;
        int middlePagePosition = (updatePageList.size() / 2);

        return updatePageList.get(middlePagePosition);
    }

    /**
     * Reorders the given update list according to the specified page order rules.
     *
     * @param pageOrderMap a map of page numbers to their ordering constraints.
     * @param updateNumberList the list of update numbers to reorder.
     * @return the correctly reordered list of update numbers.
     */
    public static List<Integer> reorderUpdates(Map<Integer, PageOrder> pageOrderMap, List<Integer> updateNumberList) {
        List<Integer> previousPageNumbersOfOccurence;
        List<Integer> nextPageNumbersOfOccurence;
        int pageNumber;

        for (int analyzedPagePosition = 0; analyzedPagePosition < updateNumberList.size(); ++analyzedPagePosition) {
            pageNumber = updateNumberList.get(analyzedPagePosition);
    
            // Get all the update numbers on the left of the pageNumber (exclued)
            previousPageNumbersOfOccurence = getLeftUpdateNumbersOfOccurrence(updateNumberList, pageNumber);

            // Get all the update numbers on the right of the pageNumber (exclued)
            nextPageNumbersOfOccurence = getRightUpdateNumbersOfOccurrence(updateNumberList, pageNumber);

            for (int previousPage : previousPageNumbersOfOccurence) {
                if (pageOrderMap.get(pageNumber).getNext().contains(previousPage)) {
                    Collections.swap(updateNumberList, updateNumberList.indexOf((previousPage)), analyzedPagePosition);
                    analyzedPagePosition = -1;
                    break;
                }
            }

            for (int nextPage : nextPageNumbersOfOccurence) {                if (pageOrderMap.get(pageNumber).getPrevious().contains(nextPage)) {
                    Collections.swap(updateNumberList, updateNumberList.indexOf((nextPage)), analyzedPagePosition); 
                    analyzedPagePosition = -1;
                    break;
                }
            }
            
        }

        return updateNumberList;
    }

    public static void main(String args[]) {
        int result = 0;

        File file = new File(INPUT_FILE_LOCATION);

        Map<Integer, PageOrder> pageOrderMap = new HashMap<>();

        try {     
            BufferedReader br = new BufferedReader(new FileReader(file));

            String inputLine;
            Pattern pattern = Pattern.compile("([0-9]+)\\|([0-9]+)");
            Matcher matcher;

            int firstNumber;
            int secondNumber;

            List<Integer> updateNumberList;
            List<Integer> correctedUpdateNumberList;

            while ((inputLine = br.readLine()) != null) {
                matcher = pattern.matcher(inputLine);

                if (matcher.matches()) {

                    firstNumber = Integer.parseInt(matcher.group(1));
                    secondNumber = Integer.parseInt(matcher.group(2));


                    if (!pageOrderMap.containsKey(firstNumber))
                        pageOrderMap.put(firstNumber, new PageOrder(firstNumber));

                    pageOrderMap.get(firstNumber).addNext(secondNumber);

                    if (!pageOrderMap.containsKey(secondNumber))
                        pageOrderMap.put(secondNumber, new PageOrder(secondNumber));

                    pageOrderMap.get(secondNumber).addPrevious(firstNumber);

                } else {
                    if (!inputLine.isEmpty()) {
                        
                        String[] updateArray = inputLine.split(",");
                        assert updateArray.length % 2 != 0;

                        updateNumberList = new ArrayList<>();

                        for (int i = 0; i < updateArray.length; ++i)
                            updateNumberList.add(Integer.parseInt(updateArray[i]));

                        if (!arePageUpdateInCorrectOrder(pageOrderMap, updateNumberList)) {
                            correctedUpdateNumberList = reorderUpdates(pageOrderMap, updateNumberList);
                            result += getMiddlePage(correctedUpdateNumberList);
                        }
                    }
                }
            }
            br.close();

            System.out.println(String.format("The total sum of the middle page numbers is: %s", result));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Even update list found, middle page does not exist: %s", e.toString()));
            return;
        }
    }
}