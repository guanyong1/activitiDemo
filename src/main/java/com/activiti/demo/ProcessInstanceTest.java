package com.activiti.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 16:55 2018/8/7
 * @Modified By:
 */
public class ProcessInstanceTest {
    //流程引擎对象
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinittion(){
        Deployment deployment = processEngine.getRepositoryService()
                                                .createDeployment()
                                                .name("helloworld")
                                                .addClasspathResource("diagram/helloWorld.bpmn")
                                                .addClasspathResource("diagram/helloWorld.png")
                                                .deploy();
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        ProcessInstance processInstance = processEngine.getRuntimeService()
                        .startProcessInstanceByKey("helloworld");
    }

    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask(){
        String name = "王五";
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
     * 完成我的任务
     */
    @Test
    public void comleteMyPersonalTask(){
        //任务ID
        String taskId = "30002";
        processEngine.getTaskService()
                        .complete(taskId);
        System.out.println("完成任务：任务ID："+taskId);
    }

    /**
     * 查询流程状态（判断流程正在执行还是结束）
     */
    @Test
    public void isProcessEnd(){
        String processInstanceId = "22501";
        ProcessInstance processInstance = processEngine.getRuntimeService()
                        .createProcessInstanceQuery()//流程实例查询
                        .processInstanceId(processInstanceId)
                        .singleResult();
        if(processInstance == null){
            System.out.println("流程已经结束");
        }else {
            System.out.println("流程未结束");
        }
    }

    /**
     * 查询历史任务
     */
    @Test
    public void findHistoryTask(){
        String taskAssignee = "张三";
        List<HistoricTaskInstance> list = processEngine.getHistoryService()
                        .createHistoricTaskInstanceQuery()//创建任务历史查询对象
                        .taskAssignee(taskAssignee)
                        .list();
        if(list != null && list.size() > 0){
            for (HistoricTaskInstance history:list) {
                System.out.println(history.getAssignee());
            }
        }
    }

    /**
     * 查询历史流程实例
     */
    @Test
    public void findHistoryInstance(){
        String processInstanceId = "22501";
        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
                        .createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();
        System.out.println(historicProcessInstance.getProcessDefinitionId());
    }
}
