#include <iostream>
#include <fstream>
#include <string>

const int STARTING_REG_VALUE = 1;
const int ADDX_COMPLETITION_TIME = 2;
const int NOOP_COMPLETITION_TIME = 1;

/*
    - addx V takes two cycles to complete. 
        After two cycles, the X register is increased by the value V. (V can be negative.)
    
    - noop takes one cycle to complete. 
        It has no other effect.

*/

/**
 * @brief returns the sum of the values of registerX at precise cycles contained in sampleCycle multiplied by the current cycle
 * 
 * @param pathToFile 
 * @return int 
 */
int sumSignalStrength(std::string pathToFile)
{
    std::ifstream f; 
    std::string instruction;

    int registerX = STARTING_REG_VALUE; // The CPU has a single register, X, which starts with the value 1
    int arg1;
    int cycleCounter = 0;
    int sampleCycle[6] = {20, 60, 100, 140, 180, 220};
    int cycleSampleSum;

    try
    {
        f.open(pathToFile);

        while (f.eof() == false)
        {
            f>>instruction;
            if (instruction == "addx")
            {
                f>>instruction;
                arg1 = stoi(instruction);

                for (int i = 0; i < ADDX_COMPLETITION_TIME; ++i)
                {
                    cycleCounter++;

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
                    cycleCounter++;

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

    return cycleSampleSum;
}


/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"The sum of the signals in the 20th, 60th, 100th, 140th, 180th, and 220th cycles is: "<<sumSignalStrength(argv[1])<<std::endl;
    return 0;
}