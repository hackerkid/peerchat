#Peer Chat
> A Simple Chat Application implemented in Java Swing

##How to run the software

Download PeerChatServer.java and PeerChatClient.java
* `javac PeerChatServer.java in terminal`
* `javac PeerChatClient.java in terminal`
* `java PeerChatServer in terminal` (Started Server)
* `java PeerChatClient in terminal` (Started Client)
* Enter `java PeerChatClient` again so that there are now two clients connected to
server

##Precuations

* Make sure that PeerChatServer is already started before running the Clients
* The software is tested only in Ubuntu/Linux.
* If port 8888 is used by some other application please change it.

##How it works?

The PeerChatServer acts only as the server. Start atleast two peerChatClient files to
start communication with each other along with the Server file running.

##How to connect to server?

When this screen pops up enter localhost.
Optional: If you are using two diffrent computers enter the ip address of the server.
##How to login?

The following login credentials can be used to login to the systemUsername

* snape iiita
* potter malfoy
* jonsnow iiita
* hulk iiita
* ironman tonystark
* batman darkknight
Once logged in the clients can send messages to each other.

##Awesome Features
* Secure communication with JCE(DES/ECB/PKCS5)
* Group chat
* Validation of sign in details in Server side
* Simple to use GUI
* Cross Platform (Windows, MAC, Linux)
