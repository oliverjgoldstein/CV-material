#include "libc.h"

void PROGRAM8() {
  
  char* start = "\n------------------------\nPROGRAM 8 STARTING\n------------------------";
  write(0, start, strlen(start));

  while(1) {
    write_Int(8);
  }

  exit();
  return;
}

void (*entry_8)() = &PROGRAM8;
