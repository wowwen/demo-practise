package com.demo.designmodel.compositeentity;

/**
 * @author juven
 * @date 2025/5/29 1:06
 * @description 创建组合实体
 */
public class CompositeEntity {
    private CoarseGrainedObject cgo = new CoarseGrainedObject();

    public void setData(String data1, String data2){
        cgo.setData(data1, data2);
    }

    public String[] getData(){
        return cgo.getData();
    }
}