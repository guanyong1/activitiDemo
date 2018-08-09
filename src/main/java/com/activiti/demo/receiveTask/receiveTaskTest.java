package com.activiti.demo.receiveTask;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 9:21 2018/8/9
 * @Modified By:
 */
public class receiveTaskTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinittion(){
        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("receiveTask/receiveTask.bpmn");
        InputStream inputStreamPng = this.getClass().getClassLoader().getResourceAsStream("receiveTask/receiveTask.png");
        Deployment deployment = processEngine.getRepositoryService()
                                                .createDeployment()
                                                .name("接收任务活动")
                                                .addInputStream("receiveTask.bpmn",inputStreamBpmn)
                                                //.addInputStream("receiveTask.png",inputStreamPng)
                                                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动流程实例,设置流程变量+获取流程变量+向后执行一步
     */
    @Test
    public void startProcessInstance(){
        ProcessInstance processInstance = processEngine.getRuntimeService()
                        .startProcessInstanceByKey("receiveTask");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getDeploymentId());

        /**查询执行对象ID*/
        Execution execution = processEngine.getRuntimeService()
                        .createExecutionQuery()
                        .processInstanceId(processInstance.getId())//使用流程实例ID
                        .activityId("_3")//当前活动的ID，对应bpmn文件中的节点ID
                        .singleResult();
        /**使用流程变量设置当日销售额，用来传递业务参数*/

        processEngine.getRuntimeService()
                        .setVariable(execution.getId(),"汇总当日销售额",21000);

        /**向后执行一步，如果流程处于等待状态，使得流程继续执行*/
        processEngine.getRuntimeService()
                        .signal(execution.getId());

        /**查询执行对象ID*/
        Execution execution2 = processEngine.getRuntimeService()
                .createExecutionQuery()
                .processInstanceId(processInstance.getId())//使用流程实例ID
                .activityId("_4")//当前活动的ID，对应bpmn文件中的节点ID
                .singleResult();
        /**从流程变量中获取汇总当日销售额*/
        Integer value =(Integer) processEngine.getRuntimeService()
                        .getVariable(execution2.getId(),"汇总当日销售额");
        System.out.println("给老板发送短信：金额是："+value);
        /**向后执行一步，如果流程处于等待状态，使得流程继续执行*/
        processEngine.getRuntimeService()
                .signal(execution2.getId());
    }


}
