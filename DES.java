/*
Notes:

For Carlson:
First off, here's a really helpful guide on writing our DES program:
http://page.math.tu-berlin.de/~kant/teaching/hess/krypto-ws2006/des.htm

Carlson, I finished the PC-1 permutation because it turned out to be very easy as well. 
Same for the other permutations, except for the S-boxes. Please implement that instead.


Status report:
"encrypt" method still incomplete
"main" method still incomplete
*/

import java.util.*;
import java.io.*;
import java.math.*;

public class DES {

	static int[] initialPermutation = {
										58,    50,   42,    34,    26,   18,    10,    2,
										60,    52,   44,    36,    28,   20,    12,    4,
										62,    54,   46,    38,    30,   22,    14,    6,
										64,    56,   48,    40,    32,   24,    16,    8,
										57,    49,   41,    33,    25,   17,     9,    1,
										59,    51,   43,    35,    27,   19,    11,    3,
										61,    53,   45,    37,    29,   21,    13,    5,
										63,    55,   47,    39,    31,   23,    15,    7
										};
										
	static int[] fExpansion = {
								 32,     1,    2,     3,     4,    5,
								  4,     5,    6,     7,     8,    9,
								  8,     9,   10,    11,    12,   13,
								 12,    13,   14,    15,    16,   17,
								 16,    17,   18,    19,    20,   21,
								 20,    21,   22,    23,    24,   25,
								 24,    25,   26,    27,    28,   29,
								 28,    29,   30,    31,    32,    1
								};
								
	static int[] pc1 = {
						  57,   49,    41,   33,    25,    17,    9,
						   1,   58,    50,   42,    34,    26,   18,
						  10,    2,    59,   51,    43,    35,   27,
						  19,   11,     3,   60,    52,    44,   36,
						  63,   55,    47,   39,    31,    23,   15,
						   7,   62,    54,   46,    38,    30,   22,
						  14,    6,    61,   53,    45,    37,   29,
						  21,   13,     5,   28,    20,    12,    4
						};

	static int[] pc2 = {
						 14,    17,   11,    24,     1,    5,
						  3,    28,   15,     6,    21,   10,
						 23,    19,   12,     4,    26,    8,
						 16,     7,   27,    20,    13,    2,
						 41,    52,   31,    37,    47,   55,
						 30,    40,   51,    45,    33,   48,
						 44,    49,   39,    56,    34,   53,
						 46,    42,   50,    36,    29,   32
						};
						
	static int[] shiftKeyLeft1 = {
									2,     3,     4,     5,     6,     7,     8,
									9,    10,    11,    12,    13,    14,    15,   
								   16,    17,    18,    19,    20,    21,    22,
								   23,    24,    25,    26,    27,    28,     1
								};

	static int[] shiftKeyLeft2 = {
									3,     4,     5,     6,     7,     8,     9,
								   10,    11,    12,    13,    14,    15,    16,    
								   17,    18,    19,    20,    21,    22,    23,
								   24,    25,    26,    27,    28,     1,     2
								};
	
	public static void main (String args[]){
		BufferedWriter bw;
		String plaintext = getStringFromFile("input.txt");
		String[] keys = {
						"0101010101010101",
						"fefefefefefefefe",
						"1f1f1f1f1f1f1f1f",
						"e0e0e0e0e0e0e0e0"
						};				
		
		//Encrypt the plaintext using the four keys
		//and write the ciphertext on output.txt
		try{
			bw = new BufferedWriter( new FileWriter( "output.txt" ) );
			for( int i = 0; i < 4; i++ ){
				bw.write( "Encryption with key " + keys[i] + ":\n" + encrypt(plaintext, keys[i]) + "\n\n" );
			}	
			bw.close();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	
	public static String encrypt(String plaintextString, String keyString){
		StringBuilder cipherText;
		String keyBits = convertHexToBinary(keyString);
		String currBlock;
		String leftHalf;
		String rightHalf;
		String temp;
		String[] cipherBlocks;
		String[] subkeys;
		
		if(keyBits.length() != 64){
			return "Error with key " + keyString + ": Key should be 64 bits long.";
		}
		
		subkeys = getSubkeys(keyBits);
		cipherBlocks = getCipherBlocks(convertTextToBinary(plaintextString));
		cipherText = new StringBuilder();
			
		for(int i = 0; i < cipherBlocks.length; i++){
			currBlock = cipherBlocks[i];
			currBlock = permute(currBlock, initialPermutation);
			leftHalf = currBlock.substring(0, 32);
			rightHalf = currBlock.substring(32, 64);
			
			for(int j = 0; j < 16; j++){
				
				 
				
				temp = rightHalf;
				rightHalf = f(rightHalf, keyBits);
				
				leftHalf = temp;
			}
		}
		
		return cipherText.toString();
	}

	
	public static String getStringFromFile(String fileName){
		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		String line;
		
		try {
			br = new BufferedReader(new FileReader(fileName));
			
			line = br.readLine();
			if(line != null) {
				sb.append(line);
				line = br.readLine();
				while (line != null) {
					sb.append("\n");
					sb.append(line);
					line = br.readLine();
				}
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();	
	}
	
	
	public static String convertTextToBinary( String s ){
		
		byte[] bytes = s.getBytes();
		StringBuilder binary = new StringBuilder();
		int val;
		
		for (byte b : bytes){
			val = b;
	
			for (int i = 0; i < 8; i++){
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}
		
		/* Tester:
		System.out.println("'" + s + "' to binary: " + binary);
		*/
		
		return binary.toString();
	}
	
	
	public static String convertHexToBinary(String s){
		StringBuilder binary = new StringBuilder(new BigInteger(s, 16).toString(2));

		//Padding 0's in front of the string:
		while(binary.length() < (s.length() * 4)){
			binary.insert(0, '0');
		}
		
		/* Tester:
		System.out.println("'" + s + "' to binary: " + binary);
		*/
		
		return binary.toString();
	}
	
	
	//Split the binary ciphertext into 64-bit blocks.
	public static String[] getCipherBlocks(String plaintextBits){
	
		String[] ciphertextBlocks;
		int len = plaintextBits.length();
		int numBlocks = len / 64;
		int beginIndex = 0;
		int endIndex = 64;
		int numPadding;
		int lastBlockIndex;
		
		if (len % 64 > 0){
			numBlocks++;
		}
		ciphertextBlocks = new String[numBlocks];
		
		for(int i = 0; endIndex <= len; i++){
			ciphertextBlocks[i] = plaintextBits.substring(beginIndex, endIndex);
			beginIndex += 64;
			endIndex += 64;
		}
		
		if(len % 64 > 0){
			lastBlockIndex = numBlocks - 1;
			ciphertextBlocks[lastBlockIndex] = plaintextBits.substring(beginIndex);
			
			numPadding = endIndex - len;
			for(int i = 0; i < numPadding; i++){
				ciphertextBlocks[lastBlockIndex] = ciphertextBlocks[lastBlockIndex] + "0";
			}
		}
		
		/* Tester:
		for(int i = 0; i < numBlocks; i++){
			System.out.println("Chunk " + i + " : " + ciphertextBlocks[i]);
		}
		*/
		
		return ciphertextBlocks;
	}
	
	
	public static String permute(String block, int[] permutation){
		StringBuilder permutedBlock = new StringBuilder();

		for(int i = 0; i < permutation.length; i++){
			permutedBlock.append(block.charAt(permutation[i] - 1));
		}
		
		
		/*Tester:
		System.out.println(block + " permuted to " + permutedBlock.toString());
		*/
		
		return permutedBlock.toString();
	}
	
	public static String[] getSubkeys(String keyBits){
		String[] subkeys = new String[16];
		String keyC;
		String keyD;
		
		keyBits = permute(keyBits, pc1);
		keyC = keyBits.substring(0, 28);
		keyD = keyBits.substring(28, 56);

		for(int i = 0; i < 16; i++){
			if(i == 0 || i == 1 || i == 8 || i == 15){
				keyC = permute(keyC, shiftKeyLeft1);
				keyD = permute(keyD, shiftKeyLeft1);
			} else{
				keyC = permute(keyC, shiftKeyLeft2);
				keyD = permute(keyD, shiftKeyLeft2);
			}
			
			subkeys[i] = permute(keyC + keyD, pc2);
		}
		
		/*Tester:
		System.out.println("Subkeys for " + keyBits + ":");
		for(int i = 0; i < 16; i++){
			System.out.println(subkeys[i]);
		}
		System.out.println();
		*/
		
		return subkeys;
	}
	
	public static String f(String block, String key){
		//Expansion:
		block = permute(block, fExpansion);
		
		
		return block;
	}

}