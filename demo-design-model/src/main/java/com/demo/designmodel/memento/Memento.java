package com.demo.designmodel.memento;

/**
 * @author juven
 * @date 2025/5/28 0:42
 * @description 包含了要被恢复的对象的状态
 */
public class Memento {
    private String state;

    public Memento(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }
}