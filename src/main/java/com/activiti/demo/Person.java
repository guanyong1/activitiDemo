package com.activiti.demo;

import java.io.Serializable;

/**
 * @Author:guang yong
 * Description:
 * @Date:Created in 11:56 2018/8/8
 * @Modified By:
 */
public class Person implements Serializable{

    /**
     * 使用序列化版本号解决Javabean（实现序列化）放置到流程变量中，Javabean的属性变化导致获取流程变量时，Javabean不能被反序列化
     */
    private static final long serialVersionUID = 5790482014544692189L;
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
