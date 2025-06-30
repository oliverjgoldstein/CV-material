#ifndef __LIBC_H
#define __LIBC_H

#include <stddef.h>
#include <stdint.h>

#include <string.h> /* memset */

#include "program-list.h"
#include "../utilities/flags.h"

// cooperatively yield control of processor, i.e., invoke the scheduler
void yield();

// write n bytes from x to the file descriptor fd
int write( int fd, void* x, size_t n );

// read to the shell.
uint8_t read( char* buffer );

// Write a uint8_t integer.
int write_Int( int number );

// Duplicate program.
pid_t fork();

// Execute another process.
int exec(char* program_name);

// Kill another process.
int kill(char* program_name);

// Exits the current process.
void exit();

// Sleeps if counter is 0 else accesses semaphore.
int wait( int id );

// Increments counter if nothing in queue else wakes up first element in priority queue of semaphore.
int signal( int id );

// Creates a file with a permissions flag and a file name.
int creat( char* name, int flag );

// Read from a file.
ssize_t read_disk( int fd, void *buf, size_t count );

// Write to a file.
ssize_t write_disk( int fd, void *buf, size_t count );

// Offset a certain number of bytes. A positive number moves the pointer left, a negative number moves it right. -1 sets it to zero.
off_t lseek( int fd, off_t movement );

// Lists files in current directory.
void ls();

// Unlinks file.
int unlink( int fd );

#define BUFFER_SIZE 100

#endif

// Does the return; need to happen?
