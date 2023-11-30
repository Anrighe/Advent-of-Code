#include <iostream>
#include <fstream>
#include <algorithm>
#include <string>
#include <stack>

/* Starting situation:

[T] [V]                     [W]    
[V] [C] [P] [D]             [B]    
[J] [P] [R] [N] [B]         [Z]    
[W] [Q] [D] [M] [T]     [L] [T]    
[N] [J] [H] [B] [P] [T] [P] [L]    
[R] [D] [F] [P] [R] [P] [R] [S] [G]
[M] [W] [J] [R] [V] [B] [J] [C] [S]
[S] [B] [B] [F] [H] [C] [B] [N] [L]
 1   2   3   4   5   6   7   8   9 

*/

/**
 * @brief Prints the stack recursively
 */
void PrintStack(std::stack<char> s)
{
    if (s.empty())
        return;
     
    char x = s.top();
    s.pop();
    PrintStack(s);
    std::cout<<x<<" ";
    s.push(x);
}

bool isDigits(const std::string str)
{
    return str.find_first_not_of("0123456789") == std::string::npos;
}

/**
 * @brief checks if string contains a number (I stole this function)
 * @return true if the string contains a number
 */
bool hasAnyDigits(const std::string& s)
{
    return std::any_of(s.begin(), s.end(), ::isdigit);
}

int findStackCount(std::string pathToFile)
{
    int stackCount = 0;
    std::ifstream fStackCount;
    char charBuffer[1000];
    std::string buffer = "";
    std::string prev = "";
    try
    {
        fStackCount.open(pathToFile);

        while (fStackCount.is_open() == true && fStackCount.eof() == false)
        {
            if (fStackCount.eof() == false)
            {   
                prev = buffer;
                fStackCount>>buffer;
                
                if (buffer.compare("move") == 0)                
                {
                    stackCount = stoi(prev);
                    fStackCount.close();
                }    
            }
        }
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    fStackCount.close();

    return stackCount;
}

std::stack<char> * startingStackGenerator(std::string pathToFile)
{   

    int stackCount = findStackCount(pathToFile); 
    std::ifstream fStack2; 
    std::stack<char> * stacks;
    std::stack<char> * stacks2;
    
    char charBuffer[1000];
    std::string buffer = "";
    std::string prev = "";
    int column = 1;
    
    try
    {
        stacks = new std::stack<char>[stackCount];
        stacks2 = new std::stack<char>[stackCount];
        fStack2.open(pathToFile);
        while (fStack2.is_open() == true && fStack2.eof() == false)
        {
            if (fStack2.eof() == false)
            {   
                fStack2.getline(charBuffer, 1000);
                buffer = charBuffer;
                //std::cout<<buffer<<std::endl;
                
                if (hasAnyDigits(buffer) == true)
                    fStack2.close();
                else
                {   

                    for (int i = 1; i<=(4*(stackCount-1))+1; i=i+4)
                    {
                        //std::cout<<"i vale: "<<i<<std::endl;
                        if (isspace(buffer[i]) == false)
                        {
                            if (column > stackCount)
                                column = 1;

                            //std::cout<<"Col "<<column<<": "<<buffer[i]<<std::endl;

                            stacks2[column-1].push(buffer[i]);
                            //std::cout<<"first Element of the stack: "<<stacks2[column-1].top()<<std::endl;

                            column++;
                        }
                        else
                            column++;
                    }
                }
            }
        }
        
        for (int i = 0; i<stackCount; i++)
        {
            while (stacks2[i].empty() == false)
            {   
                std::cout<<"IN GENERATOR"<<std::endl;
                //std::cout<<"Inserisco "<<stacks2[i].top()<<" nello stack "<<i+1<<std::endl;
                stacks[i].push(stacks2[i].top());
                stacks2[i].pop();
            }
            std::cout<<"POST WHILE"<<std::endl;
        } 

        
        //for (int i = 0; i<stackCount; i++)
        //{
            //std::cout<<"DENTRO FOR STACK"<<std::endl;
            //PrintStack(stacks[i]); 
        //}    
        //std::cout<<"PRIMO ELEMENTO DELLO STACK 9: "<<stacks[9-1].top()<<std::endl;

    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    fStack2.close();
    return stacks;
}

/**
 * @brief moves moveCount stacks from stack1 to stack2
 * 
 * @param stacks 
 * @param moveCount 
 * @param stack1 
 * @param stack2 
 * @return std::stack<char>* returns the new stack
 */
std::stack<char> * mover(std::stack<char> * stacks, int moveCount, int stack1, int stack2)
{
    for (int i = 0; i < moveCount; i++)
    {
        stacks[stack2-1].push(stacks[stack1-1].top());
        stacks[stack1-1].pop();
    }
    return stacks;
}

std::string findTopCrates(std::stack<char> * stacks, std::string pathToFile)
{
    std::string topCrates;
    std::string top;
    int stackCount = findStackCount(pathToFile);
    for(int i = 0; i < stackCount; i++)
    {
        top = stacks[i].top();
        topCrates.append(top);
    }

    return topCrates;
}

/**
 * @brief reorganizes the crates
 * 
 * @param pathToFile 
 * @return std::string returns a string containing the crates on top
 */
std::string reorganizer(std::string pathToFile)
{
    std::ifstream f; 
    std::string buffer = "";
    int moveCount;
    int stack1;
    int stack2;
    bool firstInstruction = true;

    std::stack<char> * stacks = startingStackGenerator(pathToFile);

    try
    {
        f.open(pathToFile);

        //positioning fstream in the correct position
        while(buffer.find("move") == std::string::npos)
            f>>buffer;

        while (f.is_open() == true && f.eof() == false)
        {
            if (f.eof() == false)
            {
                if (firstInstruction == true)
                {
                    f>>moveCount;   //moveCount
                    f>>buffer;      //from
                    f>>stack1;      //stack1
                    f>>buffer;      //to
                    f>>stack2;      //stack2

                    firstInstruction = false;
                }
                else
                {
                    f>>buffer;      //move
                    f>>moveCount;   //moveCount
                    f>>buffer;      //from
                    f>>stack1;      //stack1
                    f>>buffer;      //to
                    f>>stack2;      //stack2
                }
                std::cout<<"moveCount: "<<moveCount<<", stack1: "<<stack1<<", stack2: "<<stack2<<std::endl;

                stacks = mover(stacks, moveCount, stack1, stack2); 
            }
        } 
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    f.close();

    return findTopCrates(stacks, pathToFile);
}

/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"Crates on top of each stack: "<<reorganizer(argv[1])<<std::endl;
    return 0;
}