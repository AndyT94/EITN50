package main;

import java.io.IOException;
import java.net.*;
import java.util.Random;

public class Server {
	private DatagramSocket socket;
	private byte[] buffer = new byte[256];
	private int p = 23;
	private int g = 5;
	
	public static void main(String[] args) {
		new Server().run();
	}

	private void run() {
		try {
			socket = new DatagramSocket(1234);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		DatagramPacket Ain = new DatagramPacket(buffer, buffer.length);
		try {
			socket.receive(Ain);
			byte[] Abytes = Ain.getData();
			double A = Double.parseDouble(new String(Abytes));
			
			Random rand = new Random();
			int b = rand.nextInt(20);
			System.out.println("b=" + b);
			double B = Math.pow(g, b) % p;
			System.out.println("B=" + B);
			byte[] Bbytes = String.valueOf(B).getBytes();
			DatagramPacket Bout = new DatagramPacket(Bbytes, Bbytes.length, Ain.getAddress(), Ain.getPort());
			socket.send(Bout);
			
			double s = Math.pow(A, b) % p;
			System.out.println("Session key: " + s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			DatagramPacket in = new DatagramPacket(buffer, buffer.length);
			try {
				socket.receive(in);
				byte[] inData = in.getData();
				String dataString = new String(inData, 0, in.getLength());
				System.out.println(dataString);
				
				InetAddress address = in.getAddress();
				int port = in.getPort();
				String msg = "OK: !";
				DatagramPacket out = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, port);
				socket.send(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
