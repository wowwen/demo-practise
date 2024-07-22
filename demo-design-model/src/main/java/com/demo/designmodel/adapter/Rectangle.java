package com.demo.designmodel.adapter;

/**
 * @author jiangyw
 * @date 2024/7/22 11:25
 * @description // 统一的Shape接口
 */
public interface Rectangle {
    void draw(int x, int y, int width, int height);
}

/**
 * // 适配器类，将LegacyRectangle适配到Rectangle接口上
 */
class RectangleAdapter implements Rectangle{
    private LegacyRectangle legacyRectangle;

    public RectangleAdapter(LegacyRectangle legacyRectangle){
        this.legacyRectangle = legacyRectangle;
    }

    /**
     * 转换
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void draw(int x, int y, int width, int height) {
        int x1 = x;
        int y1 = y;
        int x2 = x + width;
        int y2 = y + height;
        legacyRectangle.display(x1, y1, x2, y2);
    }
}