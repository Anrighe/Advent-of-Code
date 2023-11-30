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

bool areKnotsOverlapping(Knot head, Knot tail)
{
    if (head.x == tail.x && head.y == tail.y)
        return true;
    else
        return false;
}

/* Being close counts as touching excluding overlapping*/
bool areKnotsClose(Knot head, Knot tail)
{
    if (tail.x == head.x && (tail.y+1 == head.y || tail.y-1 == head.y))
    {
        std::cout<<"IF 1"<<std::endl;
        return true;
    }
    if (tail.y == head.y && (tail.x+1 == head.x || tail.x-1 == head.x))
    {
        std::cout<<"IF 2"<<std::endl;
        return true;
    }
    if ((tail.x == head.x-1) && (tail.y == head.y-1))
    {
        std::cout<<"IF 3"<<std::endl;
        return true;
    }
    if ((tail.x == head.x+1) && (tail.y == head.y+1))
    {
        std::cout<<"IF 4"<<std::endl;
        return true;
    }

    if ((tail.x == head.x+1) && (tail.y == head.y-1))
    {
        std::cout<<"IF 5"<<std::endl;
        return true;
    }
    if ((tail.x == head.x-1) && (tail.y == head.y+1))
    {
        std::cout<<"IF 6"<<std::endl;
        return true;
    }
        
    return false;
}

bool isHeadInTailCrossRange(Knot head, Knot tail) // I SHOULD PROBABLY DELETE THE +1 CHECKS BUT IT WORKS EITHER WAY I THINK
{
    if (tail.x == head.x && (tail.y == head.y+1 || tail.y == head.y+2 || tail.y == head.y-1 || tail.y == head.y-2))
        return true;
    if (tail.y == head.y && (tail.x == head.x+1 || tail.x == head.x+2 || tail.x == head.x-1 || tail.x == head.x-2))
        return true;

    std::cout<<"RETURN FALSE CROSS RANGE"<<std::endl;
    return false;
}

bool areKnotsInSameRow(Knot head, Knot tail)
{
    if (head.y == tail.y)
        return true;
    else
        return false;
}

bool areKnotsInSameColumn(Knot head, Knot tail)
{
    if (head.x == tail.x)
        return true;
    else
        return false;
}

int movement(std::string pathToFile)
{
    std::ifstream f; 
    std::string buffer = "";
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
                
                    std::cout<<"head.x: "<<head.x<<std::endl;
                    std::cout<<"head.y: "<<head.y<<std::endl;
                    std::cout<<"tail.x: "<<tail.x<<std::endl;
                    std::cout<<"tail.y: "<<tail.y<<std::endl;
                    
                    if (areKnotsOverlapping(head, tail) == false)
                    {
                        std::cout<<"NOT OVERLAPPING"<<std::endl;
                        if (areKnotsClose(head, tail) == false) // if they are NOT touching it needs to move tail
                        {
                            std::cout<<"NOT CLOSE"<<std::endl;
                            if (isHeadInTailCrossRange(head, tail) == true)
                            {
                                if (head.x == tail.x && tail.y < head.y) // need to move tail up
                                {
                                    move(tail, 'U', 1);
                                    std::cout<<"moved tail.x to: "<<tail.x<<std::endl;
                                    std::cout<<"moved tail.y to: "<<tail.y<<std::endl;
                                }
                                else
                                {
                                    if (head.x == tail.x && tail.y > head.y)
                                    {
                                        move(tail, 'D', 1);
                                        std::cout<<"moved tail.x to: "<<tail.x<<std::endl;
                                        std::cout<<"moved tail.y to: "<<tail.y<<std::endl;    
                                    }
                                    else
                                    {
                                        if (head.y == tail.y && tail.x < head.x)
                                        {
                                            move(tail, 'R', 1);          
                                            std::cout<<"moved tail.x to: "<<tail.x<<std::endl;
                                            std::cout<<"moved tail.y to: "<<tail.y<<std::endl;
                                        }
                                        else
                                        {
                                            if (head.y == tail.y && tail.x > head.x)
                                            {
                                                move(tail, 'L', 1);
                                                std::cout<<"moved tail.x to: "<<tail.x<<std::endl;
                                                std::cout<<"moved tail.y to: "<<tail.y<<std::endl;
                                            }
                                        }                            
                                    }
                                }

                            }
                            else //need to move diagonally: the tail will take the position the head had before its movement
                            {
                                std::cout<<"moved tail.x to: "<<head.xPrev<<std::endl;
                                std::cout<<"moved tail.y to: "<<head.yPrev<<std::endl;
                                tail.x = head.xPrev;
                                tail.y = head.yPrev;
                            }
                        }
                        else
                            std::cout<<"KNOTS ARE CLOSE"<<std::endl;
                    }
                    else
                        std::cout<<"OVERLAPPING"<<std::endl;


                    std::cout<<"-----------------------"<<std::endl;

                    //SET
                    std::pair<int, int> tailPos = std::make_pair(tail.x, tail.y);
                    visited.insert(tailPos);

                    
                }

                
                //std::cout<<"direction: "<<direction<<std::endl;
                //std::cout<<"distance: "<<distance<<std::endl;

                //std::cout<<"head.x:"<<head.x<<std::endl;
                //std::cout<<"head.y:"<<head.y<<std::endl;

            }
        } 
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    f.close();

    std::cout<<"final position:"<<std::endl;    
    std::cout<<"head.x:"<<head.x<<std::endl;
    std::cout<<"head.y:"<<head.y<<std::endl;
    std::cout<<"tail.x:"<<tail.x<<std::endl;
    std::cout<<"tail.y:"<<tail.y<<std::endl;
    std::cout<<"-----------------------"<<std::endl;

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