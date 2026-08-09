#include "libc.h"

/* 

  Commands:
    start
    kill

*/

void shell() {

  char* start = "\n------------------------\nSHELL STARTING\n------------------------";
  write(0, start, strlen(start));
	
  uint8_t shell_running = 1;
  char    command[ BUFFER_SIZE ];

  // The shell always runs otherwise system resets itself:
  while( shell_running ) {

    memset( command, '\0', sizeof( char ) * BUFFER_SIZE );
    read(command);
    
    if( strncmp( &command[ 0 ], "\0", 1 ) == 0 ) {
      
      char* commandLineInterface = "\n> ";
      write ( 0, commandLineInterface, strlen(commandLineInterface) );

      while ( strncmp( &command[ 0 ], "\0", 1 ) == 0 ) {
        // Hold here until the read command is finished and the buffer is full.
      }
    }

    write( 0, "\n", 1 );

    char * split_string = strtok ( command, " " );

    // Potential rollover error here.
    int word_count = 1;
    uint8_t error_flag = 0;

    char* arg_one = NULL;
    char* arg_two = NULL;

    while ( split_string != NULL ) {

      if( word_count == 1 ) {
        arg_one = split_string;
      } else if ( word_count == 2) {
        arg_two = split_string;
      } else if ( word_count > 2 ) {
        char* arg_error            = "\nToo many arguments.\n";
        write( 0, arg_error, strlen( arg_error ) );
        error_flag = 1;
        break;
      }

      split_string = strtok (NULL, " ");

      word_count += 1;
    }

    // Have we missed an argument or already had an error?

    if( ( arg_one == NULL || arg_two == NULL ) && error_flag != 1 ) {
        char* null_error = "\nPlease enter more arguments.\n";
        error_flag       = 1;
        write(0, null_error, strlen(null_error));
    } else if ( strncmp( arg_one, "\0", 1 ) == 0 || strncmp( arg_two, "\0", 1 ) == 0) {
        char* null_error = "\nPlease enter more arguments.\n";
        error_flag       = 1;
        write(0, null_error, strlen(null_error));
    }

    if( error_flag == 0 ) {
      
      if( strncmp( arg_one, "start", 5 ) == 0 ) {

        pid_t pid = fork();

        if (pid == 0) {

          int success = exec(arg_two);

          if( success == 0 ) {
            char* error = "\nExec error.\n";
            write( 0, error, strlen( error ) );
          }
          
        } else if( pid < 0 ) {
          char* fork_error = "\nFork Failure.\n";
          write( 0, fork_error, strlen( fork_error ) );
        }

        char* child_text = "\n\nChild PID: ";
        write    (0, child_text,  strlen(child_text)  );
        write_Int(pid                                 );
      } else if( strncmp( arg_one, "kill", 4 ) == 0 ) {

        int success = kill(arg_two);

        if( success == 1 ) {
          char* text = "\n\nKilled Process: ";
          write (0, text,    strlen(text)   );
          write (0, arg_two, strlen(arg_two));
        }
      } else if( strncmp( arg_one, "ls", 2 ) == 0 ) {
        char* text = "\n\nFiles: ";
        write (0, text,    strlen(text)   );
        ls();
      }
    }
  }
  
  return;
}

void (*entry_SHELL)() = &shell;

