package main;

import java.io.*;
import java.net.*;
import java.util.Random;
 
public class Client
{
    public static void main(String args[])
    {
        DatagramSocket socket = null;
        int port = 1234;
        String message;
        
        Random rand = new Random();
        double s = 0;
        int p = 23;
        int base = 5;
        int a = rand.nextInt(20);
        System.out.println(p + " " + base + " " + a);
        double key = Math.pow(base, a) % p;
         
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
         
        try
        {
        	socket = new DatagramSocket();
            InetAddress hostAddress = InetAddress.getByName("localhost");
            
            // Sends the session key.
            byte[] bytesKey = String.valueOf(key).getBytes();
            DatagramPacket dpKey = new DatagramPacket(bytesKey, bytesKey.length, hostAddress, port);
            socket.send(dpKey);
            
            byte[] bufferAlice = new byte[65536];
            DatagramPacket replyAlice = new DatagramPacket(bufferAlice, bufferAlice.length);
            socket.receive(replyAlice);
            byte[] bytesReply = replyAlice.getData();
            double B = Double.parseDouble(bytesReply.toString());
            s = Math.pow(B, a) % p;
            System.out.println(s);
            
          
            while(true)
            {
                // Receive input from user.
            	System.out.println("Enter an important message to send: ");
                message = (String)cin.readLine();
                byte[] bytes = message.getBytes();
                 
                // Create DatagramPacket and send message.
                DatagramPacket  dp = new DatagramPacket(bytes , bytes.length , hostAddress , port);
                socket.send(dp);
                 
                // Receive message from another client.
                byte[] buffer = new byte[65536];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                socket.receive(reply);
                byte[] data = reply.getData();
                message = new String(data, 0, reply.getLength());
                 
                // Print out the details of incoming data, client ip, client port and client message.
                System.out.println(reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + message);
            }
        }        
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }
}