package com.example.jhj.drawingquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jhj.drawingquiz.model.GameModel;

import java.util.ArrayList;

/**
 * Created by namgiwon on 2017. 10. 5..
 */

public class gameAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<GameModel> data;
    private int layout;

    public gameAdapter(Context context, int layout, ArrayList<GameModel> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public GameModel getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        GameModel roomitem = data.get(position);
        TextView roomnumber = (TextView) convertView.findViewById(R.id.textview_roomnumber);
        TextView roomname = (TextView) convertView.findViewById(R.id.textview_roomname);
       // TextView roomrank = (TextView) convertView.findViewById(R.id.textview_roomrank);
        roomnumber.setText(roomitem.getRoomnumber());
        roomname.setText(roomitem.getRoomname());
        //roomrank.setText(roomitem.getRoomrank());
        return convertView;
    }
}
