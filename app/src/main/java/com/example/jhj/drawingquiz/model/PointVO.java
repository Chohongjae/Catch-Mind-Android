package com.example.jhj.drawingquiz.model;

/**
 * Created by oopsla on 2017-12-06.
 */

public class PointVO {
    private int x;
    private int y;
    private boolean lineTo;

    public PointVO(int x, int y, boolean lineTo) {
        this.x = x;
        this.y = y;
        this.lineTo = lineTo;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLineTo() {
        return lineTo;
    }

    public void setLineTo(boolean lineTo) {
        this.lineTo = lineTo;
    }
}
