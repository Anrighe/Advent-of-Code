import sys

def removeNewline(newLineStr):
    return newLineStr.replace("\n", "")

def selectToken(str, pos):
    """Selects and returns a token in a string. Uses space as the separator"""
    return removeNewline(str.split(" ")[pos-1])

def onlyContainsTuple(list):
    """Returns true if the list contains only tuple objects"""
    for element in list:
        if type(element) is not tuple:
            return False
    return True

def changeDirectory(line, filesystem, fsCurrentPath):
    """Updates the current directory path after a "cd" command

    Parameters
    ---
    line : str

    filesystem : dict {directoryPath:list of items contained in the directoryPath address}    

    Returns
    -------
    (fsCurrentPath, filesystem) : tuple
        A tuple containing the new current path
    """    
    match selectToken(line, 3):
        case "..": #  cd .. 
            if fsCurrentPath.rfind("/") == 0: #if in a directory adiacent to the root (e.g "/gqcclj")
                fsCurrentPath = fsCurrentPath[1:fsCurrentPath.rfind("/")]
            else: #if in directory not adiacent to the root (e.g. "/lmtpm/clffsvcw") 
                fsCurrentPath = fsCurrentPath[0:fsCurrentPath.rfind("/")]
        case _: #  cd <dirname>
            if fsCurrentPath == "" and selectToken(line, 3) == "/": #if I am moving to the root for the first time (based on the input.txt, it will only come here once )
                fsCurrentPath = selectToken(line, 3) # fsCurrentPath = "/"
                filesystem[fsCurrentPath] = []

            else: # cd <filename>
                if fsCurrentPath == "/": # If I currently am in the root
                    fsCurrentPath += selectToken(line, 3)    
                else:
                    if fsCurrentPath + "/" + selectToken(line, 3) not in filesystem:
                        filesystem[fsCurrentPath] = [] #TODO: IS THIS REALLY NEEDED? I don't even know anymore
                    fsCurrentPath += "/" + selectToken(line, 3)
    return (fsCurrentPath, filesystem)

def filesystemBuilder(pathToFile): 
    """Creates the filesystem based on the input.txt file

    Parameters
    ---
    pathToFile : str
        path to the input.txt file

    Returns
    -------
    list
        The dict containing the filesystem
    """   
    filesystem = {}
    fsCurrentPath = ""
    f = open(pathToFile, "r")

    for line in f:
        match selectToken(line, 1):
            case "$": #  $
                match selectToken(line, 2):
                    case "cd": #  $ cd
                        (fsCurrentPath, filesystem) = changeDirectory(line, filesystem, fsCurrentPath)
                    case "ls": #  $ ls
                        pass # there is actually no need to do anything when an "$ ls" is encountered

            case "dir": # dir <dirname>
                if fsCurrentPath == "/": # If I am in the root
                    if fsCurrentPath + selectToken(line, 2) not in filesystem[fsCurrentPath]: # If the directory isn't listed in the root value: append that dir
                        filesystem[fsCurrentPath].append(fsCurrentPath + selectToken(line, 2))
                        filesystem[fsCurrentPath + selectToken(line, 2)] = []
                else:
                    if fsCurrentPath in filesystem:
                        if fsCurrentPath + "/" + selectToken(line, 2) not in filesystem[fsCurrentPath]:
                            filesystem[fsCurrentPath + "/" + selectToken(line, 2)] = []
                            filesystem[fsCurrentPath].append(fsCurrentPath + "/" + selectToken(line, 2))
                    else:
                        filesystem[fsCurrentPath] = []
                        filesystem[fsCurrentPath].append(fsCurrentPath + "/" + selectToken(line, 2))
                     
            #files are inserted in the filesystem dictionary in a tuple ("filename", size)
            case _: #  <filesize> <filename> 
                if (selectToken(line, 2) , selectToken(line, 1)) not in filesystem[fsCurrentPath]: #if the file (token 2) isn't in the fs
                    filesystem[fsCurrentPath].append((selectToken(line, 2) , selectToken(line, 1)))    
    return filesystem


def calculateSizeTuple(list):
    totalSize = 0
    for (t1,t2) in list:
        totalSize += int(t2)
    return totalSize


def filesystemSizeCalculator(filesystem):
    filesystemSize = {}

    for key,value in filesystem.items(): # setting all directory size to -1 to indicate their size is yet to be calculated
        filesystemSize[key] = -1

    #Calculates the size of the directories that only contain files and not other directory
    for key, value in filesystem.items():
        if onlyContainsTuple(value) == True:
            filesystemSize[key] = calculateSizeTuple(value)

    uncalculated = False
    totalSize = 0

    while filesystemSize["/"] == -1:
        for key in filesystem:
            if filesystemSize[key] == -1: #if the size of the directory has yet not been calculated
                for value in filesystem[key]: #value is the list of directories and (filename, filesize)
                    if isinstance(value, tuple):
                        totalSize += int(value[1])
                    elif isinstance(value, str): # if a directory has been found
                        if filesystemSize[value] != -1: # if the directory size found inside the [key] addr has already been calculated
                            totalSize += filesystemSize[value]
                        else: # if the directory size found inside the [key] addr has NOT been calculated
                            uncalculated = True

                if uncalculated == True:
                    uncalculated = False
                    totalSize = 0
                else:
                    filesystemSize[key] = totalSize
                    totalSize = 0
    return filesystemSize


def cleanableSpace(filesystemSize, threshold):
    """Calculates the cleanable space in the filesystem by adding all directories <= to the threshold

    Parameters
    ---
    filesystemSize : dict{directoryPath:directorySize}
        path to the input.txt file

    threshold : int

    Returns
    -------
    cleanableSpaceSize : int
        The amount of space that can be cleaned
    """   
    cleanableSpaceSize = 0
    for key,value in filesystemSize.items():
        if value <= threshold:
            cleanableSpaceSize += value

    return cleanableSpaceSize

def currentUnusedSpace(filesystemSize, totalSpace):
    return totalSpace - filesystemSize["/"]


def freeSpaceforUpdate(unusedSpace, updateSpaceNeeded, filesystemSize):
    """Returns the smallest directory size that can be deleted to install the update"""
    needToFree = updateSpaceNeeded - unusedSpace
    minFound = 0
    minFoundAssigned = False

    for key, value in filesystemSize.items():
        print("CHECKING IF", value)
        if value >= needToFree:
            if minFoundAssigned == False:
                minFound = value
                minFoundAssigned = True
            elif value < minFound:
                minFound = value
            print(minFound)
    return minFound


if __name__ == "__main__":
    threshold = 100000
    totalSpace = 70000000
    updateSpaceNeeded = 30000000
    filesystem = filesystemBuilder(sys.argv[1])
    filesystemSize = filesystemSizeCalculator(filesystem)
    unusedSpace = currentUnusedSpace(filesystemSize, totalSpace)
    smallestDirDelUpdate = freeSpaceforUpdate(unusedSpace, updateSpaceNeeded, filesystemSize)
    print("Total unused space:", unusedSpace)
    print("The amount of cleanable space for threshold =", threshold, "is: ", cleanableSpace(filesystemSize, threshold))
    print("Smallest size of the directory that can be deleted to install the update: ", smallestDirDelUpdate)

    for key, value in filesystem.items():
        print(key, value)

    print("------------------------")    

    for key, value in filesystemSize.items():
        print(key, value)
        

