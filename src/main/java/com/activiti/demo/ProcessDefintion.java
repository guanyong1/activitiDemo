package com.activiti.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 9:48 2018/8/7
 * @Modified By:
 */
public class ProcessDefintion {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 流程定义部署（从classoath）
     */
    @Test
    public void deploymentProcessDefinittion(){
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的service
                                    .createDeployment()//创建一个部署对象
                                    .name("流程定义")//添加部署的名称
                                    .addClasspathResource("diagram/helloWorld.bpmn")
                                    .addClasspathResource("diagram/helloWorld.png")
                                    .deploy();//完成部署
        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署名称:"+deployment.getName());
    }

    /**
     * 流程定义部署（从zip文件）
     */
    @Test
    public void deploymentProcessDefinittion_zip(){
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagram/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的service
                .createDeployment()//创建一个部署对象
                .name("zip流程定义")//添加部署的名称
                .addZipInputStream(zipInputStream)//指定zip格式的文件完成部署
                .deploy();//完成部署
        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署名称:"+deployment.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void findProcessDefinition(){
        List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象相关的service
                        .createProcessDefinitionQuery()//创建一个流程定义的查询
                        /**指定查询条件*/
                        //.deploymentId(id)//使用部署对象ID查询
                        //.processDefinitionId(id)//使用流程定义id查询
                        //.processDefinitionKey(key)//使用流程定义的key查询
                        //.processDefinitionNameLike(name)//使用流程定义的名称模糊查询

                        /**排序*/
                        .orderByProcessDefinitionVersion().asc()//按照版本的升序排列
                        //.orderByProcessDefinitionName().desc()//按照流程定义的名称降序排列

                        /**返回的结果集*/
                        .list();//返回一个集合列表，封装流程定义
                        //.singleResult();//返回唯一结果集
                        //.count();//返回结果集数量
                        //.listPage(i,t);//分页查询
        if(list != null && list.size()>0){
            for (ProcessDefinition pd:list) {
                System.out.println("流程定义ID"+pd.getId());//流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称"+pd.getName());//对应bpmn文件中的name属性值
                System.out.println("流程定义的key"+pd.getKey());//对应bpmn文件中的id属性值
                System.out.println("流程定义的版本"+pd.getVersion());//当流程定义的key值相同，版本升级，默认1
                System.out.println("资源名称bpmn文件"+pd.getResourceName());
                System.out.println("资源名称png文件"+pd.getDiagramResourceName());
                System.out.println("部署对象ID"+pd.getDeploymentId());
                System.out.println("#################################");
            }
        }
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteProcessDefinition(){
        //使用部署ID，完成删除
        /**
         * 不带级联的删除
         * 只能删除没有启动的流程，如果流程启动，就会抛出异常
         */
//        processEngine.getRepositoryService()
//                        .deleteDeployment("7501");

        /**
         * 级联删除
         *不管流程是否启动，都能删除
         */
        processEngine.getRepositoryService()
                        .deleteDeployment("1",true);
    }

    /**
     * 查看流程图
     */
    @Test
    public void  viewPic(){
        //将生产的图片放到文件夹下
        String deploymentId = "10001";
        //获取图片资源名称
        List<String> list = processEngine.getRepositoryService()
                        .getDeploymentResourceNames(deploymentId);
        //定义图片资源的名称
        String resourceName ="";
        if(list != null && list.size()>0){
            for (String name:list) {
                if(name.indexOf(".png") >= 0){
                    resourceName = name;
                }
            }
        }
        //获取图片的输入流
        InputStream in =processEngine.getRepositoryService()
                        .getResourceAsStream(deploymentId,resourceName);
        //将图片生成到D盘目录下
        File file = new File("D:/"+resourceName);
        //将输入流的图片写到D盘下
        try {
            FileUtils.copyInputStreamToFile(in,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询最新版本的流程定义
     */
    @Test
    public void findLastVersion(){
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                        .createProcessDefinitionQuery()
                        .orderByProcessDefinitionVersion().asc()//使用流程定义的版本升序排列
                        .list();
        Map<String,ProcessDefinition> map = new LinkedHashMap<String,ProcessDefinition>();
        if(list != null && list.size() > 0){
            for (ProcessDefinition pd:list) {
                map.put(pd.getKey(),pd);
            }
        }
        List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
        if(pdList != null && pdList.size()>0){
            for (ProcessDefinition pd:pdList) {
                System.out.println("流程定义ID"+pd.getId());//流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称"+pd.getName());//对应bpmn文件中的name属性值
                System.out.println("流程定义的key"+pd.getKey());//对应bpmn文件中的id属性值
                System.out.println("流程定义的版本"+pd.getVersion());//当流程定义的key值相同，版本升级，默认1
                System.out.println("资源名称bpmn文件"+pd.getResourceName());
                System.out.println("资源名称png文件"+pd.getDiagramResourceName());
                System.out.println("部署对象ID"+pd.getDeploymentId());
                System.out.println("#################################");
            }
        }
    }

    /**
     * 删除key相同的所有不同版本的流程定义
     */
    @Test
    public void deleteProcessByKey(){
        //流程定义的key
        String processDefinitionKey = "helloworld";
        //先使用流程定义的key查询流程定义，查询出所有版本
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                        .createProcessDefinitionQuery()
                        .processDefinitionKey(processDefinitionKey)
                        .list();
        //遍历，获取每个流程定义的部署ID
        if(list != null && list.size() >0){
            for (ProcessDefinition pd:list) {
                //获取部署ID
                String deploymentId = pd.getDeploymentId();
                processEngine.getRepositoryService()
                                .deleteDeployment(deploymentId,true);
            }
        }
    }

}
