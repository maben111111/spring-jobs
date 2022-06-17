package com.spring.jobs.ctrl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.jobs.config.ScheduledTask;
import com.spring.jobs.service.CronTaskRegistrar;

/**
 * task ctrl api
 */
@Controller
@RequestMapping("/api")
public class JobInterfaceInfoController {

	@Resource
	private CronTaskRegistrar registrar;

	private Runnable task;

	/**
        *  new task
        */
	@ResponseBody
	@CrossOrigin(value = "*", allowCredentials = "true")
	@RequestMapping(value = "add", produces = "application/json; charset=utf-8")
	public String add(HttpServletRequest request) {
		//0 0 10 10 10 2022,仅执行一次
		String id = request.getParameter("id");
		registrar.addCronTask(id.toString(), doTask(id), "0/10 * * * * ?");
		return "ok";
	}

	/**
        *  del task
        */
	@ResponseBody
	@CrossOrigin(value = "*", allowCredentials = "true")
	@RequestMapping(value = "del", produces = "application/json; charset=utf-8")
	public String del(HttpServletRequest request) {
		String id = request.getParameter("id");
		registrar.removeCronTask(id.toString());
		return "ok";
	}

	/**
        *  del task
        */
	@ResponseBody
	@CrossOrigin(value = "*", allowCredentials = "true")
	@RequestMapping(value = "update", produces = "application/json; charset=utf-8")
	public String update(HttpServletRequest request) {
		String id = request.getParameter("id");
		registrar.removeCronTask(id.toString());
		registrar.addCronTask(id.toString(), doTask(id), "0/10 * * * * ?");
		return "ok";
	}

	
	/**
        *  query task
        */
	@ResponseBody
	@CrossOrigin(value = "*", allowCredentials = "true")
	@RequestMapping(value = "query", produces = "application/json; charset=utf-8")
	public String query(HttpServletRequest request) {
		String id = request.getParameter("id");
		String result = registrar.queryCronTask(id);
		return result;
	}
	
	
	/**
        *  save task & restart task
        */
	@ResponseBody
	@CrossOrigin(value = "*", allowCredentials = "true")
	@RequestMapping(value = "startFromMemory", produces = "application/json; charset=utf-8")
	public String startFromMemory(HttpServletRequest request) {
		//save in db
		String id = request.getParameter("id");
		ScheduledTask scheduledTask = registrar.queryScheduled(id); 
		String taskId = scheduledTask.id; 
		String cron = scheduledTask.cron;
		registrar.removeCronTask(taskId);

		//read from db,start new
		registrar.addCronTask(taskId, doTask(taskId), cron);
		return "ok";
	}

	
	/**
        *  do your bussiness
        */
	private Runnable doTask(String id) {
		task = new Runnable() {

			@Override
			public void run() {
				//retry in this
				System.out.println(id + ":" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
		};
		return task;
	}

}
