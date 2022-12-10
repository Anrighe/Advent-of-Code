#include <iostream>
#include <fstream>
#include <map>
#include <string>


/**
 * @brief returns true if the first interval specified by [firstValueFirstElf, secondValueFirstElf] is contained  
 * fully in the second interval [firstValueSecondElf, secondValueSecondElf]. Endpoints are included in the check
 * 
 * @param firstValueFirstElf 
 * @param secondValueFirstElf 
 * @param firstValueSecondElf 
 * @param secondValueSecondElf 
 * @return true if the first interval is fully contained in the second
 * @return false if the first interval is not fully contained in the second
 */
bool isAssignmentContained(int firstValueFirstElf, int secondValueFirstElf, int firstValueSecondElf, int secondValueSecondElf)
{
    if (firstValueFirstElf <= firstValueSecondElf && secondValueFirstElf >= secondValueSecondElf)
        return true;
    else
        return false;
}


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


int assignmentContainedCalculator(std::string pathToFile)
{
    int containedCount = 0;
    std::ifstream f; 
    char * firstElfChar = new char [100];
    char * secondElfChar = new char [100];
    char separator1 = ',';
    char separator2 = '\n';
    std::string firstElf;
    std::string secondElf;
    int firstValueFirstElf, secondValueFirstElf;
    int firstValueSecondElf, secondValueSecondElf;

    std::map<char, int> conversionMap = generateConversionMap();
    try
    {
        f.open(pathToFile);

        while (f.is_open() == true && f.eof() == false)
        {
            if (f.eof() == false)
            {
                f.getline(firstElfChar, 100, separator1);
                f.getline(secondElfChar, 100, separator2);
                firstElf = firstElfChar;
                secondElf = secondElfChar;

                /*I was too lazy and to write a function for this*/

                firstValueFirstElf = stoi(firstElf.substr(0,firstElf.length()-firstElf.find('-')));
                secondValueFirstElf = stoi(firstElf.substr(firstElf.find('-')+1, firstElf.length()-1));

                firstValueSecondElf = stoi(secondElf.substr(0,secondElf.length()-secondElf.find('-')));
                secondValueSecondElf = stoi(secondElf.substr(secondElf.find('-')+1, secondElf.length()-1));

                if (isAssignmentContained(firstValueFirstElf, secondValueFirstElf, firstValueSecondElf, secondValueSecondElf) == true)
                    containedCount++;
                else
                {
                    if (isAssignmentContained(firstValueSecondElf, secondValueSecondElf, firstValueFirstElf, secondValueFirstElf) == true)
                        containedCount++;
                }
            }
        } 
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    f.close();
    return containedCount;
}

/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"Number of assignments where one fully contains the other: "<<assignmentContainedCalculator(argv[1])<<std::endl;
    return 0;
}