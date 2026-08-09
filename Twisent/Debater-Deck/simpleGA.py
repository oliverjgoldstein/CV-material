#a very simple genetic algorithm demo
#inspired by Richard Dawkins' "The Blind Watchmaker"
#dave cliff, march 2010, for the LSCITS EngD EMM module

import random

popsize = 30

maxgens = 500

target_string = "tis backed like a whale"

genome_len = len(target_string)

pr_xover=1.0/genome_len
pr_mutn=1.0/genome_len


#random character
def randchar():
    return chr(random.randint(ord(' '),ord('z')))


#fitness function
def fitness(genome):
    score=0
    for i in range(genome_len):
        if(genome[i]==target_string[i]):
            score=score+1

    return score


#randomly select a parent
def randparent():
    rv=random.random() # uniform between 0 and 1
    rv=rv*rv*rv*rv*rv # now biased to be nearer zero
    return int(rv*popsize)


#copy with mutation
def mutcopy(c):
    #mutation?
    if(random.random()<pr_mutn):
        c=randchar()
    return c
         

#generate initial random population & fitnesses

popln = []

for i in range(popsize):
    genome = ""
    for g in range(genome_len):
        genome=genome+randchar()
    print genome
    popln.append([fitness(genome),genome])
    

#loop through update iterations until timeout
for g in range(maxgens):
    
    popln.sort() # sorts into ascending order of fitness
    popln.reverse() # now it's in descending order (highest scores first)

    #print the fittest
    print(g,popln[0])

    
    
    newpop=[]
    
    for i in range(popsize/2):

        #pick a mum and a dad

        mum=randparent()
        dad=mum
        while(dad==mum):
            dad=randparent();

        p1=mum
        p2=dad

        kid1=""
        kid2=""

        for i in range(genome_len):

            #copy with mutation
            kid1=kid1+mutcopy(popln[p1][1][i])
            kid2=kid2+mutcopy(popln[p2][1][i])
            
            #crossover?
            if(random.random()<pr_xover):
                ptmp=p1
                p1=p2
                p2=ptmp
            

        newpop.append([fitness(kid1),kid1])
        newpop.append([fitness(kid2),kid2])
            
    popln[:]=newpop[:]
    

