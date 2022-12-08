#include <iostream>
#include <fstream>

/*
Shapes:
    opponent: A for Rock, B for Paper, and C for Scissors
    response: X for Rock, Y for Paper, and Z for Scissors

Score:
    shape you selected (1 for Rock, 2 for Paper, and 3 for Scissors)
    outcome of the round (0 if you lost, 3 if the round was a draw, and 6 if you won)
*/

int shapePoints(char myMove)
{
    switch (myMove) 
    {
        case 'X':
            return 1;
        case 'Y':
            return 2;
        case 'Z':
            return 3;
        default:
            exit(-2);
    }
}

bool isRoundWon(char opponentMove, char myMove)
{
    if ((myMove == 'X' && opponentMove == 'C') || (myMove == 'Y' && opponentMove == 'A') || (myMove == 'Z' && opponentMove == 'B'))
        return true;
    return false;    
}

bool isRoundDraw(char opponentMove, char myMove)
{
    if ((opponentMove == 'A' && myMove == 'X') || (opponentMove == 'B' && myMove == 'Y') || (opponentMove == 'C' && myMove == 'Z'))
        return true;
    else
        return false;
}

int scoreCalculator(std::string pathToFile)
{
    char opponentMove;
    char myMove;
    std::string buffer;
    std::ifstream f; 
    int score = 0;
    try
    {
        f.open(pathToFile);

        while (f.is_open() == true && f.eof() == false)
        {
            f>>opponentMove;
            f>>myMove;

            if (f.eof() == false)
            {
                if (isRoundWon(opponentMove, myMove) == true)
                    score += 6;
                else
                {
                    if(isRoundDraw(opponentMove, myMove) == true)
                        score += 3;
                }
                score += shapePoints(myMove);
            }
        } 
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    f.close();
    return score;
}

/**
 * @param argv The first argument is path of the input file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"My total score is: "<<scoreCalculator(argv[1])<<std::endl;
    return 0;
}