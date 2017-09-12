package main;

import java.io.IOException;
import java.net.*;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

import utils.ByteService;
import utils.Encryptor;
import utils.PFS;

public class Server {
	private DatagramSocket socket;
	private int port = 1234;
	private byte[] buffer = new byte[256];
	private int p = 23;
	private int g = 5;
	private Random rand = new Random();

	public static void main(String[] args) {
		try {
			new Server().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run() throws Exception {
		socket = new DatagramSocket(port);

		// Handshake with perfect forward secrecy
		DatagramPacket Ain = new DatagramPacket(buffer, buffer.length);
		socket.receive(Ain);
		byte[] Abytes = Ain.getData();
		double A = Double.parseDouble(new String(Abytes));
		int b = rand.nextInt(20);

		PFS.sendFirstValue(g, p, b, socket, Ain.getAddress(), Ain.getPort());
		double s = PFS.calcPowMod((int) A, p, b);
		System.out.println("Session key: " + s);

		Encryptor enc = new Encryptor(String.valueOf(s).getBytes());
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		int hashLength = md.getDigestLength();

		while (true) {
			DatagramPacket in = new DatagramPacket(buffer, buffer.length);
			try {
				// Receive and decrypt bytes
				socket.receive(in);
				byte[] inData = in.getData();
				inData = ByteService.removePadding(inData);
				byte[] decData = enc.decrypt(inData);

				// Get message and hash
				byte[] hash = ByteService.subArray(decData, 0, hashLength);
				byte[] recMsg = ByteService.subArray(decData, hashLength, decData.length);
				System.out.println("Recieved hash: " + new String(hash));
				System.out.println("Recieved message: " + new String(recMsg));
				md.update(recMsg);
				byte[] digest = md.digest();
				System.out.println("RecHash == Hash(RecMsg)? " + ByteService.isEqual(digest, hash));

				// Sending ACK!
				InetAddress address = in.getAddress();
				int port = in.getPort();
				String msg = "ACK!";
				Date date = new Date();
				String timestamp = date.toString();
				msg = timestamp + " - " + msg;
				byte[] bytes = msg.getBytes();
				md.update(bytes);
				digest = md.digest();
				bytes = ByteService.combine(digest, bytes);

				byte[] encMsg = enc.encrypt(bytes);
				DatagramPacket out = new DatagramPacket(encMsg, encMsg.length, address, port);
				socket.send(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
