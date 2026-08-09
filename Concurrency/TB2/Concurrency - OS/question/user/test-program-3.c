#include "libc.h"

void PROGRAM3() {
  
  char* start = "\n------------------------\nPROGRAM 3 STARTING\n------------------------";
  write(0, start, strlen(start));

    pid_t pid = fork();

    if(pid == 0) {
      exec(array_of_programs[ 4 ]);
    }

    while(1) {
      write_Int(3);
    }
    
  exit();
  return;
}

void (*entry_3)() = &PROGRAM3;
