package de.longor.util;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TimedExecutor implements Runnable {
	private class TimedRunnable {
		long time;
		Runnable run;
		public TimedRunnable(Runnable call, long flashpoint) {
			time = flashpoint;
			run = call;
		}
	}
	
	private final ConcurrentLinkedDeque<TimedRunnable> runners;
	private final Thread thread;
	
	public TimedExecutor() {
		runners = new ConcurrentLinkedDeque<TimedExecutor.TimedRunnable>();
		
		thread = new Thread(this);
		thread.setName("timed-executor");
		thread.setDaemon(true);
		thread.start();
	}
	
	public void executeLater(Runnable runnable, int time) {
		long current = System.currentTimeMillis();
		long flashpoint = current + time;
		runners.add(new TimedRunnable(runnable, flashpoint));
	}
	
	@Override
	public void run() {
		ArrayList<TimedRunnable> tempL = new ArrayList<TimedRunnable>();
		
		while(!Thread.interrupted()) {
			long current = System.currentTimeMillis();
			
			// check which runnables can be run
			for(TimedRunnable r : runners) {
				if(current > r.time) {
					tempL.add(r);
				}
			}
			while(tempL.size() > 0) {
				TimedRunnable r = tempL.get(0);
				runners.remove(r);
				r.run.run();
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
