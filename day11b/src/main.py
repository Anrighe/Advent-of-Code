from monkey import Monkey
import math

"""
Operations done by a monkey are the following:
    - Monkey inspects each items in its list starting from the first
    - Worry level is changed following the transformation in the "operation" function
    - (the worry level gets divided by 3 and rounded to the nearest integer)
    - Test on the current worry level 
    - Item is thrown to another monkey
"""

ROUND_NUMBER = 10000

def initializator(filename):
    """Initializes the istances of the class Monkeys for each monkey in the 'filename' file"""
    monkeys = []
    with open(filename, 'r') as file:
        lines = file.readlines()

        for line in lines:
            if line.startswith('Monkey'):
                monkeyNumber = int(line.split()[1].replace(':',''))
            elif 'Starting items' in line:
                strStartingItems = line[line.index(':')+2:].replace('\n', '').split(', ')
                startingItems = [eval(i) for i in strStartingItems]
            elif 'Operation' in line:
                operation = line[line.index('=')+2:].replace('old', 'x')
            elif 'Test' in line:
                testNumber = int(line.replace('\n','').split()[-1])
            elif 'If true' in line:
                trueBranch = int(line.replace('\n','').split()[-1])
            elif 'If false' in line:
                falseBranch = int(line.replace('\n','').split()[-1])
                monkeys.append(Monkey(monkeyNumber, startingItems, operation, testNumber, trueBranch, falseBranch))

    return monkeys

def executeRound(monkeys, lcmTest):
    """
    Executes a round consisting of each monkey analyzing each item they're
    carrying and throwing them to other monkeys until they don't carry any item
    """
    for monkey in monkeys:
        for item in monkey.items:
            monkey.inspectCounter += 1
            newItem = monkey.operation(item)

            #After thinking about it for a couple of days I decided to google it 
            # to get some help for this one (sorry)
            monkeys[monkey.test(newItem)].items.append(newItem % lcmTest)
            
        monkey.items.clear()

    return monkeys

def findLcmTest(monkeys):
    lcmList = []
    for monkey in monkeys:
        lcmList.append(monkey.testNumber)
    lcm = math.lcm(*lcmList)
    return lcm

if __name__ == '__main__':
    monkeys = initializator('input.txt')

    lcmTest = findLcmTest(monkeys)

    for round in range(ROUND_NUMBER):
        print(f'Round {round+1}:')
        monkeys = executeRound(monkeys, lcmTest)

    monkeys.sort(key=lambda monkey : monkey.inspectCounter, reverse=True)
    
    print(f'\nThe level of monkey business is: {monkeys[0].inspectCounter} * {monkeys[1].inspectCounter} = {monkeys[0].inspectCounter*monkeys[1].inspectCounter}')
