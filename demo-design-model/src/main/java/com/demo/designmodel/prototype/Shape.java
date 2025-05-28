package com.demo.designmodel.prototype;

/**
 * @author juven
 * @date 2025/5/26 0:00
 * @description
 * 1、 创建一个抽象类Shape和扩展了Shape类的实体类；
 * 2、 定义类ShapeCache，该类把shape对象存储在一个Hashtable中，并在请求的时候返回它们的克隆；
 * 3、 PrototypPatternDemo类使用ShapeCache类来获取Shape对象；
 */
public abstract class Shape implements Cloneable{
    private String id;
    protected String type;

    abstract void draw();

    public String getType(){
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

}
