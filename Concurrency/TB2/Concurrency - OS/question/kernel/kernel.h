#ifndef __KERNEL_H
#define __KERNEL_H

#include <stddef.h>
#include <stdint.h>

#include   "GIC.h"
#include "PL011.h"
#include "SP804.h"

#include "interrupt.h"

// Include functionality from newlib, the embedded standard C library.

#include <string.h>

// Include ctx definition and the file handling system calls
#include "ctx.h"
#include "file_sys_calls.h"

// Include the shell, the only 'user' program to be installed in the kernel.

#include "../user/program-list.h"
#include "../utilities/shared_resources.h"

/* The kernel source code is made simpler by three type definitions:
 *
 * - a type that captures each component of an execution context (i.e.,
 *   processor state) in a compatible order wrt. the low-level handler
     preservation and restoration prologue and epilogue,
 * - a type that captures a process identifier, which is basically an
 *   integer, and
 * - a type that captures a process PCB.
 */

#define STACK_SIZE          0x1000  // The stack size for each PCB.
#define MAX_PRIORITY        20      // Implemented as uint8 - never descheduled.
#define MAX_CHILD           8       // Implemented as uint8.
#define SHELL_PID           1       // The PID of the shell.
#define PRIORITY            5       // Priority of a process.
#define MAX_NUMBER_SIZE     20      // The size of the largest int.
#define QSIZE               14      // The number of elements in the queue of semaphores.
#define SEMAPHORE_LIMIT     18      // The max number of semaphores.
#define NUMBER_OF_CUSTOMERS 2
#define PRIORITY_DIFFERENCE 5

// soa stands for start of array of...
extern uint32_t soa_stacks;
extern uint32_t soa_pcbs;

typedef int pid_t; 

typedef struct pcb_t {
  pid_t pid;
  ctx_t ctx;
  enum
  {
    process_running,
    process_terminated,
    process_reading,
    process_paused
  } state; 
  int     stack_index;
  int     index_in_PCB;
  int     credits;                        // Nice of process. Decremented and upon zero descheduling. 
  uint8_t priority;                       // Priority from 0 to 20. 20 = unlimited priority. 0 = never scheduled.
  struct  pcb_t* children[ MAX_CHILD ];   // These are subprocesses/parent processes.
  struct  pcb_t* parent;
  char    program_name[ BUFFER_SIZE ];
} pcb_t;

typedef struct semaphore {
  uint8_t id;
  uint8_t is_mutex;       // A mutex is a binary semaphore.
  uint8_t counter;        // Boolean for access.
  pcb_t*  priority_queue[ QSIZE ];  
} semaphore;

void scheduler                     ( ctx_t* ctx         );
void kernel_handler_rst            ( ctx_t* ctx         );
void kernel_handler_irq            ( ctx_t* ctx         );
void recursively_Kill              ( pcb_t* pcb         );
semaphore* return_semaphore_with_id( int id             );
void dequeue                       ( semaphore* S );
void enqueue                       ( semaphore* S,            pcb_t* process );
void set_mapping                   ( int pcb_array_index_arg, char* program_name );
void kernel_handler_svc            ( ctx_t* ctx,              uint32_t id      );
int  return_index_given_name       ( char* program_name );
int  search_for_index              ( int key            );

pcb_t     pcb_array[PCBNUMBER];
pcb_t*    current = NULL;

uint32_t  pid_iterator;          // The index in the pcb_array we are currently executing.
uint32_t  pcb_stack_top;         // The index in our soa_stacks that each process has.
uint32_t  next_scheduled_PCB;
uint32_t  read_index;

uint8_t   bool_PCB_full;
uint8_t   is_shell_reading;      // Has the shell performed a read.
uint8_t   buffer_full;
uint8_t   line_read_in_progress;

char      buffer[ BUFFER_SIZE ];
char*     shell_buffer_location = NULL;
char*     shell_program_name;

semaphore semaphore_array[ SEMAPHORE_LIMIT ];

#endif
