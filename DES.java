/*
Notes:
"encrypt" method still incomplete
"main" method still incomplete
"convertStringToBinary" method working completely
"getStringFromFile" method should be working; still haven't tested
*/

import java.util.*;
import java.io.*;

public class DES {
	
	public static void main (String args[]){
	
		BufferedWriter bw = new FileWriter("output.txt");
		String plaintext = getStringFromFile( "input.txt" );
		String[] keys = {
						"0101010101010101",
						"fefefefefefefefe",
						"1f1f1f1f1f1f1f1f",
						"e0e0e0e0e0e0e0e0"
						}
		
		//Encrypt the plaintext using the four keys
		//and write the ciphertext on output.txt
		
		/*
		try{
		
			bw = new BufferedWriter( new FileWriter( "output.txt" ) );
			
			for( int i = 0; i < 4; i++ ){
				bw.write( "Encryption with key " + keys[i] + ":\n" + encrypt(plaintext, keys[i]) + "\n\n" );
			}	
			
			bw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		*/
	}

	/*
	public static StringBuilder encrypt(String inputString, String keyString){
		StringBuilder cipherText;
		
		return cipherText;
	}
	*/
	
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
	
	public static StringBuilder convertStringToBinary(String s){
		
		byte[] bytes = s.getBytes();
		StringBuilder binary = new StringBuilder();
		
		for (byte b : bytes){
			 int val = b;
			 for (int i = 0; i < 8; i++){
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			 }
		}
		System.out.println("'" + s + "' to binary: " + binary);
		
		return binary;
	}
}