#ifndef __FS_
#define __FS_

// COMMENT: NEED A BLOCK SIZE OF OVER 32 BYTES IN ORDER TO WORK.

#include <stddef.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>

#include "flags.h"
#include "ctx.h"

#define MAX_FILE_NUMBER     6
#define MAX_FILE_NAME       5
#define MAX_FDT_SIZE        100
#define NUM_DIRECT_BLOCKS   10


typedef struct mapping {
    char     file_name[ MAX_FILE_NAME ];
    uint32_t inode_number;
} mapping;

typedef struct directory {
    mapping  directory_info[ MAX_FILE_NUMBER ];
    uint32_t file_counter;
    uint32_t is_full;       
} directory;

typedef struct fdt_entry {
    uint32_t inode_number;
    uint32_t block_address;
    uint32_t index_in_block;
    uint8_t  flag;
    uint8_t  is_directory;
    int      descriptor;
    char     file_name[ MAX_FILE_NAME ];
} fdt_entry;

typedef struct inode {
    uint32_t direct_block[ NUM_DIRECT_BLOCKS ];
    uint32_t inode_number;
    uint32_t in_use;

    /* 

    inode should be able to give file sizes of up to 4TB.

    Other characteristics I did not have time to add:
    int       UID;
    int       GID;
    int       mode;
    uint32_t  timestamp;
    inode*    indirect_block[3];
    int       link_num;

    */

} inode;

typedef struct super_block {
    uint32_t loc_of_root_inode;
    uint32_t inode_numbers[ MAX_FILE_NUMBER ];
    uint32_t index;
} super_block;

fdt_entry   fdt_table[ MAX_FDT_SIZE ];
super_block superblock;

int   update_root_dir_mapping( char* program_name, uint32_t inode_number, uint8_t add_or_delete );
int   update_fdt_wr_address  ( int fdt_number, int remaining_write );
void  read_inode_contents    ( uint32_t inode_number, inode* inode_dest );
void  write_inode            ( inode temp_inode );
void  write_directory        ( directory dir, inode dir_inode );
void  write_count            ( inode temp_inode, uint8_t* buffer, int fd, int count );
uint32_t    FDT_counter;
uint32_t    block_counter;
uint32_t    inode_block_count;
uint32_t    block_size;
uint32_t    inode_list;
char*       root;

#endif



