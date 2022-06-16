package com.spring.jobs.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import com.spring.jobs.config.ScheduledTask;

@Endpoint(id = "taskMonitor")
@Component
public class CronTaskRegistrar implements DisposableBean {
	
	@Autowired
	private TaskScheduler taskScheduler;
	
	private final Map<String, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>();

	@ReadOperation // 显示监控指标
	public Map<String, String> info() {
		Map<String, String> info = new HashMap<>();
		Iterator<String> iterator = scheduledTasks.keySet().iterator();
		while (iterator.hasNext()) {
			String taskId = iterator.next();
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(scheduledTasks.get(taskId).startDate);
			info.put("taskId:" + taskId, "start up :" + date);
		}

		info.put("taskSize:", scheduledTasks.size() + "");
		return info;
	}

	

	public TaskScheduler getScheduler() {
		return this.taskScheduler;
	}

	/**
	 * 新增定时任务
	 * 
	 * @param task
	 * @param cronExpression
	 */
	public void addCronTask(String taskId, Runnable task, String cronExpression) {
		addCronTask(taskId, cronExpression, new CronTask(task, cronExpression));
	}

	public void addCronTask(String taskId, String cronExpression, CronTask cronTask) {
		if (cronTask != null) {
			if (this.scheduledTasks.containsKey(taskId)) {
				removeCronTask(taskId);
			}

			this.scheduledTasks.put(taskId, scheduleCronTask(taskId, cronExpression, cronTask));
		}
	}

	/**
	 * 移除定时任务
	 */
	public void removeCronTask(String taskId) {
		ScheduledTask scheduledTask = this.scheduledTasks.remove(taskId);
		if (scheduledTask != null) {
			scheduledTask.cancel();
		}
	}

	public String queryCronTask(String taskId) {
		ScheduledTask scheduledTask = this.scheduledTasks.get(taskId);
		if (scheduledTask != null) {
			return taskId + " is running,start up from :"
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(scheduledTask.startDate);
		} else {
			return taskId + " is null";
		}
	}

	public ScheduledTask queryScheduled(String taskId) {
		ScheduledTask scheduledTask = this.scheduledTasks.get(taskId);
		return scheduledTask;
	}

	public ScheduledTask scheduleCronTask(String id, String cron, CronTask cronTask) {
		ScheduledTask scheduledTask = new ScheduledTask(id, cron, new Date());
		scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());

		return scheduledTask;
	}

	@Override
	public void destroy() {
		for (ScheduledTask task : this.scheduledTasks.values()) {
			task.cancel();
		}
		this.scheduledTasks.clear();
	}

}
