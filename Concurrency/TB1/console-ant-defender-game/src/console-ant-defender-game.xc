/*
 * console-ant-defender-game.xc
 *
 *  Created on: 16 Oct 2015
 *      Author: Oliver
 */
/////////////////////////////////////////////////////////////////////////////////////////
//
// COMS20001
// CODE SKELETON FOR X-CORE200 EXPLORER KIT
// TITLE: "Console Ant Defender Game"
// Rudimentary game combining concurrent programming and I/O.
//
/////////////////////////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <print.h>
#include <xs1.h>
#include <platform.h>

on tile[0] : in port buttons = XS1_PORT_4E; //port to access xCore-200 buttons
on tile[0] : out port leds = XS1_PORT_4F;   //port to access xCore-200 LEDs

/////////////////////////////////////////////////////////////////////////////////////////
//
//  Helper Functions provided for you
//
/////////////////////////////////////////////////////////////////////////////////////////

//DISPLAYS an LED pattern
int showLEDs(out port p, chanend fromVisualiser, chanend shutDownLED) {
  int pattern; //1st bit...separate green LED
               //2nd bit...blue LED
               //3rd bit...green LED
               //4th bit...red LED
  int running = 1;
  while (running) {
      select {
          case fromVisualiser :> pattern:   //receive new pattern from visualiser
//              p <: pattern;                //send pattern to LED port
              break;
          case shutDownLED :> running:
              printf("Sent the shutDownLED back to visualiser.\n");
              running = 0;
              break;
      }
  }
  shutDownLED <: 1;
  printf("Sent the shutDownLED back to visualiser.\n");
  return 0;
}

//READ BUTTONS and send button pattern to userAnt
void buttonListener(in port b, chanend toUserAnt, chanend shutDownToButtons) {
  int r;
  int running = 1;
  while (running) {
    select {
        case b when pinseq(15)  :> r:    // check that no button is pressed
            b when pinsneq(15) :> r;    // check if some buttons are pressed
            if ((r==13) || (r==14))     // if either button is pressed
                toUserAnt <: r;             // send button pattern to userAnt
            break;
        case shutDownToButtons :> running:
            running = 0;
            break;
    }
  }
  shutDownToButtons <: 1;
}

//WAIT function
void waitMoment() {
  timer tmr;
  int waitTime;
  tmr :> waitTime;                       //read current timer value
  waitTime += 40000000;                  //set waitTime to 0.4s after value
  tmr when timerafter(waitTime) :> void; //wait until waitTime is reached
}

//PRINT WORLD TO CONSOLE
void consolePrint(unsigned int userAntToDisplay,
                  unsigned int attackerAntToDisplay) {
  char world[25]; //world of size 23, plus 1 byte for line return
                  //                  plus 1 byte for 0-termination

  //make the current world string
  for(int i=0;i<23;i++) {
    if ((i>7) && (i<15)) world[i]='-';
    else world[i]='.';
    if (userAntToDisplay==i) world[i]='X';
    if (attackerAntToDisplay==i) world[i]='O';
  }
  world[23]='\n';  //add a line break
  world[24]=0;     //add 0-termination
  printstr(world); //send off to console
}

//PROCESS THAT COORDINATES DISPLAY
void visualiser(chanend fromUserAnt, chanend fromAttackerAnt, chanend toLEDs, chanend shutDownToVisualiser, chanend shutDownLED) {
  unsigned int userAntToDisplay = 11;
  unsigned int attackerAntToDisplay = 2;
  int pattern = 0;
  int round = 0;
  int distance = 0;
  int dangerzone = 0;
  int running = 1;
  int tempEnded = 0;
  while (running) {
    if (round==0) printstr("ANT DEFENDER GAME (press button to start)\n");
    round++;
    select {
        case shutDownToVisualiser :> tempEnded:
            printstr("Sending ShutDown TO LED\n");
            shutDownLED <: 1;
            break;
        case shutDownLED :> tempEnded:
//            running = 0;
            printstr("Received the command back from LED \n");
            break;
      case fromUserAnt :> userAntToDisplay:
        consolePrint(userAntToDisplay,attackerAntToDisplay);
        break;
      case fromAttackerAnt :> attackerAntToDisplay:
        consolePrint(userAntToDisplay,attackerAntToDisplay);
        break;
    }
    if(running != 0) {
        distance = userAntToDisplay-attackerAntToDisplay;
        dangerzone = ((attackerAntToDisplay==7) || (attackerAntToDisplay==15));
        pattern = round%2 + 8 * dangerzone + 2 * ((distance==1) || (distance==-1));
        if ((attackerAntToDisplay>7)&&(attackerAntToDisplay<15)) pattern = 15;
        toLEDs <: pattern;
    } else {
        shutDownToVisualiser <: 1;
    }
  }
}

/////////////////////////////////////////////////////////////////////////////////////////
//
//  MOST RELEVANT PART OF CODE TO EXPAND FOR YOU
//
/////////////////////////////////////////////////////////////////////////////////////////

//DEFENDER PROCESS... The defender is controlled by this process userAnt,
//                    which has channels to a buttonListener, visualiser and controller
void userAnt(chanend fromButtons, chanend toVisualiser, chanend toController, chanend shutDown, chanend shutDownToButtons, chanend shutDownToVisualiser, chanend shutDownToAttacker) {
  unsigned int userAntPosition = 11;       //the current defender position
  int buttonInput;                         //the input pattern from the buttonListener
  unsigned int attemptedAntPosition = 0;   //the next attempted defender position after considering button
  int moveForbidden;                       //the verdict of the controller if move is allowed
  int running = 1;

  int visualiserHasShutDown = 0;
  int buttonHasShutDown = 0;
  int attackerHasShutDown = 0;

  toVisualiser <: userAntPosition;         //show initial position
  while (running) {
      attemptedAntPosition = userAntPosition;
      select {
          case fromButtons :> buttonInput:
              fromButtons :> buttonInput; //expect values 13 and 14
              if(buttonInput == 13) { // If button is left in the world.
                  if(attemptedAntPosition != 0) {
                      attemptedAntPosition--;
                  } else {
                      attemptedAntPosition = 22;
                  }
              } else {
                  attemptedAntPosition++; // Create an attempted move position;
              }

              attemptedAntPosition = attemptedAntPosition % 23; // Loops round the world
              toController <: attemptedAntPosition; // Send the attempt to the controller.
              toController :> moveForbidden; // Is the move sent forbidden?
              if(moveForbidden == 0) { // If it is not forbidden:
                  userAntPosition  = attemptedAntPosition; // Update the local copy of the user's ant position.
              }
              toVisualiser <: userAntPosition;
              break;

         case shutDown :> running:
//              printf("Sending ShutDown TO Attacker");
              shutDownToAttacker <: 1;
              break;

         case shutDownToAttacker :> attackerHasShutDown:
                           printf("ATTACK ShutDown\n");
             shutDownToVisualiser <: 1;
             break;

         case shutDownToVisualiser :> visualiserHasShutDown:
              shutDownToButtons <: 1;
              break;

         case shutDownToButtons :> buttonHasShutDown:
              running = 0;
              break;
      }
     }
  shutDown <: 1; // Tell the controller that user has shutdown.
 }

//ATTACKER PROCESS... The attacker is controlled by this process attackerAnt,
//                    which has channels to the visualiser and controller
void attackerAnt(chanend toVisualiser, chanend toController, chanend shutDownToAttacker) {
  int moveCounter = 0;                       //moves of attacker so far
  unsigned int attackerAntPosition = 2;      //the current attacker position
  unsigned int attemptedAntPosition;         //the next attempted  position after considering move direction
  int currentDirection = 1;                  //the current direction the attacker is moving
  int moveForbidden = 0;                     //the verdict of the controller if move is allowed
  int running = 1;                           //indicating the attacker process is alive
  toVisualiser <: attackerAntPosition;       //show initial position

  while (running) {
      attemptedAntPosition = attackerAntPosition;
      if(((moveCounter % 31) == 0) || ((moveCounter % 37) == 0)) {
          if(currentDirection == 0) {
              currentDirection = 1;
          } else {
              currentDirection = 0;
          }
      }

      if(currentDirection == 1) {
          attemptedAntPosition++;
      } else {
          if(attemptedAntPosition != 0) {
              attemptedAntPosition--;
          } else {
              attemptedAntPosition = 22;
          }
      }

      attemptedAntPosition = attemptedAntPosition % 23; // Loops round the world
      toController <: attemptedAntPosition;
      toController :> moveForbidden;

      if(moveForbidden == 0) {
          attackerAntPosition = attemptedAntPosition;
      } else { // Try Again

          if(currentDirection == 0) {
              currentDirection = 1;
              attemptedAntPosition++;
          } else {
              currentDirection = 0;
              if(attemptedAntPosition != 0) {
                  attemptedAntPosition--;
              } else {
                  attemptedAntPosition = 22;
              }
          }

          attemptedAntPosition = attemptedAntPosition % 23; // Loops round the world
          toController <: attemptedAntPosition;
          toController :> moveForbidden; // We must read in this variable but will never be forbidden as the attacker is not trapped.
          attackerAntPosition = attemptedAntPosition;
      }
      printf("%l", attackerAntPosition);
      toVisualiser <: attackerAntPosition;
      moveCounter++;
      waitMoment();

      select {
          case shutDownToAttacker :> running:
              printf("Shutting Down ATtacker!");
              running = 0;
              break;

          default:
          break;
      }

   }

  shutDownToAttacker <: 1;
 }


//COLLISION DETECTOR... the controller process responds to ¿permission-to-move¿ requests
//                      from attackerAnt and userAnt. The process also checks if an attackerAnt
//                      has moved to winning positions.
void controller(chanend fromAttacker, chanend fromUser, chanend shutDownToUser) {
  unsigned int lastReportedUserAntPosition = 11;      //position last reported by userAnt
  unsigned int lastReportedAttackerAntPosition = 2;   //position last reported by attackerAnt
  unsigned int attempt = 0;                           //incoming data from ants
  int gameEnded = 0;                                  //indicates if game is over

  // Order of shutdown:
  // User
  // Attacker
  // Visualiser
  // Button

  int userHasShutDown = 0;


  fromUser :> attempt;                                //start game when user moves
  fromUser <: 1;                                      //forbid first move
  while (!gameEnded) {
    select {
      case fromAttacker :> attempt:

              if(attempt == lastReportedUserAntPosition) {
                  // The User has successfully blocked the move.
                  fromAttacker <: 1; // Forbid the move.
                  printf("Attacker's move blocked by user.\n");
              } else {
                  fromAttacker <: 0; // Allow the move
                  lastReportedAttackerAntPosition = attempt;
                  if(lastReportedAttackerAntPosition >= 8 && lastReportedAttackerAntPosition <= 14) {
                      printf("Attempting to shut down thread.");
                      shutDownToUser <: 1;
                  }
              }

              break;

      case fromUser :> attempt:
              if(attempt == lastReportedAttackerAntPosition) {
                  // The User has successfully blocked the move.
                  fromUser <: 1; // Forbid the move.
                  printf("User's move blocked by Attacker.\n");
              } else {
                  fromUser <: 0;
                  lastReportedUserAntPosition = attempt;
              }

              break;

      case shutDownToUser :> userHasShutDown:
          printf("User has now shutdown. \n\n");
          gameEnded = 1;
          break;

    }

    consolePrint(lastReportedUserAntPosition, lastReportedAttackerAntPosition);

  }
}

//MAIN PROCESS defining channels, orchestrating and starting the processes
int main(void) {
  chan buttonsToUserAnt,         //channel from buttonListener to userAnt
       userAntToVisualiser,      //channel from userAnt to Visualiser
       attackerAntToVisualiser,  //channel from attackerAnt to Visualiser
       visualiserToLEDs,         //channel from Visualiser to showLEDs
       attackerAntToController,  //channel from attackerAnt to Controller
       userAntToController,      //channel from userAnt to Controller
       shutDownToUser,
       shutDownToAttacker,
       shutDownToVisualiser,
       shutDownToButtons,
       shutDownLED;

  par {
    //PROCESSES FOR YOU TO EXPAND
    on tile[1]: userAnt(buttonsToUserAnt,userAntToVisualiser,userAntToController, shutDownToUser, shutDownToButtons, shutDownToVisualiser, shutDownToAttacker);
    on tile[1]: attackerAnt(attackerAntToVisualiser,attackerAntToController, shutDownToAttacker);
    on tile[1]: controller(attackerAntToController, userAntToController, shutDownToUser);

    //HELPER PROCESSES USING BASIC I/O ON X-CORE200 EXPLORER
    on tile[0]: buttonListener(buttons, buttonsToUserAnt, shutDownToButtons);
    on tile[0]: visualiser(userAntToVisualiser,attackerAntToVisualiser,visualiserToLEDs, shutDownToVisualiser, shutDownLED);
    on tile[0]: showLEDs(leds,visualiserToLEDs, shutDownLED);
  }
  return 0;
}
