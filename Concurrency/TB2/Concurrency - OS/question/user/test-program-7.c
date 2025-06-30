#include "libc.h"

void PROGRAM7() {
  
  char* start = "\n------------------------\nPROGRAM 7 STARTING\n------------------------";
  write(0, start, strlen(start));

        pid_t pid = fork();

    if(pid == 0) {
      exec(array_of_programs[ 8 ]);
    }


  while(1) {
    write_Int(7);
  }

  exit();
  return;
}

void (*entry_7)() = &PROGRAM7;
