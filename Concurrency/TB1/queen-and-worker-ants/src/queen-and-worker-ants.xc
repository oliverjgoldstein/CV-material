/*
 * queen-and-worker-ants.xc
 *
 *  Created on: 9 Oct 2015
 *      Author: Oliver
 */

#include <platform.h>
#include <stdio.h>

void queen(const unsigned char world[3][4], unsigned int x, unsigned int y, chanend cWorkerA, chanend cWorkerB, chanend cSuperQueen) {
    unsigned int reportA = 0;
    unsigned int reportB = 0;
    unsigned int harvest = 0;
    unsigned int queenCommand;
    printf("Queen is starting.");

    for(int k=0;k<5;k++) {
        printf("Queen reports overall harvest of %d food.\n", harvest);
        cWorkerA :> reportA;
        cWorkerB :> reportB;
        if (reportA > reportB) {
            cSuperQueen <: reportA;
            cSuperQueen :> queenCommand;

            if(queenCommand == 1) {
                cWorkerA <: 0;
                cWorkerB <: 1;
                harvest += reportA;
                printf("Queen orders harvest of %d food by worker A.\n", reportA);
            } else {
                cWorkerA <: 0;
                cWorkerB <: 0;
                printf("Queen Command is 0 i.e. Nothing happens.");
            }

        } else {
            cSuperQueen <: reportB;
            cSuperQueen :> queenCommand;

            if(queenCommand == 1) {
                cWorkerA <: 1; //send command: worker A must move on
                cWorkerB <: 0;
                harvest += reportB; //add harvest of worker B to overall harvest
                printf("Queen orders harvest of %d food by worker B.\n", reportA);
            } else {
                printf("Queen Command is 0 i.e. Nothing happens.");
                cWorkerA <: 0;
                cWorkerB <: 0;
            }
        }
    }
}

void super_queen(chanend cQueenOne, chanend cQueenTwo) {
    unsigned int foodQueenOne;
    unsigned int foodQueenTwo;
    for(int k=0;k<5;k++) {
        cQueenOne :> foodQueenOne;
        cQueenTwo :> foodQueenTwo;
        if(foodQueenOne > foodQueenTwo) {
            cQueenOne <: 1;
            cQueenTwo <: 0;
        } else {
            cQueenOne <: 0;
            cQueenTwo <: 1;
        }
    }
}

void worker(const unsigned char w[3][4], unsigned int x, unsigned int y, chanend cQueen) {
    unsigned int command = 0;
    unsigned int food;
    printf("Worker is starting...\n");
    for(int k=0;k<5;k++) {
        food = w[x][y];
        cQueen <: food;
        cQueen :> command;
        if(command != 0) {
            for(int i = 0;i<2;i++) {
                if ( w[(x+1)%3][y] > w[x][(y+1)%4] ) {
                    x=(x+1)%3;
                } else {
                    y=(y+1)%4;
                }
            }
        }
    }
}

int main(void) {
    const unsigned char world[3][4] = {{10,0,1,7},{2,10,0,3},{6,8,7,6}};
    chan cWorkerAtoQueenOne;
    chan cWorkerBtoQueenOne;
    chan cWorkerAtoQueenTwo;
    chan cWorkerBtoQueenTwo;
    chan cSuperQueenToQueenOne;
    chan cSuperQueenToQueenTwo;
    printf("World starts...\n");
    par {
        worker(world,0,1,cWorkerAtoQueenOne); //start concurrent ant process A
        worker(world,1,0,cWorkerBtoQueenOne); //start concurrent ant process B
        worker(world,0,1,cWorkerAtoQueenTwo);
        worker(world,0,1,cWorkerBtoQueenTwo);

        queen(world,1,1,cWorkerAtoQueenOne,cWorkerBtoQueenOne, cSuperQueenToQueenOne); //start concurrent ant process queen
        queen(world,1,1,cWorkerAtoQueenTwo,cWorkerBtoQueenTwo, cSuperQueenToQueenTwo); //start concurrent ant process queen

        super_queen(cSuperQueenToQueenOne, cSuperQueenToQueenTwo);
    }
    printf("World ends...\n");
    return 0;
}
