package com.example.jhj.drawingquiz;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JHJ on 2017-12-03.
 */

public class Data implements Serializable {
    ArrayList pathroute;

    public  ArrayList getPathroute(){
        return pathroute;
    }
    public  void setPathroute(ArrayList pathroute){
        this.pathroute=pathroute;
    }
}
