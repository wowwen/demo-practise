package com.demo.datastructure.queue;

/**
 * @author jiangyw
 * @date 2024/11/7 20:19
 * @description
 */
public class MyQueue {
    /**
     * 采用数组实现队列
     */
    private Object[] qArray;
    /**
     * 有界队列总大小
     */
    private int maxSize;
    /**
     * 对头指针
     */
    private int front;
    /**
     * 队尾指针
     */
    private int rear;
    /**
     * 队列中元素的实际数量
     */
    private int itemsIn;

    public MyQueue(int maxSize) {
        this.maxSize = maxSize;
        //初始化指定大小的队列
        qArray = new Object[maxSize];
        front = 0;
        //队尾指针初始化为-1，队列中插入操作在队尾，当插入一个元素后，队尾+1，即变成0，也就是此时，对头队尾指向的是同一个位置。
        rear = -1;
        itemsIn = 0;
    }

    /**
     * 向队尾新增数据
     */
    public void insert(Object item) {
        if (checkFull()) {
            System.out.println("队列已满");
            return;
        }
        if (rear == maxSize - 1) {
            //队尾指针已经到队尾,循环队列中下一个就要将队尾指针移动到第一个，需要将rear置为数组的下标0，故赋值为-1，插入时加1就是0了
            rear = -1;
        }
        //插入将队尾指针+1,然后将元素插入
        qArray[++rear] = item;
        itemsIn++;
    }

    /**
     * 从队头取出元素
     * @return Object
     */
    public Object remove(){
        Object removedItem = null;
        if (!checkEmpty()){
            //注意代码顺序不能错
            //step1
            removedItem = qArray[front];
            //step2
            qArray[front] = null;
            //step3
            front ++;
            //step4
            //检查队头指针是否是在队尾
            if (front == maxSize){
                front = 0;
            }
            itemsIn--;
        }
        return removedItem;
    }

    /**
     * 查看队头数据
     * @return Object
     */
    public Object peekFront(){
        return qArray[front];
    }

    private boolean checkFull(){
        return itemsIn == maxSize;
    }

    private boolean checkEmpty(){
        return itemsIn == 0;
    }
}
