package com.demo.designmodel.state;

/**
 * @author juven
 * @date 2025/5/28 15:48
 * @description
 */
public interface StateInterface {
    public void doAction(Context context);
}

class StartState implements StateInterface {

    @Override
    public void doAction(Context context) {
        System.out.println("Player is in start state");
        context.setState(this);
    }

    public String toString(){
        return "Start State";
    }
}

class StopState implements StateInterface {

    public void doAction(Context context) {
        System.out.println("Player is in stop state");
        context.setState(this);
    }

    public String toString(){
        return "Stop State";
    }
}