/* Each of the following is a low-level interrupt handler: each one is
 * tasked with handling a different interrupt type, and acts as a sort
 * of wrapper around a high-level, C-based handler.
 */

handler_rst: bl    table_copy              @ initialise interrupt vector table

             msr   cpsr, #0xD2             @ enter IRQ mode with no interrupts
             ldr   sp, =tos_irq            @ initialise IRQ mode stack

             bl    kernel_handler_rst      @ invoke C function
             b     .                       @ halt

handler_irq: sub   lr, lr, #4              @ correct return address
             stmfd sp!, { r0-r3, ip, lr }  @ save    caller-save registers

             bl    kernel_handler_irq      @ invoke C function

             ldmfd sp!, { r0-r3, ip, lr }  @ restore caller-save registers
             movs  pc, lr                  @ return from interrupt	

/* The following captures the interrupt vector table, plus a function
 * to copy it into place (which is called on reset): note that 
 * 
 * - for interrupts we don't handle an infinite loop is realised (to
 *   to approximate halting the processor), and
 * - we copy the table itself, *plus* the associated addresses stored
 *   as static data: this preserves the relative offset between each 
 *   ldr instruction and wherever it loads from.
 */

table_data:  ldr   pc, address_rst         @ reset                 vector -> SVC mode
             b     .                       @ undefined instruction vector -> UND mode
             ldr   pc, address_svc         @ supervisor call       vector -> SVC mode
             b     .                       @ abort (prefetch)      vector -> ABT mode
             b     .                       @ abort     (data)      vector -> ABT mode
             b     .                       @ reserved
             ldr   pc, address_irq         @ IRQ                   vector -> IRQ mode
             b     .                       @ FIQ                   vector -> FIQ mode

address_rst: .word handler_rst
address_irq: .word handler_irq
 
table_copy:  mov   r0, #0                  @ set destination address
             ldr   r1, =table_data         @ set source      address
             ldr   r2, =table_copy         @ set source      limit

table_loop:  ldr   r3, [ r1 ], #4          @ load  word, inc. source      address
             str   r3, [ r0 ], #4          @ store word, inc. destination address

             cmp   r1, r2                  
             bne   table_loop              @ loop if address != limit
       
             mov   pc, lr                  @ return

/* These function enable and disable IRQ interrupts respectively, by
 * toggling the 7-th bit of CPSR to either 0 or 1.
 */

.global irq_enable
.global irq_unable

irq_enable:  mrs   r0,   cpsr              @  enable IRQ interrupts
             bic   r0, r0, #0x80
             msr   cpsr_c, r0

             mov   pc, lr

irq_unable:  mrs   r0,   cpsr              @ disable IRQ interrupts
             orr   r0, r0, #0x80
             msr   cpsr_c, r0

             mov   pc, lr
