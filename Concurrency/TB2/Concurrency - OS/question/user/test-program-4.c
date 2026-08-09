#include "libc.h"

void PROGRAM4() {
  
  char* start = "\n------------------------\nPROGRAM 4 STARTING\n------------------------";
  write(0, start, strlen(start));

      pid_t pid = fork();

    if(pid == 0) {
      exec(array_of_programs[ 5 ]);
    }


  while(1) {
    write_Int(4);
  }

  exit();
  return;
}

void (*entry_4)() = &PROGRAM4;
