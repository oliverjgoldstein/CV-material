#include "libc.h"

void yield() {
  asm volatile( "svc #0 \n"  );
}

int write( int fd, void* x, size_t n ) {
  int r;

  asm volatile( "mov r0, %1 \n"
                "mov r1, %2 \n"
                "mov r2, %3 \n"
                "svc #1     \n"
                "mov %0, r0 \n" 
              : "=r" (r) 
              : "r" (fd), "r" (x), "r" (n) 
              : "r0", "r1", "r2" );

  return r;
}

int write_Int( int number ) {
  int r = 0;
  asm volatile( "mov r0, %1 \n"
                "svc #5     \n"
                "mov %0, r0 \n" 
              : "=r" (r) 
              : "r"  (number)
              : "r0" ); 

  return r;
}

// Pass the address of the char array to the kernel and raise an svc interrupt. 
// We first put the address in registers and then call an svc in order to keep the address in GPR in memory.

uint8_t read( char* buffer ) {
  // This can be used as a return value.
  uint8_t r = 0;

	asm volatile(  "mov r0, %1 \n"
                 "svc #2     \n"
                 "mov %0, r0 \n" 
               : "=r" (r) 
               : "r"  (buffer)
               : "r0" );

  return r;
}

// Returns pid of 
pid_t fork() {
  pid_t pid = -1; 
  asm volatile(  "svc #3     \n"
                 "mov %0, r0 \n" 
               : "=r" (pid) );

  return pid;
}

// Returns whether the process was successful.
int exec( char* program_name ) {
  int success = -1;

  asm volatile(  "mov r0, %1 \n"
                 "svc #4     \n"
                 "mov %0, r0 \n" 
               : "=r" (success) 
               : "r"  (program_name)
               : "r0" );

  return success;
}

// Returns whether the process was successful.
int kill( char* program_name ) {
  int success = -1;

  asm volatile(  "mov r0, %1 \n"
                 "svc #6     \n"
                 "mov %0, r0 \n" 
               : "=r" (success) 
               : "r"  (program_name)
               : "r0" );

  return success;
}

void exit() {
  asm volatile( "svc #7 \n"  );
}

// Returns whether the process was successful.
int wait( int id ) {
  int success = -1;

  asm volatile(  "mov r0, %1 \n"
                 "svc #8     \n"
                 "mov %0, r0 \n" 
               : "=r" (success) 
               : "r"  (id)
               : "r0" );

  return success;
}

// Returns whether the process was successful.
int signal( int id ) {
  int success = -1;

  asm volatile(  "mov r0, %1 \n"
                 "svc #9     \n"
                 "mov %0, r0 \n" 
               : "=r" (success) 
               : "r"  (id)
               : "r0" );

  return success;
}

// Returns the file descriptor
int creat( char* name, int flag ) {
  int fd = -1;

  asm volatile( "mov r0, %1 \n"
                "mov r1, %2 \n"
                "svc #10    \n"
                "mov %0, r0 \n" 
              : "=r" (fd) 
              : "r" (name), "r" (flag)
              : "r0", "r1");

  return fd;
}

ssize_t read_disk(int fd, void *buf, size_t count) {
  ssize_t number_of_bytes_read = -1;

  asm volatile( "mov r0, %1 \n"
                "mov r1, %2 \n"
                "mov r2, %3 \n"
                "svc #11    \n"
                "mov %0, r0 \n" 
              : "=r" (number_of_bytes_read) 
              : "r" (fd), "r" (buf), "r" (count) 
              : "r0", "r1", "r2" );

  return number_of_bytes_read;
}

ssize_t write_disk( int fd, void *buf, size_t count ) {
  ssize_t number_of_bytes_written = -1;

  asm volatile( "mov r0, %1 \n"
                "mov r1, %2 \n"
                "mov r2, %3 \n"
                "svc #12    \n"
                "mov %0, r0 \n" 
              : "=r" (number_of_bytes_written) 
              : "r" (fd), "r" (buf), "r" (count) 
              : "r0", "r1", "r2" );

  return number_of_bytes_written;
}

off_t lseek( int fd , off_t movement ) {
  off_t result = -1;

  asm volatile( "mov r0, %1 \n"
                "mov r1, %2 \n"
                "svc #13    \n"
                "mov %0, r0 \n" 
              : "=r" (result) 
              : "r"  (fd), "r" (movement)
              : "r0" );

  return result;
}

int unlink( int fd ) {
  int success = -1;

  asm volatile(  "mov r0, %1 \n"
                 "svc #14    \n"
                 "mov %0, r0 \n"
               : "=r" (success) 
               : "r"  (fd)
               : "r0" );

  return success;
}

void ls(){
  asm volatile( "svc #15 \n"  );
}



