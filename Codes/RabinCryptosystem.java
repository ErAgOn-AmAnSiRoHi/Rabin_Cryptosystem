// Java program to illustrate
// Process of Rabin Cryptosystem


import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Random;

class Cryptography {
	public static long encrypt(long m, long n) {
		// Calculate the square of the decimal value modulo n
		long c = modulo((m * m), n);
		return c;
	}

	public static long modularExponentiation(long k, long b, long m) {
		long i = 0;
		long a = 1;
		long[] binary = new long[100000];
		while (k > 0) {
			binary[(int)i] = k % 2;
			k = k / 2;
			i++;
		}
		for (int j = 0; j < i; j++) {
			if (binary[j] == 1) {
				a = (a * b) % m;
				b = (b * b) % m;
			}
			else{
				b = (b * b) % m;
			}
		}
		return a;
	}

	public static long modulo(long a, long b) {
		if (a >= 0)
			return a % b;
		else
			return b - Math.abs(a%b) % b;
	}

	public static long[] extended_euclid(long a, long b) {
		long s = 0;
		long old_s = 1;
		long t = 1;
		long old_t = 0;
		long r = b;
		long old_r = a;
		long quotient, temp;
		while (r != 0) {
			quotient = old_r / r;
			temp = r;
			r = old_r - quotient * r;
			old_r = temp;
			temp = s;
			s = old_s - quotient * s;
			old_s = temp;
			temp = t;
			t = old_t - quotient * t;
			old_t = temp;
		}
		long[] ans = new long[3];
		ans[0] = old_r;
		ans[1] = old_s;
		ans[2] = old_t;
		return ans;
	}

	public static long[] decrypt(long c, long p, long q) {
		long n = p * q;
		long p1 = modularExponentiation((p + 1) / 4, c, p);
		long p2 = p - p1;
		long q1 = modularExponentiation((q + 1) / 4, c, q);
		long q2 = q - q1;
		long[] ext = extended_euclid(p, q);
		long y_p = ext[1];
		long y_q = ext[2];
		long d1 = (y_p * p * q1 + y_q * q * p1) % n;
		long d2 = (y_p*p*q2 + y_q*q*p1) % n;
		long d3 = (y_p * p * q1 + y_q * q * p2) % n;
		long d4 = (y_p*p*q2 + y_q*q*p2) % n;
		// Print d1, d2, d3, d4 to see the four possible decrypted values
		// System.out.println("d1, d2, d3, d4: ");
		// System.out.println(d1 + " " + d2 + " " + d3 + " " + d4);
		return new long[] {d1, d2, d3, d4};
	}
}
public class RabinCryptosystem {
	public static void main(String[] args)
	{
		long [] e = new long[100000];
		long [] d = new long[100000];

		// Generate two random prime numbers which are congruent to 3 modulo 4

		Random rand = new Random();
		BigInteger p; 
		do { 
			p = BigInteger.probablePrime(16, rand); 
		} while (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))); 

		BigInteger q; 
		do { 
			q = BigInteger.probablePrime(16, rand); 
		} while (!q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) && !q.equals(p));

		long n = p.longValueExact() * q.longValueExact();

		// Take string 
		
		String str = "No, the random sentences in our generator are not computer generated. We considered using computer generated sentences when building this tool, but found the results to be disappointing. Even though it took a lot of time, all the sentences in this generator were created by us.";
		//Leave a line
		System.out.println();
		System.out.println("String: " + str);
		System.out.println();

		// Convert each character in the string to its ASCII value
		byte[] bytes = str.getBytes(Charset.forName("UTF-8"));

		// Pass each ASCII value to encryption function

		for (int i = 0; i < bytes.length; i++) {
			e[i] = Cryptography.encrypt(bytes[i], n);
		}

		// Print the encrypted message
		System.out.print("Encrypted Message: ");
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(e[i]);
		}
		System.out.println();
		// Pass each element of the encrypted message to decryption function

		for (int i = 0; i < bytes.length; i++) {
			long[] d1 = Cryptography.decrypt(e[i], p.longValueExact(), q.longValueExact());
			// print d1[] to see the four possible decrypted values
			if (d1[0] >= 0 && d1[0] <= 127) {
				d[i] = d1[0];
			}
			else if (d1[1] >= 0 && d1[1] <= 127) {
				d[i] = d1[1];
			}
			else if (d1[2] >= 0 && d1[2] <= 127) {
				d[i] = d1[2];
			}
			else if (d1[3] >= 0 && d1[3] <= 127) {
				d[i] = d1[3];
			}
		}
		// Print the decrypted message
		System.out.print("\nDecrypted Message: ");
		for (int i = 0; i < bytes.length; i++) {
			System.out.print((char)d[i]);
		}
}
}