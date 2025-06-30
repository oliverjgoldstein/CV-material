#ifndef __PROG_LIST_H
#define __PROG_LIST_H

#include <stddef.h>
#include <stdint.h>

#include <string.h>
#include "../utilities/shared_resources.h"

extern void (*entry_SHELL)();
extern void (*entry_0)();
extern void (*entry_1)();
extern void (*entry_2)();
extern void (*entry_3)();
extern void (*entry_4)();
extern void (*entry_5)();
extern void (*entry_6)();
extern void (*entry_7)();
extern void (*entry_8)();
extern void (*entry_barber)();
extern void (*entry_customer)();

char* array_of_programs[ PCBNUMBER ];

#endif