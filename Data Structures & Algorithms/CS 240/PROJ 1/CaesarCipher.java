//
//	Name: Turchik, Kyle
//	Project: 1
//	Due: 2/3/2015
//	Course: cs-240-02-w15
//
//	Description:
//	Write a program that implements a Caesar Cipher.  Provide a 
//	method "encrypt" to encrypt a message by shifting the letters 
//	a specified number of positions, another method "decrypt" to 
//	decrypt the encrypted message, and another method 
//	"printLetterCounts" to  print all non-zero letter counts.  Also, 
//	provide a class main to verify the two methods work as intended.
//

public class CaesarCipher {
	public static final char[] alpha = { //
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', //
	'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' //
	};

    /**
     * CaesarCipher encrypt
     * <p>
     * @param plaintext		The original secret message
     * @param shift			The amount of positions to shift each plaintext character
     * @return 				The encrypted message/ciphertext
     */
	public static String encrypt(String plaintext, int shift) {
		char[] encrypt = new char[alpha.length];
			//This array holds the cipher alphabet once the for loop completes
		for (int i = 0; i < alpha.length; i++)
			encrypt[i] = alpha[(i + shift) % alpha.length];

		char[] ciphertext = plaintext.toCharArray();
			//This array holds the encrypted message once the for loop completes
		for (int i = 0; i < ciphertext.length; i++)
			if (Character.isLowerCase(ciphertext[i]))
				ciphertext[i] = encrypt[ciphertext[i] - 'a'];

		return new String(ciphertext);
	}

    /**
     * CaesarCipher decrypt
     * <p>
     * @param ciphertext	The encrypted message
     * @param shift			The amount of positions each plaintext character was shifted
     * @return 				The original secret message/plaintext
     */
	public static String decrypt(String ciphertext, int shift) {
		char[] decrypt = new char[alpha.length]; 

		for (int i = 0; i < alpha.length; i++)
			decrypt[i] = alpha[Math.abs(i + (alpha.length - (shift % alpha.length))) % alpha.length];

		char[] plaintext = ciphertext.toCharArray(); 
			//This array holds the decrypted message once the for loop completes
		for (int i = 0; i < plaintext.length; i++)
			if (Character.isLowerCase(plaintext[i]))
				plaintext[i] = decrypt[plaintext[i] - 'a'];

		return new String(plaintext);
	}

    /**
     * CaesarCipher printLetterCounts
     * <p>
     * @param text		The string of text to be counted 
     */
	public static void printLetterCounts(String text) {
		char[] textArray = text.toCharArray();
			//This array can hold any kind of text message
		int count = 0;

		System.out.println("Letter Count: ");
		for (int i = 0; i < alpha.length; i++) {
			for (int j = 0; j < textArray.length; j++) {
				if (textArray[j] == alpha[i])
					count++;
			}
			if (count != 0)
				System.out.println(alpha[i] + ": " + count);

			count = 0;
		}
	}

	public static void main(String[] args) {
		String message;
		int shift;

		// Test Case 1: Given
		message = "attack at dawn";
		shift = 10;
		System.out.println("Original Message:\t" + message);
		System.out.println("Shift: " + shift);
		message = encrypt(message, shift);
		System.out.println("Encrypted Message:\t" + message);
		message = decrypt(message, shift);
		System.out.println("Decrypted Message:\t" + message);
		printLetterCounts(message);

		System.out.println("");
		
		// Test Case 2
		message = "attack at dawn";
		shift = 36;
		System.out.println("Original Message:\t" + message);
		System.out.println("Shift: " + shift);
		message = encrypt(message, shift);
		System.out.println("Encrypted Message:\t" + message);
		message = decrypt(message, shift);
		System.out.println("Decrypted Message:\t" + message);
		printLetterCounts(message);

		System.out.println("");

		// Test Case 3
		shift = 3;
		message = "et tu, brute?";
		System.out.println("Original Message:\t" + message);
		System.out.println("Shift: " + shift);
		message = encrypt(message, shift);
		System.out.println("Encrypted Message:\t" + message);
		message = decrypt(message, shift);
		System.out.println("Decrypted Message:\t" + message);
		printLetterCounts(message);
		
		System.out.println("");
		
		// Test Case 4
		shift = 28;
		message = "charlie-delta-foxtrot two five niner";
		System.out.println("Original Message:\t" + message);
		System.out.println("Shift: " + shift);
		message = encrypt(message, shift);
		System.out.println("Encrypted Message:\t" + message);
		message = decrypt(message, shift);
		System.out.println("Decrypted Message:\t" + message);
		printLetterCounts(message);

		System.out.println("");
	}
}