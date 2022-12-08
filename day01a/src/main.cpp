#include <iostream>
#include <fstream>

int maxCalories(std::string pathToFile)
{
    std::string buffer;
    std::ifstream f; 
    int partialSum = 0;
    int max = 0;

    f.open(pathToFile); 

    if (!f)
    {
        std::cerr<<"Error while opening the file " + pathToFile;
        return -1;
    }    

    while (f.is_open() == true && f.eof() == false)
    {
        std::getline(f, buffer);
        
        if (buffer.compare("") == true) //True if a separator has been found  
        {   
            if (max < partialSum)
            {
                max = partialSum;
                partialSum = 0;
            }
            else
                partialSum = 0;
        }
        else
            partialSum += stoi(buffer);
    }

    f.close();
    return max;
}

/**
 * @param argv The first argument is path of the input file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"The elf with the most calories is carrying: "<<maxCalories(argv[1])<<" calories"<<std::endl;
    return 0;
}