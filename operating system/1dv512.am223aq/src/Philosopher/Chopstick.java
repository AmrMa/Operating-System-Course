package Philosopher;

import java.io.PrintWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Chopstick {
	private static int CHOPSTICK_ID  = 1;
	private boolean available;
	private int id;
	private Lock lock;
	
	public Chopstick() {
		available = true;
		id = CHOPSTICK_ID;
		CHOPSTICK_ID++;
		lock = new ReentrantLock();
	} 

	public int getId() {
		return id;
	}
	
	public boolean take(PrintWriter log) {
		if (lock.tryLock()) {
			available = false;
			log.println("Chopstick_" + getId() + " picked-up");
			return true;
		}		
		else
			return false;
	}

	public void release(PrintWriter log) {
		log.println("Chopstick_" + getId() + " released");
		lock.unlock();		
		available = true;		
	}

}
