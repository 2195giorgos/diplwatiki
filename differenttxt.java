import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.*;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class differenttxt {
	

	public static void createFile() {
		 try {
			 
			 //create "input.txt"
		      
			 FileWriter file = new FileWriter("newstream.txt");
		     BufferedWriter writer = new BufferedWriter(file);
		      // read different stream
		     File myObj = new File("Stream.txt");
		     Scanner sc = new Scanner(myObj);
		     int numOfNumbers=0;
		     int max=-1;
	         while (sc.hasNextLine()) {
	              String line = sc.nextLine();
			      String[] array=line.split(" "); 
			      for (int i=0; i<array.length; i++) {
			    	  int number=Integer.parseInt(array[i]);
			    	  numOfNumbers++;
			    	  if(number>max) {
			    		  max=number;
			    	  }
			      }	            
		      }
	          sc.close();
	          max++;
	          System.out.println((max+"--"+numOfNumbers));     
		      writer.write(max+" "+numOfNumbers);
		      writer.newLine();
		      Scanner sc1 = new Scanner(myObj);
		    
		      while (sc1.hasNextLine()) {
	              String line = sc1.nextLine();
			      String[] array=line.split(" "); 
			      for (int i=0; i<array.length; i++) {
			    	  writer.write(array[i]); // grafei to noymero
			    	 writer.newLine();  // to paei se nea grammh
			      }           
		      }
		      writer.close();
		      sc1.close();
		}
		catch(IOException e)  {  
			e.printStackTrace();  
		} 
	}
}
