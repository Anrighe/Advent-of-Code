import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Solution
{
    /**
     * Generates a mapping based on the provided conversion map name, data, and scanner.
     * The mapping is stored in the given list of lists.
     *
     * @param myReader          The scanner used to read the data
     * @param conversionMapName The name of the conversion map
     * @param data              The data containing the mapping information
     * @param mapping           The list of lists to store the generated mapping
     */
    public static void generateMapping(Scanner myReader, String conversionMapName, String data, List<List<Long>> mapping)
    {
        String splitData[];

        Long destinationRangeStart;
        Long sourceRangeStart;
        Long rangeLength;        

        if (data.contains(conversionMapName))
        {
            while (myReader.hasNextLine() && ((data != "\n") || (data != "") || (data != " ")))
            {
                if ((data = myReader.nextLine()) == "")
                    break;

                splitData = data.split(" ");
                
                destinationRangeStart = Long.parseLong(splitData[0]);
                sourceRangeStart = Long.parseLong(splitData[1]);
                rangeLength = Long.parseLong(splitData[2]);

                List<Long> mappingEntry = new ArrayList<Long>();
                mappingEntry.add(destinationRangeStart);
                mappingEntry.add(sourceRangeStart);
                mappingEntry.add(rangeLength);
                mapping.add(mappingEntry);    
            }
        }        
    }

    /**
     * Converts the given seed RANGE, based on the provided mapping.
     * 
     * @param mapping   The mapping to use for the conversion
     * @param seed      The seed to convert
     * @param seeds     The list of seeds
     * @param seedIndex The index of the seed to convert
     * @return          The converted seed
     */
    public static List<List<Long>> conversionStep(List<List<Long>> mapping, List<List<Long>> seeds, int seedIndex)
    {
        List<Long> seedRangeEntry = seeds.get(seedIndex);

        Long startingSeedRange = seedRangeEntry.get(0);
        Long endingSeedRange = seedRangeEntry.get(1);

        Long startingConversionRange = null;
        Long endingConversionRange = null;
        Long newStartingConversionRange = null;
        Long newEndingConversionRange = null;

        for (var mappingEntry : mapping)
        {
            startingConversionRange = mappingEntry.get(1);
            endingConversionRange = mappingEntry.get(1) + mappingEntry.get(2) - 1L;
            
            newStartingConversionRange = mappingEntry.get(0);
            newEndingConversionRange = mappingEntry.get(0) + mappingEntry.get(2) - 1L;

            // If the seed range is fully contained in the conversion range
            if (startingSeedRange >= startingConversionRange && endingSeedRange <= endingConversionRange) // Fully contained
            {   
                List <Long> newEntry = new ArrayList<>();
                
                newEntry.add(startingSeedRange - startingConversionRange + newStartingConversionRange);
                newEntry.add(endingSeedRange - startingConversionRange + newStartingConversionRange);
                seeds.set(seedIndex, newEntry);

                return seeds;
            }
            // If a partial left is found
            else if (startingSeedRange < startingConversionRange && endingSeedRange >= startingConversionRange && endingSeedRange <= endingConversionRange)
            {
                List <Long> newEntry = new ArrayList<>();
                newEntry.add(newStartingConversionRange);
                newEntry.add(endingSeedRange - startingConversionRange + newStartingConversionRange);
                seeds.set(seedIndex, newEntry);

                // Appends the extra to the seeds list
                List <Long> extraEntry = new ArrayList<>();
                extraEntry.add(startingSeedRange);
                extraEntry.add(startingConversionRange - 1L);
                seeds.add(extraEntry);

                return seeds;
            }
            // If a partial right is found
            else if (startingSeedRange > startingConversionRange && startingSeedRange <= endingConversionRange && endingSeedRange > endingConversionRange)
            {
                
                List <Long> newEntry = new ArrayList<>();
                newEntry.add(startingSeedRange - startingConversionRange + newStartingConversionRange);
                newEntry.add(newEndingConversionRange);
                seeds.set(seedIndex, newEntry);

                // Appends the extra to the seeds list
                List <Long> extraEntry = new ArrayList<>();
                extraEntry.add(endingConversionRange + 1L);
                extraEntry.add(endingSeedRange);
                seeds.add(extraEntry);

                return seeds;
            }
        }
        
        // If no match is found
        return seeds;
    }
    
    /**
     * FIRST PART:
     * Generates for each starting seed the corresponding result based on the input mapping consisting of:
     * - seed-to-soil map
     * - soil-to-fertilizer map
     * - fertilizer-to-water map
     * - water-to-light map
     * - light-to-temperature map
     * - temperature-to-humidity map
     * - humidity-to-location map
     * With some given seeds, calculates the resulting location value after all the mappings are applied.
     * 
     * Mapping generation example:
     * seed-to-soil map:
     *  50 98 2
     *  52 50 48
     * 
     * converts to a mapping equal to:
     * seed  soil
     *  98    50
     *  99    51
     * 
     * If the mapping is not specified explicitly, the number stays the same for the corresponding step. 
     * The range of number are covered by the long data type, but to ensure no overflow happens, the BigInteger data type is used.
     * ---------------------------------------------------------------------------------------------------------------------------
     * 
     * SECOND PART:
     * In the second part of the problem the seeds are given as a range of numbers, for example the pair:
     * "3416930225 56865175" indicates that the range of seeds is from 3416930225 to 3416930225 + 56865175 - 1 = 3473795400.
     * So there technically are 56865175 elements that need to be converted 7 times (one for each step).
     * 
     * Since the brute force approach would not have been feasible, the conversion is done by using the ranges of the inputs.
     * In this solution, the elements that needs to be converted have been called seeds, despite the fact is the name of the first 
     * elements to be converted.
     * 
     * With ranges there could be 4 different cases:
     * 1) The seed range is fully contained in the conversion range:
     *  In this case the seed range is immediately converted to the corresponding range of the conversion map.
     *  Example:
     *      seed range: (79, 92) is fully contained in the conversion range (50, 98)
     * 
     * 2) A Partial Left is found:
     *  In this case the seed range is partially contained in the conversion range, but the left part of the seed range is not.
     *  Example:
     *      seed range: (1, 10) is partially contained in the conversion range (5, 15)
     * 
     * 3) A Partial Right is found:
     *  Same as the previous case, but the right part of the seed range is not contained in the conversion range.
     *  Example:
     *      seed range: (10, 20) is partially contained in the conversion range (5, 15)
     * 
     * 4) No match is found:
     *  In this case the seed range is not contained in the conversion range.
     *  Example:
     *      seed range: (1, 10) is not contained in the conversion range (15, 20)
     * 
     * When a partial match of the range is found, a new range needs to be created and added to the seeds list, meanwhile the part of
     * the seeds range that matches gets converted. 
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        Long res = Long.MAX_VALUE;

        List<List<Long>> seeds = new ArrayList<>();

        List<List<Long>> seedToSoil = new ArrayList<List<Long>>();
        List<List<Long>> soilToFertilizer = new ArrayList<List<Long>>();
        List<List<Long>> fertilizerToWater = new ArrayList<List<Long>>();
        List<List<Long>> waterToLight = new ArrayList<List<Long>>();
        List<List<Long>> lightToTemperature = new ArrayList<List<Long>>();
        List<List<Long>> temperatureToHumidity = new ArrayList<List<Long>>();
        List<List<Long>> humidityToLocation = new ArrayList<List<Long>>();

        Long startingSeedRange = null;
        boolean startingSeedRangeFound = false;

        try
        {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);

            String data = "";
            String[] splitData;

            if (myReader.hasNextLine())
            {
                data = myReader.nextLine();
                splitData = data.split(":");

                for (String element : splitData[1].stripLeading().split(" "))
                {   
                    if (!startingSeedRangeFound)
                    {
                        startingSeedRange = Long.parseLong(element);
                        startingSeedRangeFound = true;
                    }
                    else
                    {   
                        List<Long> seedRange = new ArrayList<>();
                        seedRange.add(startingSeedRange);
                        Long endingSeedRange = startingSeedRange + Long.parseLong(element) - 1L;

                        seedRange.add(endingSeedRange);
                        seeds.add(seedRange);

                        startingSeedRangeFound = false;
                    }
                }       
            }

            while(myReader.hasNextLine())
            {
                data = myReader.nextLine();
                generateMapping(myReader, "seed-to-soil map:", data, seedToSoil);
                generateMapping(myReader, "soil-to-fertilizer map:", data, soilToFertilizer);
                generateMapping(myReader, "fertilizer-to-water map:", data, fertilizerToWater);
                generateMapping(myReader, "water-to-light map:", data, waterToLight);
                generateMapping(myReader, "light-to-temperature map:", data, lightToTemperature);
                generateMapping(myReader, "temperature-to-humidity map:", data, temperatureToHumidity);
                generateMapping(myReader, "humidity-to-location map:", data, humidityToLocation);
            }

            for (int i = 0; i < seeds.size(); i++)
                seeds = conversionStep(seedToSoil, seeds, i);        

            for (int i = 0; i < seeds.size(); i++)
                seeds = conversionStep(soilToFertilizer, seeds, i);

            for (int i = 0; i < seeds.size(); i++)
                seeds = conversionStep(fertilizerToWater, seeds, i);

            for (int i = 0; i < seeds.size(); i++)
                seeds = conversionStep(waterToLight, seeds, i);

            for (int i = 0; i < seeds.size(); i++)
                seeds = conversionStep(lightToTemperature, seeds, i);

            for (int i = 0; i < seeds.size(); i++)
                seeds = conversionStep(temperatureToHumidity, seeds, i);

            for (int i = 0; i < seeds.size(); i++)
                seeds = conversionStep(humidityToLocation, seeds, i);

            for (var seed : seeds)
            {
                if (seed.get(0) < res)
                    res = seed.get(0);
            }
            
            myReader.close();
            System.out.println("Result: " + res);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Was not able to locate the input file.");
            e.printStackTrace();
        }
    }
}