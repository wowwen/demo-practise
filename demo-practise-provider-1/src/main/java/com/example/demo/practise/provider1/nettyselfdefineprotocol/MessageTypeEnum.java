package com.example.demo.practise.provider1.nettyselfdefineprotocol;

public enum MessageTypeEnum {
    REQUEST((byte)1), RESPONSE((byte)2), PING((byte)3), PONG((byte)4), EMPTY((byte)5);

    private byte type;

    //枚举类构造函数，需要添加此构造函数，否则REQUEST((byte)1)会报错Expected 0 arguments but found 1
    MessageTypeEnum(byte type) {
        this.type = type;
    }

    public int getType(){
        return type;
    }

    /**
     * 枚举类的values()方法可以将枚举类转变为一个枚举类类型的数组，因为枚举中没有下标，我们没有办法
     * 通过下标来快速查找到需要的枚举类，这时候，转变为数组之后，我们就可以通过数组的下标，来找到我
     * 们需要的枚举类。
     * 两种方式获取type，1.通过。方式
     *                  2.通过getType()
     * @param type
     * @return
     */
    public static MessageTypeEnum get(byte type){
        for (MessageTypeEnum value : values()) {
            if (value.type == type){
                return value;
            }
        }
        //全没有匹配的，则抛出异常
        throw new RuntimeException("不支持的type：" + type);
    }

    public static void main(String[] args) {
        for (MessageTypeEnum value : values()) {
            System.out.println(value + "--" + value.type);
        }
            System.out.println("完成");
    }
}
