package talecraft.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedExecutor {
	private final ScheduledExecutorService executor;
	public TimedExecutor() {
		executor = Executors.newScheduledThreadPool(1);
	}
	
	public void executeLater(Runnable runnable, int time) {
		executor.schedule(runnable, time, TimeUnit.MILLISECONDS);
	}
	
}
