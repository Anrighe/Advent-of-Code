#include <iostream>
#include <fstream>
#include <string>

/**
 * @brief checks if the arguments are all different
 * @return true if the arguments are all different
 */
bool areCharDifferent(char c1, char c2, char c3, char c4)
{
    if (c1 != c2 && c1 != c3 && c1 != c4 && c2 != c3 && c2 != c4 && c3 != c4) 
        return true;
    else
        return false;
}

/**
 * @brief finds the index of the datastream where the first occurrence of the marker ends
 * @param pathToFile 
 * @return std::string returns the index of the last char of the marker.
 *  The index returned counts the first char of the string as the char of index 1
 */
int markerFinder(std::string pathToFile)
{
    std::ifstream f; 
    std::string buffer = "";
    char c1, c2, c3, c4;
    
    try
    {
        f.open(pathToFile);
        f>>buffer;

        for (long unsigned int i = 0; i < buffer.length(); i++)
        {

            if (i >= 3)
            {
                c1 = buffer[i-3];
                c2 = buffer[i-2];
                c3 = buffer[i-1];
                c4 = buffer[i];
                if (areCharDifferent(c1, c2, c3, c4) == true)
                {
                    std::cout<<"("<<c1<<", "<<c2<<", "<<c3<<", "<<c4<<")"<<std::endl;
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
    std::cout<<"Number of characters processed before the first marker: "<<markerFinder(argv[1])<<std::endl;
    return 0;
}