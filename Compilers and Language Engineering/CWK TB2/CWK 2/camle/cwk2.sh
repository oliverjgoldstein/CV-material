#!/bin/bash

make
printf "\n\n"
printf "Test 0 produces: \n"
./antlr3 camle -hs test0.w
printf "\n\n"
printf "Test 1 produces: \n"
./antlr3 camle -hs test1.w
printf "\n\n"
printf "Test 2 produces: \n"
./antlr3 camle -hs test2.w
printf "\n\n"
printf "Test 3 produces: \n"
./antlr3 camle -hs test3.w
printf "\n\n" 
printf "Test 4 produces: \n"
./antlr3 camle -hs test4.w
printf "\n\n"
printf "Test 5 produces: \n"
./antlr3 camle -hs test5.w
printf "\n\n"
printf "Test 6 produces: \n"
./antlr3 camle -hs test6.w
printf "\n\n"
printf "Test 7 produces: \n"
./antlr3 camle -hs test7.w
printf "\n\n"
printf "CWK 2 produces: \n"
./antlr3 camle -hs cwk2.w
printf "\n\n"
printf "\n"

ghci ../cwk2.hs