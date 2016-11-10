# ARP Poisoning Attack

ARP Poisoning takes advantage of the overly trusting ARP system. By sending forged ARP packets, an attacker can cause a victim to send traffic to any nodes on the local network. First we will passively eavesdrop then we will show how to actively manipulate the victim’s traffic. To complete this attack we have used:
  - Python
  - Scapy
  - Wireshark

In this project, we demonstrate ARP Poisoning attck attack using Scapy in Kali Linux VM. We use the host Mac OS machine as the victim and show how our exploit is similar to existing tools like Ettercap. 

To run this exploit:
1. Enable IP Forwarding using: echo 1 > /proc/sys/net/ipv4/ip_forward
2. Run the script using: sudo python arppoison.py

### Project members
- Anant Gupta (15) 
- Rajat Gupta (16) 
