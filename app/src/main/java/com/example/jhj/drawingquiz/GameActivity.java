package com.example.jhj.drawingquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jhj.drawingquiz.model.GameModel;
import com.example.jhj.drawingquiz.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    static FloatingActionButton fab;
    TextView textView;
    LinearLayout container;
    EditText inputText;
    String name;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    ChatClient client;
    UserModel userModel;
    private FirebaseAuth auth;
    private List<UserModel> userModels = new ArrayList<>();
    private FirebaseDatabase database;
    MyView myView;
    String b;
    LinearLayout linearLayout;
    GameModel g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //////////////////////////////////////////////////


        auth = FirebaseAuth.getInstance();
        final String a = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        g = (GameModel) getIntent().getSerializableExtra("gamemodel");
        myView = new MyView(GameActivity.this, g);

        // Toast.makeText(this, g.getRoomname(), Toast.LENGTH_SHORT).show();
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        linearLayout.addView(myView);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setVisibility(View.GONE);
        //////////////////////////////////////////////////
//        if(Integer.parseInt(g.getRoomnumber()) < client.game_people ){
//            Toast.makeText(this, "방 인원이 꽉 찼습니다.", Toast.LENGTH_SHORT).show();
//            finish();
//        }


        userModel = new UserModel();
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("여기들어옴", snapshot.getValue().toString());
                    userModel = snapshot.getValue(UserModel.class);
                    userModels.add(userModel);

                }
                for (int i = 0; i < userModels.size(); i++) {
                    if (userModels.get(i).getUid().equals(a)) {
                        userModel.setUserName(userModels.get(i).getUserName().toString());
                        b = userModel.getUserName().toString();
                        Log.d("느으아앙: ", b);
                        client = new ChatClient(GameActivity.this, b, myView, g);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                client.sendMsg("clear-" + "지워주세요," + "!" + ",ㅇㅇ");
                                Log.d("TAGTAGTAG", "들어옴");
                            }
                        });
                        client.startClient();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        inputText = (EditText) findViewById(R.id.inputText);
        textView = (TextView) findViewById(R.id.textView);
        container = (LinearLayout) findViewById(R.id.container);

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftInputWindow(inputText);
                return true;
            }
        });

        mListView = (ListView) findViewById(R.id.ListView);
        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);
//        if (client.chat_point[0].equals("drawer")) {
//            Log.d("무슨값?",client.chat_point[0]);
//            if (client.chat_point[1].equals(true)) {
//                Toast.makeText(this, "호랑이를 그려주세요", Toast.LENGTH_LONG).show();
//            }
//        }
    }

    public void onSendBtnClicked(View v) {
        if(inputText.getText().toString().equals("")){
            Toast.makeText(this, "TEXT를 입력하세요", Toast.LENGTH_SHORT).show();
        }else{
            String msg = inputText.getText().toString();
            inputText.setText("");
            client.sendMsg("chat-" + b + "," + ":" + "," + msg);//+","+g.getRoomname());
    }



    }

    public void hideSoftInputWindow(View editView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(editView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public void typeMsg(String msg) {
        if (!msg.equals("")) {
            mAdapter.addItem(msg);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        public TextView chat;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListDataChat> mListData = new ArrayList<ListDataChat>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.chat_list, null);


                holder.chat = (TextView) convertView.findViewById(R.id.chat_list);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListDataChat mData = mListData.get(position);


            holder.chat.setText(mData.chat);


            return convertView;
        }

        public void addItem(String chat) {
            ListDataChat addInfo = null;
            addInfo = new ListDataChat();

            addInfo.chat = chat;


            mListData.add(addInfo);
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }


        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onBackPressed() {

        client.sendMsg("chat-" + b + "," + ":" + "," + "bye");


        Log.d("몇명ㅇㅇ", client.GAME_PEOPLE + "명");
        if (client.GAME_PEOPLE == 1) {
            Intent ReturnIntent = new Intent();
            ReturnIntent.putExtra("roomname", g.getRoomname());
            setResult(Activity.RESULT_OK, ReturnIntent);
            finish();
        } else {
            Intent ReturnIntent = new Intent();
            ReturnIntent.putExtra("roomname", g.getRoomname());
            setResult(Activity.RESULT_CANCELED, ReturnIntent);
            finish();
        }

    }

}


