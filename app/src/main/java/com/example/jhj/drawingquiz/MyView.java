package com.example.jhj.drawingquiz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.jhj.drawingquiz.model.GameModel;
import com.example.jhj.drawingquiz.model.PointVO;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oopsla on 2017-12-01.
 */

public class MyView extends View {
    Paint paint = new Paint();
    Path path = new Path();
    String jsonStr;
    JSONObject j = new JSONObject();
    String point;
    ChatClient client;
    GameModel g;
    public MyView(Context context,GameModel g) {
        super(context);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        this.g = g;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        Gson gson = new Gson();
        PointVO pointVO = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //path.moveTo(x, y);
                //pointVO = new PointVO(x, y, false);
                point="point-"+x+","+y+",false";//+","+g.getRoomname();

                break;
            case MotionEvent.ACTION_MOVE:
                //path.lineTo(x, y);
                //pointVO = new PointVO(x, y, true);
                point="point-"+x+","+y+",true";//+","+g.getRoomname();
                break;
        }
//        if (pointVO != null) {
//            jsonStr = gson.toJson(pointVO);
//
//        }
//        if(point != null){
//
//        }
        Log.d("좌표!", String.valueOf(x));
        Log.d("좌표?", String.valueOf(y));
        //invalidate();
        return true;
    }

    public void update(String finalstr,int x,int y) {

        Log.d("로그", "----");
//        Gson gson = new Gson();
//        PointVO vo = gson.fromJson(str, PointVO.class);
//        if (vo.isLineTo()) {
//            path.lineTo(vo.getX(), vo.getY());
//        } else {
//            path.moveTo(vo.getX(), vo.getY());
//        }
        if(finalstr.equals("false")){
            path.moveTo(x,y);
        }else if(finalstr.equals("clear")){
            path.rewind();
        }
        else{
            path.lineTo(x,y);
        }

        invalidate();
    }


}


