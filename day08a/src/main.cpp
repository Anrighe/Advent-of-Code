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

bool isTreeOnEdge(int ** grid, int gridWidth, int gridHeight, int currentWidth, int currentHeight)
{
    if ((currentHeight == 0) || (currentHeight == gridHeight-1) || (currentWidth == 0) || (currentWidth == gridWidth-1))
    {
        //std::cout<<"grid["<<currentHeight<<"]["<<currentWidth<<"]: "<<grid[currentHeight][currentWidth]<<" is on the edge"<<std::endl;
        return true;
    }
    else
        return false;
}
bool isTreeVisibleNorth(int ** grid, int gridWidth, int gridHeight,  int currentHeight, int currentWidth)
{
    for (int i = 1; currentHeight-i >= 0; i++)
    {
        if (grid[currentHeight][currentWidth] <= grid[currentHeight-i][currentWidth])
            return false;
    }
    return true;
}
bool isTreeVisibleSouth(int ** grid, int gridWidth, int gridHeight,  int currentHeight, int currentWidth)
{
    for (int i = 1; currentHeight+i < gridHeight; i++)
    {
        if (grid[currentHeight][currentWidth] <= grid[currentHeight+i][currentWidth])
            return false;
    }
    return true;
}
bool isTreeVisibleEast(int ** grid, int gridWidth, int gridHeight,  int currentHeight, int currentWidth)
{
    for (int i = 1; currentWidth+i < gridWidth; i++)
    {
        //std::cout<<"Checking if ""grid["<<i<<"]["<<j<<"]: "<<grid[i][j]<<" is visible"<<std::endl;
        if (grid[currentHeight][currentWidth] <= grid[currentHeight][currentWidth+i])
            return false;
    }
    return true;
}
bool isTreeVisibleWest(int ** grid, int gridWidth, int gridHeight,  int currentHeight, int currentWidth)
{
    for (int i = 1; currentWidth-i >= 0; i++)
    {
        if (grid[currentHeight][currentWidth] <= grid[currentHeight][currentWidth-i])
        {
            std::cout<<"WEST RETURNING FALSE"<<std::endl;
            return false;
        }
    }
    return true;
}




/**
 * @brief check if a tree at a given coordinate is visible
 * 
 * @return true if visible
 * @return false if not visible
 */
bool isTreeVisibile(int ** grid, int gridWidth, int gridHeight, int currentHeight, int currentWidth)
{
    if (isTreeVisibleNorth(grid, gridWidth, gridHeight, currentHeight, currentWidth) == true) 
    {
        std::cout<<"FROM NORTH"<<std::endl;
        return true;
    }
    if (isTreeVisibleSouth(grid, gridWidth, gridHeight, currentHeight, currentWidth) == true)
    {
        std::cout<<"FROM SOUTH"<<std::endl;
        return true;
    }
    if (isTreeVisibleEast(grid, gridWidth, gridHeight, currentHeight, currentWidth) == true)
    {
        std::cout<<"FROM EAST"<<std::endl;
        return true;
    }
    if (isTreeVisibleWest(grid, gridWidth, gridHeight, currentHeight, currentWidth) == true)
    {
        std::cout<<"FROM WEST"<<std::endl;
        return true;
    }

    return false;
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
    //std::cout<<gridWidth<<std::endl;
    //std::cout<<gridHeight<<std::endl;

    int ** grid = generateGrid(gridWidth, gridHeight);
    //grid[RIGA][COLONNA] = 5; //DEBUG IT SEEMS TO WORK

    grid = gridPopulator(grid, gridWidth, gridHeight, pathToFile);
    //printGrid(grid, gridWidth, gridHeight); //debug

    for (int i = 0; i < gridHeight; ++i)
    {
        for (int j = 0; j < gridWidth; ++j)
        {
            
            /*
            if (i == 0 || j == 0 || i == gridHeight-1 || j == gridWidth-1)
            {
                std::cout<<"grid["<<i<<"]["<<j<<"]: "<<grid[i][j]<<" is on the edge"<<std::endl;
                visibleTreeCounter++;
            }*/
            if (isTreeOnEdge(grid, gridWidth, gridHeight, j, i) == true)
            {   
                //std::cout<<"grid["<<j<<"]["<<i<<"]: "<<grid[j][i]<<" is on the edge"<<std::endl;
                visibleTreeCounter++;
                //std::cout<<grid[i][j]<<" is on the edge"<<std::endl;
            }
            else
            {
                if(isTreeVisibile(grid, gridWidth, gridHeight, i, j) == true)
                {
                    std::cout<<"grid["<<i<<"]["<<j<<"]: "<<grid[i][j]<<" is visible"<<std::endl<<std::endl;
                    visibleTreeCounter++;
                    //std::cin>>visibleTreeCounter;
                }
                else
                    std::cout<<"grid["<<i<<"]["<<j<<"]: "<<grid[i][j]<<" isn't visible"<<std::endl<<std::endl;
                /*
                if (grid[i][j] > grid[i-1][j] && grid[i][j] > grid[i+1][j] && grid[i][j] > grid[i][j-1] && grid[i][j] > grid[i][j+1])
                {
                    std::cout<<"grid["<<i<<"]["<<j<<"]: "<<grid[i][j]<<" is visible"<<std::endl;
                    visibleTreeCounter++;
                }*/
            }
        }
    }

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