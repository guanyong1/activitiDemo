package com.activiti.demo.start;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
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
public class startTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinittion(){
        InputStream inputStreamBpmn = this.getClass().getClassLoader().getResourceAsStream("start/start.bpmn");
        InputStream inputStreamPng = this.getClass().getClassLoader().getResourceAsStream("start/start.png");
        Deployment deployment = processEngine.getRepositoryService()
                                                .createDeployment()
                                                .name("开始活动")
                                                .addInputStream("start.bpmn",inputStreamBpmn)
                                                //.addInputStream("ExclusiveGateWay.png",inputStreamPng)
                                                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动流程实例,判断流程是否结束
     */
    @Test
    public void startProcessInstance(){
        ProcessInstance processInstance = processEngine.getRuntimeService()
                        .startProcessInstanceByKey("start");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());

        /**判断流程是否结束*/
        ProcessInstance processInstance1 =processEngine.getRuntimeService()
                        .createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .singleResult();
        if(processInstance1 != null){
            /**查询历史*/
            HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
                            .createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstance.getId())
                            .singleResult();
            System.out.println(historicProcessInstance.getId()+"    "+historicProcessInstance.getStartTime());
        }
    }

}
