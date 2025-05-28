package com.demo.designmodel.state;

/**
 * @author juven
 * @date 2025/5/28 15:53
 * @description 使用 Context 来查看当状态 State 改变时的行为变化
 */
public class StatePatternDemo {
    public static void main(String[] args) {
        Context context = new Context();

        StartState startState = new StartState();
        startState.doAction(context);

        System.out.println(context.getState().toString());

        StopState stopState = new StopState();
        stopState.doAction(context);

        System.out.println(context.getState().toString());
    }
}
