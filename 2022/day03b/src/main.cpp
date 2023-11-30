#include <iostream>
#include <fstream>
#include <map>
#include <string>


std::map<char, int> generateConversionMap()
{
    std::map<char, int> conversionMap;
    int i = 1;
    for (char charIterator = 'a'; charIterator <= 'z'; charIterator++)
    {
        conversionMap.insert(std::pair<char, int>(charIterator, i));
        i++;
    }
    for (char charIterator = 'A'; charIterator <= 'Z'; charIterator++)
    {
        conversionMap.insert(std::pair<char, int>(charIterator, i));
        i++;
    }
    return conversionMap;
}

char findCommonChar(std::string firstRucksack, std::string secondRucksack, std::string thirdRucksack)
{
    char element;
    for (long unsigned int i = 0; i < firstRucksack.length(); i++)
    {
        element = firstRucksack[i];
        if (secondRucksack.find(element) != std::string::npos && thirdRucksack.find(element) != std::string::npos ) // found
            return element;
    }
    std::cerr<<"Did not find an error in the compartments"<<std::endl;
    exit(-2);
}


int priorityErrorCalculator(std::string pathToFile)
{
    int totalPriority = 0;
    std::ifstream f; 
    std::string firstRucksack;
    std::string secondRucksack;
    std::string thirdRucksack;
    char commonChar;
    std::map<char, int> conversionMap = generateConversionMap();
    try
    {
        f.open(pathToFile);

        while (f.is_open() == true && f.eof() == false)
        {
            if (f.eof() == false)
            {
                f>>firstRucksack;
                f>>secondRucksack;
                f>>thirdRucksack;    
                commonChar = findCommonChar(firstRucksack, secondRucksack, thirdRucksack);
                totalPriority += conversionMap.at(commonChar);
            }
        } 
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    f.close();
    return totalPriority;
}

/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"The sum of the priorities is: "<<priorityErrorCalculator(argv[1])<<std::endl;
    return 0;
}