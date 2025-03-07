package com.demo.flink.learn.stream.split;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author jiangyw
 * @date 2025/2/16 21:08
 * @description 使用侧输出流实现分流
 */
public class SplitBySideOutputDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        //将不同传感器的数据输出到不同的流 w1--》w1  w2--->w2
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888)
                .map(new WaterSensorFunctionImpl());

        //侧输出流的标签，第一个参数标签名，第二个参数侧输出流中的数据类型
        OutputTag<WaterSensor> w1Tag = new OutputTag<>("w1-stream",
                Types.POJO(WaterSensor.class));
        //侧输出流的标签，第一个参数标签名，第二个参数侧输出流中的数据类型
        OutputTag<WaterSensor> w2Tag = new OutputTag<>("w2-stream",
                Types.POJO(WaterSensor.class));

        //此处的输出类型定义的是主流的输出类型
        SingleOutputStreamOperator<WaterSensor> process = sensorDs.process(new ProcessFunction<WaterSensor,
                WaterSensor>() {
            @Override
            public void processElement(WaterSensor value, Context ctx, Collector<WaterSensor> out) throws Exception {
                if ("w1".equals(value.getId())) {
                    /**
                     * 上下文调用output()，将数据放入侧输出流
                     * 第一个参数：Tag对象
                     * 第二个参数：放入侧输出流中的数据
                     */
                    ctx.output(w1Tag, value);
                } else if ("w2".equals(value.getId())) {

                    /**
                     * 上下文调用output()，将数据放入侧输出流
                     * 第一个参数：Tag对象
                     * 第二个参数：放入侧输出流中的数据
                     */
                    ctx.output(w2Tag, value);
                } else {
                    //非w1, w2的数据，继续放到主流中
                    out.collect(value);
                }

            }
        });
        //打印主流的
        process.print("主流");
        //获取侧输出流
        SideOutputDataStream<WaterSensor> w1 = process.getSideOutput(w1Tag);
        SideOutputDataStream<WaterSensor> w2 = process.getSideOutput(w2Tag);
        //打印侧输出流的
        w1.print("w1侧输出流");
        w2.print("w2侧输出流");

        /**
         * w1侧输出流:1> WaterSensor{id='w1', ts=1, temperature=1}
         */
        env.execute();
    }

}
