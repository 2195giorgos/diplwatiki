import java.util.*;

// H klash ayth krataei ta zeygaria, ton arithmo poy diabazoyme san Key kai to estimate_count san Value

public class Pair {
	
	private int Key;
	private int Value;
	
	public Pair(int Key,int Value) {
		this.Key=Key;
		this.Value=Value;
	}
	
	public  int getKey() {
		return Key;
	}
	
	public  int getValue() {
		return Value;
	}
	
	public void addTovalue() {
		Value++;
	}
}
