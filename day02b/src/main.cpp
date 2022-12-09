#include <iostream>
#include <fstream>

/*
Shapes:
    opponent:                            A for Rock, B for Paper, and C for Scissors
    old classification for your move:    X for Rock, Y for Paper, and Z for Scissors
    second column:     X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win

Score:
    shape you selected:     (1 for Rock, 2 for Paper, and 3 for Scissors)
    outcome of the round:   (0 if you lost, 3 if the round was a draw, and 6 if you won)
*/

/*I'm fairly sure this day was supposed to make you use enums but whatever. It's not like someone is going to read this*/

char findMove(char opponentMove, char roundOutcome)
{

    if (roundOutcome == 'X') //if I need to lose
    {
        switch (opponentMove)
        {
            case 'A': //if rock
                return 'Z'; //I will play scissors
            case 'B': //if Paper
                return 'X'; //I will play rock
            case 'C': //if Scissors     
                return 'Y'; //I will play Paper 
            default:
                exit(-1);          
        }
    }
    else 
    {
        if (roundOutcome == 'Z') //if I need to win
        {
            switch (opponentMove)
            {
                case 'A': //if rock
                    return 'Y'; //I will play paper
                case 'B': //if Paper
                    return 'Z'; //I will play scissors
                case 'C': //if Scissors     
                    return 'X'; //I will play rock
                default:
                    exit(-2);     
            }
        }
        else //if the round needs to end in draw
        {
            switch (opponentMove)
            {
                case 'A': //if rock
                    return 'X'; //I will play rock
                case 'B': //if Paper
                    return 'Y'; //I will play paper
                case 'C': //if Scissors     
                    return 'Z'; //I will play scissors
                default:
                    exit(-3);                 
            }
        }

    }
}

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
    if (myMove == 'Z')
        return true;
    return false;    
}

bool isRoundDraw(char opponentMove, char myMove)
{
    if (myMove == 'Y')
        return true;
    else
        return false;
}

int scoreCalculator(std::string pathToFile)
{
    char opponentMove;
    char secondColumn;
    char myMove;
    std::ifstream f; 
    int score = 0;
    try
    {
        f.open(pathToFile);

        while (f.is_open() == true && f.eof() == false)
        {
            f>>opponentMove;
            f>>secondColumn;

            if (f.eof() == false)
            {
                if (isRoundWon(opponentMove, secondColumn) == true)
                    score += 6;
                else
                {
                    if(isRoundDraw(opponentMove, secondColumn) == true)
                        score += 3;
                }
                myMove = findMove(opponentMove, secondColumn);
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
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"My total score is: "<<scoreCalculator(argv[1])<<std::endl;
    return 0;
}