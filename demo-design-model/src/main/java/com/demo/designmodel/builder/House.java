package com.demo.designmodel.builder;

/**
 * @author jiangyw
 * @date 2024/7/21 20:16
 * @description // 首先，我们定义房屋类 House，它具有多个属性，如地基、结构、屋顶和装修。
 */
public class House {
    private String foundation;
    private String structure;
    private String roof;
    private String interior;

    public void setFoundation(String foundation) {
        this.foundation = foundation;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public void setRoof(String roof) {
        this.roof = roof;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    @Override
    public String toString() {
        return "House [foundation=" + foundation + ", structure=" + structure + ", roof=" + roof + ", interior=" + interior + "]";
    }
}
