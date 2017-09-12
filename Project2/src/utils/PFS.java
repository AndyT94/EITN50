package utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PFS {
	public static double calcPowMod(int g, int p, int x) {
		return Math.pow(g, x) % p;
	}

	public static void sendFirstValue(int g, int p, int x, DatagramSocket socket, InetAddress address, int port)
			throws IOException {
		double X = PFS.calcPowMod(g, p, x);
		byte[] Xbytes = String.valueOf(X).getBytes();
		DatagramPacket Xout = new DatagramPacket(Xbytes, Xbytes.length, address, port);
		socket.send(Xout);
	}
}
