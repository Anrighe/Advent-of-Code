import sys

def removeNewline(newLineStr):
    return newLineStr.replace("\n", "")

def selectToken(str, pos):
    return removeNewline(str.split(" ")[pos-1])

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
    
    w = open("cd.txt", "w")

    for line in f:
        match selectToken(line, 1):
            case "$": #  $
                match selectToken(line, 2):
                    case "cd": #  $ cd
                        match selectToken(line, 3):
                            case "..": #  cd .. 
                                if fsCurrentPath.rfind("/") == 0: #if in a directory adiacent to the root (e.g "/gqcclj")
                                    fsCurrentPath = fsCurrentPath.replace(fsCurrentPath[1:], "")
                                    w.write("cd .. -> " + fsCurrentPath + "\n") #debug
                                    print(fsCurrentPath)
                                else: #if in directory not adiacent to the root (e.g. "/lmtpm/clffsvcw") 
                                    fsCurrentPath = fsCurrentPath.replace(fsCurrentPath[fsCurrentPath.rfind("/"):], "")
                                    w.write("cd ..  -> " + fsCurrentPath + "\n") #debug
                                    print(fsCurrentPath)
                            case _: #  cd <dirname>
                                if fsCurrentPath == "" and selectToken(line, 3) == "/": #if I am moving to the root for the first time
                                    fsCurrentPath = selectToken(line, 3) # fsCurrentPath = "/"
                                    w.write("cd " + selectToken(line, 3) + "-> " + fsCurrentPath + "\n") #debug
                                    filesystem[fsCurrentPath] = []
                                    print(fsCurrentPath)

                                else: # cd <filename>
                                    if fsCurrentPath == "/": # If I currently am in the root
                                        fsCurrentPath += selectToken(line, 3)
                                        w.write("cd " + fsCurrentPath + "\n") #debug       
                                    else:
                                        fsCurrentPath += "/" + selectToken(line, 3)
                                        w.write("cd " + fsCurrentPath + "\n") #debug
                                

                    case "ls": #  $ ls
                        pass

            case "dir": # dir
                pass
                """if fsCurrentPath == "/" and fsCurrentPath + selectToken(line, 2) not in filesystem[fsCurrentPath]: # If I currently am in the root and the directory isn't listed in the root value
                    
                    filesystem[fsCurrentPath].append(fsCurrentPath + selectToken(line, 2))
                    #print("Appending to", fsCurrentPath, fsCurrentPath + selectToken(line, 2))
            else:
                if fsCurrentPath + "/" + selectToken(line, 2) in filesystem and fsCurrentPath + "/" + selectToken(line, 2) not in filesystem[fsCurrentPath]:
                    filesystem[fsCurrentPath + "/" + selectToken(line, 2)].append(fsCurrentPath + "/" + selectToken(line, 2))
                    #print("Appending to", fsCurrentPath, fsCurrentPath + "/" + selectToken(line, 2))
                else:
                    filesystem[fsCurrentPath + "/" + selectToken(line, 2)] = []
                    filesystem[fsCurrentPath + "/" + selectToken(line, 2)].append(fsCurrentPath + "/" + selectToken(line, 2))
                    #print("Appending to", fsCurrentPath, fsCurrentPath + "/" + selectToken(line, 2))"""

                    
                

            case _: #  <filesize> <filename> 
                pass
        #print(fsCurrentPath)

        #for key,value in filesystem.items():
            #print(key)
        
        #print(filesystem["/gqcclj"])





    return filesystem




def filesystemCleaner():
    pass








if __name__ == "__main__":
    filesystem = filesystemBuilder(sys.argv[1])
    filesystemCleaner()