#include "libc.h"

void PROGRAM5() {
  
  char* start = "\n------------------------\nPROGRAM 5 STARTING\n------------------------";
  write(0, start, strlen(start));

      pid_t pid = fork();

    if(pid == 0) {
      exec(array_of_programs[ 6 ]);
    }


  while(1) {
    write_Int(5);
  }

  exit();
  return;
}

void (*entry_5)() = &PROGRAM5;
