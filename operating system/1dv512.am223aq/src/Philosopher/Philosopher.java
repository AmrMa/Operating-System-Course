package Philosopher;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class Philosopher implements Runnable {

	private static final int EATING = 1;
	private static final int THINKING = 2;
	private static final int HUNGRY = 3;


	private int time;
	private int id;
	private static int PHILOSOPHER_ID  = 1;
	private static final int MAX_THINKING_TIME = 10;
	private static final int MAX_EATING_TIME = 10;
	private int state;
	private Chopstick leftChopstick;
	private Chopstick rightChopstick;	
	private int tookLeft;
	private int tookRight;
	private static Random rnd = new Random();
	private PrintWriter log;
	private Lock lock;
	private boolean stopped;
	private static int howManyLocked;
	private static Lock globallock = new ReentrantLock();

	private ArrayList<Integer> thinkingTime;
	private ArrayList<Integer> hungryTime;
	private ArrayList<Integer> eatingTime;
		
	public Philosopher(Chopstick leftChopstick, Chopstick rightChopstick, PrintWriter log) {
		id = PHILOSOPHER_ID;
		PHILOSOPHER_ID++;
		this.leftChopstick = leftChopstick;
		this.rightChopstick = rightChopstick;
		time = 1 + rnd.nextInt(MAX_THINKING_TIME);
		this.log = log;
		state = THINKING;

		thinkingTime = new ArrayList<Integer>();
		hungryTime = new ArrayList<Integer>();
		eatingTime = new ArrayList<Integer>();
		thinkingTime.add(0);
		lock = new ReentrantLock();
	}

	public void stop() {
		lock.lock();
		stopped = true;
		lock.unlock();
	}

	public boolean isStopped() {
		lock.lock();
		stopped = this.stopped;
		lock.unlock();

		return stopped;
	}

	public static boolean isDeadlockDetected(int number) {
		boolean detected = false;
		globallock.lock();
		detected = (howManyLocked == number);
		globallock.unlock();
		return detected;
	}

	
	public void run() {
		log.println("Philosopher_" + getId() + " is THINKING");

		while(!isStopped()) {
			switch(state) {
				case THINKING:
					think();
				break;
				case HUNGRY:
					tryToTakeChopsticks();
				break;
				case EATING:
					eat();
				break;
			}

			try {
				Thread.sleep(1);
			}
			catch(Exception ex) {
				// interrupted
				stop();
			}
		}
	}

	private void eat() {
		time--;
		increase(eatingTime);
		if (time==0) {
			tookRight--;
			rightChopstick.release(log);
			tookLeft--;
			leftChopstick.release(log);

			globallock.lock();
			howManyLocked--;
			//System.out.println("Unlock "+howManyLocked);
			globallock.unlock();

			time = 1 + rnd.nextInt(MAX_THINKING_TIME);
			log.println("Philosopher_" + getId() + " is THINKING");
			state = THINKING;
			thinkingTime.add(0);
		}
	}

	private void tryToTakeChopsticks() {
		if (tookLeft==0) {
			if (leftChopstick.take(log)) {
				tookLeft++;

				globallock.lock();
				howManyLocked++;
				//System.out.println("Lock "+howManyLocked);
				globallock.unlock();

				try {
					// Thread.sleep(1001); // Uncomment in order to make deadlock easier
				}
				catch(Exception ex) {
				}
			}
			else {
				increase(hungryTime);
			}
			return;
		}

		if (tookLeft > 0 && tookRight==0) {
			if (rightChopstick.take(log)) {
				tookRight++;
				time = 1 + rnd.nextInt(MAX_EATING_TIME);
				
				log.println("Philosopher_" + getId() + " is EATING");
				state = EATING;
				eatingTime.add(0);
			}
			else {
				increase(hungryTime);
			}
			return;
		}		
	}

	private void think() {
		time--;
		increase(thinkingTime);
		if (time==0) {			
			log.println("Philosopher_" + getId() + " is HUNGRY");
			state = HUNGRY;
			hungryTime.add(0);
		}
	}

	private void increase(ArrayList<Integer> times) {
		if (times.size()>0) {
			int pos = times.size() - 1;
			times.set(pos, times.get(pos) + 1);
		}
	}
	
	public int getId() {
		return id;
	}

	public int getAverageHungryTime() {
		if (hungryTime.size()>0) {
			int summ = 0;
			for(Integer time : hungryTime)
				summ += time;

			return summ / hungryTime.size();
		}
		else
			return 0;
	}

	public int getNumberOfEatingTurns() {
		return eatingTime.size();
	}

	public int getAverageEatingTime() {
		if (eatingTime.size()>0) {
			int summ = 0;
			for(Integer time : eatingTime)
				summ += time;

			return summ / eatingTime.size();
		}
		else
			return 0;

	}

	public int getAverageThinkingTime() {
		if (thinkingTime.size()>0) {
			int summ = 0;
			for(Integer time : thinkingTime) {
				//System.out.println("  "+id+":: "+time);
				summ += time;
			}

			return summ / thinkingTime.size();
		}
		else
			return 0;
	}	
} 
