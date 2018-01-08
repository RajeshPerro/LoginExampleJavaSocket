# LoginExampleJavaSocket
A sample program to make a client server login including SHA-256 password hashing and sending data between Server and Client <br>using Network Socket.<br>

To run the program :<br>
Run : MultithreadSocketServer.java first then run Client.java<br>
Then go back to the console of MultithreadSocketServer.java give the demo input for user name and password for the user.<br>
  (any user name and password you want)<br>

Then, come back to the client GUI : try to login with the user name and password you provided just now.<br>

################# How the code works ###################<br>
Client : When user is giving input of passwrod <br>
	message [1 | size of user name | 0] <br>
		-> Server okay ready to read user name response [1 | 0 | 0]<br>
	message [client name]<br>
		-> Server okay here is accountsalt for client or client not found: <br>
				response [2 | accountsalt.length | randomSalt.length] <br>
				send(accountsalt)<br>
				send(randomSalt)<br>
			-response [2 | 0 |4] <br>


Client : Step-1 : Take the Password from then input field <br>
		 Step-2 : hash it with accountsalt <br>
		 Step-3 : re-hash it with randomSalt<br>
		 message [3 | Len | 0] 	<br>
		 message [generated data from step-3]<br>

	Server : if it's okay:  response [3 | 0 | 2]<br>
				else  	response [4 | 0 | 4]<br>

Client : Print Success or Wrong based on server response.<br>				
NB : message[] and response are byte arrays..<br>

******************************************************<br>
NB : 2 files : PasswordHash.java and Client_Register.java is  not related with our samle login program.<br>
It was developed just for demonastrate how password hashing works! and registration made for client registration which is not<br>
fully implemented.<br>

Cheers!<br>
Rajesh.
