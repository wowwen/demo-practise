package com.demo.practise.algorithm;

import java.util.Scanner;

/**
 * @author owen
 * 使用循环链表解决约瑟夫环问题
 * https://blog.csdn.net/weixin_43570367/article/details/105896737
 */
public class JosephCircle {

    public  static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入总人数N（N >= 2）：");
        int n = scanner.nextInt();
        if (n < 2){
            System.out.println("人数需大于2");
            return;
        }

        //构建链表并获取头节点，把头节点赋值给currentNode
        Node currentNode = buildData(n);
        //用来计数
        int count = 0;
        //循环链表当前节点的上一个节点
        Node beforeNode = null;
        //遍历循环链表
        while (currentNode != currentNode.next){
            count++;
            if (count == 3){
                //向后移动节点
                beforeNode.next = currentNode.next;
                System.out.println("出环的编号是：" + currentNode.data);
                count = 0;
                currentNode = currentNode.next;
            }else{
                //向后移动节点
                beforeNode = currentNode;
                currentNode = currentNode.next;
            }
            //表示只有两个节点了，不再进行出环操作
            if (beforeNode.data == currentNode.next.data){
                //跳出循环
                break;
            }
        }
        //最后留在环中的编号
        System.out.println("最后留在环中得编号："+ currentNode.data + "," + currentNode.next.data);

    }

    /**
     * 构建单向循环链表
     *
     * @param n 人数
     * @return 返回头节点
     */
    private static Node buildData(int n){
        //循环链表得头节点
        Node head = null;
        //循环链表当前节点的前一个节点
        Node prev = null;
        for (int i = 1; i <= n; i++) {
            Node newNode = new Node(i);
            //如果是第一个节点
            if (i == 1){
                head = newNode;
                prev = head;
                //跳出当前循环，进行下一次循环
                continue;
            }
            //如果不是第一个节点
            prev.next = newNode;
            prev = newNode;
            //如果是最后一个节点
            if (i == n){
                prev.next = head;
            }
        }
        return head;
    }

    /**
     * 链表节点
     */
    static class Node{
        //当前存储得数据
        int data;
        //当前节点的下一个节点
        Node next;
        //构造函数
        public Node(int data) {
            this.data = data;
        }
    }
}
