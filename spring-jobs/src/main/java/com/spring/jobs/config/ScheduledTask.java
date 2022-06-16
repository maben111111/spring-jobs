package com.spring.jobs.config;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public final class ScheduledTask {

	public String id;

	public String cron;

	public Date startDate;

	public volatile ScheduledFuture<?> future;

	public ScheduledTask(String id,String cron,Date startDate) {
		this.id = id;
		this.cron = cron;
		this.startDate = startDate;
	}

	/**
	 * 取消定时任务
	 */
	public void cancel() {
		ScheduledFuture<?> future = this.future;
		if (future != null) {
			future.cancel(true);
		}
	}
}
