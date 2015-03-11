import java.util.*;
import java.io.*;

public class Test {
	
	public static void main (String args[]){
		String Str = new String("010");
		String Str2 = new String("000");

		for(int i = 0; i < Str.length(); i++){
			System.out.println((char)(Str.charAt(i) ^ Str2.charAt(i)));
		}
	}
	
}	