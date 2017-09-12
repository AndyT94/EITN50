package main;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import org.bouncycastle.crypto.CryptoException;

import utils.ByteService;
import utils.Encryptor;
import utils.PFS;

public class Client {
	private int port = 1234;
	private int p = 23;
	private int g = 5;
	private Random rand = new Random();
	private byte[] buffer = new byte[256];

	public static void main(String[] args) {
		try {
			new Client().run();
		} catch (NoSuchAlgorithmException | CryptoException | IOException e) {
			e.printStackTrace();
		}
	}

	private void run() throws CryptoException, NoSuchAlgorithmException, IOException {
		DatagramSocket socket = new DatagramSocket();
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		InetAddress hostAddress = InetAddress.getByName("localhost");

		int a = rand.nextInt(20);
		PFS.sendFirstValue(g, p, a, socket, hostAddress, port);

		DatagramPacket firstReply = new DatagramPacket(buffer, buffer.length);
		socket.receive(firstReply);
		byte[] bytesReply = firstReply.getData();
		double B = Double.parseDouble(new String(bytesReply));
		double s = PFS.calcPowMod((int) B, p, a);
		System.out.println("Session key: " + s);

		Encryptor enc = new Encryptor(String.valueOf(s).getBytes());
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		int hashLength = md.getDigestLength();

		while (true) {
			// Receive input from user.
			System.out.println("Enter an important message to send: ");
			String message = (String) cin.readLine();
			Date date = new Date();
			String timestamp = date.toString();
			message = timestamp + " - " + message;
			byte[] bytes = message.getBytes();
			md.update(message.getBytes());
			byte[] digest = md.digest();
			bytes = ByteService.combine(digest, bytes);
			byte[] encBytes = enc.encrypt(bytes);

			System.out.println("Sending encrypted message: " 
					+ new String(encBytes));

			// Create DatagramPacket and send message.
			DatagramPacket dp = new DatagramPacket(encBytes, encBytes.length, hostAddress, port);
			socket.send(dp);

			// Receive message from another client.
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			byte[] data = reply.getData();
			data = ByteService.removePadding(data);

			byte[] decData = enc.decrypt(data);
			byte[] hash = ByteService.subArray(decData, 0, hashLength);
			byte[] recMsg = ByteService.subArray(decData, hashLength, decData.length);
			System.out.println("\nRecieved hash: " + new String(hash));
			message = new String(recMsg);
			
			md.update(recMsg);
			digest = md.digest();
			System.out.println("RecHash == Hash(RecMsg)? " + ByteService.isEqual(digest, hash));
			
			// Print out the details of incoming data, client ip, client
			// port and client message.
			System.out.println("Recieved message: " + message);
		}
	}
}