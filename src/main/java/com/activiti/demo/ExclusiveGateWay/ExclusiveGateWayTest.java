package com.activiti.demo.ExclusiveGateWay;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
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
 * @Date:Created in 9:21 2018/8/9
 * @Modified By:
 */
public class ExclusiveGateWayTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinittion(){
        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("ExclusiveGateWay/ExclusiveGateWay.bpmn");
        InputStream inputStreamPng = this.getClass().getClassLoader().getResourceAsStream("ExclusiveGateWay/ExclusiveGateWay.png");
        Deployment deployment = processEngine.getRepositoryService()
                                                .createDeployment()
                                                .name("排他网关")
                                                .addInputStream("ExclusiveGateWay.bpmn",inputStreamBpmn)
                                                //.addInputStream("ExclusiveGateWay.png",inputStreamPng)
                                                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        ProcessInstance processInstance = processEngine.getRuntimeService()
                        .startProcessInstanceByKey("ExclusiveGateWay");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getName());
    }

    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask(){
        List<Task> list = processEngine.getTaskService()
                        .createTaskQuery()
                        .taskAssignee("德邦")
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
        String taskId = "112504";
        //完成任务的同时，设置流程变量，使用流程变量来指定完成任务后，下一个连线，对应ExclusiveGateWay.bpmn文件中#{money}
        Map<String,Object> map = new HashMap<>();
        //使用了排他网关，使用money作为判断走哪条分支，所以money字段必传
        map.put("money",800);
        processEngine.getTaskService()
                        .complete(taskId,map);
    }

}
