import sys

def removeNewline(newLineStr):
    return newLineStr.replace("\n", "")

def selectToken(str, pos):
    return removeNewline(str.split(" ")[pos-1])

def onlyContainsTuple(list):
    for element in list:
        if type(element) is not tuple:
            return False
    return True

def changeDirectory(line, filesystem, fsCurrentPath):
    #w = open("cd.txt", "a")
    match selectToken(line, 3):
        case "..": #  cd .. 
            if fsCurrentPath.rfind("/") == 0: #if in a directory adiacent to the root (e.g "/gqcclj")
                fsCurrentPath = fsCurrentPath[1:fsCurrentPath.rfind("/")]
                #w.write("cd .. -> " + fsCurrentPath + "\n") #debug
                #print(fsCurrentPath)
            else: #if in directory not adiacent to the root (e.g. "/lmtpm/clffsvcw") 
                fsCurrentPath = fsCurrentPath[0:fsCurrentPath.rfind("/")]
                #w.write("cd ..  -> " + fsCurrentPath + "\n") #debug
                #print(fsCurrentPath)
        case _: #  cd <dirname>
            if fsCurrentPath == "" and selectToken(line, 3) == "/": #if I am moving to the root for the first time (based on the input.txt, it will only come here once )
                fsCurrentPath = selectToken(line, 3) # fsCurrentPath = "/"
                #w.write("cd " + selectToken(line, 3) + "-> " + fsCurrentPath + "\n") #debug
                filesystem[fsCurrentPath] = []
                #print(fsCurrentPath)

            else: # cd <filename>
                if fsCurrentPath == "/": # If I currently am in the root
                    fsCurrentPath += selectToken(line, 3)
                    #w.write("cd " + fsCurrentPath + "\n") #debug       
                else:
                    if fsCurrentPath + "/" + selectToken(line, 3) not in filesystem:
                        #print("CREO KEY FS")
                        filesystem[fsCurrentPath] = [] #TODO: IS THIS REALLY NEEDED? I don't even know anymore
                    fsCurrentPath += "/" + selectToken(line, 3)
                    #w.write("cd " + fsCurrentPath + "\n") #debug
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
    
    #w = open("cd.txt", "w")

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
                        #print("creo la chiave: ", selectToken(line, 2))
                        filesystem[fsCurrentPath + selectToken(line, 2)] = []
                else:
                    #print("checking if ", fsCurrentPath, " already is in the fs")
                    if fsCurrentPath in filesystem:
                        #print("Nel dizionario è già presente la chiave: ", fsCurrentPath)

                        if fsCurrentPath + "/" + selectToken(line, 2) not in filesystem[fsCurrentPath]:
                            filesystem[fsCurrentPath + "/" + selectToken(line, 2)] = []
                            filesystem[fsCurrentPath].append(fsCurrentPath + "/" + selectToken(line, 2))
                        #print("Appending to", fsCurrentPath, fsCurrentPath + "/" + selectToken(line, 2))
                    else:
                        #print("CREO LA KEY:", fsCurrentPath)
                        filesystem[fsCurrentPath] = []
                        filesystem[fsCurrentPath].append(fsCurrentPath + "/" + selectToken(line, 2))
                        #print("Appending to", fsCurrentPath, fsCurrentPath + "/" + selectToken(line, 2))

                    
                
            #for files i was thinking of inserting them in the filesystem dictionary in a tuple ("filename", size)
            case _: #  <filesize> <filename> 
                #print("line: ", line)
                #print("fsCurrentPath:", fsCurrentPath)
                #print("fsCurrentPath: ",fsCurrentPath)
                #if fsCurrentPath not in filesystem: #if
                    #print("aggiungo la chiave", fsCurrentPath)
                    #filesystem[fsCurrentPath] = []
                if (selectToken(line, 2) , selectToken(line, 1)) not in filesystem[fsCurrentPath]: #if the file (token 2) isn't in the fs
                    #print("append a", fsCurrentPath, "di ", (selectToken(line, 2) , selectToken(line, 1)))
                    filesystem[fsCurrentPath].append((selectToken(line, 2) , selectToken(line, 1)))
                    
        #print(fsCurrentPath)

    #for key, value in filesystem.items():
        #print(key ,value )
        
    #print(filesystem["/lmtpm"])
    return filesystem


def calculateSizeTuple(list):
    totalSize = 0
    for (t1,t2) in list:
        totalSize += int(t2)
    return totalSize
        
def calculateSizeGeneric(list, filesystem, filesystemSize):
    pass



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
            #print("KEY:",key)
            if filesystemSize[key] == -1: #if the size of the directory has yet not been calculated
                for value in filesystem[key]: #value is the list of directories and (filename, filesize)
                    #print("VALUE: ", value)
                    #print("VALUE TYPE: ", type(value))
                    #if isinstance(value, str) == True:
                    #    print(value, True)
                    #    print(value, "STR")
                    #if isinstance(value, tuple) == True:
                    #    print(value, True)
                    #    print(value, "TUP")

                    ##NON VA NEGLI IF SOTTOSTANTI
                    if isinstance(value, tuple):
                        #print("TUPLE",value, "FOUND")
                        totalSize += int(value[1])
                    elif isinstance(value, str): # if a directory has been found
                        #print("DIR",value, "FOUND")
                        if filesystemSize[value] != -1: # if the directory size found inside the [key] addr has already been calculated
                            totalSize += filesystemSize[value]
                            print(totalSize)
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


if __name__ == "__main__":
    threshold = 100000
    filesystem = filesystemBuilder(sys.argv[1])
    filesystemSize = filesystemSizeCalculator(filesystem)
    print("The amount of cleanable space for threshold =", threshold, "is: ", cleanableSpace(filesystemSize, threshold))