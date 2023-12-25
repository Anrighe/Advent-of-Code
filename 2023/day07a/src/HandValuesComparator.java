import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;

public class HandValuesComparator implements Comparator<String>
{
    private Map <String, Pair<Integer, Integer>> base;

    HandValuesComparator(Map <String, Pair<Integer, Integer>> base) 
    {
        this.base = base;
    }

    /**
     * Compares two strings based on their associated values in the 'base' map.
     * The comparison is done in two steps:
     * 1. First, it compares the second value of the pair in the 'base' map.
     * 2. If the second values are the same, it compares each character of the key string.
     *    The characters are compared based on their order in the 'order' list.
     * 
     * @param s1 the first string to compare
     * @param s2 the second string to compare
     * @return a negative integer if s1 is less than s2, zero if they are equal, or a positive integer if s1 is greater than s2
     */
    @Override
    public int compare(String s1, String s2)
    {
        // Compares the seoond value of the pair
        int result = Integer.compare(base.get(s1).getValue(), base.get(s2).getValue());
        
        // If the second value is the same, it compares each character of the key
        if (result == 0)
        {
            List<String> order = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A");
            for (int i = 0; i < s1.length(); ++i)
            {
                char char1 = s1.charAt(i);
                char char2 = s2.charAt(i);
    
                result = Integer.compare(order.indexOf(Character.toString(char1)), order.indexOf(Character.toString(char2)));

                if (result != 0)
                    return result;
            }   
        }

        return result;
    }
}
