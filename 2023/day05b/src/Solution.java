import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.math.BigInteger;


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
     * Converts the given seed based on the provided mapping.
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
            System.out.println("Mapping entry: " + mappingEntry.get(0) + " " + mappingEntry.get(1) + " " + mappingEntry.get(2));
            startingConversionRange = mappingEntry.get(1);
            endingConversionRange = mappingEntry.get(1) + mappingEntry.get(2) - 1L;
            
            newStartingConversionRange = mappingEntry.get(0);
            newEndingConversionRange = mappingEntry.get(0) + mappingEntry.get(2) - 1L;
            

            
            if (startingSeedRange >= startingConversionRange && endingSeedRange <= endingConversionRange) // Fully contained
            {
                System.out.println("Range (" + startingSeedRange + ", " + endingSeedRange + ") fully contained in the conversion range: (" + startingConversionRange + ", " + endingConversionRange + ")");
                List <Long> newEntry = new ArrayList<>();
                System.out.println("Adding new entry (seedIndex=" + seedIndex + "): " + (startingSeedRange - startingConversionRange + newStartingConversionRange) + " " + (endingSeedRange - startingConversionRange + newStartingConversionRange));
                newEntry.add(startingSeedRange - startingConversionRange + newStartingConversionRange);
                newEntry.add(endingSeedRange - startingConversionRange + newStartingConversionRange);

                seeds.set(seedIndex, newEntry);
            }
            else if (startingSeedRange < startingConversionRange && endingSeedRange >= startingConversionRange && endingSeedRange <= endingConversionRange)
            {
                System.out.println("Partial Left for seed range (" + startingSeedRange + ", " + endingSeedRange + ") and conversion range (" + startingConversionRange + ", " + endingConversionRange + ")");
                List <Long> newEntry = new ArrayList<>();
                newEntry.add(newStartingConversionRange);
                newEntry.add(endingSeedRange - startingConversionRange + newStartingConversionRange);
                System.out.println("Converting seed range (seedIndex=" + seedIndex + "): " + newStartingConversionRange + " " + (endingSeedRange - startingConversionRange + newStartingConversionRange));
                seeds.set(seedIndex, newEntry);

                // need to append the extra to the seeds list
                List <Long> extraEntry = new ArrayList<>();
                extraEntry.add(startingSeedRange);
                extraEntry.add(startingConversionRange - 1L);
                System.out.println("Adding new entry to seeds (seedIndex=" + seedIndex + "): " + startingSeedRange + " " + (startingConversionRange - 1L));
                seeds.add(extraEntry);
            }
            else if (startingSeedRange > startingConversionRange && startingSeedRange <= endingConversionRange && endingSeedRange > endingConversionRange)
            {
                System.out.println("Partial Right for seed range (" + startingSeedRange + ", " + endingSeedRange + ") and conversion range (" + startingConversionRange + ", " + endingConversionRange + ")");
                List <Long> newEntry = new ArrayList<>();
                newEntry.add(startingSeedRange - startingConversionRange + newStartingConversionRange);
                newEntry.add(newEndingConversionRange);
                System.out.println("Converting seed range (seedIndex=" + seedIndex + "): " + (startingSeedRange - startingConversionRange + newStartingConversionRange) + " " + newEndingConversionRange);
                seeds.set(seedIndex, newEntry);

                // need to append the extra to the seeds list
                List <Long> extraEntry = new ArrayList<>();
                extraEntry.add(endingConversionRange + 1L);
                extraEntry.add(endingSeedRange);
                System.out.println("Adding new entry to seeds (seedIndex=" + seedIndex + "): " + (newEndingConversionRange + 1L) + " " + endingSeedRange);
                seeds.add(extraEntry);
            }
            else
            {
                System.out.println("Seed range (" + startingSeedRange + ", " + endingSeedRange + ") not compatible with conversion range (" + startingConversionRange + ", " + endingConversionRange + ")");
            }


    
        }

        return seeds;
    }
    
    /**
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
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        Long res = 0L;

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
        int currentSeed = 1;
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
                    //System.out.println("Current seed: " + currentSeed++);
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

            List<Long> seedRangeEntry;
            
            System.out.println("SEEDS BEFORE CONVERSION:");
            for (var seed : seeds)
                System.out.println("Seed: " + seed.get(0) + " " + seed.get(1));
            System.out.println("-------------------------------------");

            for (int i = 0; i < seeds.size(); i++)
            {
                //System.out.println(seeds.size());
                //seedRangeEntry = seeds.get(i); // ES: 79 92
                
                seeds = conversionStep(seedToSoil, seeds, i);        

                System.out.println("SEEDS AFTER CONVERSION:");
                for (var seed : seeds)
                    System.out.println("Seed: " + seed.get(0) + " " + seed.get(1));
                System.out.println("-------------------------------------");   

                //seeds = conversionStep(soilToFertilizer, seed, seeds, i);

                /* 
                seed = conversionStep(fertilizerToWater, seed, seeds, i);
                seeds.set(i, seed);

                seed = conversionStep(waterToLight, seed, seeds, i);
                seeds.set(i, seed);

                seed = conversionStep(lightToTemperature, seed, seeds, i);
                seeds.set(i, seed);

                seed = conversionStep(temperatureToHumidity, seed, seeds, i);
                seeds.set(i, seed);

                seed = conversionStep(humidityToLocation, seed, seeds, i);
                seeds.set(i, seed); */
            } 

            for (int i = 0; i < seeds.size(); i++)
            {
                seeds = conversionStep(soilToFertilizer, seeds, i);
                System.out.println("STEP 2: SEEDS AFTER CONVERSION:");
                for (var seed : seeds)
                    System.out.println("Seed: " + seed.get(0) + " " + seed.get(1));
                System.out.println("-------------------------------------"); 
            }

            for (int i = 0; i < seeds.size(); i++)
            {
                seeds = conversionStep(fertilizerToWater, seeds, i);
                System.out.println("STEP 3: SEEDS AFTER CONVERSION:");
                for (var seed : seeds)
                    System.out.println("Seed: " + seed.get(0) + " " + seed.get(1));
                System.out.println("-------------------------------------"); 
            }

            for (int i = 0; i < seeds.size(); i++)
            {
                seeds = conversionStep(waterToLight, seeds, i);
                System.out.println("STEP 4: SEEDS AFTER CONVERSION:");
                for (var seed : seeds)
                    System.out.println("Seed: " + seed.get(0) + " " + seed.get(1));
                System.out.println("-------------------------------------"); 
            }

            for (int i = 0; i < seeds.size(); i++)
            {
                seeds = conversionStep(lightToTemperature, seeds, i);
                System.out.println("STEP 5: SEEDS AFTER CONVERSION:");
                for (var seed : seeds)
                    System.out.println("Seed: " + seed.get(0) + " " + seed.get(1));
                System.out.println("-------------------------------------"); 
            }

            for (int i = 0; i < seeds.size(); i++)
            {
                seeds = conversionStep(temperatureToHumidity, seeds, i);
                System.out.println("STEP 6: SEEDS AFTER CONVERSION:");
                for (var seed : seeds)
                    System.out.println("Seed: " + seed.get(0) + " " + seed.get(1));
                System.out.println("-------------------------------------"); 
            }

            for (int i = 0; i < seeds.size(); i++)
            {
                seeds = conversionStep(humidityToLocation, seeds, i);
                System.out.println("STEP 7: SEEDS AFTER CONVERSION:");
                for (var seed : seeds)
                    System.out.println("Seed: " + seed.get(0) + " " + seed.get(1));
                System.out.println("-------------------------------------"); 
            }

            // print seeds after conversion
            System.out.println("SEEDS AFTER CONVERSION:");
            for (var seed : seeds)
                System.out.println("Seed: " + seed.get(0) + " " + seed.get(1));



            /*/
            for (var seed : seeds)
            {
                if (seed.compareTo(res) < 0)
                    res = seed;
            }*/
            
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