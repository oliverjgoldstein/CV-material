import os

""" This is to read in files to the dictionaries in the automaton. """

# dir holds current path.
dir         = os.path.dirname(__file__)

def read( file_path ):

    full_path    = os.path.join( dir, file_path )
    f            = open( full_path, "rU" )
    set_of_words = frozenset(f.read().split('\n'))
    f.close()
    return set_of_words

def read_dict( file_path, delimeter ):
    
    full_path   = os.path.join( dir, file_path )
    f           = open( full_path,"rU" )
    dictionary  = dict()

    for line in f.read().replace("*","").split('\n'):
        if line:
            dictionary[str(line.split(delimeter)[0])] = int(line.split(delimeter)[1])
    f.close()

    return dictionary