package vn.com.skyone.metro2sky;

import java.util.Date;
import java.util.TimerTask;

public class TaskDoctor extends TimerTask {
	private CopyDoctor copy;

	public TaskDoctor(String[] args) {
		copy = new CopyDoctor(args);
	}

	@Override
	public void run() {
		long id = new Date().getTime();
		System.out.println("lap: " + id / 1000);
		copy.copyDoctor();
	}

}
