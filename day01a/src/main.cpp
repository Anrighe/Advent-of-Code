#include <iostream>
#include <fstream>

int maxCalories(std::string pathToFile)
{
    std::string buffer;
    std::ifstream f; 
    int partialSum = 0;
    int max = 0;

    try
    {
        f.open(pathToFile); 

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
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }

    f.close();
    return max;
}

/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"The elf with the most calories is carrying: "<<maxCalories(argv[1])<<" calories"<<std::endl;
    return 0;
}