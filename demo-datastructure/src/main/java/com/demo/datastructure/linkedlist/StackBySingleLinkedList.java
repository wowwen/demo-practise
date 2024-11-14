package com.demo.datastructure.linkedlist;

/**
 * @author jiangyw
 * @date 2024/11/9 15:33
 * @description 用单向链表实现栈的功能
 */
public class StackBySingleLinkedList {
    private SingleLinkedList link;

    public StackBySingleLinkedList() {
        link = new SingleLinkedList();
    }

    /**
     * 添加元素
     * @param obj
     */
    public void push(Object obj){
        link.addHead(obj);
    }

    /**
     * 移除栈顶元素
     * @return 被移除的元素
     */
    public Object pop(){
       return link.deleteHead();
    }

    /**
     * 判断是否为空
     */
    public boolean isEmpty(){
        return link.isEmpty();
    }

    /**
     * 打印栈内元素信息
     */
    public void display(){
        link.display();
    }
}
