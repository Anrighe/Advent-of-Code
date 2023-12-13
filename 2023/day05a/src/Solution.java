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
    public static BigInteger conversionStep(List<List<Long>> mapping, BigInteger seed, List<BigInteger> seeds, int seedIndex)
    {
        for (var mappingEntry : mapping)
        {
            if (seed.compareTo(BigInteger.valueOf(mappingEntry.get(1))) >= 0 && seed.compareTo(BigInteger.valueOf(mappingEntry.get(1) + mappingEntry.get(2))) <= 0) 
            {
                seed = seed.subtract(BigInteger.valueOf(mappingEntry.get(1))).add(BigInteger.valueOf(mappingEntry.get(0)));                
                return seed;
            }   
        }

        return seed;
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

        BigInteger res = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));

        List<BigInteger> seeds = new ArrayList<>();

        List<List<Long>> seedToSoil = new ArrayList<List<Long>>();
        List<List<Long>> soilToFertilizer = new ArrayList<List<Long>>();
        List<List<Long>> fertilizerToWater = new ArrayList<List<Long>>();
        List<List<Long>> waterToLight = new ArrayList<List<Long>>();
        List<List<Long>> lightToTemperature = new ArrayList<List<Long>>();
        List<List<Long>> temperatureToHumidity = new ArrayList<List<Long>>();
        List<List<Long>> humidityToLocation = new ArrayList<List<Long>>();

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
                    seeds.add(new BigInteger(element));    
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
            {
                BigInteger seed = seeds.get(i);
                
                seed = conversionStep(seedToSoil, seed, seeds, i);
                seeds.set(i, seed);
                
                seed = conversionStep(soilToFertilizer, seed, seeds, i);
                seeds.set(i, seed);

                seed = conversionStep(fertilizerToWater, seed, seeds, i);
                seeds.set(i, seed);

                seed = conversionStep(waterToLight, seed, seeds, i);
                seeds.set(i, seed);

                seed = conversionStep(lightToTemperature, seed, seeds, i);
                seeds.set(i, seed);

                seed = conversionStep(temperatureToHumidity, seed, seeds, i);
                seeds.set(i, seed);

                seed = conversionStep(humidityToLocation, seed, seeds, i);
                seeds.set(i, seed);
            }

            for (var seed : seeds)
            {
                if (seed.compareTo(res) < 0)
                    res = seed;
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