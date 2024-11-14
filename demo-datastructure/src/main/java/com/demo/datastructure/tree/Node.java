package com.demo.datastructure.tree;

/**
 * @author jiangyw
 * @date 2024/11/11 0:39
 * @description 二叉树节点类
 */
public class Node {
    int data;   //节点数据
    Node leftChild; //左子节点的引用
    Node rightChild; //右子节点的引用
    boolean isDelete;//表示节点是否被删除

    public Node(int data){
        this.data = data;
    }
    //打印节点内容
    public void display(){
        System.out.println(data);
    }
}
