#!/bin/sh
python disk.py --host=127.0.0.1 --port=1235 --file=disk.bin --block-num=65536 --block-len=16 &
make launch-qemu &
make launch-gdb &
