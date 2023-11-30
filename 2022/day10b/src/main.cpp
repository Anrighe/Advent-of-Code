#include <iostream>
#include <fstream>
#include <string>

const int STARTING_REG_VALUE = 1;
const int ADDX_COMPLETITION_TIME = 2;
const int NOOP_COMPLETITION_TIME = 1;
const int DISPLAY_LENGTH = 40;
const int DISPLAY_HEIGHT = 6;

/*
    - addx V takes two cycles to complete. 
        After two cycles, the X register is increased by the value V. (V can be negative.)
    
    - noop takes one cycle to complete. 
        It has no other effect.

*/

/**
 * @brief initializes the display to space characters. 
 *  For debug purpouses
 * 
 * @param unrolledDisplay 
 */
void initializeDisplay (char unrolledDisplay[])
{
    for (int i = 0; i < DISPLAY_LENGTH*DISPLAY_HEIGHT; ++i)
        unrolledDisplay[i] = ' ';
}

/**
 * @brief prints the current status of the display
 * 
 * @param unrolledDisplay 
 */
void visualize(char unrolledDisplay[])
{
    for (int i = 0; i < DISPLAY_LENGTH*DISPLAY_HEIGHT; ++i)
    {
        std::cout<<unrolledDisplay[i];
        if (i == 39 || i == 79 || i == 119 || i == 159 || i == 199 || i == 239)
            std::cout<<std::endl;
    }
}

/**
 * @brief draws the current pixel 
 * 
 * @param cycleCounter 
 * @param registerX 
 * @param unrolledDisplay 
 * @param row since the registerX value never go over 40, it's used to create an offset, 
 * in order that both the cycleCounter and the registerX are referring to the same row of the display 
 */
void cycle(int &cycleCounter, int registerX, char unrolledDisplay[], int row)
{
    if ((registerX == cycleCounter-(row*40))||(registerX-1 == cycleCounter-(row*40))||(registerX+1 == cycleCounter-(row*40)))
        unrolledDisplay[cycleCounter] = '#';
    else
        unrolledDisplay[cycleCounter] = '.';

    cycleCounter++;
}

/**
 * @brief returns the sum of the values of registerX at precise cycles contained in sampleCycle multiplied by the current cycle
 * 
 * @param pathToFile 
 */
void display(std::string pathToFile)
{
    std::ifstream f; 
    std::string instruction;

    //the sprite is "###" and the central # is used to measure its position 
    int registerX = STARTING_REG_VALUE; // The CPU has a single register, X, which starts with the value 1

    int arg1;
    int cycleCounter = 0;
    int sampleCycle[6] = {20, 60, 100, 140, 180, 220};
    int cycleSampleSum;
    char unrolledDisplay[DISPLAY_HEIGHT*DISPLAY_LENGTH];
    int row = 0;

    try
    {
        f.open(pathToFile);

        while (f.eof() == false)
        {
            row = cycleCounter / 40;
            f>>instruction;
            if (instruction == "addx")
            {
                f>>instruction;
                arg1 = stoi(instruction);

                for (int i = 0; i < ADDX_COMPLETITION_TIME; ++i)
                {    
                    cycle(cycleCounter, registerX, unrolledDisplay, row);
                    for (int j = 0; j < 6; ++j)
                    {
                        if(cycleCounter == sampleCycle[j])
                            cycleSampleSum += (sampleCycle[j] * registerX);
                    }
                }
                registerX += arg1;
            }
            else
            {
                for (int i = 0; i < NOOP_COMPLETITION_TIME; ++i)
                {
                    cycle(cycleCounter, registerX, unrolledDisplay, row);
                    for (int j = 0; j < 6; ++j)
                    {
                        if(cycleCounter == sampleCycle[j])
                            cycleSampleSum += (sampleCycle[j] * registerX);
                    }
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

    visualize(unrolledDisplay);
}


/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"Display output:"<<std::endl;
    display(argv[1]);
    return 0;
}