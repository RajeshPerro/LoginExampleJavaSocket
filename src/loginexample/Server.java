/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginexample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rajesh
 */
public class Server implements Runnable {

    Socket serverClient;
    int clientNo;
    String user, pass;
    byte[] GuessNumberRead;
    byte[] clientRspPart1 = new byte[3];
    byte[] clientRspPart2 = new byte[2];
    byte[] clientName, clientGenData;
    byte[] serverResponse = new byte[4];

    private static final int SALT_SIZE = 64;
    byte[] accountsalt, ranSalt;
    String sharedSecret, sharedSecHashWithSessionSalt;

    public Server(Socket inSocket, int counter, String usr_name, String user_pass) {
        this.serverClient = inSocket;
        this.clientNo = counter;
        this.user = usr_name;
        this.pass = user_pass;
        System.out.println("Gettind data : " + serverClient.toString() + "Client number : " + clientNo + "\n User Name : " + user + " Password : " + pass);
    }

    @Override
    public void run() {

        try {

            DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
            DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
            boolean loop = true;
            String passwordToHash = pass;

            //*****This one is our account salt********
            accountsalt = getSalt();
            System.out.print("Salt in bytes : " + accountsalt + "\n");
            //*****This one is a random session salt********     
            ranSalt = generateSalt();
            System.out.print("Random Salt in bytes : " + ranSalt + "\n");

            //*****This is our Shared sharedSecret********
            sharedSecret = get_SHA_256_SecurePassword(passwordToHash, accountsalt);
            System.out.println("After hash with the salt : " + sharedSecret);

//*****This is What we are expecting from client side********
            sharedSecHashWithSessionSalt = get_SHA_256_SecurePassword(sharedSecret, ranSalt);
            System.out.println("After hash by Session Salt : " + sharedSecHashWithSessionSalt);

            while (loop) {
                inStream.read(clientRspPart1, 0, 3);
                System.out.println("Response : {0} " + clientRspPart1[0] + " {1} " + clientRspPart1[1] + " {2} " + clientRspPart1[2]);
                Byte fb = new Byte(clientRspPart1[0]);
                int FirstValofRes = fb.intValue();
                switch (FirstValofRes) {
                    case 1:
                        Byte bg = new Byte(clientRspPart1[1]);
                        int tempNameLen = bg.intValue();
                        System.out.println("Length = " + tempNameLen);
                        clientName = new byte[tempNameLen];
                        inStream.read(clientName);
                        System.out.println("Name form byte array : " + Arrays.toString(clientName));
                        byte[] byteArray1 = new byte[tempNameLen];
                        for (int i = 0; i < tempNameLen; i++) {
                            byteArray1[i] = clientName[i];

                        }

                        String Name = new String(byteArray1);
                        System.out.println("\nUser Name : " + Name + "\n");
                        if (Name.equalsIgnoreCase(user)) {
                            System.out.println("Yes! Name Found in DB.\n");
                            serverResponse[0] = 2;
                            serverResponse[1] = (byte) accountsalt.length;
                            serverResponse[2] = (byte) ranSalt.length;

                        } else {
                            serverResponse[0] = 2;
                            serverResponse[1] = 0;
                            serverResponse[2] = 4;
                        }
                        outStream.write(serverResponse, 0, 3);
                        outStream.flush();
                        System.out.println("Salt before sending : "+accountsalt);
                        outStream.write(accountsalt);
                        outStream.flush();
                         System.out.println("RandpmSalt before sending : "+ranSalt);
                        outStream.write(ranSalt);
                        outStream.flush();

                       // break;
                    case 2:
                        System.out.println("Do nothing");
                        inStream.read(clientRspPart1,0,3);
                        System.out.println("Response : {0} " + clientRspPart1[0] + " {1} " + clientRspPart1[1] + " {2} " + clientRspPart1[2]);
                        clientGenData = new byte[ranSalt.length];
                        inStream.read(clientGenData);
                        
                        byte[] byteArrayclientData = new byte[ranSalt.length];
                        for (int i = 0; i < ranSalt.length; i++) {
                            byteArrayclientData[i] = clientGenData[i];

                        }

                        String dataReturnFromClient = new String(byteArrayclientData);
                        System.out.println("Data back from client : " + dataReturnFromClient);
                        
                        if(sharedSecHashWithSessionSalt.equals(dataReturnFromClient))
                        {
                            serverResponse [0] = 3;
                            serverResponse [1] = 0;
                            serverResponse [2] = 2;
                        }
                        else
                        {
                            serverResponse [0] = 4;
                            serverResponse [1] = 0;
                            serverResponse [2] = 4;
                        }
                        outStream.write(serverResponse, 0, 3);
                        break;
                
                }
                loop=false;
            }
            inStream.close();
            outStream.close();
            serverClient.close();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String get_SHA_256_SecurePassword(String passwordToHash, byte[] salt) {

        //Use MessageDigest md = MessageDigest.getInstance("SHA-256");
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;

    }

    public byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        return salt;
    }
}
