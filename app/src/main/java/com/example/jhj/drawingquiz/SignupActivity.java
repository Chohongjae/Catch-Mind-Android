package com.example.jhj.drawingquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jhj.drawingquiz.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText name;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = (EditText) findViewById(R.id.signupActivity_email);
        name = (EditText) findViewById(R.id.signupActivity_name);
        password = (EditText) findViewById(R.id.signupActivity_password);

        signup = (Button) findViewById(R.id.signupActivity_button_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().length() == 0 || name.getText().toString().length() == 0 || password.getText().toString().length() == 0) {
                    Toast.makeText(SignupActivity.this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "패스워드를 6자이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkEmail(email.getText().toString()) == false) {
                    Toast.makeText(SignupActivity.this, "메일형식을 지켜주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()
                ).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String uid = task.getResult().getUser().getUid();
                        UserModel userModel = new UserModel();
                        userModel.userName = name.getText().toString();
                        userModel.userEmail = email.getText().toString();
                        userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        userModel.point = 0;
                        FirebaseDatabase.getInstance().getReference().child("users").push().setValue(userModel);



                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);   //*****************왜 안가지지? 왜 메인으로가지냐?
                        startActivity(intent);
                        finish();
                        Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();


                    }
                });

            }
        });
    }

    private boolean checkEmail(String email) {

        String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";

        Pattern p = Pattern.compile(mail);

        Matcher m = p.matcher(email);

        return m.matches();

    }
}
