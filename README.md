# LoginExampleJavaSocket
A sample program to make a client server login including SHA-256 password hashing and sending data between Server and Client using Network Socket.

To run the program :
Run : MultithreadSocketServer.java first then run Client.java
Then go back to the console of MultithreadSocketServer.java give the demo input for user name and password for the user.
  (any user name and password you want)

Then, come back to the client GUI : try to login with the user name and password you provided just now.

################# How the code works ###################<br>
Client : When user is giving input of passwrod <br>
	message [1 | size of user name | 0] 
		-> Server okay ready to read user name response [1 | 0 | 0]
	message [client name]
		-> Server okay here is accountsalt for client or client not found: 
				response [2 | accountsalt.length | randomSalt.length] 
				send(accountsalt)
				send(randomSalt)
			-response [2 | 0 |4] 


Client : Step-1 : Take the Password from then input field
		 Step-2 : hash it with accountsalt 
		 Step-3 : re-hash it with randomSalt
		 message [3 | Len | 0] 	
		 message [generated data from step-3]

	Server : if it's okay:  response [3 | 0 | 2]
				else  	response [4 | 0 | 4]

Client : Print Success or Wrong based on server response.				
NB : message[] and response are byte arrays..

******************************************************
NB : 2 files : PasswordHash.java and Client_Register.java is  not related with our samle login program.
It was developed just for demonastrate how password hashing works! and registration made for client registration which is not
fully implemented.

Cheers!
Rajesh.
