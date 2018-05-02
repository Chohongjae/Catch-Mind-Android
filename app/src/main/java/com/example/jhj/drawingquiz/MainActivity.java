package com.example.jhj.drawingquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jhj.drawingquiz.model.GameModel;
import com.example.jhj.drawingquiz.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    ArrayAdapter<CharSequence> adspin1;
    ArrayAdapter<CharSequence> adspin2;
    String roomname;
    String roomnumber;
    String roomrank;
    Button button;
    TextView tv;
    TextView tv1;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private ListView mListView = null;
    //    private ListViewAdapter mAdapter = null;
    private ArrayList<GameModel> gameModels = new ArrayList<>();
    private ArrayList<UserModel> userModels = new ArrayList<>();
    private gameAdapter gAdapter;
    GameModel gameModel;
    UserModel userModel;
   // ChatClient chatClient;
    MyView myView;
    String key="";
    String key2="";
    Random random ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameModel = new GameModel();
        userModel = new UserModel();
        tv = (TextView) findViewById(R.id.textView);
        tv1 = (TextView) findViewById(R.id.textView2);
        mListView = (ListView) findViewById(R.id.mList);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        random = new Random();

        final String a = auth.getCurrentUser().getUid();
        Toast.makeText(MainActivity.this, a, Toast.LENGTH_SHORT).show();


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
                        userModel.setPoint(userModels.get(i).getPoint());
                        tv.setText(userModel.getUserName().toString());
                        tv1.setText(""+userModel.getPoint()+"점");
                        Log.d("NAME: ", userModel.getUserName());
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database.getReference().child("gamerooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("kkakakak", snapshot.getValue().toString());
                    gameModel = snapshot.getValue(GameModel.class);
                    gameModels.add(gameModel);
                    //gameModel = dataSnapshot.getValue(GameModel.class);
                    Log.d("aaaaa", gameModel.getRoomname());

                }


                gAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        gAdapter = new gameAdapter(this, R.layout.item_game, gameModels);
        mListView.setAdapter(gAdapter);

        myView = new MyView(this,gameModel);
       //chatClient = new ChatClient(this, userModel.getUserName(), myView, gameModel);
////////////////////////이부분이문제!!
        ////////////////////////리스트마다의값을 받아와야함!
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameModel g = (GameModel) mListView.getAdapter().getItem(position);

                //Toast.makeText(MainActivity.this, "ㅋㅋㅋㅋㅋ", Toast.LENGTH_SHORT).show();
               ///////////////////////다른방 어떻게 구분?
                Log.d("게임인원",String.valueOf(ChatClient.GAME_PEOPLE));
                String split[] = g.getRoomnumber().split("명");
                if (ChatClient.GAME_PEOPLE < Integer.parseInt(split[0])) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("gamemodel", g);
                    startActivityForResult(intent, 1);
                }else{
                    Toast.makeText(MainActivity.this, "방 인원이 꽉 찼습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ///////////////////////////////////////////
        Log.d("aaaaa", "222222222");
//        /=====================


//        =====================
//       // tv.setText(auth.getCurrentUser().getEmail());
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainactivity_bottomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main:
                        break;

                    case R.id.make_room:
                        final LayoutInflater inflater = getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                        final Spinner sp1 = (Spinner) dialogView.findViewById(R.id.spinner1);
                       // final Spinner sp2 = (Spinner) dialogView.findViewById(R.id.spinner2);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("방만들기");
                        builder.setView(dialogView);
                        adspin1 = ArrayAdapter.createFromResource(MainActivity.this, R.array.roomname, android.R.layout.simple_spinner_item);
                        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp1.setAdapter(adspin1);

//                        adspin2 = ArrayAdapter.createFromResource(MainActivity.this, R.array.roomrank, android.R.layout.simple_spinner_item);
//                        adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        sp2.setAdapter(adspin2);

                        Log.d("zzzzzzzzz", "33333333333333333");

                        builder.setPositiveButton("방만들기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final EditText edit_name = (EditText) dialogView.findViewById(R.id.roomname_edittext);

                                roomname = edit_name.getText().toString();
                                roomnumber = sp1.getSelectedItem().toString();
                        //        roomrank = sp2.getSelectedItem().toString();




                                gameModel.setRoomnumber(roomnumber);//roomnumber = roomnumber;//sp1.getSelectedItem().toString();
                                gameModel.setRoomname(roomname);// = roomname;//edit_name.getText().toString();
                               // gameModel.setRoomrank(roomrank);//roomrank = roomrank;//sp2.getSelectedItem().toString();
                                database.getReference().child("gamerooms").push().setValue(gameModel);




                                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                intent.putExtra("gamemodel", gameModel);

//                                startActivity(intent);

                                startActivityForResult(intent, 1);
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        break;

                    case R.id.random_room:


//                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                GameModel g = (GameModel) mListView.getAdapter().getItem(position);
//
//                                //Toast.makeText(MainActivity.this, "ㅋㅋㅋㅋㅋ", Toast.LENGTH_SHORT).show();
//                                ///////////////////////다른방 어떻게 구분?
//                                Log.d("게임인원",String.valueOf(ChatClient.GAME_PEOPLE));
//                                String split[] = g.getRoomnumber().split("명");
//                                if (ChatClient.GAME_PEOPLE < Integer.parseInt(split[0])) {
//                                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
//                                    intent.putExtra("gamemodel", g);
//                                    startActivityForResult(intent, 1);
//                                }else{
//                                    Toast.makeText(MainActivity.this, "방 인원이 꽉 찼습니다.", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
                }
                return true;
            }
        });

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        String a = data.getStringExtra("roomname");
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    for (int i = 0; i < gameModels.size(); i++) {
                        if (a.equals(gameModels.get(i).getRoomname())) {
                            gameModels.remove(i);
                        }
                    }
//                    for (int j = 0; j <= gameModels.size(); j++) {
//                        if (a.equals(gameModels.get(j).getRoomname())) {
                    Query applesQuery = database.getReference().child("gamerooms").orderByChild("roomname").equalTo(a);
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot appleSnapshot: dataSnapshot.getChildren()){
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    gAdapter.notifyDataSetChanged();


                }
                if (resultCode == Activity.RESULT_CANCELED) {
                }

        }

    }
}
