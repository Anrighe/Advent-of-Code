#include <iostream>
#include <fstream>

/**
 * @brief Swaps two elements of an array of integers
 * @return The new array with swapped elements
 */
int* swap(int array[], int index1, int index2)
{
    int tmp = array[index1];
    array[index1] = array[index2];
    array[index2] = tmp; 
    return array;
}

/**
 * @brief if partialSum is greater than one of the elements of the array it takes its place. This check is done for each elements of the array
 * 
 * @param top Array of 3 integers
 * @return The new array "sorted"
 */
int* caloriesContender(int top[], int partialSum)
{
    if (partialSum > top[2])
    {
        top[2] = partialSum;
        if (top[2] > top[1])
        {
            top = swap(top, 1, 2);
            if (top[1] > top[0])
                top = swap(top, 0, 1);
        }
    }    
    return top;
}

int* maxCalories(std::string pathToFile)
{
    std::string buffer;
    std::ifstream f; 
    int partialSum = 0;
    int* top = new int[3];

    for (int i = 0; i < 3; ++i)
        top[i] = 0;

    try
    {
        f.open(pathToFile);

        while (f.is_open() == true && f.eof() == false)
        {
            std::getline(f, buffer);
            
            if (buffer.compare("") == true) //True if a separator has been found     
            {
                    top = caloriesContender(top, partialSum);
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
    return top;
}

/**
 * @param argv The first argument is path of the input file 
 */
int main(int argc, char *argv[])
{
    int totalCalories = 0;
    int * top3Calories = new int[3];
    top3Calories = maxCalories(argv[1]);
    std::cout<<"The elfs with the most calories are carrying: "<<std::endl;

    for (int i = 0; i<3; ++i)
    {    
        std::cout<<top3Calories[i]<<" calories"<<std::endl;
        totalCalories += top3Calories[i];
    }

    std::cout<<"The top three Elves are carrying: "<<totalCalories<<" calories"<<std::endl;


    return 0;
}