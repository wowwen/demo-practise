package com.demo.flink.learn.sql;

import com.demo.flink.learn.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.InputGroup;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.types.Row;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.call;

/**
 * @author jiangyw
 * @date 2025/3/9 5:39
 * @description 自定义标量函数
 */
public class MyScalarFunctionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<WaterSensor> sensorDs = env.fromElements(
                new WaterSensor("s1", 1L, 1),
                new WaterSensor("s2", 2L, 2),
                new WaterSensor("s3", 3L, 3),
                new WaterSensor("s3", 5L, 5),
                new WaterSensor("s4", 6L, 5)
        );

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //流转表
        Table table = tableEnv.fromDataStream(sensorDs);
        tableEnv.createTemporaryView("sensor", table);

        //注册函数
        tableEnv.createTemporaryFunction("MyHashFunction", MyHashFunction.class);

        //调用自定义函数
        //方式1 sql
        Table sqlTable = tableEnv.sqlQuery("select MyHashFunction(id) from sensor");
        //此处调用了sql的execute（），就不需要env.execute()了
        TableResult sqlResult = sqlTable.execute();
        sqlResult.print();

//        //方式2 table api
//        Table apiTable = table.select(call("MyHashFunction", $("id")));
//        //同样，调用了sql的execute（），就不需要env.execute()了
//        TableResult apiResult = apiTable.execute();
//        apiResult.print();

    }

    public static class MyHashFunction extends ScalarFunction {
        /**
         *
         * @param obj 接收任意类型的输入 @DataTypeHint(inputGroup = InputGroup.ANY)时用来给table
         *            api使用的，只用sql的话，可以不用写。意思是输入是任意类型，table api使用时候，会提取这个注解中的类型进行解析，所以需要指明输入的类型。
         *            此处输入参数是个Object，意思是啥都可以，则这个可以为ANY
         * @return 指定返回int类型， 返回啥由自己定
         */
        public int eval(@DataTypeHint(inputGroup = InputGroup.ANY) Object obj){
            //随便写的逻辑
            return obj.hashCode();
        }

    }
}
