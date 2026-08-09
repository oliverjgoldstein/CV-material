/*
 * playing-around.xc
 *
 *  Created on: 11 Oct 2015
 *      Author: Oliver
 */

#include <platform.h>
#include <stdio.h>
extern "C" {
    int f();
}
int main() {
    par {
        f();
        f();
    }
}
