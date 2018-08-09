package com.activiti.demo.sequenceFlow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
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
public class sequFlowTest {
    //流程引擎对象
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinittion(){
        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("sequenceFlow/sequenceFlow.bpmn");
        InputStream inputStreamPng = this.getClass().getClassLoader().getResourceAsStream("sequenceFlow/sequenceFlow.png");
        Deployment deployment = processEngine.getRepositoryService()
                                                .createDeployment()
                                                .name("连线")
                                                .addInputStream("sequenceFlow.bpmn",inputStreamBpmn)
                                                .addInputStream("sequenceFlow.png",inputStreamPng)
                                                .deploy();
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        ProcessInstance processInstance = processEngine.getRuntimeService()
                        .startProcessInstanceByKey("sequenceFlow");
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
        String taskId = "65004";
        //完成任务的同时，设置流程变量，使用流程变量来指定完成任务后，下一个连线，对应sequenceFlow.bpmn文件中#{message=='不重要'}
        Map<String,Object> map = new HashMap<>();
//        map.put("message","不重要");
        map.put("message","重要");
        processEngine.getTaskService()
                        .complete(taskId,map);
        System.out.println("完成任务：任务ID："+taskId);
    }

}
