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

std::stack<char> * startingStackGenerator(std::string pathToFile)
{   
    int stackCount = 0;
    std::ifstream fStack; 
    std::ifstream fStack2; 
    std::stack<char> * stacks;
    std::stack<char> * stacks2;
    
    char charBuffer[1000];
    std::string buffer = "";
    std::string prev = "";
    int column = 1;
    
    try
    {
        fStack.open(pathToFile);

        while (fStack.is_open() == true && fStack.eof() == false)
        {
            if (fStack.eof() == false)
            {   
                prev = buffer;
                fStack>>buffer;
                //std::cout<<buffer<<std::endl;
                
                if (buffer.compare("move") == 0)                
                {
                    stackCount = stoi(prev);
                    //std::cout<<"stackCount: "<<stackCount<<std::endl;
                    stacks = new std::stack<char>[stackCount];
                    stacks2 = new std::stack<char>[stackCount];
                    fStack.close();
                }    
            }
        }
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
        for (int i = 0; i<stackCount+1; i++)
        {
            while (stacks2[i].empty() == false)
            {   
                //std::cout<<"Inserisco "<<stacks2[i].top()<<" nello stack "<<i+1<<std::endl;
                stacks[i].push(stacks2[i].top());
                stacks2[i].pop();
            }
            
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
    fStack.close();
    fStack2.close();

    return stacks;
}

std::string reorganizer(std::string pathToFile)
{
    std::ifstream f; 
    std::string topCrates = "";
    std::string buffer = "";
    std::string separator = " ";
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



                    
            }
        } 
    }
    catch(const std::exception& e)
    {
        std::cerr<<e.what();
        exit(-1);
    }
    f.close();
    return topCrates;
}

/**
 * @param argv The first argument is the path of the input.txt file 
 */
int main(int argc, char *argv[])
{
    std::cout<<"Crates on top of each stack: "<<reorganizer(argv[1])<<std::endl;
    return 0;
}