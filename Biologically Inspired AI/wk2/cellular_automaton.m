colormap(gray)
pattern=elementaryCellularAutomata(110,200)
pattern( pattern==1 )=255;
image(pattern)