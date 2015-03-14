/*
Notes:

For Carlson:
First off, here's a really helpful guide on writing our DES program:
http://page.math.tu-berlin.de/~kant/teaching/hess/krypto-ws2006/des.htm

Carlson, I finished the PC-1 permutation because it turned out to be very easy as well. 
Same for the other permutations, except for the S-boxes. Please implement that instead.


Status report:
"f" method still needs S-boxes
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
						
	static int[] afterSBoxPermutation = {
										 16,   7,  20,  21,
										 29,  12,  28,  17,
										  1,  15,  23,  26,
										  5,  18,  31,  10,
										  2,   8,  24,  14,
										 32,  27,   3,   9,
										 19,  13,  30,   6,
										 22,  11,   4,  25,
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
								
	static int[] finalPermutation = {
										40,     8,   48,    16,    56,   24,    64,   32,
										39,     7,   47,    15,    55,   23,    63,   31,
										38,     6,   46,    14,    54,   22,    62,   30,
										37,     5,   45,    13,    53,   21,    61,   29,
										36,     4,   44,    12,    52,   20,    60,   28,
										35,     3,   43,    11,    51,   19,    59,   27,
										34,     2,   42,    10,    50,   18,    58,   26,
										33,     1,   41,     9,    49,   17,    57,   25
										};	

	static int[][][] s_boxes = { 
								{
										{ 14,  4,  13,  1,   2, 15,  11,  8,   3, 10,   6, 12,   5,  9,   0,  7},
										{  0, 15,   7,  4,  14,  2,  13,  1,  10,  6,  12, 11,   9,  5,   3,  8},
										{  4,  1,  14,  8,  13,  6,   2, 11,  15, 12,   9,  7,   3, 10,   5,  0},
										{ 15, 12,   8,  2,   4,  9,   1,  7,   5, 11,   3, 14,  10,  0,   6, 13}
								},

						     	{
										{ 15,  1,   8, 14,   6, 11,   3,  4,   9,  7,   2, 13,  12,  0,   5, 10},
									    {  3, 13,   4,  7,  15,  2,   8, 14,  12,  0,   1, 10,   6,  9,  11,  5},
									    {  0, 14,   7, 11,  10,  4,  13,  1,   5,  8,  12,  6,   9,  3,   2, 15},
									    { 13,  8,  10,  1,   3, 15,   4,  2,  11,  6,   7, 12,   0,  5,  14,  9}
								},
								
								{
										{ 10,  0,   9, 14,   6,  3,  15,  5,   1, 13,  12,  7,  11,  4,   2,  8},
									    { 13,  7,   0,  9,   3,  4,   6, 10,   2,  8,   5, 14,  12, 11,  15,  1},
									    { 13,  6,   4,  9,   8, 15,   3,  0,  11,  1,   2, 12,   5, 10,  14,  7},
									    {  1, 10,  13,  0,   6,  9,   8,  7,   4, 15,  14,  3,  11,  5,   2, 12}
								},
								{
										{  7, 13,  14,  3,   0,  6,   9, 10,   1,  2,   8,  5,  11, 12,   4, 15},
									    { 13,  8,  11,  5,   6, 15,   0,  3,   4,  7,   2, 12,   1, 10,  14,  9},
									    { 10,  6,   9,  0,  12, 11,   7, 13,  15,  1,   3, 14,   5,  2,   8,  4},
									    {  3, 15,   0,  6,  10,  1,  13,  8,   9,  4,   5, 11,  12,  7,   2, 14}
								},
								{
										{  2, 12,   4,  1,   7, 10,  11,  6,   8,  5,   3, 15,  13,  0,  14,  9},
									    { 14, 11,   2, 12,   4,  7,  13,  1,   5,  0,  15, 10,   3,  9,   8,  6},
									    {  4,  2,   1, 11,  10, 13,   7,  8,  15,  9,  12,  5,   6,  3,   0, 14},
									    { 11,  8,  12,  7,   1, 14,   2, 13,   6, 15,   0,  9,  10,  4,   5,  3}
								},
							 	{
										{ 12,  1,  10, 15,   9,  2,   6,  8,   0, 13,   3,  4,  14,  7,   5, 11},
										{ 10, 15,   4,  2,   7, 12,   9,  5,   6,  1,  13, 14,   0, 11,   3,  8},
										{  9, 14,  15,  5,   2,  8,  12,  3,   7,  0,   4, 10,   1, 13,  11,  6},
										{  4,  3,   2, 12,   9,  5,  15, 10,  11, 14,   1,  7,   6,  0,   8, 13}
								},
								{
										{  4, 11,   2, 14,  15,  0,   8, 13,   3, 12,   9,  7,   5, 10,   6,  1},
									    { 13,  0,  11,  7,   4,  9,   1, 10,  14,  3,   5, 12,   2, 15,   8,  6},
									    {  1,  4,  11, 13,  12,  3,   7, 14,  10, 15,   6,  8,   0,  5,   9,  2},
									    {  6, 11,  13,  8,   1,  4,  10,  7,   9,  5,   0, 15,  14,  2,   3, 12}

								},
								{
										{ 13,  2,   8,  4,   6, 15,  11,  1,  10,  9,   3, 14,   5,  0,  12,  7},
									    {  1, 15,  13,  8,  10,  3,   7,  4,  12,  5,   6, 11,   0, 14,   9,  2},
									    {  7, 11,   4,  1,   9, 12,  14,  2,   0,  6,  10, 13,  15,  3,   5,  8},
									    {  2,  1,  14,  7,   4, 10,   8, 13,  15, 12,   9,  0,   3,  5,   6, 11}
								}
							};

	static BufferedWriter bw;

	public static void main (String args[]){
		String plaintext = getStringFromFile("input.txt");
		String[] keys = {
						"0101010101010101",
						"fefefefefefefefe",
						"1f1f1f1f1f1f1f1f",
						"e0e0e0e0e0e0e0e0"
						};
		//"133457799BBCDFF1", test key					
		
		//Encrypt the plaintext using the four keys
		//and write the ciphertext on output.txt
		try{
			bw = new BufferedWriter( new FileWriter( "output.txt" ) );
			bw.write("Encrypting plaintext '" + plaintext + "':\n\n");
			for( int i = 0; i < 4; i++ ){
				bw.write("Encryption with key " + keys[i] + ": \n");
				bw.write("Ciphertext: " + encrypt(plaintext, keys[i]));
				bw.newLine();
				bw.newLine();
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
		//Print subkeys for each of the 16 rounds.
		try{
			bw.write("Subkeys for " + keyString + ": ");
			bw.newLine();
			for(int j = 0; j < 16; j++){	
					bw.write("["+(j+1)+"] "+convertBinaryToHex(subkeys[j]));
					bw.newLine();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}

		cipherBlocks = getCipherBlocks(convertTextToBinary(plaintextString));
		
		cipherText = new StringBuilder();
			
		for(int i = 0; i < cipherBlocks.length; i++){
			currBlock = cipherBlocks[i];
			currBlock = permute(currBlock, initialPermutation);
			leftHalf = currBlock.substring(0, 32);
			rightHalf = currBlock.substring(32, 64);
			
			for(int j = 0; j < 16; j++){	
				temp = rightHalf;
				rightHalf = xor(leftHalf, f(rightHalf, subkeys[j]));
				leftHalf = temp;
			}
		
			currBlock = permute(rightHalf + leftHalf, finalPermutation);
			cipherText.append(currBlock);
		}
		String answer = convertBinaryToHex (cipherText.toString());
		return answer;
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

	public static String convertBinaryToHex(String s) {
	   StringBuilder hex = new StringBuilder(new BigInteger(s, 2).toString(16));

	   while(hex.length() < (s.length() / 4)){
			hex.insert(0, '0');
		}

	   return hex.toString().toUpperCase();
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
		
		block = xor(key, block);
		//S-Boxes:
		block = s_box(block);
		
		//Last permutation:
		block = permute(block, afterSBoxPermutation);
		
		return block;
	}
	
	public static String s_box (String block){
		StringBuilder block2 = new StringBuilder();

		int beginIndex = 0, endIndex = 6;
		for(int i=0; i<8; i++){
			String temp = block.substring(beginIndex, endIndex);
			beginIndex += 6;
			endIndex += 6;

			StringBuilder bin_y = new StringBuilder();
			bin_y.append(temp.charAt(0));
			bin_y.append(temp.charAt(5));

			StringBuilder bin_x = new StringBuilder();
			bin_x.append(temp.charAt(1));
			bin_x.append(temp.charAt(2));
			bin_x.append(temp.charAt(3));
			bin_x.append(temp.charAt(4));

			int index_y = Integer.parseInt(bin_y.toString(), 2);
			int index_x = Integer.parseInt(bin_x.toString(), 2);
			
			int bin_temp  = s_boxes[i][index_y][index_x];
			StringBuilder bin_string = new StringBuilder(Integer.toBinaryString(bin_temp));
			 
			while(bin_string.length() < 4){
				bin_string.insert(0, '0');
			}
			
			block2.append(bin_string.toString());
		}

		return block2.toString();
	}

	public static String xor(String a, String b){
		StringBuilder result = new StringBuilder();
		int len = a.length();
		
		for(int i = 0; i < len; i++){
			result.append((a.charAt(i) ^ b.charAt(i)));
		}
		
		return result.toString();
	}

}