package utils;

import java.util.Arrays;

/**
 * A class for byte computations
 * 
 * @author adsec36
 *
 */
public class ByteService {
	/**
	 * Remove trailing zeros
	 * 
	 * @param array
	 * @return byte array
	 */
	public static byte[] removePadding(byte[] x) {
		int i = x.length - 1;
		while (x[i] == 0) {
			i--;
		}
		byte[] res = new byte[i + 1];
		System.arraycopy(x, 0, res, 0, i + 1);
		return res;
	}

	/**
	 * Combines two byte arrays
	 * 
	 * @param x
	 *            first byte array
	 * @param y
	 *            second byte array
	 * @return byte array
	 */
	public static byte[] combine(byte[] x, byte[] y) {
		byte[] z = new byte[x.length + y.length];
		System.arraycopy(x, 0, z, 0, x.length);
		System.arraycopy(y, 0, z, x.length, y.length);
		return z;
	}

	/**
	 * Creates a subarray
	 * 
	 * @param x
	 *            array to copy from
	 * @param start
	 *            start index of x
	 * @param end
	 *            end index of x
	 * @return byte array
	 */
	public static byte[] subArray(byte[] x, int start, int end) {
		byte[] res = new byte[end - start];
		System.arraycopy(x, start, res, 0, end - start);
		return res;
	}

	/**
	 * Returns true if the byte arrays have the same content, else false
	 * 
	 * @param x
	 *            first byte array
	 * @param y
	 *            second byte array
	 * @return boolean
	 */
	public static boolean isEqual(byte[] x, byte[] y) {
		return Arrays.equals(x, y);
	}
}
