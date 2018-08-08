package com.activiti.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 10:48 2018/8/8
 * @Modified By:
 */
public class ProcessVariablesTest {
    //流程引擎对象
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinittion(){
        Deployment deployment = processEngine.getRepositoryService()
                        .createDeployment()
                        .name("流程定义")
                        .addClasspathResource("diagram/helloWorld.bpmn")
                        .addClasspathResource("diagram/helloWorld.png")
                        .deploy();
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        processEngine.getRuntimeService()
                        .startProcessInstanceByKey("helloworld");
    }

    /**
     * 设置流程变量
     */
    @Test
    public void setVariables(){
        TaskService taskService = processEngine.getTaskService();
        //任务ID
        String taskId = "37504";
        /**1：设置流程变量，使用基本数据类型*/
        //使用local方法，绑定taskId,即这条数据会存taskId,存了taskID后，下一级审批任务将不会有这个属性
//        taskService.setVariableLocal(taskId,"请假天数",3);
//        taskService.setVariable(taskId,"请假日期",new Date());
//        taskService.setVariable(taskId,"请假原因","回家探亲");

        /**2：设置流程变量，使用Javabean类型*/
        //流程变量类型没有Javabean类型但是可以存序列化数据，所有要将bean实现序列化接口
        Person person = new Person();
        person.setId(10);
        person.setName("韩梅梅");
        taskService.setVariable(taskId,"测试信息",person);
    }

    /**
     * 获取流程变量
     */
    @Test
    public void getVariables(){
        TaskService taskService = processEngine.getTaskService();
        String taskId = "37504";
        /**1：获取流程变量，使用基本数据类型*/
//        Integer days = (Integer) taskService.getVariable(taskId,"请假天数");
//        Date date =(Date) taskService.getVariable(taskId,"请假日期");
//        String reason = (String) taskService.getVariable(taskId,"请假原因");
//        System.out.println(days+":"+date+":"+reason);

        /**2：获取流程变量，使用Javabean类型*/
        /**
         * 当一个Javabean（实现序列化）放置到流程变量中，要求Javabean的属性不能发生变化
         * 如果发生变化，再获取时抛出异常，提示该Javabean不能反序列化
         * 解决方案：在bean对象中实现序列化时添加序列化版本号
         *           private static final long serialVersionUID = 5790482014544692189L;
         *
         */
        Person person = (Person) taskService.getVariable(taskId,"测试信息");
        System.out.println(person);
    }

    /**
     * 模拟设置和获取流程变量的场景
     */
    @Test
    public void setAndGetVariables(){
        RuntimeService runtimeService = processEngine.getRuntimeService();

        TaskService taskService = processEngine.getTaskService();

        /**设置流程变量*/
        //使用执行对象ID，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
        //runtimeService.setVariable(id,name,value);

        //使用执行对象ID，和map集合设置流程变量，map的key就是流程变量的名称，map的value就是流程变量的值，一次可以设置多个值
        //runtimeService.setVariables(id,map);

        //使用任务ID，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
        //taskService.setVariable(id,name,value);

        //使用任务ID，和map集合设置流程变量，map的key就是流程变量的名称，map的value就是流程变量的值，一次可以设置多个值
        //taskService.setVariables(id,map);

        //启动流程实例的同时，设置流程变量
        //runtimeService.startProcessInstanceByKey(id,map);

        //完成任务的同时，设置流程变量
        //taskService.complete(id,map);

        /**获取流程变量*/
        //使用执行对象ID和流程变量的名称，获取流程变量的值
        //runtimeService.getVariable(id,name);

        //使用执行对象ID，获取所有流程变量
        //runtimeService.getVariables(id);

        //使用执行对象ID和流程变量名称的集合，获取流程变量
        //runtimeService.getVariables(id,list);

        //使用任务ID和流程变量的名称，获取流程变量的值
        //taskService.getVariable(id,name);

        //使用任务ID，获取所有流程变量
        //taskService.getVariables(id);

        //使用任务ID和流程变量名称的集合，获取流程变量
        //taskService.getVariables(id,list);
    }

    /**
     * 完成任务
     */
    @Test
    public void completeMyPersonalTask(){
        //任务ID
        String taskId = "52502";
        processEngine.getTaskService()
                        .complete(taskId);
    }

    /**
     * 查询流程变量的历史表
     */
    @Test
    public void findHistoryProcessVariables(){
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                        .createHistoricVariableInstanceQuery()
                        .variableName("测试信息")
                        .list();
        if(list != null && list.size() > 0){
            for (HistoricVariableInstance h:list) {
                System.out.println(h);
            }
        }
    }
}
