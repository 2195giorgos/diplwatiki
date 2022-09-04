import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.File;                    // Needed for file operation
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;   
import org.apache.commons.lang3.ArrayUtils;
import java.util.*;


public class CountSketch {
	
	private static HashMap<Integer, Integer> hsbrut = new HashMap<Integer, Integer>();
	
	private static int t; // row
	private static int b; // coll
	private int [] A; // A random hash
	private int [] B; // B random hash
	private static int [] h; // h table hash
	private static int [] s; // s table hash
	
	private float epsilon =  0.2f; //0.38f;      //1500; //0.99f; //15555
	private float delta = 0.00001f;      //0.15f; //155555.5f;
	private int k = 10; // ta top k antikeimena
	private  PriorityQueue<Pair > pq= new PriorityQueue<>(k, Comparator.comparing(Pair::getValue));// taksinomw to heap me bash to estimate san Value
	

	// calculate t and b with parameter delta and epslilon
	public CountSketch(){
		
		this.b= 8*k; 
			
		System.out.println("emfanise sthles b");
		System.out.println(b);
		
		this.B=new int [b];	
	}	
	
	public void computeT(String fileToRead) {
		int n =findNFromFile(fileToRead);
		this.t=(int)Math.abs(Math.ceil(Math.log(n/delta)));
		
		this.A=new int [t];
		this.h=new int [t];
		this.s=new int [t];
		
		System.out.println("emfanise grammes t");
		System.out.println(t);
	}
	
	public int findNFromFile(String fileToRead) {
		try  {  
			
			// read txt file
				FileReader myObj = new FileReader(fileToRead);
				Scanner myReader = new Scanner(myObj);
				String firstLine=myReader.nextLine();
				String[] array=firstLine.split(" ");
				return Integer.parseInt(array[1]);
				
		}catch(IOException e)  {  
			e.printStackTrace();  
		}
		return -1;

	}
	
	// find prime 
	public int findPrime() {
		
		// call class primalityTest
		primalityTest pt = new    primalityTest();
		// find the prime number with call fuction isPrime with step 4 
		for ( int p = b+1; p < 2*b; p++) {
	         if (pt.isPrime(p,4)) {
	        	 return p;
	         }
		}   
		return 0;
	}
	
	//calculate hash h
		public static long hashfuction_h(int k,long p,int A, int B){

			return ((((A*k) + B) % p) %b);
		}
		
		//calculate hash s
		public static long hashfuction_s(int k,long p,int A, int B){
			int result_s=(int)((((A*k) + B) % p) %2);
			if (result_s == 0) {
				   result_s =result_s - 1;
				} else {
				   result_s = 1;
				}
		
			return (result_s);
		}

		//calculate median
		
		public static int medianCalnew(){
			int med=0;	
			int []temp=new int [t];
			for(int i=0;i<t;i++) {
				temp[i]=h[i]*s[i];
			}
			Arrays.sort(temp);
			if(t%2==1) // if t is even
			{
				med=temp[((t+1)/2)];
					
			}else{        // if t is odd

				med=(temp[t/2]+temp[t/2+1])/2;	
			}
			return med;

		}
		
		// afairw ola ta stoixeia to heap mexri na vrw ayto poy psaxnw, molis to vrw prosthetw kata 1  thn timh toy kai ksanabazw ta stoixeia
		//stis theseis tous
		boolean updateHeap(Pair pair) {
		    List<Pair> keys = new ArrayList<Pair>();
		    boolean found=false;
			while(!pq.isEmpty()) { // oso h lista den einai adeia
				Pair head=pq.poll(); // afairw to kefali ths listas
				keys.add(head);     // prosthetw sth lista to head
				if(keys.get(keys.size()-1).getKey()==pair.getKey()) { // pairnw to teleytaio stoixeio me to pair poy eixa  
					found=true; // to briskw oti yparxei
					keys.get(keys.size()-1).addTovalue(); // prosthetw to teleytaio stoixeio kata 1
					break;
				}
			}
			for(int i=0;i<keys.size();i++) {  
				pq.add(keys.get(i));
			}
			return found;
		}
		
		
		void checkheap(int q) {
			int estimate_count=medianCalnew();
			Pair pair=new Pair(q,Math.abs(estimate_count));
			if (pq.isEmpty() || pq.size()<3){ // if priority queue is empty
				pq.add(pair); // add pair
			}
			else if(!updateHeap(pair)){ // ama to breis prostheteis kata 1 alliws sigkrinw ta estimates tou value tou head me to stoixeio pou exw
					Pair head=pq.poll(); // pairei to head kai to diagrafei taytoxrona apo thn lista 
					if(Math.abs(estimate_count)>head.getValue()){ // if estimate_count >head
						pq.add(pair); // add to pair
					}else {
						pq.add(head); // alliws to prosthetw sto head, dhladh to epistrefw sth thesh you
					}
			}
		}
	
	// add q in hash fuction h and s, i for[1,t]
	public void fillH(int q, int A,int B, long p,String action) {
		for(int i=0;i<t;i++) {
			int h1= (int)hashfuction_h(q,p,A,B); // call hash function h
			int s1=(int)hashfuction_s(q,p,A,B);    // call hash function s
			this.h[i]=h1;
			this.s[i]=s1;
			this.h[i]=(action=="add") ? this.h[i]+s1 : this.h[i]-s1; //elegxw an einai add
		}
	}
	
	
	public void fillCFromFile(String Stream,String action){
			try  {  
				
				// read txt file
				FileReader myObj = new FileReader(Stream);
				Scanner myReader = new Scanner(myObj);
				myReader.nextLine();

				
				//for each line read
				while (myReader.hasNextLine()) {
					
					String data = myReader.nextLine();
					String[] array=data.split(","); // split with ","
					int q=Integer.parseInt(array[0]); // read first integer on line is object q
					int p=findPrime(); // call function findPrime to find the prime number
					Random rand= new Random();
					int A= rand.nextInt(t-1); //create the random number to put the hash random array
				    int B=rand.nextInt(t);      
				    ArrayUtils.add(this.A, A); // add random object A to random Array A from hash
				    ArrayUtils.add(this.B, B); // add random object B to random Array B from hash
				    fillH(q,A,B,p,action);
				    checkheap(q);
				}		
				while(!pq.isEmpty()) { 
					Pair head=pq.poll(); 
					System.out.println();
					System.out.println("o q poy brisketai sto heap - count estimate");
					System.out.println(head.getKey()+"-"+head.getValue());
					
				}
				myReader.close();
			 }catch(IOException e)  {  
				 e.printStackTrace();  
			 }
	}
	
 //calculate bruteforce Stream
	public static void bruteforce(String Stream, String action) {
		
		try  {  
		
		// read txt file
			FileReader myObj = new FileReader(Stream);
			Scanner myReader = new Scanner(myObj);
			myReader.nextLine();
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] array=data.split(","); // split with ","
				int q=Integer.parseInt(array[0]); // read first integer on line is object q
				if (hsbrut.containsKey(q)) { // an yparxei sth lista to q aykshse kata 1 
					int newSum=(action=="sub") ? hsbrut.get(q)-1 : hsbrut.get(q)+1; // ama eimaste sto stream1 +1 alliws -1
					hsbrut.put(q, newSum);
				}
				else {
					hsbrut.put(q,1); // den yparxei sth lista
				}
			}	
			myReader.close();
		}catch(IOException e)  {  
			e.printStackTrace();  
		}
		correctHeapValues();
		System.out.println();
		System.out.println("ta q poy phra kai briskontai sto heap - value poy exei");
		hsbrut.forEach((key, value) -> System.out.println(key + " : " + Math.abs((Integer)value)));
		

		
	}
	
	// gyrizw oles tis times toy heap meta thn enwsh S2-S1 se apolyth timh
	static void  correctHeapValues() {
		 for (Map.Entry<Integer,Integer> entry : hsbrut.entrySet()) {
	           if(entry.getValue()<0) {
	        	   hsbrut.put(entry.getKey(), entry.getValue()*(-1));
	           }
	    }
	}
	
	void getfinallist() {
		ValueComparator bvc =  new ValueComparator(hsbrut);
		TreeMap<Integer, Integer> finalresult = new TreeMap<Integer, Integer>(bvc);
		finalresult.putAll(hsbrut);
		Set set = finalresult.entrySet();
		Iterator it = set.iterator();
		int counter=0;
		System.out.println();
		
		 Map.Entry lastEntry=finalresult.lastEntry();
		 int nk=Math.abs((Integer)lastEntry.getValue());
		
		System.out.println("emfanise top 10 antikeimena ");
		while(counter<10) {  
			if(it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
			 	int value=Math.abs((Integer)pair.getValue());
				if(GoodQuality(value,nk)) {
					System.out.print("Key is: "+pair.getKey() + " and ");
					System.out.println("Value is: "+value);
					counter++;
				}
			}else {
				break;
			}
		}
	}
	
	public boolean GoodQuality(int ni,int nk) {
		if(ni>(1-epsilon)*nk) {
			return true;
		}
		return false;
	}
	
}

//Kanei sort to Hashmap
class ValueComparator implements Comparator<Integer> {

    Map<Integer, Integer> base;
    public ValueComparator(Map<Integer, Integer> base) {
        this.base = base;
    }
  
    public int compare(Integer a, Integer b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
}
