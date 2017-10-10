package Philosopher;

import java.io.PrintWriter;
import java.util.ArrayList;



public class DiningPhilosopher {
	private int time;
	private ArrayList<Philosopher> philosophers;
	private ArrayList<Chopstick> chopsticks;
	private PrintWriter log;

	public DiningPhilosopher() {
		time = 10000;
	}

	public void setSimulationTime(int time) {
		this.time = time;

	}

	public void initialize() {
		philosophers = new ArrayList<Philosopher>();
		chopsticks = new ArrayList<Chopstick>();
		try {
			log = new PrintWriter("Log.txt");
		}
		catch(Exception ex) {
			System.out.println("Cannot create log file");
			System.exit(1);
		}

		for(int i=0; i<5; i++)
			chopsticks.add(new Chopstick());

		philosophers.add(new Philosopher(chopsticks.get(0), chopsticks.get(4), log)) ;
		for(int i=0; i<4; i++)
			philosophers.add(new Philosopher(chopsticks.get(i+1), chopsticks.get(i), log));
	}

	public void start() {

		for (Philosopher p : philosophers) {
			Thread thread = new Thread(p);
			thread.start();
		}

		try {
			while(time>=1000 && !Philosopher.isDeadlockDetected(philosophers.size())) {
				Thread.sleep(1000);
				time -= 1000;
			}

			if (!Philosopher.isDeadlockDetected(philosophers.size()))
				Thread.sleep(time);
		}
		catch(Exception ex) {
			// interrupted
		}

		if (Philosopher.isDeadlockDetected(philosophers.size()))
			log.println("Deadlock detected");

		for (Philosopher p : philosophers) {
			p.stop();		
		}

		try {
			Thread.sleep(1);
		}
		catch(Exception ex) {
			// interrupted
		}

		

		log.close();
	}

	public ArrayList<Philosopher> getPhilosophers() {
		return philosophers;
	}

	public static void main(String args[]) {
		DiningPhilosopher dp = new DiningPhilosopher();
		dp.setSimulationTime(100);
		dp.initialize();
		dp.start();
		ArrayList<Philosopher> philosophers = dp.getPhilosophers();
	
		System.out.println("Number of eating turns ");
		for (Philosopher p : philosophers) {
			System.out.println(p.getId() + "- " + p.getNumberOfEatingTurns());	
		}
		System.out.println("Average hungry time ");
		for (Philosopher p : philosophers) {
			System.out.println(p.getId() + "- " + p.getAverageHungryTime());	
		}
		System.out.println("Average eating time ");
		for (Philosopher p : philosophers) {
			System.out.println(p.getId() + "- " + p.getAverageEatingTime());	
		}
		System.out.println("Average thinking time ");
		for (Philosopher p : philosophers) {
			System.out.println(p.getId() + "- " + p.getAverageThinkingTime());	
		}
		
	}
}
