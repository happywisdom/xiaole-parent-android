package com.hovans.android.util;

import android.os.Process;
import android.util.Log;
import com.hovans.android.log.LogByCodeLab;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple stop watch, allowing for timing of a number of tasks,
 * exposing total running time and running time for each named task.
 *
 * <p>Conceals use of <code>System.currentTimeMillis()</code>, improving the
 * readability of application code and reducing the likelihood of calculation errors.
 *
 * <p>Note that this object is not designed to be thread-safe and does not
 * use synchronization.
 *
 * <p>This class is normally used to verify performance during proof-of-concepts
 * and in development, rather than as part of production applications.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since May 2, 2001
 */
public class StopWatch {
	/**
	 * Identifier of this stop watch.
	 * Handy when we have output from multiple stop watches
	 * and need to distinguish between them in log or console output.
	 */
	private final String id;
	private final long startTime;
	private final long tid;
	private boolean keepTaskList = true;
	private final List<TaskInfo> taskList = new LinkedList<TaskInfo>();
	/** Start time of the current task */
	private long startTimeMillis;
	/** Is the stop watch currently running? */
	private boolean running;
	/** Name of the current task */
	private String currentTaskName;
	private TaskInfo lastTaskInfo;
	private int taskCount;
	/** Total running time */
	private long totalTimeMillis;

	/**
	 * Construct a new stop watch. Does not start any task.
	 */
	public StopWatch() {
		this.id = "";
		this.startTime = System.currentTimeMillis();
		this.tid = Process.myTid();
	}

	/**
	 * Construct a new stop watch with the given id.
	 * Does not start any task.
	 * @param id identifier for this stop watch.
	 * Handy when we have output from multiple stop watches
	 * and need to distinguish between them.
	 */
	public StopWatch(String id) {
		this.id = id;
		this.startTime = System.currentTimeMillis();
		this.tid = Process.myTid();
	}

	public StopWatch(String id, long startTime) {
		this.id = id;
		this.startTime = startTime;
		this.tid = Process.myTid();
	}

	public String getId() {
		return id;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getTid() {
		return tid;
	}

	/**
	 * Determine whether the TaskInfo array is built over time. Set this to
	 * "false" when using a StopWatch for millions of intervals, or the task
	 * info structure will consume excessive memory. Default is "true".
	 */
	public void setKeepTaskList(boolean keepTaskList) {
		this.keepTaskList = keepTaskList;
	}

	/**
	 * Start an unnamed task. The results are undefined if {@link #stop()}
	 * or timing methods are called without invoking this method.
	 * @see #stop()
	 */
	public void start() throws IllegalStateException {
		start("");
	}

	/**
	 * Start a named task. The results are undefined if {@link #stop()}
	 * or timing methods are called without invoking this method.
	 * @param taskName the name of the task to start
	 * @see #stop()
	 */
	public void start(String taskName) throws IllegalStateException {
		if (running) {
			throw new IllegalStateException("Can't start StopWatch: it's already running");
		}
		startTimeMillis = System.currentTimeMillis();
		running = true;
		currentTaskName = taskName;
	}

	/**
	 * Stop the current task. The results are undefined if timing
	 * methods are called without invoking at least one pair
	 * {@link #start()} / {@link #stop()} methods.
	 * @see #start()
	 */
	public void stop() throws IllegalStateException {
		if (!running) {
			throw new IllegalStateException("Can't stop StopWatch: it's not running");
		}
		long lastTime = System.currentTimeMillis() - startTimeMillis;
		totalTimeMillis += lastTime;
		lastTaskInfo = new TaskInfo(currentTaskName, lastTime);
		if (keepTaskList) {
			taskList.add(lastTaskInfo);
		}
		++taskCount;
		running = false;
		currentTaskName = null;
	}

	/**
	 * Return whether the stop watch is currently running.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Return the time taken by the last task.
	 */
	public long getLastTaskTimeMillis() throws IllegalStateException {
		if (lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task interval");
		}
		return lastTaskInfo.getTimeMillis();
	}

	/**
	 * Return the name of the last task.
	 */
	public String getLastTaskName() throws IllegalStateException {
		if (lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task name");
		}
		return lastTaskInfo.getTaskName();
	}

	/**
	 * Return the last task as a TaskInfo object.
	 */
	public TaskInfo getLastTaskInfo() throws IllegalStateException {
		if (lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task info");
		}
		return lastTaskInfo;
	}

	/**
	 * Return the total time in milliseconds for all tasks.
	 */
	public long getTotalTimeMillis() {
		return totalTimeMillis;
	}

	/**
	 * Return the total time in seconds for all tasks.
	 */
	public double getTotalTimeSeconds() {
		return totalTimeMillis / 1000.0;
	}

	/**
	 * Return the number of tasks timed.
	 */
	public int getTaskCount() {
		return taskCount;
	}

	/**
	 * Return an array of the data for tasks performed.
	 */
	public TaskInfo[] getTaskInfo() {
		if (!keepTaskList) {
			throw new UnsupportedOperationException("Task info is not being kept!");
		}
		return taskList.toArray(new TaskInfo[taskList.size()]);
	}

	/**
	 * Return a short description of the total running time.
	 */
	public String shortSummary() {
		return "StopWatch '" + id + "': running time (millis) = " + getTotalTimeMillis();
	}

	/**
	 * Return a string with a table describing all tasks performed.
	 * For custom reporting, call getTaskInfo() and use the task info directly.
	 */
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder(shortSummary());
		sb.append('\n');
		if (!keepTaskList) {
			sb.append("No task info kept");
		} else {
			sb.append("-----------------------------------------\n");
			sb.append("ms     %     Task name\n");
			sb.append("-----------------------------------------\n");
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumIntegerDigits(5);
			nf.setGroupingUsed(false);
			NumberFormat pf = NumberFormat.getPercentInstance();
			pf.setMinimumIntegerDigits(3);
			pf.setGroupingUsed(false);
			for (TaskInfo task : getTaskInfo()) {
				sb.append(nf.format(task.getTimeMillis())).append("  ");
				sb.append(pf.format(task.getTimeSeconds() / getTotalTimeSeconds())).append("  ");
				sb.append(task.getTaskName()).append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * Return an informative string describing all tasks performed
	 * For custom reporting, call <code>getTaskInfo()</code> and use the task info directly.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(shortSummary());
		if (keepTaskList) {
			for (TaskInfo task : getTaskInfo()) {
				sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeMillis());
				long percent = Math.round((100.0 * task.getTimeSeconds()) / getTotalTimeSeconds());
				sb.append(" = ").append(percent).append("%");
			}
		} else {
			sb.append("; no task info kept");
		}
		return sb.toString();
	}

	/**
	 * Inner class to hold data about one task executed within the stop watch.
	 */
	public static final class TaskInfo {

		private final String taskName;
		private final long timeMillis;

		TaskInfo(String taskName, long timeMillis) {
			this.taskName = taskName;
			this.timeMillis = timeMillis;
		}

		/**
		 * Return the name of this task.
		 */
		public String getTaskName() {
			return taskName;
		}

		/**
		 * Return the time in milliseconds this task took.
		 */
		public long getTimeMillis() {
			return timeMillis;
		}

		/**
		 * Return the time in seconds this task took.
		 */
		public double getTimeSeconds() {
			return timeMillis / 1000.0;
		}
	}

	static Long globalStartTime = System.currentTimeMillis();
	static final Object stopWatchLock = new Object();
	static final ConcurrentHashMap<String, StopWatch> sStopWatches = new ConcurrentHashMap<String, StopWatch>();
	static final String TAG = "StopWatch";

	public static void startStopWatch(String oldKey, Object... args) {
		if (LogByCodeLab.d()) {
			startStopWatch(String.format(oldKey, args));
		}
	}

	public static void endStopWatch(String oldKey, Object... args) {
		if (LogByCodeLab.d()) {
			endStopWatch(String.format(oldKey, args));
		}
	}

	public static void startStopWatch(String oldKey) {
		if (LogByCodeLab.d()) {
			synchronized (stopWatchLock) {
				String key = Process.myTid() + "_" + oldKey;
				StopWatch sw = sStopWatches.get(key);
				if (sw == null) {
					sw = new StopWatch(key);
					sStopWatches.put(key, sw);
				}

				if (!sw.isRunning()) {
					Log.i(TAG, "StopWatch - key : [" + key + "] started");
					sw.start(key);
				}
			}
		}
	}

	public static long endStopWatch(String oldKey) {
		if (LogByCodeLab.d()) {
			synchronized (stopWatchLock) {
				String key = Process.myTid() + "_" + oldKey;
				StopWatch sw = sStopWatches.get(key);
				if (sw == null) {
					sw = new StopWatch(key);
					sStopWatches.put(key, sw);
				}

				if (sw.isRunning()) {
					sw.stop();
				}

				Log.v(TAG, sw.prettyPrint());

				long taskStartTime = sw.getStartTime() - globalStartTime;
				long taskTotalTime = sw.getTotalTimeMillis();
				long taskEndTime = taskStartTime + taskTotalTime;

				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setMinimumIntegerDigits(10);
				nf.setGroupingUsed(false);

				StringBuilder stopWatchStringBuilder = new StringBuilder();
				stopWatchStringBuilder.append(nf.format(sw.getTid())).append(",").append(nf.format(taskStartTime)).append("-").append(oldKey).append(",").append(taskStartTime + "").append(",").append(taskEndTime + "").append(",").append(taskTotalTime + "");
				stopWatchStringList.add(stopWatchStringBuilder.toString());

				sStopWatches.remove(key);

				return taskTotalTime;
			}
		}
		return 0;
	}

	static ArrayList<String> stopWatchStringList = new ArrayList<String>();
}
