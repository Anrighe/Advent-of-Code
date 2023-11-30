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

char findCommonChar(std::string firstCompartment, std::string secondCompartment)
{
    char element;
    for (long unsigned int i = 0; i < firstCompartment.length(); i++)
    {
        element = firstCompartment[i];
        if (secondCompartment.find(element) != std::string::npos) // found
        {
            std::cout<<"Found occurrence of "<<element<<std::endl;
            return element;
        }      
    }
    std::cerr<<"Did not find an error in the compartments"<<std::endl;
    exit(-2);
}


int priorityErrorCalculator(std::string pathToFile)
{
    int totalPriority = 0;
    std::ifstream f; 
    std::string line;
    char commonChar;
    std::map<char, int> conversionMap = generateConversionMap();
    try
    {
        f.open(pathToFile);

        while (f.is_open() == true && f.eof() == false)
        {
            if (f.eof() == false)
            {
                f>>line;
                std::string firstCompartment = line.substr(0, line.length()/2);
                std::string secondCompartment = line.substr((line.length()/2), line.length());                
                commonChar = findCommonChar(firstCompartment, secondCompartment);
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