# EITN50
EITN50 - Advanced computer security

TLS and IPsec are protocols to provide a secure connection between two communicating entities.
These protocols are very popular.  One thing these two protocols have in common is that they are
session based.  For many use cases this is not a problem and even fit natural with the nature of the
application, e.g.  VPN or secure connection to your bank server.  However, as explained by Matts-
son and Selander [1], these session based protocols are not always a good solution.  Particularly
for IoT devices one works now on standardizing an alternative called Object security.
The paper of Selander et all [2] gives you a technical description how things are organized in a
solution for IoT devices.  You should not (even try to) implement the protocols of this paper but
you should use the paper as an advanced example.  The solution you have to implement can be -
should be - much simpler but yet capture the main ideas.

## Assignment 1
In this project you have to implement a proof-of-concept implementation of a secure connec-
tion for two parties that should fulfill the following, your solution should
1.  work on the principle of object security,
2.  provide integrity, confidentiality, and replay protection,
3.  use UDP as the way to exchange data between the two parties,
4.  work on the principle of forward security,
5.  should have at least two distinct parts; handshake and (protected) data exchange,
6.  actually work when we test it,
7.  document the design choices for your implementation.

The code should be documented in the project report and should be provided in source code using
ordinary text files from which we can build two programs,  each for each communicating entity.
Use either Java or Python and in case you use non-default libraries/components your source code
delivery should provide all code needed to build the programs.
Examples of useful cryptography libraries are
• BouncyCastle when you program in Java, and
• PyCrypto or cryptography when you program in python.

## References
[1]  J Mattsson, G Selander, Object Security in Web of Things, 2014, W3 Org.
[2]  G Selander, F Palombini, J Mattsson, L Seitz, Application Layer Security for the Internet of
Things, unpublished.
