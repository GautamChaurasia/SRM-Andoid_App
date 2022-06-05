package com.example.ezee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    EditText fgtpassusername;
    Button fgtpassbutton;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        fgtpassusername  = findViewById(R.id.fgtpassusername);
        fgtpassbutton = findViewById(R.id.fgtpassbutton);
        mauth = FirebaseAuth.getInstance();

        fgtpassbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = fgtpassusername.getText().toString();

                Task t = mauth.sendPasswordResetEmail(email);
                t.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPassActivity.this, "Password reset email sent, check your Inbox", Toast.LENGTH_SHORT).show();
                            ForgotPassActivity.super.onBackPressed();
                            finish();
                        }
                        else Toast.makeText(ForgotPassActivity.this, "Email not registered with us", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}