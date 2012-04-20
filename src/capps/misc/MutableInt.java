package capps.misc; 

public class MutableInt {

	private int val; 

	public MutableInt(int x) {
		val = x; 
	}

	public MutableInt inc() {
		val++; 
		return this; 
	}

	public MutableInt dec() {
		val--; 
		return this; 
	}

	public MutableInt set(int newVal) {
		val = newVal; 
		return this; 
	}

	public int toInt() {
		return val; 
	}

}
