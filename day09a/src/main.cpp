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

/**
 * @brief moves both head and tail and returns the number of unique position visited by the tail
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
    std::set<std::pair<int, int>> visited;

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
                    move(head, direction, 1);
                    
                    if (areKnotsOverlapping(head, tail) == false)
                    {
                        if (areKnotsClose(head, tail) == false) // if they are NOT touching it needs to move tail
                        {
                            if (isHeadInTailCrossRange(head, tail) == true)
                            {
                                if (head.x == tail.x && tail.y < head.y) // need to move tail up
                                    move(tail, 'U', 1);
                                else
                                {
                                    if (head.x == tail.x && tail.y > head.y)
                                        move(tail, 'D', 1); 
                                    else
                                    {
                                        if (head.y == tail.y && tail.x < head.x)
                                            move(tail, 'R', 1);          
                                        else
                                        {
                                            if (head.y == tail.y && tail.x > head.x)
                                                move(tail, 'L', 1);
                                        }                            
                                    }
                                }

                            }
                            else //need to move diagonally: the tail will take the position the head had before its movement
                            {
                                tail.x = head.xPrev;
                                tail.y = head.yPrev;
                            }
                        }
                    }

                    std::pair<int, int> tailPos = std::make_pair(tail.x, tail.y);
                    visited.insert(tailPos);
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
    std::cout<<"The number of position visited at least once from the tail: "<<movement(argv[1])<<std::endl;
    return 0;
}