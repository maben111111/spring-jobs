# spring-jobs
spring-jobs
 * entrance：StartApplication
 * 
 * 
 * 1.create new task, code:JobInterfaceInfoController->add
 * http://localhost:8080/api/add?id=1
 * 
 * 2.query task
 * http://localhost:8080/api/query?id=1
 * 
 * 3.delete task
 * http://localhost:8080/api/del?id=1
 * 
 * 4.save task
 * http://localhost:8080/api/add?id=2
 * http://localhost:8080/api/startFromMemory?id=2
 * 
 * 5.monitor
 * http://localhost:8080/actuator/taskMonitor
 * 
 * 6.retry
 * JobInterfaceInfoController->doTask,retry your job
 * 
