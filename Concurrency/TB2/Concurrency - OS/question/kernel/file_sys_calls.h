#ifndef __FS_CALL
#define __FS_CALL

#include "ctx.h"

// These are all file system calls => inc. print functions.

// Self explanatory {

// Must be called by kernel_handler_rst() otherwise file system calls may not work.
void    initialise_file_system();
int     create_file   (ctx_t* ctx, int is_root);
void    set_up_root_directory();
void    print_Num     ( int number );
void    print_String  ( char *x, int n );

extern void read_from_disk( ctx_t* ctx );
extern void write_to_disk ( ctx_t* ctx );
extern void offset        ( ctx_t* ctx );
extern void list_files    ();
extern void unlink_file   (ctx_t* ctx);

#endif