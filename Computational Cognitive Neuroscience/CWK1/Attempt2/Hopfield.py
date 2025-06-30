import numpy as np
import matplotlib.pyplot as plt
import matplotlib.lines as mlines
import matplotlib.patches as mpatches

def plotLetter(letter):
    black = '#000000'; white='#FFFFFF'; gray='#AAAAAA'
    squareSide = 0.03

    plt.figure(figsize=(5,5))
    ax = plt.axes([0,0,6.5,6.5])

    # Plotting squares
    for i in xrange(25):
        coords = np.array([i%5*squareSide, (4-i/5)*squareSide])
        color = black if letter[i]< 0 else white
        square = mpatches.Rectangle(coords, squareSide, squareSide,
                                                      color = color, linewidth=0.5)
        ax.add_patch(square)

    # Plotting grid
    for i in xrange(6):
        x = (5-i%5)*squareSide
        s1, s2 = np.array([[x, x], [0, 5*squareSide]])
        ax.add_line(mlines.Line2D(s1, s2, lw=1, color = gray ))
        ax.add_line(mlines.Line2D(s2, s1, lw=1, color = gray ))

patterns = np.array([
   [-1,-1,-1,-1,1,1,-1,1,1,-1,1,-1,1,1,-1,1,-1,1,1,-1,1,-1,-1,-1,1.],   # Letter D
   [-1,-1,-1,-1,-1,1,1,1,-1,1,1,1,1,-1,1,-1,1,1,-1,1,-1,-1,-1,1,1.],    # Letter J
   [1,-1,-1,-1,-1,-1,1,1,1,1,-1,1,1,1,1,-1,1,1,1,1,1,-1,-1,-1,-1.],     # Letter C
   [-1,1,1,1,-1,-1,-1,1,-1,-1,-1,1,-1,1,-1,-1,1,1,1,-1,-1,1,1,1,-1.],], # Letter M
   dtype=np.float)

n = patterns.shape[0]
m = patterns.shape[1]
eta = 1./n

# training
weights = np.zeros((m,m))
for i in xrange(m-1):
    for j in xrange(i+1,m):
        weights[i,j] = eta*np.dot(patterns[:,i], patterns[:,j])
        weights[j,i] = weights[i,j]

activations = np.zeros(m)
states = np.array([1,1,1,1,-1,1,-1,1,1,-1,1,1,-1,1,1,1,1,1,1,-1,-1,1,1,1,1],
                   dtype=np.float)
plotLetter(states)

# recalling
np.random.seed(10)
for itr in range(4):
    for i in np.random.permutation(m): # asynchronous activation
        activations[i] = np.dot(weights[i,:], states)
        states[i]=np.tanh(activations[i])
    plotLetter(states)
plt.show()
