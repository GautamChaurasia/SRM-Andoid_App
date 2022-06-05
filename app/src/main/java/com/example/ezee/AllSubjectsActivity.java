package com.example.ezee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AllSubjectsActivity extends AppCompatActivity {

    static String wsem;
    DatabaseReference reference,dbRef;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;
    String usersDept;
    List<String> list1;
    SubjectAdapter adapter;
    RecyclerView subjectsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_subjects);

        Intent intent = getIntent();
        wsem = intent.getStringExtra("wsem");
        Toast.makeText(this, "Got" + wsem, Toast.LENGTH_SHORT).show();

        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();

        subjectsRecView = findViewById(R.id.subjectsRecView);

        DocumentReference data = fst.collection("Users").document(mAuth.getCurrentUser().getUid());
        data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    usersDept = documentSnapshot.getString("Dept");
                    Log.i("ayushhh","ayushhh");
                    Log.i("usersDept",usersDept);

                    if (usersDept == "COMP") {
                        usersDept = "Computer Engineering";
                    }
                }
                else Toast.makeText(AllSubjectsActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                showSubj(usersDept);
            }
        }, 5000);


    }

    public void showSubj(String usersDept) {
        Log.i("Department",usersDept);
        Log.i("wsem**********",wsem);

        dbRef = reference.child("questionPapers").child(usersDept).child(wsem);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();

                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String data = snapshot.getKey();

                        list1.add(data);
                    }
                    subjectsRecView.setHasFixedSize(true);

                    subjectsRecView.setLayoutManager(new GridLayoutManager(AllSubjectsActivity.this,2));
                    adapter = new SubjectAdapter(list1,wsem);
                    subjectsRecView.setAdapter(adapter);
                } else {
                    Toast.makeText(AllSubjectsActivity.this, "No data exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllSubjectsActivity.this, "Errrror--->" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}