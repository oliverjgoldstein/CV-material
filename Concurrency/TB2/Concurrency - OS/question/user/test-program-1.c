#include "libc.h"

int get_int() {
    return 1;
}

void PROGRAM1() {
    
    char* start = "\n------------------------\nPROGRAM 1 STARTING\n------------------------";
    write(0, start, strlen(start));
    
    pid_t pid = fork(); 

    if(pid == 0) {
        exec(array_of_programs[ 3 ]);
    }

    while(1) {
        int x = get_int();
        write_Int(x);
    }
    
    exit();
    return;
}

void (*entry_1)() = &PROGRAM1;