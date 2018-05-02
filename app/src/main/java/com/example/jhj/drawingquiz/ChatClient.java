package com.example.jhj.drawingquiz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChatClient {
    public static final String ip = "192.168.219.103";
    DataOutputStream out;
    Context context;
    Handler handler;
    String name;
    String msg;
    String rcvMsg;
    String server_msg;
    MyView myView;
    JSONObject j;
    Point point = new Point();
    int x;
    int y;
    String finalstr;
    public static int GAME_PEOPLE=1;
    String server_answer = "";
    String chat_point[];
    int score = 0;
    int point1 = 0;
    UserModel um;
    String drawing_people = "";
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private GameModel g;

    ChatClient(Context context, String name, MyView myView, GameModel g) {

        this.context = context;
        this.name = name;
        this.myView = myView;
        this.g = g;

    }

    public void startClient() {
        handler = new Handler();

        ConnectThread thread = new ConnectThread();
        thread.start();
    }

    class ConnectThread extends Thread {

        @Override
        public void run() {
            try {
                Socket socket = new Socket(ip, 7777);

                Thread sender = new Thread(new ClientSender(socket, name));
                //Thread pathsender = new Thread(new PathSender(socket));
                Thread receiver = new Thread(new ClientReceiver(socket));
                //Thread pathreciever = new Thread(new PathReceiver(socket));

                sender.start();
                //pathsender.start();
                receiver.start();
                //pathreciever.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    class ClientSender implements Runnable {
        Socket socket;

        String name;

        ClientSender(Socket socket, String name) {
            this.socket = socket;
            this.name = name;

            Log.d("이름은!:", name + ">");
            try {
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                if (out != null) {
                    out.writeUTF(name);
                    out.writeUTF(g.getRoomname());
                    //    out.writeUTF(g.getRoomname());
                }
                while (out != null) {
                    if (msg != null) {

                        out.writeUTF(msg);


//                          out.writeUTF(name + ":" + msg);
                        msg = null;

                    }
                    if (myView.point != null) {
                        out.writeUTF(myView.point);
                        myView.point = null;
//                        if (myView.point != null) {
//                            //Log.d("jsonStr : ", myView.point);
//                            out.writeUTF(myView.point);
//                            myView.point = null;
//                        }
                    }
                    Thread.sleep(20);


                }


            } catch (IOException e) {
            } catch (InterruptedException e) {
            }
//            finally {
//                try {
//                    out.writeUTF("bye");
//                    Log.d("tg","@@@@@@@@@@@");
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public void sendMsg(String msg) {
        this.msg = msg;

    }


///////////////////////////////////////////////////////////////////////////////////////////////////

    class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream in;

        ClientReceiver(Socket socket) {
            try {
                this.socket = socket;
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (in != null) {
                Activity activity = (Activity) context;


                try {
                    String str = in.readUTF();
                    Log.d("온 값 : ", str);
                    chat_point = str.split("-");
                    if (chat_point[0].equals("chat")) {
                        String real_chat[] = chat_point[1].split(",");

                        //     if(real_chat[3].equals(g.getRoomname())) {
                        rcvMsg = real_chat[0] + real_chat[1] + real_chat[2];

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((GameActivity) context).typeMsg(rcvMsg);
                            }
                        });
                        //   }


                    } else if (chat_point[0].equals("point")) {
                        String point_section[] = chat_point[1].split(",");
                        //   if(point_section[3].equals(g.getRoomname())) {
                        if (point_section[2].equals("false")) {
                            x = Integer.parseInt(point_section[0]);
                            y = Integer.parseInt(point_section[1]);
                            finalstr = "false";
                        } else {
                            x = Integer.parseInt(point_section[0]);
                            y = Integer.parseInt(point_section[1]);
                            finalstr = "true";

                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myView.update(finalstr, x, y);
                            }
                        });
                        //}
                    } else if (chat_point[0].equals("drawer")) {
                        //        if(chat_point[2].equals(g.getRoomname())) {
                        final boolean clickable = Boolean.parseBoolean(chat_point[1]);


                        Log.d("gogo ::: ", String.valueOf(clickable));
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        return !clickable;
                                    }
                                });
                            }
                        });
                        //        }
                    } else if (chat_point[0].equals("answer")) {
                        server_answer = chat_point[1];
                        Log.d("정답", server_answer);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((GameActivity) context).typeMsg(server_answer);
                            }
                        });
                    } else if (chat_point[0].equals("score")) {
//                            if(chat_point[1].equals(name)){
////플레이어값 1,1,
//                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
//                                mDatabase.updateChildren({ )
//                            }
//                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
//                        mDatabase.child("point").setValue(score+=Integer.parseInt(chat_point[2]));
//
//                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child("point");
//                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                mDatabase.setValue(point1+Integer.parseInt(chat_point[2]));
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                        if(chat_point[1].equals(name)) {
//                            score += Integer.parseInt(chat_point[2]);
//                            auth = FirebaseAuth.getInstance();
//                            DatabaseReference userpoint = FirebaseDatabase.getInstance().getReference().child("users");
//                            Query query = userpoint.orderByChild("uid").equalTo(auth.getCurrentUser().getUid());
//                            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
//                                        child.getRef().child("point").setValue(score);
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                        um = new UserModel();
//                        database = FirebaseDatabase.getInstance();
//                        um.setPoint(score);//roomrank = roomrank;//sp2.getSelectedItem().toString();
//                        database.getReference().child("users").push().setValue(um);
                    } else if (chat_point[0].equals("random")) {
                        //     if(chat_point[2].equals(g.getRoomname())) {
                        final String a = chat_point[1];
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((GameActivity) context).typeMsg(a);
                            }
                        });
                        //   }
                    } else if (chat_point[0].equals("hi")) {
                        drawing_people = chat_point[1];
                        Log.d("맞냐?", "dp: " + drawing_people + " name: " + name);
                        if (drawing_people.equals(name)) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GameActivity.fab.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GameActivity.fab.setVisibility(View.GONE);
                                }
                            });
                        }
                    } else if (chat_point[0].equals("clear")) {

                        finalstr = "clear";
                        x = 0;
                        y = 0;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myView.update(finalstr, x, y);
                            }
                        });
                    } else {
                        if (chat_point.length == 3) {
                            Log.d("chat_point[1] : ", "" + Integer.parseInt(chat_point[1]));
                            GAME_PEOPLE = Integer.parseInt(chat_point[1]);
                            server_msg = chat_point[0] + chat_point[1] + chat_point[2];
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ((GameActivity) context).typeMsg(server_msg);
                                }
                            });
                        }
                    }

                } catch (IOException e) {
                }


            }
        }

    }


}

///////////////////////////////////////////////////////////////////////////////////////////////////