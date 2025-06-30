#ifndef __SHARED_H
#define __SHARED_H

#include <stddef.h>
#include <stdint.h>

#define BUFFER_SIZE 100         // Implemented as uint32 - the size of the shell buffer.
#define PCBNUMBER   15          // The number of PCBs.   

uint8_t id_barber_ready;
uint8_t id_customer_ready;
uint8_t id_access_to_seats;

#endif