
import java.util.*;
import java.io.*;
import java.io.File;  

public class main {
	
	public static void main(String[] args) {

		//differenttxt dif=new differenttxt();
		//dif.createFile();
		
		// call class CountSketch
		
		CountSketch ck= new CountSketch();
		ck.computeT("mushrooms.txt");
		ck.fillCFromFile("mushrooms.txt","add");
		ck.bruteforce("mushrooms.txt","add");
		ck.computeT("connect.txt");
		ck.fillCFromFile("connect.txt","sub");
		ck.bruteforce("connect.txt","sub");
		ck.getfinallist();
		
	}   
	
}


