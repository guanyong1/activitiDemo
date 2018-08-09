package com.activiti.demo.personalTask;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 16:55 2018/8/7
 * @Modified By:
 */
public class personalTask02Test {

    //流程引擎对象
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinittion(){
        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("personalTask/personalTask02.bpmn");
        Deployment deployment = processEngine.getRepositoryService()
                                                .createDeployment()
                                                .name("任务分配")
                                                .addInputStream("personalTask02.bpmn",inputStreamBpmn)
                                                //.addInputStream("sequenceFlow.png",inputStreamPng)
                                                .deploy();
        System.out.println(deployment.getId());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        ProcessInstance processInstance = processEngine.getRuntimeService()
                        .startProcessInstanceByKey("personalTask02");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessInstanceId());
    }

    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask(){
        String name = "阿卡丽";
        List<Task> list = processEngine.getTaskService()
                        .createTaskQuery()//创建任务查询对象
                        .taskAssignee(name)//指定个人任务查询，指定办理人
                        //.taskCandidateUser(user)//组任务的办理人查询
                        //.processDefinitionId(id)//使用流程定义的ID查询
                        .list();
        if(list != null && list.size()>0){
            for (Task task:list) {
                System.out.println("任务ID："+task.getId());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务创建时间："+task.getCreateTime());
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("流程实例："+task.getProcessInstanceId());
                System.out.println("执行对象ID："+task.getExecutionId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
                System.out.println("###############");
            }
        }
    }

    /**
     * 可以分配个人任务从一个人到另一个人（认领任务）
     */
    @Test
    public void setAssigneeTask(){
        //任务ID
        String taskId = "170004";
        //指定的办理人
        String userId = "阿卡丽";
        processEngine.getTaskService()
                .setAssignee(taskId,userId);
    }

    /**
     * 完成我的任务
     */
    @Test
    public void comleteMyPersonalTask(){
        //任务ID
        String taskId = "170004";
        processEngine.getTaskService()
                        .complete(taskId);
        System.out.println("完成任务：任务ID："+taskId);
    }
}
