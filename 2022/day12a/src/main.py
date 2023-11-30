from dijkstar import Graph, find_path
from functools import partial

def ifUpperBorder(row, col, rc, cc):
    if row == 0:
        return True
    else:
        return False

def ifLowerBorder(row, col, rc, cc):
    if row == rc-1:
        return True
    else:
        return False

def ifLeftBorder(row, col, rc, cc):
    if col == 0:
        return True
    else:
        return False

def ifRightBorder(row, col, rc, cc):
    if col == cc-1:
        return True
    else:
        return False


def printGridNum(grid):
    for row in grid:
        for element in row:
            if element > 9:
                print(f'{element}.', end='')
            else:
                print(f'{element}', end='')
        print()


def shortestPath(filename):
    """Populates a graph based on the terrain input and finds the shortest path from S to E"""
    heightGridChar = []
    heightGridNum = []
    rowCount = 0
    colCount = 0
    startingPosition = (0,0)
    finalPosition = (0,0)
    with open(filename, "r") as file:
        lines = file.readlines()

        colCount = len(lines[0].replace('\n',''))
        
        for line in lines:
            rowCount += 1
            heightGridChar.append([*line.replace('\n','')])    

        for i in range(rowCount):
            heightGridNum.append([])

        ifUpperBorderP = partial(ifUpperBorder, rc = rowCount, cc = colCount)
        ifLowerBorderP = partial(ifLowerBorder, rc = rowCount, cc = colCount)
        ifLeftBorderP = partial(ifLeftBorder, rc = rowCount, cc = colCount)
        ifRightBorderP = partial(ifRightBorder, rc = rowCount, cc = colCount)

        i = 0
        j = 0
        for line in lines:
            for element in line:
                if element == 'S':
                    startingPosition = (i, j)
                    heightGridNum[i].append(ord('a') - ord('a'))
                    j += 1
                elif element == 'E':
                    finalPosition = (i, j)
                    heightGridNum[i].append(ord('z') - ord('a'))
                    j += 1
                elif element == '\n':
                    i += 1
                    j = 0
                else:
                    heightGridNum[i].append(ord(element) - ord('a'))
                    j += 1

        graph = Graph()

        row = 0
        col = 0
        for line in heightGridNum:
            for element in line:
                graph.add_node((row, col))
                col += 1
            row +=1
            col = 0

        # Not the greatest way to do this but it works
        row = 0
        col = 0
        for line in heightGridNum:
            for element in line:
                if ifUpperBorderP(row, col) != True and heightGridNum[row-1][col] <= heightGridNum[row][col]+1:
                    graph.add_edge((row, col), (row-1, col), 1)
                if ifLowerBorderP(row, col) != True and heightGridNum[row+1][col] <= heightGridNum[row][col]+1:
                    graph.add_edge((row, col), (row+1, col), 1)
                if ifLeftBorderP(row,col) != True and heightGridNum[row][col-1] <= heightGridNum[row][col]+1:
                    graph.add_edge((row, col), (row, col-1), 1)
                if ifRightBorderP(row,col) != True and heightGridNum[row][col+1] <= heightGridNum[row][col]+1:
                    graph.add_edge((row, col), (row, col+1), 1)
                col += 1 
            row +=1
            col = 0

        shortestPath = find_path(graph, startingPosition, finalPosition)

    return shortestPath[3]


if __name__ == '__main__':
    stepCounter = shortestPath('input.txt')
    print(f'\nThe goal has been reached in: {stepCounter} steps')
