#include "libc.h"


void PROGRAM2() {

  char* start = "\n------------------------\nPROGRAM 2 STARTING\n------------------------";
  write(0, start, strlen(start));

  int fd = creat( "P2-f.txt", O_RDWR );
  
  int aggregate = 0;

  char* write_buffer = "Writing to the file.";
  ssize_t result = write_disk( fd, write_buffer, strlen(write_buffer) );

  if(result == -1) {
      char* error = "\nWrite to disk failed.\n";
      write(0,error,strlen(error));
  }

  aggregate += strlen(write_buffer);

  char* new_text = "\nAdding more info.";
  write_disk( fd, new_text, strlen(new_text) );

  aggregate += strlen(write_buffer);

  uint8_t read_buffer[ aggregate ];
  // This sets the rw pointer to zero.
  lseek(fd, -1);

  result = read_disk( fd, read_buffer, aggregate );

  if(result == -1) {
      char* error = "\nRead of disk failed.\n";
      write(0,error,strlen(error));
  }

  for(uint8_t x = 0; x < aggregate; x++) {
      write( 0, &read_buffer[x], 1 );
  }

  char* deletion_text = "P2 file unlinking in: ";
  for(uint8_t deletion = 5; deletion > 0; deletion--) {
    write(0,deletion_text,strlen(deletion_text));
    write_Int(deletion);
    write(0,"\n",1);
  }

  unlink(fd);

  exit();
  return;
}

void (*entry_2)() = &PROGRAM2;
