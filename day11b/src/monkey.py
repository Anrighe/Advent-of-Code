import math
from functools import lru_cache

class Monkey:
    def __init__(self, monkeyNumber, startingItems, operation, testNumber, trueBranch, falseBranch): 
        self.monkeyNumber = monkeyNumber
        self.items = startingItems
        
        # operation calculates the worry level of an item
        # After the operation it ONLY needs to be rounded down to the nearest integer
        exec(f'self.operation = lambda x : math.floor({operation})')
        self.testNumber = testNumber
        self.trueBranch = trueBranch
        self.falseBranch = falseBranch
        self.inspectCounter = 0
    
    def monkeyBehaviour(self):
        print(f'Monkey {self.monkeyNumber}:')
        print(f'Items: {self.items}:')


    def test(self, item):
        if item % self.testNumber == 0:
            return self.trueBranch
        else:
            return self.falseBranch
    


    


