#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Jun 18 12:46:42 2023

@author: Alex

https://magna-power.com/learn/kb/instrumentation-programming-with-python
"""

import time, socket

port = 5555

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

s.connect(('192.168.0.112', port))

s.sendall('*IDN?\n'.encode())

print(s.recv(4096))

Centercmd = 'SENS:FREQ:CENT 1000000' + "\n"
Spancmd = 'SENS:FREQ:SPAN 10000000' + "\n"

s.sendall(Centercmd.encode())
s.sendall(Spancmd.encode())

F = 1000000

for i in range(0,10):
    F += 100000000
    f = str(F)
    cmd = 'SENS:FREQ:CENT ' + f + '\n'
    print(cmd)
    s.sendall(cmd.encode())
    time.sleep(5)


s.close()



"""
https://rfmw.em.keysight.com/wireless/helpfiles/e5055a/Programming/GP-IB_Command_Finder/Sense/Frequency.htm

List of commands for center frequency

SENS:FREQ:CENT 1000000

sense2:frequency:center 1mhz

sense2:frequency:center 1e6

---------------------------

List of commands for span

SENS:FREQ:SPAN 1000000
sense2:frequency:span max

--------------------------

List of commands for sweep

SENS:SWE:DWEL .1
sense2:sweep:dwell min
"""
