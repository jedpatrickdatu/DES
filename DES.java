/*
Notes:

For Carlson:
First off, here's a really helpful guide on writing our DES program:
http://page.math.tu-berlin.de/~kant/teaching/hess/krypto-ws2006/des.htm

Please create a function that does the PC-1 permutation to the key:

public static String pc1(String keyString);

This function will be called by the encrypt function below. 
Good luck!


Status report:
"encrypt" method still incomplete
"main" method still incomplete
*/

import java.util.*;
import java.io.*;

public class DES {
	
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
		StringBuilder cipherText = new StringBuilder();
		String[] cipherBlocks = getCipherBlocks(convertStringToBinary(plaintextString));
		String leftHalf;
		String rightHalf;
		
		for(int i = 0; i < cipherBlocks.length; i++){
			leftHalf = cipherBlocks[i].substring(0, 32);
			rightHalf = cipherBlocks[i].substring(32, 64);
			
			for(int j = 0; j < 16; j++){
			
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
	
	
	public static String convertStringToBinary( String s ){
		
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

}