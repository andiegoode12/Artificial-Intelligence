"""
Andie Goode
Knapsack BFS
"""
import math
import itertools
from collections import deque

def BFS(items, weight, values,capacity):
    #starting stack is empty
    stack = deque(['_'])
    visited = deque()
    popped = [0]
    W = 0
    V = 0
    w = 0
    v = 0
    solution = []
    #remove blank as we are starting to take items
    stack.popleft()
    #put items in stack
    for i in range(0, len(items)):
        stack.append([items[i]])
    #while the stack is not empty
    while len(stack) != 0:
        #set popped to the item popped off the stack
        popped[0] = stack.popleft()
        for i in range(0, len(popped[0])):
            w += weight[popped[0][i]-1]
            v += values[popped[0][i]-1]
        if (w <= capacity) and (v > V):
            W = w
            V = v
            solution = popped[0]
        w = 0
        v = 0
        #add item popped off to stack to visited
        visited.append(popped[-1][:])
        #loop through items not yet picked to make combinations
        for i in range(popped[-1][-1], len(items)):
            #add next item to knapsack
            popped[-1].append(items[i])
            stack.append(popped[-1][:])
            #remove last item from knapsack
            popped[-1].pop()
    print 'total weight:',W
    print 'total value:',V
    print 'solution:',solution


def main():
    #Read info from file and extract integers
    f = open("knapsack.txt","r")
    #read first line
    capacity = f.readline()
    capacity = capacity[9:len(capacity)].strip("\n") #slice out leading nonintegers
    capacity = int(capacity) #set capacity to first int
    #read second line
    tmpweight = f.readline()
    tmpweight = tmpweight[8:len(tmpweight)].strip("\n") #slice out leading nonintegers
    weight = tmpweight.split(",") #split at commas
    weight = list(map(int, weight)) #map int weights to list
    #read last line
    tmpvals = f.readline()
    tmpvals = tmpvals[7:len(tmpvals)].strip("\n") #slice out leading nonintegers
    values = tmpvals.split(",") #split at commas
    values = list(map(int, values)) #map int values to list
    #close file
    f.close()
    items = []
    for i in range(1, len(values)+1):
        items.append(i)
    BFS(items, weight, values, capacity)

main()
