package com.winnovature.tag;

import java.security.interfaces.DSAParams;
import java.math.BigInteger;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.nio.file.Files;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class TagSigner {
	private static PublicKey pubk;
	private static PrivateKey prvk;

	static {
		try {
			byte[] keyBytes = Files.readAllBytes(new File("/home/ConfigFile/privateKey").toPath());  //KeyPair/privateKey
			final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("DSA");
			TagSigner.prvk = kf.generatePrivate(spec); 
			keyBytes = Files.readAllBytes(new File("/home/ConfigFile/publicKey").toPath()); //KeyPair/publicKey
			final X509EncodedKeySpec spec2 = new X509EncodedKeySpec(keyBytes);
			kf = KeyFactory.getInstance("DSA");
			TagSigner.pubk = kf.generatePublic(spec2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static byte[] sign(final String dataString, final PrivateKey prvKey, final String sigAlg) throws Exception {
		final Signature sig = Signature.getInstance(sigAlg);
		sig.initSign(prvKey);
		sig.update(dataString.getBytes());
		return sig.sign();
	}

	private static boolean verify(final String dataunsigned, final PublicKey pubKey, final String sigAlg,
			final byte[] sigbytes) throws Exception {
		final Signature sig = Signature.getInstance(sigAlg);
		sig.initVerify(pubKey);
		sig.update(dataunsigned.getBytes());
		return sig.verify(sigbytes);
	}

	public static String sha256(final byte[] base) {
		try {
			final MessageDigest digest = MessageDigest.getInstance("SHA-256");
			final byte[] hash = digest.digest(base);
			final StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; ++i) {
				final String hex = Integer.toHexString(0xFF & hash[i]);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			System.out.println(hexString.toString());
			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String getSignedHash(final String TID, final String EPCID) throws Exception {
		final String datafile = String.valueOf(TID) + EPCID;
		final byte[] sigbytes = sign(datafile, TagSigner.prvk, "SHAwithDSA"); // TagSigner.prvk SHAwithDSA
		System.out.println("Signature(in hex):: " + Utils.byteToHex(sigbytes));
		final String hash256 = sha256(sigbytes);
		System.out.println("256 Hash String :: " + hash256.toUpperCase());
		final boolean result = verify(datafile, TagSigner.pubk, "SHAwithDSA", sigbytes); // TagSigner.pubk
		System.out.println("Signature Verification Result = " + result);
		return hash256.toUpperCase();
	}

	public static void main(final String[] args) throws Exception {

		byte[] keyBytes = Files
				.readAllBytes(new File("D:/home/ConfigFile/OueryException_202007161594886729173.txt").toPath());
		System.out.println("keyBytes :" + keyBytes);

		/*
		 * final String sData = getSignedHash("E200341201691D0002500009",
		 * "34161FA820328C6E02002120"); final KeyPair pair = new KeyPair(TagSigner.pubk,
		 * TagSigner.prvk); final DSAPublicKey pubKey = (DSAPublicKey)pair.getPublic();
		 * final DSAPrivateKey priKey = (DSAPrivateKey)pair.getPrivate(); final
		 * DSAParams params = priKey.getParams(); final BigInteger p = params.getP();
		 * final BigInteger q = params.getQ(); final BigInteger g = params.getG(); final
		 * BigInteger x = priKey.getX(); final BigInteger y = pubKey.getY();
		 * System.out.println(); System.out.println("DSA Key Parameters: ");
		 * System.out.println("p = " + p); System.out.println("q = " + q);
		 * System.out.println("g = " + g); System.out.println("x = " + x);
		 * System.out.println("y = " + y); System.out.println();
		 * System.out.println("DSA Key Verification: ");
		 * System.out.println("What's key size? " + p.bitLength());
		 * System.out.println("Is p a prime? " + p.isProbablePrime(200));
		 * System.out.println("Is q a prime? " + q.isProbablePrime(200));
		 * System.out.println("Is p-1 mod q == 0? " +
		 * p.subtract(BigInteger.ONE).mod(q)); System.out.println("Is g**q mod p == 1? "
		 * + g.modPow(q, p)); System.out.println("Is q > x? " + (q.compareTo(x) == 1));
		 * System.out.println("Is g**x mod p == y? " + g.modPow(x, p).equals(y));
		 */
	}

}
