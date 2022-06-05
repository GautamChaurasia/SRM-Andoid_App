package com.example.ezee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button regbtn;
    EditText registerName, registerName2, registerusername, reguid, password, confirmpassword;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;
    Loading load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName = findViewById(R.id.registerName);
        registerName2 = findViewById(R.id.registerName2);
        registerusername = findViewById(R.id.registerusername);
        reguid = findViewById(R.id.uid);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        regbtn = findViewById(R.id.regbtn);

        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();

        reguid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) reguid.setHint("RollNo_Year_Dept_Div");
                else reguid.setHint("UID");
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = registerName.getText().toString();
                String lname = registerName2.getText().toString();
                String email = registerusername.getText().toString();
                String uid = reguid.getText().toString();
                String pwd = password.getText().toString();
                String cpwd = confirmpassword.getText().toString();

                load = new Loading(RegisterActivity.this);

                if (TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(uid) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(cpwd))
                    Toast.makeText(RegisterActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                else if (pwd.length() < 6)
                    Toast.makeText(RegisterActivity.this, "Atleast 6 characters required in Password", Toast.LENGTH_SHORT).show();
                else if(!pwd.equals(cpwd))
                    Toast.makeText(RegisterActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                else{
                    String[] mand = uid.split("_");
                    if(mand.length != 4)
                        Toast.makeText(RegisterActivity.this, "Incorrect UID", Toast.LENGTH_SHORT).show();
                    else{
                        load.startLoading();
                        regUser(fname, lname, email, mand, pwd);
                    }
                }
            }
        });
    }

    private void regUser(String fname, String lname, String email, String[] mand, String pwd) {
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            String[] keys = {"Roll_Num", "Year", "Dept", "Div"};

                            HashMap<String, Object> udets = new HashMap<>();
                            udets.put("admin", false);
                            udets.put("email", email);
                            udets.put("fname", fname);
                            udets.put("lname", lname);
                            for(int i=0; i<mand.length; i++){
                                udets.put(keys[i], mand[i]);
                            }

                            fst.collection("Users").document(mAuth.getCurrentUser().getUid()).set(udets).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FirebaseAuth.getInstance().signOut();
                                        load.dismissDialog();
                                        Toast.makeText(RegisterActivity.this, "Registration Successful, a verification link has been sent to your email", Toast.LENGTH_SHORT).show();
                                        RegisterActivity.super.onBackPressed();
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    load.dismissDialog();
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                load.dismissDialog();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}