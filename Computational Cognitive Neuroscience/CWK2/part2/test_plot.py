import matplotlib.pyplot as plt
import numpy as np

# fig = plt.figure()
# ax = fig.add_subplot(111)
x = np.linspace(0, 1, 11)
y = np.linspace(0, 1, 11)
X = np.meshgrid(x, y)
print(X)
a = [[1,2],[5,6]]
# print(Y)
# zs = np.array([fun(x,y) for x,y in zip(np.ravel(X), np.ravel(Y))])
# Z = zs.reshape(X.shape)
plt.imshow(a, cmap='hot', interpolation='nearest')
plt.show()
