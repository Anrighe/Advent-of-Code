#include <iostream>
#include <fstream>
#include <string>


int findGridHeight(std::string pathToFile)
{
    std::ifstream fHeight;
    std::string buffer = ""; 
    int height = 0;
    try
    {
        fHeight.open(pathToFile);
        while (fHeight.is_open() == true && fHeight.eof() == false)
        {
            if (fHeight.eof() == false)
            {
                fHeight>>buffer;
                height++;
            }
        } 
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }  
    fHeight.close(); 
    return height; 
}
int findGridWidth(std::string pathToFile)
{
    std::ifstream fWidth;
    std::string buffer = ""; 
    try
    {
        fWidth.open(pathToFile);
        if (fWidth.is_open() == true && fWidth.eof() == false)
        {
            fWidth>>buffer;
        } 
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }  
    fWidth.close();
    return buffer.length();
}

int ** generateGrid(int gridWidth, int gridHeight)
{
    int ** grid = new int * [gridHeight];
    for (int i = 0; i < gridHeight; ++i)
        grid[i] = new int[gridWidth];
    for (int i = 0; i < gridHeight; ++i)
    {
        for (int j = 0; j < gridWidth; ++j)
            grid[i][j] = 0;
    }
    return grid;
}

void printGrid(int ** grid, int gridWidth, int gridHeight)
{
    for (int i = 0; i < gridHeight; ++i)
    {
        for (int j = 0; j < gridWidth; ++j)
            std::cout<<grid[i][j];
        std::cout<<std::endl;
    }
}

bool isTreeOnEdge()
{
    return true;
}
bool isTreeVisibleNorth()
{
    return true;
}
bool isTreeVisibleSouth()
{
    return true;
}
bool isTreeVisibleEast()
{
    return true;
}
bool isTreeVisibleWest()
{
    return true;
}




/**
 * @brief check if a tree at a given coordinate is visible
 * 
 * @return true if visible
 * @return false if not visible
 */
bool isTreeVisibile()
{

    return true;
}

/**
 * @brief counts the number of visible trees
 * 
 * @param pathToFile 
 * @return int: the number of tree that are visible
 */
int visibleTreeCalculator(std::string pathToFile)
{
    std::ifstream f; 
    std::string buffer = "";
    int visibleTreeCounter = 0;
    int gridWidth = findGridWidth(pathToFile);
    int gridHeight = findGridHeight(pathToFile);
    
    int ** grid = generateGrid(gridWidth, gridHeight);

    grid[2][1] = 5; //DEBUG IT SEEMS TO WORK
    printGrid(grid, gridWidth, gridHeight);


    /*
    try
    {
        f.open(pathToFile);
        
        while (f.is_open() == true && f.eof() == false)
        {
            if (f.eof() == false)
            {
                f>>buffer;
                std::cout<<buffer<<" "<<buffer.length()<<std::endl;
            }
        } 

    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    f.close();*/

    return visibleTreeCounter;
}

/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"Number of visible trees: "<<visibleTreeCalculator(argv[1])<<std::endl;
    return 0;
}