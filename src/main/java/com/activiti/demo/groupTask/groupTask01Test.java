package com.activiti.demo.groupTask;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
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
public class groupTask01Test {

    //流程引擎对象
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinittion(){
        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("groupTask/groupTask01.bpmn");
        Deployment deployment = processEngine.getRepositoryService()
                                                .createDeployment()
                                                .name("组任务分配")
                                                .addInputStream("groupTask01.bpmn",inputStreamBpmn)
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
                        .startProcessInstanceByKey("groupTask");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessInstanceId());
    }

    /**
     * 查询当前人的组任务
     */
    @Test
    public void findMyGroupTask(){
        String name = "德邦";
        List<Task> list = processEngine.getTaskService()
                        .createTaskQuery()//创建任务查询对象
                        .taskCandidateUser(name)//组任务的办理人查询
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
     * 查询正在执行的任务办理人表
     */
    @Test
    public void findRunPerosnTask(){
        //任务ID
        String taskId = "180004";
        List<IdentityLink> list =  processEngine.getTaskService()
                        .getIdentityLinksForTask(taskId);
        if(list != null && list.size() > 0){
            for (IdentityLink i:list) {
                System.out.println(i.getTaskId()+"   "+i.getType()+"    "+i.getProcessInstanceId()+"  "+i.getUserId());
                System.out.println("############");
            }
        }
    }

    /**
     * 查询历史任务的办理人表
     */
    @Test
    public void findHistoryPersonTask(){
        //流程实例ID
        String processInstanceId = "180001";
        List<HistoricIdentityLink> list = processEngine.getHistoryService()
                        .getHistoricIdentityLinksForProcessInstance(processInstanceId);
        if(list != null && list.size() > 0){
            for (HistoricIdentityLink i:list) {
                System.out.println(i.getTaskId()+"   "+i.getType()+"    "+i.getProcessInstanceId()+"  "+i.getUserId());
                System.out.println("############");
            }
        }
    }

    /**
     * 拾取任务，将组任务分给个人任务,指定任务的办理人
     */
    @Test
    public void claim(){
        //将组任务分配给个人任务
        //任务ID
        String taskId = "180004";
        //分配的个人任务（可以是组任务中的成员，也可以是非组任务的成员）
        String userId = "剑圣";
        processEngine.getTaskService()
                        .claim(taskId,userId);
    }

    /**
     * 将个人任务回退到组任务,之前一定是个组任务
     */
    @Test
    public void setAssigee(){
        //任务ID
        String taskId = "180004";
        processEngine.getTaskService()
                        .setAssignee(taskId,null);
    }

    /**
     * 向组任务中添加人员
     */
    @Test
    public void addGroupUser(){
        //任务ID
        String taskId = "180004";
        //成员办理人
        String userId = "轮子妈";
        processEngine.getTaskService()
                        .addCandidateUser(taskId,userId);
    }

    /**
     * 从组任务中删除成员
     */
    @Test
    public void deleteGroupUser(){
        //任务ID
        String taskId = "180004";
        //成员办理人
        String userId = "嘉文";
        processEngine.getTaskService()
                        .deleteCandidateUser(taskId,userId);
    }

    /**
     * 完成我的任务
     */
    @Test
    public void comleteMyPersonalTask(){
        //任务ID
        String taskId = "65001";
        processEngine.getTaskService()
                        .complete(taskId);
        System.out.println("完成任务：任务ID："+taskId);
    }

}
