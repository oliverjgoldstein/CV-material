#include "program-list.h"
#include "libc.h"
#include "IPC_critical_variable.h"

void barber() {

    int barber_ready     = id_barber_ready;
    int customer_ready   = id_customer_ready;
    int access_to_seats  = id_access_to_seats;

    while(1) {
        
        char* barber_text = "\nBarber awake and executing.\n";
        write( 0, barber_text, strlen(barber_text) );

        wait(customer_ready);       // If no customer sleep and put in queue.

        barber_text = "\nBarber received a customer.\n";
        write( 0, barber_text, strlen(barber_text) );

        wait(access_to_seats);      // If no access to seats sleep and put in queue.

        barber_text = "\nBarber has access to seats.\n";
        write( 0, barber_text, strlen(barber_text) );

        number_of_free_seats += 1;  // Given access increment variable.

        signal(barber_ready);       // Wake up customer or just inc counter.

        barber_text = "\nBarber has signalled barber_ready.\n";
        write( 0, barber_text, strlen(barber_text) );

        signal(access_to_seats);    // Wake up customer or just inc counter.

                barber_text = "\nBarber complete.\n";
        write( 0, barber_text, strlen(barber_text) );
    }

    exit();
}

void (*entry_barber)() = &barber;