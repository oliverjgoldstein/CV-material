#include "program-list.h"
#include "libc.h"
#include "IPC_critical_variable.h"

void customer() {
    
    int barber_ready     = id_barber_ready;
    int customer_ready   = id_customer_ready;
    int access_to_seats  = id_access_to_seats;

    wait(access_to_seats); 

    if( number_of_free_seats > 0 ) {    
        number_of_free_seats -= 1;          // Decrease seat number if not full.
        signal(customer_ready);             // Wake up the barber or increase the counter.

        char* barber_text = "\nCustomer signal to customer_ready complete.\n";
        write( 0, barber_text, strlen(barber_text) );

        signal(access_to_seats);            // Increase the access to seats counter.
        
        barber_text = "\nCustomer signal to access_to_seats complete.\n";
        write( 0, barber_text, strlen(barber_text) );

        wait(barber_ready);          

        barber_text = "\nCompleted customer script.\n";
        write( 0, barber_text, strlen(barber_text) );   
    } else {
        signal(access_to_seats);
    }

    exit();
}

void (*entry_customer)() = &customer;