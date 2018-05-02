package com.example.jhj.drawingquiz;

/**
 * Created by JHJ on 2017-11-25.
 */
import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;


public class ListData {
    public String roomnumber;
    public String roomname;

    public String roomrank;
/*
    public static final Comparator<ListData>ALPHA_COMPARATOR = new Comparator<ListData>() {
        private  final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(ListData mListDate_1, ListData mListDate_2,ListData mListDate_3) {
            return sCollator.compare(mListDate_1.roomname, mListDate_2.roomnumber,mListDate_3.roomrank);
        }
    };
*/
}
