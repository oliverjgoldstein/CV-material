#include "kernel.h"

// This includes all file system system calls:
#include "file_sys_calls.h"

void kernel_handler_rst( ctx_t* ctx              ) { 

  /* 
    
    Initialise PCBs representing processes stemming from execution of
    the two user programs.  Note in each case that
       
    - the CPSR value of 0x50 means the processor is switched into USR 
      mode, with IRQ interrupts enabled, and
    - the PC and SP values matche the entry point and top of stack. 

    First process must always be the shell process. 

    Once the PCBs are initialised, we (arbitrarily) select one to be
    restored (i.e., executed) when the function then returns.

   */


  char *x = "\nLoading Kernel...\n";
  print_String(x, strlen(x));

  bool_PCB_full         = 0;
  is_shell_reading      = 0;
  read_index            = 0;
  buffer_full           = 0;
  pcb_stack_top         = 1;
  next_scheduled_PCB    = 0;
  line_read_in_progress = 0;
  shell_program_name    = "SHELL";

  initialise_file_system();

  // Initialise all semaphores:

  memset( &semaphore_array, 0, sizeof(semaphore) * SEMAPHORE_LIMIT );

  // The first two here are binary semaphores (mutexes):
  semaphore barber_ready;
  semaphore access_to_seats;
  semaphore customer_ready;

  barber_ready.is_mutex    = 1;
  access_to_seats.is_mutex = 1;
  customer_ready.is_mutex  = 0;

  barber_ready.counter     = 0;
  access_to_seats.counter  = NUMBER_OF_CUSTOMERS;
  customer_ready.counter   = 0;

  barber_ready.id          = id_barber_ready;
  customer_ready.id        = id_customer_ready;
  access_to_seats.id       = id_access_to_seats;

  for( uint8_t queue_looper = 0; queue_looper < QSIZE; queue_looper++ ) {
      barber_ready.priority_queue   [ queue_looper ] = NULL;
      access_to_seats.priority_queue[ queue_looper ] = NULL;
      customer_ready.priority_queue [ queue_looper ] = NULL;
  }

  semaphore_array[ 0 ] = barber_ready;
  semaphore_array[ 1 ] = customer_ready;
  semaphore_array[ 2 ] = access_to_seats;

  // Let's clear the buffer to read into from the shell.
  memset( buffer,                         '\0',    sizeof(char) * BUFFER_SIZE );
  memset( &pcb_array[ next_scheduled_PCB ], 0,     sizeof(pcb_t) );

  memset ( &pcb_array[ next_scheduled_PCB ].program_name, '\0', sizeof(char) * BUFFER_SIZE );
  memcpy ( &pcb_array[ next_scheduled_PCB ].program_name, shell_program_name, strlen(shell_program_name) );

  // I set this to pcb_stack_top as it is a unique id. I re use this when replacing the PCB.

  pcb_array[ next_scheduled_PCB ].pid          = SHELL_PID;
  pcb_array[ next_scheduled_PCB ].ctx.cpsr     = 0x50;
  pcb_array[ next_scheduled_PCB ].ctx.pc       = ( uint32_t )( entry_SHELL );
  pcb_array[ next_scheduled_PCB ].ctx.sp       = ( uint32_t )( &soa_stacks + pcb_stack_top * STACK_SIZE );
  pcb_array[ next_scheduled_PCB ].state        = process_running;
  pcb_array[ next_scheduled_PCB ].priority     = MAX_PRIORITY;           
  pcb_array[ next_scheduled_PCB ].credits      = pcb_array[ next_scheduled_PCB ].priority; 
  pcb_array[ next_scheduled_PCB ].stack_index  = pcb_stack_top;
  pcb_array[ next_scheduled_PCB ].index_in_PCB = 0;
  pcb_array[ next_scheduled_PCB ].parent       = NULL;

  for( uint8_t child_iterator = 0; child_iterator < MAX_CHILD; child_iterator++ ) {
    pcb_array[ next_scheduled_PCB ].children[ child_iterator ] = NULL;  
  }

  current = &pcb_array[ next_scheduled_PCB ];
  memcpy( ctx, &pcb_array[ next_scheduled_PCB ].ctx, sizeof( ctx_t ) );

  // Post pcb updates:
  pcb_stack_top      += 1;

  TIMER0->Timer1Load     = 0x00100000; // select period = 2^20 ticks ~= 1 sec
  TIMER0->Timer1Ctrl     = 0x00000002; // select 32-bit   timer
  TIMER0->Timer1Ctrl    |= 0x00000040; // select periodic timer
  TIMER0->Timer1Ctrl    |= 0x00000020; // enable          timer interrupt
  TIMER0->Timer1Ctrl    |= 0x00000080; // enable          timer

  UART0->IMSC           |= 0x00000010; // enable UART    (Rx) interrupt
  UART0->CR              = 0x00000301; // enable UART (Tx+Rx)

  GICC0->PMR             = 0x000000F0; // unmask all                 interrupts
  GICD0->ISENABLER[ 1 ] |= 0x00001010; // enable timer and UART (Rx) interrupt
  GICC0->CTLR            = 0x00000001; // enable GIC interface
  GICD0->CTLR            = 0x00000001; // enable GIC distributor

  irq_enable();

  x = "\nKernel Loaded\n";
  print_String(x, strlen(x));

  return;
}


















void scheduler( ctx_t* ctx ) {

  /*
  
  1. Check to see if credits are 0 - if so deschedule it.
  2. Start from the index of the current.
  In this slightly inefficient implementation (as no pointer arrays used), we go through all PCB's.

  */ 


  // Move current program to memory.
  memcpy( &current->ctx, ctx, sizeof( ctx_t ) );

  if( line_read_in_progress == 1 ) {
    memcpy( ctx, &pcb_array[ 0 ].ctx, sizeof( ctx_t ) );
    return;
  }

  if( current->priority != MAX_PRIORITY ) {
    // char* h = "Current credits: ";
    // print_String(h,strlen(h));
    // print_Num(current->credits);
    // print_String("\n",1);
    current->credits  -= 1;
    // print_Num(current->credits);
    // print_String("\n",1);
  }

  uint8_t error_flag = 1;
  pcb_t *next        = NULL;

  // Iterate through processes.
  while( error_flag == 1 ) {

    // If error flag is set we skip the process.

    uint32_t number_of_processes = 1;

    if( bool_PCB_full == 1 ) {
      number_of_processes = ( uint32_t )PCBNUMBER;
    } else {
      number_of_processes = ( pcb_stack_top - 1 );
    }

    next_scheduled_PCB += 1;
    next_scheduled_PCB %= number_of_processes;
    error_flag          = 0;
    next                = &pcb_array[ next_scheduled_PCB ];

    // Rescheduled if 0 credits, unless max priority.
    if( next->credits <= 0 && next->priority != MAX_PRIORITY ) {
      next->credits = next->priority;
      error_flag = 1;
    }

    // If reading and buffer not full skip. We only allow shell to read in this OS.
    // If there is only one process, we do not want to get stuck in an infinite while loop.
    if( next->state == process_reading ) {
      if( buffer_full == 1 ) {
        
        // Copy data to shell.

        is_shell_reading = 0;
        buffer_full      = 0;
        memcpy( shell_buffer_location, buffer, BUFFER_SIZE * sizeof( char ) );
        memset( buffer,                '\0',   BUFFER_SIZE * sizeof( char ) );
        next->state      = process_running;

      }
    } else if( next->state == process_terminated ) {
      error_flag = 1;
    } else if( next->state == process_paused ) {
      error_flag = 1;
    }

  } // End of while.

  memcpy( ctx, &next->ctx, sizeof( ctx_t ) );
  current = next;
  return;
}



















void kernel_handler_svc( ctx_t* ctx, uint32_t id ) { 
  /* Based on the identified encoded as an immediate operand in the
   * instruction, 
   *
   * - read  the arguments from preserved usr mode registers,
   * - perform whatever is appropriate for this system call,
   * - write any return value back to preserved usr mode registers.
   */

  switch( id ) {

    // yield()
    case 0x00 : { 
      scheduler( ctx );
      break;
    }




    // write( fd, x, n )
    case 0x01 : {

      // Ignores file descriptor - assumed to be zero - stdout.

      int   fd = ( int   )( ctx->gpr[ 0 ] );  
      char*  x = ( char* )( ctx->gpr[ 1 ] );  
      int    n = ( int   )( ctx->gpr[ 2 ] ); 

      print_String(x, n);
      
      ctx->gpr[ 0 ] = n;
      scheduler(ctx);
      break;
    }





    // A read instruction.
    case 0x02 : {
      // Only shell can read.
      if( current->pid != SHELL_PID ) {
        ctx->gpr[ 0 ] = 0;
        break;
      }
      if( is_shell_reading == 0 ) {

        shell_buffer_location  = ( char* )( ctx->gpr[ 0 ] );
        current->state         = process_reading;
        is_shell_reading       = 1;
        ctx->gpr[ 0 ]          = 1;
        scheduler( ctx );

      }

      break;
    }





    // A fork instruction.
    case 0x03 : {

      // We need to choose the pcb stack index.

      uint32_t pcb_array_index = -1;

      if( ( pcb_stack_top >= PCBNUMBER ) || ( bool_PCB_full == 1 ) ) {

        uint8_t process_found_flag = 0;
        bool_PCB_full = 1;

        // Let's iterate and find an unitialised block.
        for( uint32_t process_looper = 0; process_looper < PCBNUMBER; process_looper++ ) {
          if( pcb_array[ process_looper ].state == process_terminated ) {

            // Let's use the same stack index and pid index.

            pcb_stack_top      = pcb_array[ process_looper ].stack_index;
            pcb_array_index    = process_looper;
            process_found_flag = 1;
            break;
          }
        }

        if( process_found_flag == 0 ) {
          char *x = "\nThere are no available PCB Blocks.\n";
          print_String(x, strlen(x));

          // Tell the shell there has been an error.
          ctx->gpr[ 0 ] = (int)( -1 );
          break;
        }
      }

      else if( ( pcb_stack_top < PCBNUMBER ) && ( bool_PCB_full != 1 ) ) {
        pcb_array_index = pcb_stack_top - 1;
      }

      // Before we initialise the PCB, let's check there is enough space for a child.

      uint8_t child_no_space = 1;

      // Make the forked process a child of the current process.
      for( uint32_t child_iterator = 0; child_iterator < MAX_CHILD; child_iterator++) {

        if( current->children[ child_iterator ] == NULL ) {
          current->children[ child_iterator ] = &pcb_array[ pcb_array_index ];
          child_no_space = 0;
          break;
        }


        // If a process is terminated, all it's children must also be terminated
        else if( current->children[ child_iterator ]->state == process_terminated ) {
          current->children[ child_iterator ] = &pcb_array[ pcb_array_index ];
          child_no_space = 0;
          break;
        }
      }

      if( child_no_space == 1 ) {
        char *error = "\nProcess cannot be forked More than ";
        char *error2 = " times. \n";

        print_String(error, strlen(error));
        print_Num(MAX_CHILD);
        print_String(error2, strlen(error2));

        // Tell the shell there has been an error.
        ctx->gpr[ 0 ] = (int)( -1 );
        break;
      }

      memcpy( &pcb_array[ pcb_array_index ].ctx, ctx, sizeof( ctx_t ) );
      memcpy( &soa_stacks + ( pcb_stack_top - 1 ) * STACK_SIZE, &soa_stacks + ( current->stack_index - 1 ) * STACK_SIZE, STACK_SIZE );
      memset( &pcb_array[ pcb_array_index ].program_name, '\0', sizeof(char) * BUFFER_SIZE );
      memcpy( &pcb_array[ pcb_array_index ].program_name, current->program_name, sizeof(char) * strlen( current->program_name ) );

      pcb_array[ pcb_array_index ].pid          = pcb_stack_top;
      pcb_array[ pcb_array_index ].stack_index  = pcb_stack_top;
      pcb_array[ pcb_array_index ].ctx.sp      += ( pcb_stack_top - current->stack_index ) * STACK_SIZE;
      pcb_array[ pcb_array_index ].state        = process_running;

      // Do not want 0 priority or never scheduled.

      if( current->priority == 1 ) {
        pcb_array[ pcb_array_index ].priority   = 1;
      } else {
        pcb_array[ pcb_array_index ].priority   = ( current->priority ) - PRIORITY_DIFFERENCE;
        if((current->priority - PRIORITY_DIFFERENCE) <= 0) {
          pcb_array[ pcb_array_index ].priority = 1;
        }
      }

      pcb_array[ pcb_array_index ].credits      = pcb_array[ pcb_array_index ].priority; 
      pcb_array[ pcb_array_index ].parent       = current;
      pcb_array[ pcb_array_index ].index_in_PCB = pcb_array_index; 
      pcb_array[ pcb_array_index ].ctx.gpr[ 0 ] = 0; // Tell the child, it is indeed the child.

      for( uint8_t child_iterator = 0; child_iterator < MAX_CHILD; child_iterator++ ) {
        pcb_array[ pcb_array_index ].children[ child_iterator ] = NULL;  
      }

      ctx->gpr[ 0 ] = (int)pcb_stack_top;

      // Both of these have a range from 1 to 16.
      if( pcb_stack_top < (PCBNUMBER) && bool_PCB_full != 1 ) {
        pcb_stack_top += 1;
      }

      scheduler( ctx );
      break;
    }







    // exec system call:
    case 0x04 : {
      
      char* program_name  = ( char* )( ctx->gpr[ 0 ] );


      // Let's find the relevant program and execute it.
      uint8_t found_program = 0;
      for( uint32_t process_looper = 0; process_looper < PCBNUMBER; process_looper++ ) {
        if( strcmp( array_of_programs[ process_looper ], program_name ) == 0 ) {
          memset( current->program_name, '\0', sizeof(char) * BUFFER_SIZE );
          memcpy( current->program_name, array_of_programs[ process_looper ], sizeof(char) * strlen( array_of_programs[ process_looper ] ) );
          found_program         = 1;
          ctx->sp               = (uint32_t) &soa_stacks + current->stack_index * STACK_SIZE;
          ctx->gpr[ 0 ]         = (int)1;

          // Which program are they trying to run?

          switch(process_looper) {
            
            case 0 : {
              ctx->pc = (uint32_t) entry_0;
              break;
            }

            case 1 : {
              ctx->pc = (uint32_t) entry_1;
              break;
            }

            case 2 : {
              ctx->pc = (uint32_t) entry_2;
              break;
            }

            case 3 : {
              ctx->pc = (uint32_t) entry_3; 
              break;
            } 

            case 4 : {
              ctx->pc = (uint32_t) entry_4; 
              break;
            } 

            case 5 : {
              ctx->pc = (uint32_t) entry_5;
              break;
            }

            case 6 : {
              ctx->pc = (uint32_t) entry_6;
              break;
            }

            case 7 : {
              ctx->pc = (uint32_t) entry_7; 
              break;
            } 

            case 8 : {
              ctx->pc = (uint32_t) entry_8; 
              break;
            } 

            case 9 : {
              ctx->pc = (uint32_t) entry_barber; 
              break;
            }

            case 10  : {
              ctx->pc = (uint32_t) entry_customer; 
              break;
            }

            default : {
              // It failed let's kill the current process
              ctx->gpr[ 0 ] = (int)0; 
              current->state = process_terminated;
              break;
            }
          }

          scheduler(ctx);

          break;
        }
      }

      if( found_program == 0 ) {
        char* error = "\nNo Matching Processes Found. Please feel free to build a dynamic loader if you have time. I certainly didn't.\n";
        print_String(error, strlen(error) );
        ctx->gpr[ 0 ] = (int)0; 
        // Terminate the process as they made a mistake upon input.
        current->state = process_terminated;
        break;
      }

      break;
    }



    // Writes an int number:
    case 0x05 : {
      int number    = (int)( ctx->gpr[ 0 ] );
      ctx->gpr[ 0 ] = (int)1;
      print_Num(number);
      scheduler(ctx);
      break; 
    }





    // Kills a process.
    case 0x06 : {

      char* program_name  = ( char* )( ctx->gpr[ 0 ] );
      int index_to_kill = return_index_given_name(program_name);

      if( index_to_kill == -1 ) {
        char* error = "\nNo Processes With That Name Found!\n";
        print_String( error, strlen(error) );
        break;
      }

      while( index_to_kill != -1 ) {
  
        if( pcb_array[ index_to_kill ].state == process_terminated ) {
          char* p_killed = "\nProcess already killed\n";
          print_String( p_killed, strlen(p_killed) );
          break;
        }
  
        // Recursive killing structure for all children.
  
        recursively_Kill( &pcb_array[ index_to_kill ] );
        index_to_kill = return_index_given_name(program_name);
      }

      ctx->gpr[ 0 ] = (int)1;
      scheduler( ctx );
      break;
    }



    // Exit.
    case 0x07 : {
      current->state = process_terminated;
      scheduler(ctx);
      break;
    }





    // Wait.
    case 0x8 : {

      int semaphore_id  = ( int )( ctx->gpr[ 0 ] );

      semaphore* S = return_semaphore_with_id(semaphore_id);

      if( S == NULL ) {
        char *error = "\nNo semaphore with that ID!\n";
        print_String(error, strlen(error));
        ctx->gpr[ 0 ] = (int)0;
        scheduler(ctx);
        break;
      }

      if( S->counter == 0 ) {

        current->state = process_paused;
        ctx->gpr[ 0 ] = (int)1;
        enqueue(S, current);
        scheduler(ctx);
        break;

      } else if ( S->counter > 0 ) {

        S->counter -= 1;
        ctx->gpr[ 0 ] = (int)1;
        scheduler(ctx);
        break;

      } else {

        ctx->gpr[ 0 ] = (int)0;
        scheduler(ctx);
        break;
      }
    }


    // Signal.
    case 0x09 : {

      int semaphore_id  = ( int )( ctx->gpr[ 0 ] );

      semaphore* S = return_semaphore_with_id(semaphore_id);

      if( S == NULL ) {
        char *error = "\nNo semaphore with that ID!\n";
        print_String(error, strlen(error));
        ctx->gpr[ 0 ] = (int)0;
        break;
      }

      uint8_t empty_queue = 1;
      for( uint8_t s = 0; s < QSIZE; s++ ) {
          if( S->priority_queue[s] != NULL && S->priority_queue[s]->state != process_terminated ) {
              empty_queue = 0;
          }
      }
  
      if( empty_queue == 0 ) {

        dequeue(S);

      } else if ( empty_queue == 1 ) {

        S->counter += 1;

      }

      ctx->gpr[ 0 ] = (int)1;

      break;
    }


    // Creat syscall:
    case 0xA : {
      int fd = create_file(ctx, 0); // 0 as arg as it is not from kernel.
      ctx->gpr[ 0 ] = fd;
      break;
    }

    // Read from disk syscall:
    case 0xB : {
      read_from_disk(ctx);
      break;
    }

    case 0xC : {
      write_to_disk(ctx);
      break;
    }

    case 0xD : {
      offset(ctx);
      break;
    }

    case 0xE : {
      unlink_file(ctx);
      break;
    }

    case 0xF : {
      list_files();
      break;
    }



    // Unknown SVC.
    default   : {
      char *error = "\nUnknown Interrupt Call.\n";
      print_String(error, strlen(error));
      break;
    }
  }

  return;
}




// Find the next available slot to get access (add to the priority queue)
void enqueue( semaphore* S, pcb_t* process ) {
  for(uint8_t s = 0; s < QSIZE; s++ ) {
    if( S->priority_queue[s] == NULL || S->priority_queue[s]->state == process_terminated ) {
        S->priority_queue[s] = process;
        break;
    }
  }

  return;
}


void dequeue( semaphore* S ) {

    // Find the first process and set it's state to running.
  if( S->priority_queue[0] != NULL || S->priority_queue[0]->state != process_terminated ) {
      S->priority_queue[0]->state = process_running;
  }
 
  // move everything left to enforce priority queue structure.
 
  for (uint8_t k = 0; k < ( QSIZE - 1 ); k++) {
      S->priority_queue[k] = S->priority_queue[k+1];
  }

  return;
} 












void kernel_handler_irq(ctx_t* ctx) {
  // Read  the interrupt identifier so we know the source.

  uint32_t id = GICC0->IAR;

  // Handle the interrupt, then clear (or reset) the source.

  if( id == GIC_SOURCE_TIMER0 ) {
    
    kernel_handler_svc(ctx, (uint32_t) 0x00);
    TIMER0->Timer1IntClr = 0x01;

  } else if ( id == GIC_SOURCE_UART0 ) {

    char read = ( char )PL011_getc( UART0 );

    // We don't want to include the newline character which is OS dependent in the buffer.
    uint8_t end_of_line = 0;
    if( ( strncmp( "\r" , &read, 1) == 0 ) || ( strncmp( "\n" , &read, 1) == 0 ) ) {
      end_of_line = 1;
    }

    if(end_of_line==1 && read_index == 0) {
      end_of_line = 0;
    } else {

    if( is_shell_reading == 1 && buffer_full != 1 ) {

      if( end_of_line != 1 ) {

        // If a line read is in progress - keep scheduling the shell.

        line_read_in_progress = 1;
        buffer[read_index] = read;
        read_index += 1;
        read_index %= BUFFER_SIZE + 1;

      }

      if( end_of_line == 1 ) {

        // Pressed enter key.
        buffer_full = 1;
        read_index  = 0;
        line_read_in_progress = 0;
        end_of_line = 0;

      } else if( read_index >= (BUFFER_SIZE - 1) ) {

        // Only allowed exactly BUFFER_SIZE - 1 to inc \0
        buffer_full = 1;
        read_index  = 0;
        line_read_in_progress = 0;
        end_of_line = 0;

      }
    }
  }
    PL011_putc( UART0, read );

    UART0->ICR = 0x10;

  }

  // Step 5: write the interrupt identifier to signal we're done.

  GICC0->EOIR = id;
  return;
}











void recursively_Kill (pcb_t* pcb) {
  
  if( pcb == NULL ) {
    return;
  } else {

    pcb->state = process_terminated;
    char* new_process_name = "KILLED PROCESS";
    memcpy( &pcb->program_name, new_process_name, strlen(new_process_name) );
    for( uint8_t child_iterator = 0; child_iterator < MAX_CHILD; child_iterator++ ) {
      recursively_Kill( pcb->children[ child_iterator ] );
    }
  }

  return;
}














// Returns an index in the PCB array given a program name.
int return_index_given_name( char* program_name ) {

// Skip out shell at index 0 so it is never killed.

  for( uint32_t process_looper = 1; process_looper < PCBNUMBER; process_looper++ ) {
    if( strcmp( program_name, pcb_array[ process_looper ].program_name ) == 0 ) {
      return process_looper;
    }
  }

  return -1;
}







semaphore* return_semaphore_with_id( int id ) {
  for( uint32_t s_looper = 0; s_looper < SEMAPHORE_LIMIT; s_looper++ ) {
    if( semaphore_array[ s_looper ].id == id ) {
      return &semaphore_array[ s_looper ];
    }
  }

  return NULL;
}










void print_String( char *x, int n ) {
  for( int i = 0; i < n; i++ ) {
    PL011_putc( UART0, *x++ );
  }
  return;
}















void print_Num( int number ) {
  uint8_t negative_flag = 0;
  
  if (number < 0) {
    number *= -1;
    negative_flag = 1;
  }

  int digit            = number % 10;
  uint8_t index_of_int = 0;
  char num_array[ MAX_NUMBER_SIZE ];
  memset( num_array, '\0', sizeof( char ) * MAX_NUMBER_SIZE );
  
  if(negative_flag == 1) {
    num_array[0] = '-';
    index_of_int += 1;
  }

  // Fill in the number array.

  while( 0 <= digit && digit <= 9 ) {
    char char_from_number = itox( digit );
    number /= 10;
    digit   = number;
    digit  %= 10;
    
    num_array[ index_of_int ] = char_from_number;
    index_of_int += 1;

    if( number == 0 ) {
      break;
    }
  }

  // Print the number array in reverse.

  for (int x = index_of_int; x >= 0; --x) {
    PL011_putc( UART0, num_array[ x ] );
  }
  return;
}










