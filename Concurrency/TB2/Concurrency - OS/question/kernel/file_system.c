#include "file_system.h"
#include "file_sys_calls.h"
#include "disk.h"

/* 
    These need to be initialised on startup.
    I assume inodes do NOT take up more than one block.
    Convert all block_counter instances into multiples of the block length.
*/ 

void initialise_file_system() {

    // Initialise all variables.

    FDT_counter       = 0;
    superblock.index  = 0;
    inode_list        = 0;
    root              = "/";
    block_size        = disk_get_block_len();
    inode_block_count = ( sizeof(inode) / block_size ) + ( ( sizeof(inode) % block_size ) == 0 ? 0 : 1 );

    disk_wr( 0, 0, disk_get_block_len()*disk_get_block_num() );

    uint32_t block_space_of_sb               = ( sizeof(superblock) / block_size ) + ( ( sizeof(superblock) % block_size ) == 0 ? 0 : 1 );
    block_counter                            = 0;
    superblock.loc_of_root_inode             = block_counter + block_space_of_sb;
    superblock.inode_numbers[ inode_list ]   = superblock.loc_of_root_inode;
    inode_list                              += 1;
    // Let's write the superblock to disk.
    disk_wr( block_counter,  ( const uint8_t* ) &superblock, sizeof(super_block) );    
    block_counter += block_space_of_sb;

    // Only directory currently is root.
    // We need to initialise the mount point (or root directory);
    create_file(NULL, 1);

    return;
}











int create_file(ctx_t* ctx, int is_root) {
    
    char* program_name;
    int   flag;         
    if(is_root == 0) {

        program_name  = ( char* )( ctx->gpr[ 0 ] );
        flag          = ( int   )( ctx->gpr[ 1 ] );

    } else {
        program_name  = root;
        flag          = O_DIR;
    }

    // Let's initialise the inode.
    inode temp_inode;
    temp_inode.inode_number = block_counter;    // It's inode_number is its location in memory.
    temp_inode.in_use       = 1;                // The inode is in use.

    for( uint32_t block_looper = 0; block_looper < NUM_DIRECT_BLOCKS; block_looper++ ) {
        temp_inode.direct_block[ block_looper ] = block_counter + inode_block_count + block_looper + 1;
    }

    // Lets write the inode to disk.
    disk_wr( temp_inode.inode_number,  ( const uint8_t* ) &temp_inode, sizeof(temp_inode) );

    if( is_root == 0 ) {
        // Update the root directory with the mapping from file to address inode number:
        int success = update_root_dir_mapping( program_name, temp_inode.inode_number, 1 );
        if(success == -1) {
            return -1;
        }
        char* result = "\nRoot Directory successfully updated.";
        print_String( result, strlen(result) );
    }

    // Increment the block_counter variable to account for direct blocks/inode.
    block_counter += inode_block_count + NUM_DIRECT_BLOCKS;

    // Let's create a new entry in the fdt table.
    memset( fdt_table[ FDT_counter ].file_name, '\0',         sizeof(char) * MAX_FILE_NAME );
    memcpy( fdt_table[ FDT_counter ].file_name, program_name, sizeof(char) * MAX_FILE_NAME );
    fdt_table[ FDT_counter ].flag           = flag;
    fdt_table[ FDT_counter ].inode_number   = temp_inode.inode_number;
    fdt_table[ FDT_counter ].block_address  = 0;
    fdt_table[ FDT_counter ].index_in_block = 0;
    fdt_table[ FDT_counter ].descriptor     = FDT_counter;

    if(is_root == 1) {
        fdt_table[ FDT_counter ].is_directory = 1;
    } else {
        fdt_table[ FDT_counter ].is_directory = 0;
    }


    if( is_root == 1 ) {
        
        // Initialise directory:

        directory root_directory;
        root_directory.file_counter = 0; 
        root_directory.is_full      = 0;

        for( uint32_t file_looper = 0; file_looper < MAX_FILE_NUMBER; file_looper++ ) {
            memset( root_directory.directory_info[ file_looper ].file_name, '\0', sizeof(char) * MAX_FILE_NAME );
        }

        int size = sizeof(root_directory);
        uint32_t block_looper = 0;

        // Update the FDT rw pointer.
        int file_ok = update_fdt_wr_address( FDT_counter, size );

        if( file_ok == -1 ) {
            char* error = "\nFile is too big.\n";
            print_String(error, strlen(error));
            return -1;
        }

        while( size > block_size ) {
            disk_wr( temp_inode.direct_block[ block_looper ], (( const uint8_t* ) &root_directory) + block_looper * block_size, block_size );
            size         -= block_size;
            block_looper += 1;
        }

        if( size > 0 ) {
            disk_wr( temp_inode.direct_block[ block_looper ], ( const uint8_t* ) &root_directory + block_looper * block_size, size );
        }
    }

    uint32_t ret = FDT_counter;

    superblock.inode_numbers[ inode_list ]   = temp_inode.inode_number;
    inode_list                              += 1;

    FDT_counter       += 1;
    superblock.index  += 1;
    return ret;
}



void read_inode_contents( uint32_t inode_number, inode* inode_dest ) {

    uint8_t data[ block_size * NUM_DIRECT_BLOCKS ];
    memset( data, 0, sizeof(data) );
    
    for(uint32_t block_looper = 0; block_looper < NUM_DIRECT_BLOCKS; block_looper++ ) {
        disk_rd( inode_number + block_looper, ( uint8_t* ) &data[ block_looper * block_size ], block_size );
    }

    memcpy( inode_dest, data, sizeof(inode) );

    return;
}

void unlink_file(ctx_t* ctx) {
    int fd = ( int )( ctx->gpr[ 0 ] );
    inode file_inode;
    read_inode_contents( fdt_table[fd].inode_number, &file_inode );
    file_inode.in_use = 0;
    fdt_table[fd].flag = CLOSED;
    disk_wr( file_inode.inode_number,  ( const uint8_t* ) &file_inode, sizeof(file_inode) );
    update_root_dir_mapping( "", fdt_table[fd].inode_number, 2 );
    ctx->gpr[0] = 1;
    return;
}

void list_files() {
    inode root_inode;

    read_inode_contents( superblock.loc_of_root_inode, &root_inode );

    // This will hold the directory
    uint8_t data[ NUM_DIRECT_BLOCKS * block_size ];

    // Let's read into the blocks
    for ( uint32_t block_looper = 0; block_looper < NUM_DIRECT_BLOCKS; block_looper++ ) {
        disk_rd( root_inode.direct_block[ block_looper ], ( uint8_t* ) &data[ block_looper * block_size ], block_size );
    }

    // Let's cast the blocks into a directory pointer.
    directory* root_directory = ( directory* )data;
    print_String("\n",1);
    for(uint32_t dir = 0; dir < root_directory->file_counter; dir++) {
        char* text = "File Name: ";
        print_String(text,strlen(text));
        print_String(root_directory->directory_info[dir].file_name,strlen(root_directory->directory_info[dir].file_name));
        text = " With Inode Number: ";
        print_String(text,strlen(text));
        print_Num(root_directory->directory_info[dir].inode_number);
        print_String("\n",1);
    }
    
}

void read_from_disk( ctx_t* ctx ) {

    ssize_t bytes_read  = 0;
    int fd              = ( int       )( ctx->gpr[ 0 ] );
    uint8_t* buffer     = ( uint8_t*  )( ctx->gpr[ 1 ] ); 
    int count           = ( int       )( ctx->gpr[ 2 ] );

    if( fdt_table[ fd ].flag == O_WRONLY ) {
        char* error = "\nThis is a write only file.\n";
        print_String( error, strlen(error) );
        ctx->gpr[ 0 ] = -1;
        return;
    } else if ( fdt_table[ fd ].flag == CLOSED ) {
        char* error = "\nThis file has been deleted\n";
        print_String( error, strlen(error) );
        ctx->gpr[ 0 ] = -1;
        return;
    }

    // Obviously they can read from any file. Open computer philosophy. Encouraging data insecurity since 1754.
    inode temp_inode;
    
    read_inode_contents( fdt_table[ fd ].inode_number, &temp_inode );

    // Let's fill data with what we want to read:
    uint8_t data[ block_size * NUM_DIRECT_BLOCKS ];

    memset( data, 0, sizeof(data) );
    for( uint32_t block_looper = 0; block_looper < NUM_DIRECT_BLOCKS; block_looper++ ) {
        disk_rd( temp_inode.direct_block[ block_looper ], ( uint8_t* ) &data[ block_looper * block_size ], block_size );
    }

    // Let's find where the current read write pointer is:

    uint32_t rw_pointer = fdt_table[ fd ].block_address * block_size + fdt_table[ fd ].index_in_block;

    if ( count > NUM_DIRECT_BLOCKS * block_size - ( fdt_table[ fd ].block_address * block_size ) + ( fdt_table[ fd ].index_in_block + 1 ) ) {
        char* error = "\nCannot read over limit.\n";
        print_String(error,strlen(error));
        ctx->gpr[ 0 ] = -1;
        return;
    }

    // Finally let's copy the data into the buffer:
    // rw_pointer = 0;
    memcpy( buffer, &data[ rw_pointer ], count );


    int file_ok = update_fdt_wr_address( fd, count );

    if( file_ok == -1 ) {
        char* error = "\nFile is too big, read write pointer has been reset.\n";
        print_String( error, strlen(error) );
        fdt_table[ fd ].index_in_block = 0;
        fdt_table[ fd ].block_address  = 0;
        ctx->gpr[ 0 ] = -1;
        return;
    }

    ctx->gpr[ 0 ] = count;
    return;
}



void write_to_disk( ctx_t* ctx ) {
    ssize_t bytes_read  = 0;
    int      fd         = ( int       )( ctx->gpr[ 0 ] );
    uint8_t* buffer     = ( uint8_t*  )( ctx->gpr[ 1 ] ); 
    int      count      = ( int       )( ctx->gpr[ 2 ] );

    if( fdt_table[ fd ].flag == O_RONLY ) {
        char* error = "\nThis is a read only file.\n";
        print_String( error, strlen(error) );
        ctx->gpr[ 0 ] = -1;
        return;
    } else if ( fdt_table[ fd ].flag == CLOSED ) {
        char* error = "\nThis file has been deleted\n";
        print_String( error, strlen(error) );
        ctx->gpr[ 0 ] = -1;
        return;
    }

    if(count == 0){
        char* error = "\nCannot write zero bytes.\n";
        print_String(error, strlen(error));
        ctx->gpr[ 0 ] = -1;
        return;
    }

    inode temp_inode;
    
    read_inode_contents( fdt_table[ fd ].inode_number, &temp_inode );

    uint32_t current_block    = fdt_table[ fd ].block_address;
    int remaining_block_space = block_size - fdt_table[ fd ].index_in_block; 

    uint8_t  data[ block_size ];

    memset( data, 0, block_size );

    if(count <= remaining_block_space) {

        disk_rd ( temp_inode.direct_block[ current_block ], (uint8_t *) data,                   block_size );
        memcpy  ( data + fdt_table[ fd ].index_in_block, buffer,     count );
        disk_wr ( temp_inode.direct_block[ current_block ], (const uint8_t *) data,             block_size );

    } else {
        write_count ( temp_inode, buffer, fd, count );
    }
    
    char* text = "\nFile Write With Descriptor: ";
    print_String(text,strlen(text));
    print_Num(fd);
    print_String("\n",1);



    int file_ok = update_fdt_wr_address( fd, count );

    if( file_ok == -1 ) {
        char* error = "\nFile is too big, read write pointer has been reset.\n";
        print_String( error, strlen(error) );
        fdt_table[ fd ].index_in_block = 0;
        fdt_table[ fd ].block_address  = 0;
        ctx->gpr[ 0 ] = -1;
        return;
    }
    
    ctx->gpr[ 0 ] = count;
    return;
}

void offset(ctx_t* ctx) {
    int fd               = ( int )( ctx->gpr[ 0 ] );
    int movement         = ( int )( ctx->gpr[ 1 ] );
    uint8_t error_flag   = 0;
    int current_index    = fdt_table[fd].block_address*block_size + fdt_table[fd].index_in_block;
    // We subtract as positive is 'left'.

    current_index       -= movement;

    if(current_index < 0) {
        current_index = 0;
        error_flag    = 1;
    }

    fdt_table[fd].block_address  = current_index/block_size;
    fdt_table[fd].index_in_block = current_index%block_size;

    if(movement == -1) {
        fdt_table[fd].block_address  = 0;
        fdt_table[fd].index_in_block = 0;
    }

    ctx->gpr[0] = 1;

    if(error_flag==1){
        ctx->gpr[0] = -1;
    }
    return;
}

void write_count( inode temp_inode, uint8_t* buffer, int fd, int count ) {

    // Let's fill data with what we want to read:
    uint8_t data[ block_size * NUM_DIRECT_BLOCKS ];

    memset( data, 0, sizeof(data) );
    for( uint32_t block_looper = 0; block_looper < NUM_DIRECT_BLOCKS; block_looper++ ) {
        disk_rd( temp_inode.direct_block[ block_looper ], ( uint8_t* ) &data[ block_looper * block_size ], block_size );
    }

    int     address_block   = fdt_table[fd].block_address;
    int     index_in_block  = fdt_table[fd].index_in_block;
    
    memcpy( data + index_in_block + address_block*block_size, buffer, count );

    for( uint32_t block_looper = 0; block_looper < NUM_DIRECT_BLOCKS; block_looper++ ) {
        disk_wr( temp_inode.direct_block[ block_looper ], ( uint8_t* ) &data[ block_looper * block_size ], block_size );
    }
}


// 1 represents addition, 2 represents deletion from the directory.
int update_root_dir_mapping( char* program_name, uint32_t inode_number, uint8_t add_or_delete ) {

        // Let's first get the first direct block.
        inode root_inode;

        read_inode_contents( superblock.loc_of_root_inode, &root_inode );

        // This will hold the directory
        uint8_t data[ NUM_DIRECT_BLOCKS * block_size ];

        // Let's read into the blocks
        for ( uint32_t block_looper = 0; block_looper < NUM_DIRECT_BLOCKS; block_looper++ ) {
            disk_rd( root_inode.direct_block[ block_looper ], ( uint8_t* ) &data[ block_looper * block_size ], block_size );
        }

        // Let's cast the blocks into a directory pointer.
        directory* root_directory = ( directory* )data;

        // Check the directory isn't full:

        if( root_directory->is_full == 1 ) {
            char* error = "\nRoot directory full.\n";
            print_String( error, strlen(error) );
            return -1;
        }

        // Update the mapping such that the directory now has both inode number and program name.

        if(add_or_delete == 1) {
            memcpy( root_directory->directory_info[ root_directory->file_counter ].file_name, program_name, sizeof(char) * strlen(program_name) );
            root_directory->directory_info[ root_directory->file_counter ].inode_number = inode_number;
            root_directory->file_counter += 1;
        } else if (add_or_delete == 2) {
            for(uint32_t dir_iterator = 0; dir_iterator < root_directory->file_counter; dir_iterator++ ) {
                if(root_directory->directory_info[ dir_iterator ].inode_number == inode_number) {
                    char* name = "DELETED";
                    memcpy( root_directory->directory_info[ dir_iterator ].file_name, name, sizeof(char) * strlen(name) );
                    root_directory->directory_info[ dir_iterator ].inode_number = 0;
                }                
            }
        }

        // Clean up:

        if( root_directory->file_counter >= MAX_FILE_NUMBER ) {
            root_directory->is_full = 1;
        }

        // Let's write the directory back to disk at the location of the direct blocks.
        // Let's write into the blocks
        int size = sizeof(directory);
        uint32_t block_looper = 0;

        while(size > block_size) {
            disk_wr( root_inode.direct_block[ block_looper ], ( ( const uint8_t* ) root_directory ) + block_looper * block_size, block_size );
            size         -= block_size;
            block_looper += 1;
        }   

        if( size > 0 ) {
            disk_wr( root_inode.direct_block[ block_looper ], ( ( const uint8_t* ) root_directory ) + block_looper * block_size, size );
        }

    return 1;
}











int update_fdt_wr_address( int fdt_number, int remaining_write ) {
    // Update rw pointer.
    // Let's find the remaining space in the current block:
    int remaining_block_space = block_size - ( fdt_table[ fdt_number ].index_in_block );

    if ( fdt_table[ fdt_number ].block_address + remaining_write > NUM_DIRECT_BLOCKS * block_size ) {
        char* error = "\nThis file write or read exceeded file boundaries.\n";
        print_String( error, strlen(error) );
    }

    if( fdt_table[ fdt_number ].block_address  == NUM_DIRECT_BLOCKS ) {
        fdt_table[ fdt_number ].block_address  = NUM_DIRECT_BLOCKS;
        fdt_table[ fdt_number ].index_in_block = block_size - 1;
        return -1;
    }

    while( remaining_write > remaining_block_space ) {

        // If we have don't much remaining space in current blocks
        // Be careful this may allow a write if it just under the limit, so we manually set the rw pointer.
        if( fdt_table[ fdt_number ].block_address  == NUM_DIRECT_BLOCKS && remaining_write > 0 ) {
            fdt_table[ fdt_number ].block_address  = NUM_DIRECT_BLOCKS;
            fdt_table[ fdt_number ].index_in_block = block_size - 1;
            return -1;
        }

        fdt_table[ fdt_number ].block_address  += 1;                        // Move to a new block.
        remaining_write                        -= remaining_block_space;    // We have written some of it.
        remaining_block_space                   = block_size;               // We entered a new block.
        fdt_table[ fdt_number ].index_in_block += block_size;   
        fdt_table[ fdt_number ].index_in_block %= block_size;
    }



    if( remaining_write <= remaining_block_space && remaining_write > 0 ) {

        if( fdt_table[ fdt_number ].block_address  == NUM_DIRECT_BLOCKS ) {
            fdt_table[ fdt_number ].block_address  = NUM_DIRECT_BLOCKS;
            fdt_table[ fdt_number ].index_in_block = block_size - 1;
            return -1;
        }

        fdt_table[ fdt_number ].index_in_block += remaining_write; 

        if( remaining_write == remaining_block_space ) {
            fdt_table[ fdt_number ].index_in_block = block_size - 1;
        }


        remaining_write = 0;
    }

    return 0;
}























