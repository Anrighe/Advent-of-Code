#include <iostream>
#include <fstream>
#include <string>
#include <cstring>


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
 * @brief Populates the grid with the input on the input.txt file
 * 
 * @param grid 
 * @param gridWidth 
 * @param gridHeight 
 * @param pathToFile 
 * @return int** the updated grid of trees 
 */
int ** gridPopulator(int ** grid, int gridWidth, int gridHeight, std::string pathToFile)
{
    std::ifstream fPopulator; 
    std::string buffer = "";
    std::string currentChar = ""; 
    int currentRow = 0;
    try
    {
        fPopulator.open(pathToFile);
        
        while (fPopulator.is_open() == true && fPopulator.eof() == false)
        {
            if (fPopulator.eof() == false)
            {
                fPopulator>>buffer;
                for (int i = 0; i < gridWidth; i++)
                {
                    currentChar = buffer[i];
                    grid[currentRow][i] = stoi(currentChar);
                }
                currentRow++;
            }
        } 
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    fPopulator.close();

    return grid;
}

/**
 * @brief counts the number of visible trees
 * 
 * @param pathToFile 
 * @return int: the number of tree that are visible
 */
int visibleTreeCalculator(std::string pathToFile)
{
    //std::ifstream f; 
    //std::string buffer = "";
    int visibleTreeCounter = 0;
    int gridWidth = findGridWidth(pathToFile);
    int gridHeight = findGridHeight(pathToFile);
    
    int ** grid = generateGrid(gridWidth, gridHeight);

    //grid[2][1] = 5; //DEBUG IT SEEMS TO WORK

    grid = gridPopulator(grid, gridWidth, gridHeight, pathToFile);
    printGrid(grid, gridWidth, gridHeight);



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