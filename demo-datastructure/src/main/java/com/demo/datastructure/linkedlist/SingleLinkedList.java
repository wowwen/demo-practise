package com.demo.datastructure.linkedlist;


/**
 * @author jiangyw
 * @date 2024/11/8 2:47
 * @description 实现单向链表
 */
public class SingleLinkedList {
    /**
     * 链表节点的个数，即链表的大小
     */
    private int size;
    /**
     * 头节点
     */
    private Node head;

    public SingleLinkedList() {
        size = 0;
        head = null;
    }

    /**
     * 在链表头部添加元素，注意：这里做的是单向链表，只能从头部开始
     *
     * @param obj 待插入头部的元素
     */
    public void addHead(Object obj) {
        Node newHead = new Node(obj);
        if (size == 0) {
            head = newHead;
            size++;
            return;
        }
        //将新的节点的指针指向下一个节点
        newHead.nextNode = head;
        //将头部元素换成新的
        head = newHead;
        size++;
    }

    /**
     * 从链表头部删除元素,并放回删除的元素
     */
    public Object deleteHead() {
        Object data = head.data;
        //将旧头部的下一个元素提升为新头部
        head = head.nextNode;
        size--;
        return data;
    }

    /**
     * 查找指定元素，找到了返回，找不到返回null
     */
    public Node find(Object toBeFind) {
        Node current = head;
        int tmpSize = size;
        while (tmpSize > 0) {
            //不是空，就开始找
            if (toBeFind.equals(current.data)) {
                return current;
            }
            current = current.nextNode;
            tmpSize--;
        }
        return null;
    }

    /**
     * 删除指定的元素
     *
     * @param tobeDeleted 待删除元素
     * @return true：删除成功 false：删除失败
     */
    public boolean delete(Object tobeDeleted) {
        if (size == 0) {
            return false;
        }
        //设置临时变量
        Node current = this.head;
        Node previous = this.head;
        while (!current.data.equals(tobeDeleted)) {
            if (current.nextNode == null) {
                //已到达最后一个节点，仍然没有找到待删除元素
                return false;
            }
            previous = current;
            current = current.nextNode;
        }
        //如果删除的节点是第一个节点，即前面的不等条件没有进去，或者进去了但是没有进if条件
        if (current.equals(head)) {
            //将旧头的下一个节点变成到头部
            head = current.nextNode;
            size--;
        } else {
            //将前一个节点的下一个节点指向被删除的节点的下一个节点
            previous.nextNode = current.nextNode;
            size--;
        }
        return true;
    }

    /**
     * 判断链表是否为空
     */
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * 显示节点信息
     */
    public void display(){
        if (size > 0){
            Node node = this.head;
            int tmpSize = this.size;
            if (tmpSize == 1){
                //当前只有一个节点
                System.out.println(node.data);
                return;
            }
            while (tmpSize > 0){
                if (node.equals(head)){
                    System.out.println("链表头" + node.data);
                }
                if (node.nextNode == null){
                    System.out.println("链表尾" + node.data);
                }
                System.out.println(node.data + "->");
                node = node.nextNode;
                tmpSize--;
            }
            System.out.println("====");
        }else {
            //如果链表为空
            System.out.println("[]");
        }
    }


    private class Node {
        /**
         * 每个节点的数据
         */
        private Object data;
        /**
         * 每个节点指向下一个节点的连接
         */
        private Node nextNode;

        public Node(Object data) {
            this.data = data;
        }
    }


}
