/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginexample;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author rajesh
 */
public class MultithreadSocketServer {

    public static void main(String[] args) throws Exception {
        int PortNum = 9090;
        int counter = 0;
        String userName, password;
        try {
            ServerSocket server = new ServerSocket(PortNum);
            System.out.println("Waiting for client on port " + server.getLocalPort() + "...");
            Scanner sc = new Scanner(System.in);

            while (true) {
                counter++;
                Socket serverClient = server.accept();
                System.out.println("Just connected to " + serverClient.getRemoteSocketAddress());
                System.out.println(" >> " + "Client No:" + counter + " started!");
                
                System.out.println("Please Provide a user Name : ");
                userName = sc.nextLine();
                System.out.println("Please Provide a password for the user : ");
                password = sc.nextLine();
               
                Thread t = new Thread(new Server(serverClient, counter, userName, password));
                t.start();
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
