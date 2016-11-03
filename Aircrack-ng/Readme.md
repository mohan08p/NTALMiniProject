# Cracking WPA/WPA2 passwords with Aircrack-ng

This project is based on cracking WPA/WPA2 networks which use pre-shared keys. Most wireless access points now use Wi-Fi Protected Access II with a pre-shared key for wireless security, known as WPA2-PSK. WPA2 uses a stronger encryption algorithm, AES, that's very difficult to crack but not impossible. There are three common methods used to crack WPA/WPA2 passwords :

  - Dictionary attack (cowpatty, aircrack)
  - Bruteforce (john the ripper, reaver, JTR + aircrack)
  - Social engineering (Evil twin method)

In this project, we demonstrate dictionary attack using _aircrack-ng_ in Kali Linux. We use a large list of commonly used passwords and tools like _crunch_ and _rsmangler_ to generate custom lists. The attack is based on the four-way handshake between clients and access points. Once we capture the four-way handshake, the password can be cracked offline using _aircrack-ng_.

##### Project members
- Rohit Burman (07) 
- Deepesh bathija (03) 
- Rohit Bijani (06)