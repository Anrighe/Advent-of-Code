import sys

def removeNewline(newLineStr):
    return newLineStr.replace("\n", "")

def selectToken(str, pos):
    return removeNewline(str.split(" ")[pos-1])

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




def filesystemCleaner():
    pass








if __name__ == "__main__":
    filesystem = filesystemBuilder(sys.argv[1])
    filesystemCleaner()