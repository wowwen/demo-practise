package com.demo.datastructure.stack;

import java.util.Arrays;

/**
 * @author jiangyw
 * @date 2024/11/6 22:41
 * @description 通过数据实现栈的作用
 */
public class SelfDefineStack<T> {
    /**
     * 采用数组实现栈
     */
    private Object[] stack;
    /**
     * 数组的大小
     */
    private int size;

    public SelfDefineStack(Object[] stack) {
        this.stack = stack;
    }

    public SelfDefineStack() {
        //设定初始容量
        stack = new Object[10];
    }

    /**
     * 判断是否为空
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 返回栈顶元素，但是不移除
     */
    public T peek() {
        T t = null;
        if (size > 0) {
            t = (T) stack[size - 1];
        }
        return t;
    }

    /**
     * 入栈
     *
     * @param t 入栈的元素
     */
    public void push(T t) {
        //扩容
        expandCapacity(size + 1);
        //塞入
        stack[size] = t;
        //赋值
        size++;
    }

    /**
     * 出栈
     */
    public T pop() {
        T t = peek();
        //栈顶置空
        if (size > 0) {
            stack[size - 1] = null;
            size--;
        }
        return t;
    }

    /**
     * 扩容
     * @param size 新的数组大小
     */
    private void expandCapacity(int size){
        int length = stack.length;
        if (size > length){
            //复制给int型会向下取证，7.6 -> 7
            size = size * 3 / 2 + 1;
            //复制进入新的数组，多的置为null
            stack = Arrays.copyOf(stack, size);
        }
    }

    public static void main(String[] args) {
        System.out.println( 7 * 3 / 2);
        int i = 5 * 3 / 2 + 1;
        System.out.println(i);
    }
}
