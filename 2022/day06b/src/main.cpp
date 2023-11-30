#include <iostream>
#include <fstream>
#include <string>

/**
 * @brief checks the string passed as argument contains unique characters
 * @return true if the string contains unique chars
 */
bool areCharDifferent(std::string str)
{
    for(long unsigned int i = 0; i < str.length(); i++)
    {
        for(long unsigned int j = i + 1; j < str.length(); j++)
        {
            if (str[i] == str[j])
                return false;
        }
    }
    return true;
}
/**
    
 * @brief finds the index of the datastream where the first occurrence of the marker ends
 * @param pathToFile 
 * @return std::string returns the index of the last char of the marker.
 *  The index returned counts from the first char of the string as the char having index 1
 */
int markerFinder(std::string pathToFile)
{
    std::ifstream f; 
    std::string buffer = "";
    std::string messageMarker;
    
    try
    {
        f.open(pathToFile);
        f>>buffer;

        for (long unsigned int i = 0; i < buffer.length(); i++)
        {

            if (i >= 13)
            {
                messageMarker = buffer.substr(i-13, 13);
                if (areCharDifferent(messageMarker) == true)
                {
                    return i+1;
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

    return -1;
}

/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"Number of characters processed before the first message marker: "<<markerFinder(argv[1])<<std::endl;
    return 0;
}