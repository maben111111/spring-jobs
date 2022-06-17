package com.spring.jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
*main class
*/
@EnableScheduling
@SpringBootApplication
public class StartApplication {
	public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
        System.out.println("start up!!!");
    }
}
/**
 * 启动这个类，不需要修改配置文件或者数据库支持
 * 
 * 测试步骤：
 * 1.新建一个job，通过cron表达式确定是启动一次还是每十秒执行一次，具体可以查看JobInterfaceInfoController 类中的 add 方法
 * http://localhost:8080/api/add?id=1
 * 
 * 2.查询一个job
 * http://localhost:8080/api/query?id=1
 * 
 * 3.删除一个job
 * http://localhost:8080/api/del?id=1
 * 
 * 4.持久化一个job
 * http://localhost:8080/api/add?id=2
 * http://localhost:8080/api/startFromMemory?id=2
 * 
 * 5.监控，超时可以根据启动时间来进行判断并进行处理
 * http://localhost:8080/actuator/taskMonitor
 * 
 * 6.重试
 * 根据实际业务修改JobInterfaceInfoController 类中的 doTask 方法
 * 
 * 7.扩展性
 * 把service注册成微服务，部署多台机器提供服务，通过ctrl动态控制所有的jobs创建、删除等。
 * 
 * 8.数据库
 * 本例没有使用数据库，可以考虑加入redis缓存
 */
