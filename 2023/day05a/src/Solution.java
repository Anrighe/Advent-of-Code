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
     * @param myReader          the scanner used to read the data
     * @param conversionMapName the name of the conversion map
     * @param data              the data containing the mapping information
     * @param mapping           the list of lists to store the generated mapping
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

                for (var seedToSoilEntry : seedToSoil)
                {
                    
                    if (seed.compareTo(BigInteger.valueOf(seedToSoilEntry.get(1))) >= 0 && seed.compareTo(BigInteger.valueOf(seedToSoilEntry.get(1) + seedToSoilEntry.get(2))) <= 0) 
                    {
                        seed = seed.subtract(BigInteger.valueOf(seedToSoilEntry.get(1))).add(BigInteger.valueOf(seedToSoilEntry.get(0)));
                        seeds.set(i, seed);                        
                        break;
                    }   
                }

                for (var soilToFertilizerEntry : soilToFertilizer)
                {
                    if (seed.compareTo(BigInteger.valueOf(soilToFertilizerEntry.get(1))) >= 0 && seed.compareTo(BigInteger.valueOf(soilToFertilizerEntry.get(1) + soilToFertilizerEntry.get(2))) <= 0) 
                    {
                        seed = seed.subtract(BigInteger.valueOf(soilToFertilizerEntry.get(1))).add(BigInteger.valueOf(soilToFertilizerEntry.get(0)));
                        seeds.set(i, seed);
                        break;
                    }
                }

                for (var fertilizerToWaterEntry : fertilizerToWater)
                {
                    if (seed.compareTo(BigInteger.valueOf(fertilizerToWaterEntry.get(1))) >= 0 && seed.compareTo(BigInteger.valueOf(fertilizerToWaterEntry.get(1) + fertilizerToWaterEntry.get(2))) <= 0) 
                    {
                        seed = seed.subtract(BigInteger.valueOf(fertilizerToWaterEntry.get(1))).add(BigInteger.valueOf(fertilizerToWaterEntry.get(0)));
                        seeds.set(i, seed);
                        break;
                    }
                }

                for (var waterToLightEntry : waterToLight)
                {
                    if (seed.compareTo(BigInteger.valueOf(waterToLightEntry.get(1))) >= 0 && seed.compareTo(BigInteger.valueOf(waterToLightEntry.get(1) + waterToLightEntry.get(2))) <= 0) 
                    {
                        seed = seed.subtract(BigInteger.valueOf(waterToLightEntry.get(1))).add(BigInteger.valueOf(waterToLightEntry.get(0)));
                        seeds.set(i, seed);
                        break;
                    }
                }

                for (var lightToTemperatureEntry : lightToTemperature)
                {
                    if (seed.compareTo(BigInteger.valueOf(lightToTemperatureEntry.get(1))) >= 0 && seed.compareTo(BigInteger.valueOf(lightToTemperatureEntry.get(1) + lightToTemperatureEntry.get(2))) <= 0) 
                    {
                        seed = seed.subtract(BigInteger.valueOf(lightToTemperatureEntry.get(1))).add(BigInteger.valueOf(lightToTemperatureEntry.get(0)));
                        seeds.set(i, seed);
                        break;
                    }
                }

                for (var temperatureToHumidityEntry : temperatureToHumidity) 
                {
                    if (seed.compareTo(BigInteger.valueOf(temperatureToHumidityEntry.get(1))) >= 0 && seed.compareTo(BigInteger.valueOf(temperatureToHumidityEntry.get(1) + temperatureToHumidityEntry.get(2))) <= 0) 
                    {
                        seed = seed.subtract(BigInteger.valueOf(temperatureToHumidityEntry.get(1))).add(BigInteger.valueOf(temperatureToHumidityEntry.get(0)));
                        seeds.set(i, seed);
                        break;
                    }
                }

                for (var humidityToLocationEntry : humidityToLocation)
                {
                    if (seed.compareTo(BigInteger.valueOf(humidityToLocationEntry.get(1))) >= 0 && seed.compareTo(BigInteger.valueOf(humidityToLocationEntry.get(1) + humidityToLocationEntry.get(2))) <= 0) 
                    {
                        seed = seed.subtract(BigInteger.valueOf(humidityToLocationEntry.get(1))).add(BigInteger.valueOf(humidityToLocationEntry.get(0)));
                        seeds.set(i, seed);
                        break;
                    }
                }
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