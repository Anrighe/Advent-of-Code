#include <iostream>
#include <fstream>
#include <string>
#include <set>
#include <bits/stdc++.h>


/*
the head (H) and tail (T) must always be touching (diagonally adjacent and even overlapping both count as touching)
....
.TH.
....

....
.H..
..T.
....

...
.H. (H covers T)
...

If the head is ever two steps directly up, down, left, or right from the tail, 
the tail must also move one step in that direction so it remains close enough
.....    .....    .....
.TH.. -> .T.H. -> ..TH.
.....    .....    .....

...    ...    ...
.T.    .T.    ...
.H. -> ... -> .T.
...    .H.    .H.
...    ...    ...

Otherwise, if the head and tail aren't touching and aren't in the same row or column, 
the tail always moves one step diagonally to keep up
.....    .....    .....
.....    ..H..    ..H..
..H.. -> ..... -> ..T..
.T...    .T...    .....
.....    .....    .....

.....    .....    .....
.....    .....    .....
..H.. -> ...H. -> ..TH.
.T...    .T...    .....
.....    .....    .....

Assume the head and the tail both start at the same position
*/

struct Knot
{
    int x = 0;
    int y = 0;
    int xPrev = 0;
    int yPrev = 0;
    bool diag = false;
};

/**
 * @brief moves a knot to another coordinate based on the direction and the distance of the movement
 * 
 * @param knot 
 * @param direction 
 * @param distance 
 */
void move(Knot &knot, char direction, int distance)
{
    knot.xPrev = knot.x;
    knot.yPrev = knot.y;
    
    switch (direction)
    {
        case 'U':
            knot.y += distance;
            break;
        case 'D':
            knot.y -= distance;
            break;
        case 'L':
            knot.x -= distance;
            break;
        case 'R':
            knot.x += distance;
            break;
        default:
            break;
    }
}

/**
 * @brief returns true if both head and tail are in the same position
 * 
 * @param head 
 * @param tail 
 * @return true 
 * @return false 
 */
bool areKnotsOverlapping(Knot head, Knot tail)
{
    if (head.x == tail.x && head.y == tail.y)
        return true;
    else
        return false;
}

/**
 * @brief returns true if both knots are close*
 *  *Being close counts as touching excluding overlapping
 * 
 * @param head 
 * @param tail 
 * @return true 
 * @return false 
 */
bool areKnotsClose(Knot head, Knot tail)
{
    if (tail.x == head.x && (tail.y+1 == head.y || tail.y-1 == head.y))
        return true;
    if (tail.y == head.y && (tail.x+1 == head.x || tail.x-1 == head.x))
        return true;
    if ((tail.x == head.x-1) && (tail.y == head.y-1))
        return true;
    if ((tail.x == head.x+1) && (tail.y == head.y+1))
        return true;
    if ((tail.x == head.x+1) && (tail.y == head.y-1))
        return true;
    if ((tail.x == head.x-1) && (tail.y == head.y+1))
        return true;
  
    return false;
}

/**
 * @brief returns true if the tail's distance from the head is 2 and it is in the same column or row
 * 
 * @param head 
 * @param tail 
 * @return true 
 * @return false 
 */
bool isHeadInTailCrossRange(Knot head, Knot tail)
{
    if (tail.x == head.x && ( tail.y == head.y+2 || tail.y == head.y-2))
        return true;
    if (tail.y == head.y && ( tail.x == head.x+2 || tail.x == head.x-2))
        return true;

    return false;
}

/**
 * @brief returns true if the knots share the same row
 * 
 * @param head 
 * @param tail 
 * @return true 
 * @return false 
 */
bool areKnotsInSameRow(Knot head, Knot tail)
{
    if (head.y == tail.y)
        return true;
    else
        return false;
}

/**
 * @brief returns true if the knots share the same column
 * 
 * @param head 
 * @param tail 
 * @return true 
 * @return false 
 */
bool areKnotsInSameColumn(Knot head, Knot tail)
{
    if (head.x == tail.x)
        return true;
    else
        return false;
}

void printKnotSet(std::set<std::pair<int, int>> visited)
{
    for (std::set<std::pair<int, int>>::iterator it=visited.begin(); it != visited.end(); ++it)     
        std::cout<<it->first<<" "<<it->second<<std::endl;
}

/**
 * @brief moves a knot at a time, considering a couple of knot as the head and the tail,
 *  then returns the number of unique position visited by the tail
 * 
 * @param pathToFile 
 * @return int 
 */
int movement(std::string pathToFile)
{
    std::ifstream f; 
    char direction; 
    int distance = 0;
    Knot head;
    Knot tail;
    Knot * rope = new Knot [10];
    std::set<std::pair<int, int>> visited;
    bool movedDiag = false; //used to indicate if some knot has made a diagonal move
    int diagPos = 0; //used to indicate which knot has made a diagonal move
    try
    {
        f.open(pathToFile);
        
        while (f.is_open() == true && f.eof() == false)
        {
            if (f.eof() == false)
            {
                f>>direction;
                f>>distance;
                
                for(int i = 0; i < distance; i++) // one step at a time
                {
                    move(rope[0], direction, 1);
                    
                    for (int j = 1; j < 10; j++)
                    {
                        if (areKnotsOverlapping(rope[j-1], rope[j]) == false)
                        {
                            if (areKnotsClose(rope[j-1], rope[j]) == false) // if they are NOT touching it needs to move tail
                            {
                                if (isHeadInTailCrossRange(rope[j-1], rope[j]) == true)
                                {
                                    if (rope[j-1].x == rope[j].x && rope[j].y < rope[j-1].y) // need to move tail up
                                    {
                                        move(rope[j], 'U', 1);
                                        movedDiag = false;
                                    }
                                    else
                                    {
                                        if (rope[j-1].x == rope[j].x && rope[j].y > rope[j-1].y)
                                        {
                                            move(rope[j], 'D', 1); 
                                            movedDiag = false;
                                        }
                                        else
                                        {
                                            if (rope[j-1].y == rope[j].y && rope[j].x < rope[j-1].x)
                                            {
                                                move(rope[j], 'R', 1);  
                                                movedDiag = false;        
                                            }
                                            else
                                            {
                                                if (rope[j-1].y == rope[j].y && rope[j].x > rope[j-1].x)
                                                {
                                                    move(rope[j], 'L', 1);
                                                    movedDiag = false;
                                                    rope[j].diag = true;
                                                }
                                            }                            
                                        }
                                    }
                                }
                                else //need to move diagonally: the tail will take the position the head had before its movement
                                {   
                                    if (movedDiag == false)
                                    {
                                        rope[j].xPrev = rope[j].x;
                                        rope[j].yPrev = rope[j].y;

                                        rope[j].x = rope[j-1].xPrev;
                                        rope[j].y = rope[j-1].yPrev;
                                        rope[j].diag = true;
                                        movedDiag = true;
                                        diagPos = j;
                                    }
                                    else
                                    {   
                                        rope[j].xPrev = rope[j].x;        
                                        rope[j].yPrev = rope[j].y;  
                                        rope[j].x = rope[j].x + (rope[diagPos].x - rope[diagPos].xPrev);
                                        rope[j].y = rope[j].y + (rope[diagPos].y - rope[diagPos].yPrev);
                                    }
                                }
                            }
                        }
                    }
                    //resetting movedDiag and diagPos
                    movedDiag = false;
                    diagPos = 0; 

                    std::pair<int, int> tailPos = std::make_pair(rope[9].x, rope[9].y);
                    visited.insert(tailPos);
                    for (int k = 0; k < 10; k++)
                        rope[k].diag = false; //SETTING DIAG TO FALSE FOR ALL KNOTS AT THE END OF EACH UPDATE CYCLE
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

    return visited.size();
}


/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"The number of position visited at least once from the tail of the rope: "<<movement(argv[1])<<std::endl;
    return 0;
}