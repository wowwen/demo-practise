package com.demo.datastructure.linkedlist;

/**
 * @author jiangyw
 * @date 2024/11/10 13:01
 * @description 采用双端链表实现队列
 */
public class QueueByDoublePointLinkedList {
    private DoublePointLinkedList dp;

    public QueueByDoublePointLinkedList(){
        dp = new DoublePointLinkedList();
    }
    public void insert(Object data){
        dp.addTail(data);
    }

    public void delete(){
        dp.deleteHead();
    }

    public boolean isEmpty(){
        return dp.isEmpty();
    }

    public int getSize(){
        return dp.getSize();
    }

    public void display(){
        dp.display();
    }
}
