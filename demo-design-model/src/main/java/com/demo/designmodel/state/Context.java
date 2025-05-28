package com.demo.designmodel.state;

/**
 * @author juven
 * @date 2025/5/28 15:50
 * @description Context 是一个带有某个状态的类
 */
public class Context {
    private StateInterface state;

    public Context(){
        state = null;
    }

    public void setState(StateInterface state){
        this.state = state;
    }

    public StateInterface getState(){
        return state;
    }
}
