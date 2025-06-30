#include "libc.h"

void PROGRAM0() {
    
    char* start = "\n------------------------\nPROGRAM 0 STARTING\n------------------------\n";
    write(0, start, strlen(start));

    int fd = creat( "P0-file.txt", O_RDWR );
    
    int aggregate = 0;

    char* write_buffer = "Writing to the file.";
    ssize_t result = write_disk( fd, write_buffer, strlen(write_buffer) );

    if(result == -1) {
        char* error = "\nWrite to disk failed.\n";
        write(0,error,strlen(error));
    }

    aggregate += strlen(write_buffer);

    char* new_text = "\nAdding more info - a second write.";
    write_disk( fd, new_text, strlen(new_text) );

    aggregate += strlen(write_buffer);

    uint8_t read_buffer[ aggregate ];
    memset( read_buffer, 0, aggregate );

    lseek(fd, (aggregate/2) - 1);

    result = read_disk( fd, read_buffer, 8 );

    if(result == -1) {
        char* error = "\nRead of disk failed.\n";
        write(0,error,strlen(error));
    }

    for(uint8_t x = 0; x < 8; x++) {
        write( 0, &read_buffer[x], 1 );
    }

    exit();
    return;
}

void (*entry_0)() = &PROGRAM0;