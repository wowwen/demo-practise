package com.demo.designmodel.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juven
 * @date 2025/5/28 0:43
 * @description 定义类Caretaker对象负责从Memento中恢复对象的状态；
 */
public class CareTaker {
    private List<Memento> mementoList = new ArrayList<Memento>();

    public void add(Memento state){
        mementoList.add(state);
    }

    public Memento get(int index){
        return mementoList.get(index);
    }
}