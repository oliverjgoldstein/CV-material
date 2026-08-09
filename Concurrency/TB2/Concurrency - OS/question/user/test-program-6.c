#include "libc.h"

void PROGRAM6() {
  
  char* start = "\n------------------------\nPROGRAM 6 STARTING\n------------------------";
  write(0, start, strlen(start));

        pid_t pid = fork();

    if(pid == 0) {
      exec(array_of_programs[ 7 ]);
    }


  while(1) {
    write_Int(6);
  }

  exit();
  return;
}

void (*entry_6)() = &PROGRAM6;
